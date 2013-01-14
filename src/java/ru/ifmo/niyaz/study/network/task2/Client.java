package ru.ifmo.niyaz.study.network.task2;

import ru.ifmo.niyaz.study.network.ByteArrayReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 29.10.12
 * Time: 1:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * 1. List of active servers.
 * 2. Read from server and port
 */
public class Client {

    static final long REMOVE_TIMEOUT = 5000;
    static final long PRINT_TIMEOUT = 2000;
    static final int PORT_TO_LISTEN = 9999;
    static final int MAX_PACKET_LEN = 10000;

    public static void main(String[] args) throws IOException,
            InterruptedException {
        try (DatagramSocket ds = new DatagramSocket(null)) {
            ds.bind(new InetSocketAddress(PORT_TO_LISTEN));
            DatagramPacket dp = new DatagramPacket(new byte[MAX_PACKET_LEN], MAX_PACKET_LEN);
            final Map<User, Long> users = new HashMap<>();
            new Thread() {
                @Override
                public void run() {
                    while (!currentThread().isInterrupted()) {
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
                                    e.remove();
                                }
                            }
                            printAllServers(users);
                        }
                    }
                }
            }.start();
            while (!Thread.currentThread().isInterrupted()) {
                ds.receive(dp);
                synchronized (users) {
                    User user = readUser(dp);
                    if (user == null) {
                        System.err.println("bad user");
                        continue;
                    }
                    users.remove(user);
                    users.put(user, System.currentTimeMillis());
                }
            }
        }
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
            String hostName = reader.readLine(40);
            long timeStamp = reader.readLong();
            String userName = reader.readLine(400);
            int type = reader.readInt();
            int port = reader.readInt();
            return new User(hostAddress, hostName, timeStamp, userName, type, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("bad user");
            return null;
        }
    }

}


//
//static DatagramSocket ds;
//
//    public static void main(String[] args) throws IOException {
//
//        DatagramSocket socket = new DatagramSocket();
//        while (true) {
//            List<Byte> z = new ArrayList<>();
//            System.err.println(InetAddress.getLocalHost().getHostAddress());
//            addMessage(z, InetAddress.getLocalHost().getHostAddress());
//            addMessage(z, InetAddress.getLocalHost().getHostName());
//            addMessage(z, "niyaz.nigmatullin");
//            addLong(z, System.currentTimeMillis() / 1000);
//            byte[] f = new byte[z.size()];
//            for (int i = 0; i < z.size(); i++) {
//                f[i] = z.get(i);
//            }
//            DatagramPacket dp = new DatagramPacket(f, f.length);
//            dp.setSocketAddress(new InetSocketAddress("192.168.1.34", 9999));
//            socket.send(dp);
//            System.err.println("sent");
//            Thread.sleep(12000);
//        }
//        ds = new DatagramSocket(new InetSocketAddress(InetAddress.getLocalHost().getHostName(), 9999));
//        DatagramPacket dp = new DatagramPacket(new byte[10000], 0, 10000);
//        while (true) {
//            ds.receive(dp);
//            ByteArrayReader r = new ByteArrayReader(new ByteArrayInputStream(dp.getData(), dp.getOffset(),
//                    dp.getLength()));
//            System.err.println(r.nextLine());
//            System.err.println(r.nextLine());
//            System.err.println(r.nextLine());
//        }
//    }
//}
//
//
//import java.io.*;
//import java.net.*;
//import java.util.*;
//
//public class Server {
