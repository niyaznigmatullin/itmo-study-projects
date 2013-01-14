package ru.ifmo.niyaz.study.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 29.10.12
 * Time: 1:56
 * To change this template use File | Settings | File Templates.
 */
public class ByteArrayReader extends DataInputStream {

    public ByteArrayReader(InputStream is) {
        super(is);
    }

    public String readLine(int maxLen) throws IOException {
        maxLen = Math.min(maxLen, 65535);
        int x = readShort();
        if (x != 0) {
            throw new IOException("expected string length in range [0, " + maxLen + "]");
        }
        return readUTF();
    }

    public Hash readHash() throws IOException {
        int x = readInt();
        if (x < 0 || x > 65535) {
            throw new IOException("too long hash " + x);
        }
        byte[] e = new byte[x];
        read(e);
        return new Hash(e);
    }

}
