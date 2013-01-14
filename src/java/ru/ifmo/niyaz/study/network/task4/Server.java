package ru.ifmo.niyaz.study.network.task4;

import org.apache.commons.io.FileUtils;
import ru.ifmo.niyaz.study.network.ByteArrayReader;
import ru.ifmo.niyaz.study.network.ByteArrayWriter;
import ru.ifmo.niyaz.study.network.Hash;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 29.10.12
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    static final int PORT_TO_WORK_ON = 9999;
    static Charset cSet = Charset.forName("UTF-8");
    static int currentFileName;

    public static void main(String[] args) throws IOException {
        currentFileName = 0;
        final File folder = new File(args[0]);
        if (folder.exists()) {
            FileUtils.forceDelete(folder);
            if (!folder.mkdirs()) {
                System.err.println("couldn't create folder");
                return;
            }
        }
        final Map<Hash, String> fileNameByHash = new HashMap<>();
        new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    try {
                        doBroadcast(folder);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }.start();
        ServerSocket inputSocket = new ServerSocket(PORT_TO_WORK_ON);
        while (!Thread.currentThread().isInterrupted()) {
            try (Socket socket = inputSocket.accept()) {
                try (ByteArrayReader reader = new ByteArrayReader(socket.getInputStream())) {
                    try (ByteArrayWriter writer = new ByteArrayWriter(socket.getOutputStream())) {
                        int type = reader.readInt();
                        if (type == 0) {
                            writer.writeInt(fileNameByHash.size());
                            for (Hash e : fileNameByHash.keySet()) {
                                writer.writeByteArray(e.getByteArray());
                            }
                        } else if (type == 1) {
                            Hash hash = reader.readHash();
                            String fileName = fileNameByHash.get(hash);
                            if (fileName != null) {
                                writer.writeByte(1);
                                writer.writeByteArray(FileUtils.readFileToByteArray(new File(folder, fileName)));
                            } else {
                                writer.writeByte(0);
                            }
                        } else if (type == 2) {
                            System.out.println("put");
                            byte[] text = reader.readLine(65535).getBytes(cSet);
                            Hash hash = md5(text);
                            if (!fileNameByHash.containsKey(hash)) {
                                String fileName = currentFileName++ + "";
                                fileNameByHash.put(hash, fileName);
                                File file = new File(folder, fileName);
                                FileUtils.writeByteArrayToFile(file, text);
                                System.out.println("put success");
                                writer.writeByte(1);
                            } else {
                                writer.writeByte(0);
                            }
                        } else {
                            System.err.println("bad type = " + type);
                        }
                    }
                }
            }
        }
    }

    private static void doBroadcast(File folder) {
        try (DatagramSocket ds = new DatagramSocket()) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayWriter w = new ByteArrayWriter(os);
            w.writeLine(InetAddress.getLocalHost().getHostAddress());
            w.writeLine(InetAddress.getLocalHost().getHostName());
            w.writeByteArray(getContentID(folder).getByteArray());
            byte[] toSend = os.toByteArray();
            DatagramPacket dp = new DatagramPacket(toSend, 0, toSend.length,
                    new InetSocketAddress("192.168.1.255", PORT_TO_WORK_ON));
            ds.send(dp);
        } catch (IOException e) {
            System.err.println("couldn't broadcast on " + new InetSocketAddress("192.168.1.255", PORT_TO_WORK_ON));
            return;
        }
        System.err.println("did broadcast");
    }

    static Hash getContentID(File folder) {
        StringBuilder sb = new StringBuilder();
        File[] files = folder.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File e : files) {
            sb.append(e.getName()).append(e.length()).append(e.lastModified());
        }
        return md5(sb.toString().getBytes(cSet));
    }

    static Hash md5(byte[] a) {
        try {
            return new Hash(MessageDigest.getInstance("MD5").digest(a));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("couldn't make md5 hash");
            return null;
        }
    }

}
