package ru.ifmo.niyaz.study.network.task4;

import ru.ifmo.niyaz.study.network.ByteArrayReader;

import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 20.12.12
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class MessageReceiver {

    public static void main(String[] args) {
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        try (Socket socket = new Socket(hostName, port)) {
            ByteArrayReader reader = new ByteArrayReader(socket.getInputStream());
            int lines = reader.readInt();
            for (int i = 0; i < lines; i++) {
                try {
                    String message = reader.readLine(10000);
                    System.out.println(message);
                } catch (IOException e) {
                    System.err.println("couldn't read data");
                }
            }
        } catch (IOException e) {
            System.err.println("IOError");
        }
    }

}
