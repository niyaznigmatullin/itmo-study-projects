package ru.ifmo.niyaz.study.network.task4;

import ru.ifmo.niyaz.study.network.ByteArrayReader;
import ru.ifmo.niyaz.study.network.ByteArrayWriter;
import ru.ifmo.niyaz.study.network.Hash;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 29.10.12
 * Time: 1:44
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    static final long REMOVE_TIMEOUT = 10000;
    static final long PRINT_TIMEOUT = 2000;
    static final int PORT_TO_LISTEN = 9999;
    static final int MAX_PACKET_LEN = 10000;

    public static void main(String[] args) throws IOException,
            InterruptedException {
        final DatagramPacket dp = new DatagramPacket(new byte[MAX_PACKET_LEN], MAX_PACKET_LEN);
        final Map<User, Long> users = new HashMap<>();
        final Map<User, String> contents = new HashMap<>();
        final Map<User, List<String>> fileLists = new HashMap<>();
        final Map<Hash, User> files = new HashMap<>();
        new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(PRINT_TIMEOUT);
                    } catch (InterruptedException e) {
                        break;
                    }
                    synchronized (users) {
                        long time = System.currentTimeMillis();
                        for (Iterator<User> e = users.keySet().iterator(); e.hasNext(); ) {
                            User n = e.next();
                            if (time - users.get(n) > REMOVE_TIMEOUT) {
                                System.out.println(n + " removed successfully");
                                e.remove();
                                contents.remove(n);
                                fileLists.remove(n);
                            }
                        }
                    }
                }
            }
        }.start();
//            final ServerSocket serverSocket = new ServerSocket();
//            serverSocket.bind(new InetSocketAddress(PORT_TO_LISTEN));
        new Thread() {
            @Override
            public void run() {
                try (DatagramSocket ds = new DatagramSocket(null)) {
                    ds.bind(new InetSocketAddress(PORT_TO_LISTEN));
                    while (!isInterrupted()) {
                        ds.receive(dp);
                        synchronized (users) {
                            User user = readUser(dp);
                            if (user == null) {
                                System.err.println("bad user");
                                continue;
                            }
                            if (!contents.containsKey(user) || !contents.get(user).equals(user.contentID)) {
                                users.remove(user);
                                try (Socket socket = new Socket(user.hostAddress, PORT_TO_LISTEN)) {
                                    contents.put(user, user.contentID);
                                    ByteArrayWriter writer = new ByteArrayWriter(socket.getOutputStream());
                                    writer.writeInt(0);
                                    writer.flush();
                                    ByteArrayReader reader = new ByteArrayReader(socket.getInputStream());
                                    int fileCount = reader.readInt();
                                    List<String> hashes = new ArrayList<>();
                                    for (int i = 0; i < fileCount; i++) {
                                        Hash hash = reader.readHash();
                                        hashes.add(hash.toString());
                                        files.put(hash, user);
                                    }
                                    fileLists.put(user, hashes);
                                    reader.close();
                                    writer.close();
                                } catch (ConnectException e) {
                                    System.err.println("couldn't get list of files of " + user);
                                }
                            }
                            users.put(user, System.currentTimeMillis());
                        }
                    }
                } catch (IOException e) {
                    System.err.println("exception happened while receiving broadcasts");
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            public void run() {
                try (Scanner stdIn = new Scanner(System.in)) {
                    while (!isInterrupted()) {
                        Scanner sc = new Scanner(stdIn.nextLine());
                        String op = sc.next();
                        if (op.startsWith("list")) {
                            for (User e : users.keySet()) {
                                System.out.println(e);
                                List<String> files = fileLists.get(e);
                                for (String file : files) {
                                    System.out.println("   " + file);
                                }
                            }
                        } else if (op.startsWith("get")) {
                            String hashString = sc.next();
                            Hash hash = new Hash(hashString);
                            User user = files.get(hash);
                            all:
                            {
                                if (user != null) {
                                    try (Socket socket = new Socket(user.hostAddress, PORT_TO_LISTEN)) {
                                        try (ByteArrayReader reader = new ByteArrayReader(socket.getInputStream())) {
                                            try (ByteArrayWriter writer = new ByteArrayWriter(socket.getOutputStream())) {
                                                writer.writeInt(1);
                                                writer.writeByteArray(hash.getByteArray());
                                                writer.flush();
                                                byte got = reader.readByte();
                                                if (got == 1) {
                                                    System.out.println("read from " + user);
                                                    System.out.println("message: ");
                                                    System.out.println(reader.readLine(65535));
                                                    break all;
                                                }
                                            }
                                        }
                                    } catch (UnknownHostException e) {
                                        System.err.println("no host " + user);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                System.err.println("couldn't get message from " + user);
                            }
                        } else if (op.equals("put")) {
                            String ip = sc.next();
                            String line = sc.nextLine();
                            try (Socket socket = new Socket(ip, PORT_TO_LISTEN)) {
                                try (ByteArrayReader reader = new ByteArrayReader(socket.getInputStream())) {
                                    try (ByteArrayWriter writer = new ByteArrayWriter(socket.getOutputStream())) {
                                        writer.writeInt(2);
                                        writer.writeLine(line);
                                        writer.flush();
                                        if (reader.readByte() == 0) {
                                            System.out.println("file already exists");
                                        } else {
                                            System.out.println("file added");
                                        }
                                    }
                                }
                            } catch (UnknownHostException e) {
                                System.err.println("couldn't answer ip, not found " + ip);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("invalid type of operation requested " + op);
                        }
                    }
                }
            }
        }.start();
    }

    private static void printAllServers(Map<User, Long> users) {
        System.out.println("===================================");
        System.out.print("Current time is: ");
        System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        for (User e : users.keySet()) {
            System.out.println(e);
        }
        System.out.println("===================================");
        System.out.println();
    }

    static User readUser(DatagramPacket dp) throws IOException {
        try {
            ByteArrayInputStream bs = new ByteArrayInputStream(dp.getData(),
                    dp.getOffset(), dp.getLength());
            ByteArrayReader reader = new ByteArrayReader(bs);
            String hostAddress = reader.readLine(20);
            String name = reader.readLine(40);
            String contentID = reader.readHash().toString();
            return new User(hostAddress, name, contentID);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("bad user");
            return null;
        }
    }

}