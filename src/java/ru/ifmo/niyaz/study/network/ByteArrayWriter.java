package ru.ifmo.niyaz.study.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 29.10.12
 * Time: 1:56
 * To change this template use File | Settings | File Templates.
 */
public class ByteArrayWriter extends DataOutputStream {

    public ByteArrayWriter(OutputStream os) {
        super(os);
    }

    public void writeLine(String s) throws IOException {
        writeShort(0);
        writeUTF(s);
    }

    public void writeByteArray(byte[] a) throws IOException {
        writeInt(a.length);
        write(a);
    }

}
