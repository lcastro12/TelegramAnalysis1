package org.telegram.messenger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SerializedData {
    private DataInputStream in;
    private ByteArrayInputStream inbuf;
    protected boolean isOut;
    private DataOutputStream out;
    private ByteArrayOutputStream outbuf;

    public SerializedData() {
        this.isOut = true;
        this.outbuf = new ByteArrayOutputStream();
        this.out = new DataOutputStream(this.outbuf);
    }

    public SerializedData(int size) {
        this.isOut = true;
        this.outbuf = new ByteArrayOutputStream(size);
        this.out = new DataOutputStream(this.outbuf);
    }

    public SerializedData(byte[] data) {
        this.isOut = true;
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(data);
        this.in = new DataInputStream(this.inbuf);
    }

    public SerializedData(File file) throws IOException {
        this.isOut = true;
        FileInputStream is = new FileInputStream(file);
        byte[] data = new byte[((int) file.length())];
        new DataInputStream(is).readFully(data);
        is.close();
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(data);
        this.in = new DataInputStream(this.inbuf);
    }

    public void writeInt32(int x) {
        writeInt32(x, this.out);
    }

    protected void writeInt32(int x, DataOutputStream out) {
        int i = 0;
        while (i < 4) {
            try {
                out.write(x >> (i * 8));
                i++;
            } catch (IOException e) {
                FileLog.m800e("tmessages", "write int32 error");
                return;
            }
        }
    }

    public void writeInt64(long i) {
        writeInt64(i, this.out);
    }

    protected void writeInt64(long x, DataOutputStream out) {
        int i = 0;
        while (i < 8) {
            try {
                out.write((int) (x >> (i * 8)));
                i++;
            } catch (IOException e) {
                FileLog.m800e("tmessages", "write int64 error");
                return;
            }
        }
    }

    public boolean readBool() {
        int consructor = readInt32();
        if (consructor == -1720552011) {
            return true;
        }
        if (consructor == -1132882121) {
            return false;
        }
        FileLog.m800e("tmessages", "Not bool value!");
        return false;
    }

    public void writeBool(boolean value) {
        if (value) {
            writeInt32(-1720552011);
        } else {
            writeInt32(-1132882121);
        }
    }

    public int readInt32() {
        return readInt32(null);
    }

    public int readInt32(boolean[] error) {
        int i = 0;
        int j = 0;
        while (j < 4) {
            try {
                i |= this.in.read() << (j * 8);
                j++;
            } catch (IOException e) {
                if (error != null) {
                    error[0] = true;
                }
                FileLog.m800e("tmessages", "read int32 error");
                return 0;
            }
        }
        if (error == null) {
            return i;
        }
        error[0] = false;
        return i;
    }

    public long readInt64() {
        return readInt64(null);
    }

    public long readInt64(boolean[] error) {
        long i = 0;
        int j = 0;
        while (j < 8) {
            try {
                i |= ((long) this.in.read()) << (j * 8);
                j++;
            } catch (IOException e) {
                if (error != null) {
                    error[0] = true;
                }
                FileLog.m800e("tmessages", "read int64 error");
                return 0;
            }
        }
        if (error == null) {
            return i;
        }
        error[0] = false;
        return i;
    }

    public void writeRaw(byte[] b) {
        try {
            this.out.write(b);
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write raw error");
        }
    }

    public void writeRaw(byte[] b, int offset, int count) {
        try {
            this.out.write(b, offset, count);
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write raw error");
        }
    }

    public void writeByte(int i) {
        try {
            this.out.writeByte((byte) i);
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write byte error");
        }
    }

    public void writeByte(byte b) {
        try {
            this.out.writeByte(b);
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write byte error");
        }
    }

    public void readRaw(byte[] b) {
        try {
            this.in.read(b);
        } catch (Exception e) {
            FileLog.m800e("tmessages", "read raw error");
        }
    }

    public byte[] readData(int count) {
        byte[] arr = new byte[count];
        readRaw(arr);
        return arr;
    }

    public String readString() {
        int sl = 1;
        try {
            int l = this.in.read();
            if (l >= 254) {
                l = (this.in.read() | (this.in.read() << 8)) | (this.in.read() << 16);
                sl = 4;
            }
            byte[] b = new byte[l];
            this.in.read(b);
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.in.read();
            }
            return new String(b, "UTF-8");
        } catch (Exception e) {
            FileLog.m800e("tmessages", "read string error");
            return null;
        }
    }

    public byte[] readByteArray() {
        int sl = 1;
        try {
            int l = this.in.read();
            if (l >= 254) {
                l = (this.in.read() | (this.in.read() << 8)) | (this.in.read() << 16);
                sl = 4;
            }
            byte[] bArr = new byte[l];
            this.in.read(bArr);
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.in.read();
            }
            return bArr;
        } catch (Exception e) {
            FileLog.m800e("tmessages", "read byte array error");
            return null;
        }
    }

    public void writeByteArray(byte[] b) {
        try {
            if (b.length <= 253) {
                this.out.write(b.length);
            } else {
                this.out.write(254);
                this.out.write(b.length);
                this.out.write(b.length >> 8);
                this.out.write(b.length >> 16);
            }
            this.out.write(b);
            int i = b.length <= 253 ? 1 : 4;
            while ((b.length + i) % 4 != 0) {
                this.out.write(0);
                i++;
            }
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write byte array error");
        }
    }

    public void writeString(String s) {
        try {
            writeByteArray(s.getBytes("UTF-8"));
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write string error");
        }
    }

    public void writeByteArray(byte[] b, int offset, int count) {
        if (count <= 253) {
            try {
                this.out.write(count);
            } catch (Exception e) {
                FileLog.m800e("tmessages", "write byte array error");
                return;
            }
        }
        this.out.write(254);
        this.out.write(count);
        this.out.write(count >> 8);
        this.out.write(count >> 16);
        this.out.write(b, offset, count);
        int i = count <= 253 ? 1 : 4;
        while ((count + i) % 4 != 0) {
            this.out.write(0);
            i++;
        }
    }

    public double readDouble() {
        try {
            return Double.longBitsToDouble(readInt64());
        } catch (Exception e) {
            FileLog.m800e("tmessages", "read double error");
            return 0.0d;
        }
    }

    public void writeDouble(double d) {
        try {
            writeInt64(Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            FileLog.m800e("tmessages", "write double error");
        }
    }

    public int length() {
        return this.isOut ? this.outbuf.size() : this.inbuf.available();
    }

    protected void set(byte[] newData) {
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(newData);
        this.in = new DataInputStream(this.inbuf);
    }

    public byte[] toByteArray() {
        return this.outbuf.toByteArray();
    }
}
