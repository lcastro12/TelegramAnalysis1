package jawnae.pyronet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteStream {
    private final List<ByteBuffer> queue = new ArrayList();

    public void append(ByteBuffer buf) {
        if (buf == null) {
            throw new NullPointerException();
        }
        this.queue.add(buf);
    }

    public boolean hasData() {
        int size = this.queue.size();
        for (int i = 0; i < size; i++) {
            if (((ByteBuffer) this.queue.get(i)).hasRemaining()) {
                return true;
            }
        }
        return false;
    }

    public int getByteCount() {
        int sum = 0;
        for (int i = 0; i < this.queue.size(); i++) {
            sum += ((ByteBuffer) this.queue.get(i)).remaining();
        }
        return sum;
    }

    public void get(ByteBuffer dst) {
        if (dst == null) {
            throw new NullPointerException();
        }
        for (ByteBuffer data : this.queue) {
            ByteBuffer data2 = data2.slice();
            if (data2.remaining() > dst.remaining()) {
                data2.limit(dst.remaining());
                dst.put(data2);
                return;
            }
            dst.put(data2);
            if (!dst.hasRemaining()) {
                return;
            }
        }
    }

    public void discard(int count) {
        int original = count;
        while (count > 0) {
            ByteBuffer data = (ByteBuffer) this.queue.get(0);
            if (count < data.remaining()) {
                data.position(data.position() + count);
                count = 0;
                break;
            }
            this.queue.remove(0);
            count -= data.remaining();
        }
        if (count != 0) {
            throw new PyroException("discarded " + (original - count) + "/" + original + " bytes");
        }
    }

    public byte read() {
        ByteBuffer data = (ByteBuffer) this.queue.get(0);
        byte result = data.get();
        if (!data.hasRemaining()) {
            this.queue.remove(0);
        }
        return result;
    }
}
