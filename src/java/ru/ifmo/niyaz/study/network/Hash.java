package ru.ifmo.niyaz.study.network;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: niyaz.nigmatullin
 * Date: 21.12.12
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
public class Hash {
    private byte[] hash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hash hash1 = (Hash) o;

        if (!Arrays.equals(hash, hash1.hash)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hash != null ? Arrays.hashCode(hash) : 0;
    }

    public Hash(byte[] hash) {
        this.hash = hash.clone();
    }

    public Hash(String s) {
        hash = fromHex(s);
    }

    @Override
    public String toString() {
        return new String(toHex(hash));
    }

    public byte[] getByteArray() {
        return hash.clone();
    }

    static byte[] fromHex(String s) {
        if ((s.length() & 1) == 1) {
            return null;
        }
        byte[] ret = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            ret[i / 2] = (byte) Integer.parseInt(s.substring(i, i + 2), 16);
        }
        return ret;
    }

    static String toHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (byte e : a) {
            String put = Integer.toHexString(e & 0xFF);
            while (put.length() < 2) {
                put = "0" + put;
            }
            sb.append(put);
        }
        return sb.toString();
    }
}
