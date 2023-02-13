package cn.cyberict.ncha.business.commons.utils;

import java.io.ByteArrayOutputStream;


public class SachByteArrayOutputStream extends ByteArrayOutputStream {

    public SachByteArrayOutputStream() {
        super();
    }

    public byte[] getBytes() {
        return super.buf;
    }

    public int getMyCount() {
        return super.count;
    }

}
