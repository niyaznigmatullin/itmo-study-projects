package ru.ifmo.niyaz.study.network.task2;

import ru.ifmo.niyaz.study.network.ByteArrayWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 29.10.12
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public class Server {


    static final int PORT_TO_SEND_MESSAGE = 12345;

    public static void main(String[] args) throws IOException {
        File folder = new File("here");
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                System.err.println("couldn't create folder");
                return;
            }
        }
        new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    try {
                        doBroadcast();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }.start();
        ServerSocket inputSocket = new ServerSocket(PORT_TO_SEND_MESSAGE);
        while (!Thread.currentThread().isInterrupted()) {
            try (Socket socket = inputSocket.accept()) {
                    ByteArrayWriter writer = new ByteArrayWriter(socket.getOutputStream());
                List<String> list = new ArrayList<>();
                Scanner sc = new Scanner(new File(args[0]));
                while (sc.hasNextLine()) {
                    list.add(sc.nextLine());
                }
                writer.writeInt(list.size());
                for (String e : list) {
                    writer.writeLine(e);
                }
            }
        }
    }

    private static void doBroadcast() {
        try (DatagramSocket ds = new DatagramSocket()) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayWriter w = new ByteArrayWriter(os);
            w.writeLine(InetAddress.getLocalHost().getHostAddress());
            w.writeLine(InetAddress.getLocalHost().getHostName());
            w.writeLong(System.currentTimeMillis() / 1000);
            w.writeLine("abacabadabacabaeabacabadabacaba");
            w.writeInt(1);
            w.writeInt(PORT_TO_SEND_MESSAGE);
            byte[] toSend = os.toByteArray();
            DatagramPacket dp = new DatagramPacket(toSend, 0, toSend.length, new InetSocketAddress("255.255.255.255", 9999));
            ds.send(dp);
        } catch (IOException e) {
            System.err.println("couldn't broadcast on " + new InetSocketAddress("255.255.255.255", 9999));
            return;
        }
        System.err.println("did broadcast");
    }

}
