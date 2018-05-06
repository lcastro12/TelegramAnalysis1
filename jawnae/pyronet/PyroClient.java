package jawnae.pyronet;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PyroClient {
    private Object attachment;
    private boolean doEagerWrite;
    private boolean doShutdown;
    private final SelectionKey key;
    private long lastEventTime;
    private final List<PyroClientListener> listeners;
    private final ByteStream outbound;
    private final PyroSelector selector;
    private long timeout;

    class C02651 implements Runnable {
        C02651() {
        }

        public void run() {
            try {
                if (PyroClient.this.key.channel().isOpen()) {
                    ((SocketChannel) PyroClient.this.key.channel()).close();
                }
            } catch (Exception e) {
                PyroClient.this.selector().scheduleTask(this);
            }
        }
    }

    PyroClient(PyroSelector selector, InetSocketAddress bind, InetSocketAddress host) throws IOException {
        this(selector, bindAndConfigure(selector, SocketChannel.open(), bind));
        ((SocketChannel) this.key.channel()).connect(host);
    }

    PyroClient(PyroSelector selector, SelectionKey key) {
        this.doEagerWrite = false;
        this.doShutdown = false;
        this.timeout = 0;
        this.selector = selector;
        this.selector.checkThread();
        this.key = key;
        this.key.attach(this);
        this.outbound = new ByteStream();
        this.listeners = new CopyOnWriteArrayList();
        this.lastEventTime = System.currentTimeMillis();
    }

    public void addListener(PyroClientListener listener) {
        this.selector.checkThread();
        this.listeners.add(listener);
    }

    public void removeListener(PyroClientListener listener) {
        this.selector.checkThread();
        this.listeners.remove(listener);
    }

    public void removeListeners() {
        this.selector.checkThread();
        this.listeners.clear();
    }

    public PyroSelector selector() {
        return this.selector;
    }

    public void attach(Object attachment) {
        this.attachment = attachment;
    }

    public <T> T attachment() {
        return this.attachment;
    }

    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) ((SocketChannel) this.key.channel()).socket().getLocalSocketAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) ((SocketChannel) this.key.channel()).socket().getRemoteSocketAddress();
    }

    public void setTimeout(int ms) throws IOException {
        this.selector.checkThread();
        ((SocketChannel) this.key.channel()).socket().setSoTimeout(ms);
        this.lastEventTime = System.currentTimeMillis();
        this.timeout = (long) ms;
    }

    public void setLinger(boolean enabled, int seconds) throws IOException {
        this.selector.checkThread();
        ((SocketChannel) this.key.channel()).socket().setSoLinger(enabled, seconds);
    }

    public void setKeepAlive(boolean enabled) throws IOException {
        this.selector.checkThread();
        ((SocketChannel) this.key.channel()).socket().setKeepAlive(enabled);
    }

    public void setEagerWrite(boolean enabled) {
        this.doEagerWrite = enabled;
    }

    public void writeCopy(ByteBuffer data) throws PyroException {
        write(this.selector.copy(data));
    }

    public void write(ByteBuffer data) throws PyroException {
        this.selector.checkThread();
        if (!this.key.isValid()) {
            return;
        }
        if (this.doShutdown) {
            throw new PyroException("shutting down");
        }
        this.outbound.append(data);
        if (this.doEagerWrite) {
            try {
                onReadyToWrite(System.currentTimeMillis());
                return;
            } catch (NotYetConnectedException e) {
                adjustWriteOp();
                return;
            } catch (IOException exc) {
                onConnectionError(exc);
                this.key.cancel();
                return;
            }
        }
        adjustWriteOp();
    }

    public int flush() {
        int total = 0;
        while (this.outbound.hasData()) {
            int written;
            try {
                written = onReadyToWrite(System.currentTimeMillis());
            } catch (IOException e) {
                written = 0;
            }
            if (written == 0) {
                break;
            }
            total += written;
        }
        return total;
    }

    public int flushOrDie() throws PyroException {
        int total = 0;
        while (this.outbound.hasData()) {
            int written;
            try {
                written = onReadyToWrite(System.currentTimeMillis());
            } catch (IOException e) {
                written = 0;
            }
            if (written == 0) {
                throw new PyroException("failed to flush, wrote " + total + " bytes");
            }
            total += written;
        }
        return total;
    }

    public boolean hasDataEnqueued() {
        this.selector.checkThread();
        return this.outbound.hasData();
    }

    public void shutdown() {
        this.selector.checkThread();
        this.doShutdown = true;
        if (!hasDataEnqueued()) {
            dropConnection();
        }
    }

    public void dropConnection() {
        this.selector.checkThread();
        if (!isDisconnected()) {
            new C02651().run();
            onConnectionError("local");
        }
    }

    public boolean isDisconnected() {
        this.selector.checkThread();
        return !((SocketChannel) this.key.channel()).isOpen();
    }

    void onInterestOp(long now) {
        if (this.key.isValid()) {
            try {
                if (this.key.isConnectable()) {
                    onReadyToConnect(now);
                }
                if (this.key.isReadable()) {
                    onReadyToRead(now);
                }
                if (this.key.isWritable()) {
                    onReadyToWrite(now);
                    return;
                }
                return;
            } catch (Exception exc) {
                onConnectionError(exc);
                this.key.cancel();
                return;
            }
        }
        onConnectionError("remote");
    }

    boolean didTimeout(long now) {
        if (this.timeout != 0 && now - this.lastEventTime > this.timeout) {
            return true;
        }
        return false;
    }

    private void onReadyToConnect(long now) throws IOException {
        this.selector.checkThread();
        this.lastEventTime = now;
        this.selector.adjustInterestOp(this.key, 8, false);
        boolean result = ((SocketChannel) this.key.channel()).finishConnect();
        for (PyroClientListener listener : this.listeners) {
            listener.connectedClient(this);
        }
    }

    private void onReadyToRead(long now) throws IOException {
        this.selector.checkThread();
        this.lastEventTime = now;
        SocketChannel channel = (SocketChannel) this.key.channel();
        ByteBuffer buffer = this.selector.networkBuffer;
        buffer.clear();
        if (channel.read(buffer) == -1) {
            throw new EOFException();
        }
        buffer.flip();
        for (PyroClientListener listener : this.listeners) {
            listener.receivedData(this, buffer);
        }
    }

    private int onReadyToWrite(long now) throws IOException {
        this.selector.checkThread();
        int sent = 0;
        ByteBuffer buffer = this.selector.networkBuffer;
        buffer.clear();
        this.outbound.get(buffer);
        buffer.flip();
        if (buffer.hasRemaining()) {
            sent = ((SocketChannel) this.key.channel()).write(buffer);
        }
        if (sent > 0) {
            this.outbound.discard(sent);
        }
        for (PyroClientListener listener : this.listeners) {
            listener.sentData(this, sent);
        }
        adjustWriteOp();
        if (this.doShutdown && !this.outbound.hasData()) {
            dropConnection();
        }
        return sent;
    }

    void onConnectionError(final Object cause) {
        this.selector.checkThread();
        try {
            ((SocketChannel) this.key.channel()).close();
            if (cause instanceof ConnectException) {
                for (PyroClientListener listener : this.listeners) {
                    listener.unconnectableClient(this, (Exception) cause);
                }
            } else if (cause instanceof EOFException) {
                for (PyroClientListener listener2 : this.listeners) {
                    listener2.disconnectedClient(this);
                }
            } else if (cause instanceof IOException) {
                for (PyroClientListener listener22 : this.listeners) {
                    listener22.droppedClient(this, (IOException) cause);
                }
            } else if (!(cause instanceof String)) {
                for (PyroClientListener listener222 : this.listeners) {
                    listener222.unconnectableClient(this, null);
                }
            } else if (cause.equals("local")) {
                for (PyroClientListener listener2222 : this.listeners) {
                    listener2222.disconnectedClient(this);
                }
            } else if (cause.equals("remote")) {
                for (PyroClientListener listener22222 : this.listeners) {
                    listener22222.droppedClient(this, null);
                }
            } else {
                throw new IllegalStateException("illegal cause: " + cause);
            }
        } catch (Exception e) {
            this.selector.scheduleTask(new Runnable() {
                public void run() {
                    PyroClient.this.onConnectionError(cause);
                }
            });
        }
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + getAddressText() + "]";
    }

    private final String getAddressText() {
        if (!this.key.channel().isOpen()) {
            return "closed";
        }
        InetSocketAddress sockaddr = getRemoteAddress();
        if (sockaddr == null) {
            return "connecting";
        }
        return sockaddr.getAddress().getHostAddress() + "@" + sockaddr.getPort();
    }

    void adjustWriteOp() {
        this.selector.checkThread();
        this.selector.adjustInterestOp(this.key, 4, this.outbound.hasData());
    }

    static final SelectionKey bindAndConfigure(PyroSelector selector, SocketChannel channel, InetSocketAddress bind) throws IOException {
        selector.checkThread();
        channel.socket().bind(bind);
        return configure(selector, channel, true);
    }

    static final SelectionKey configure(PyroSelector selector, SocketChannel channel, boolean connect) throws IOException {
        selector.checkThread();
        channel.configureBlocking(false);
        channel.socket().setSoLinger(true, 4);
        channel.socket().setReuseAddress(false);
        channel.socket().setKeepAlive(false);
        channel.socket().setTcpNoDelay(true);
        channel.socket().setReceiveBufferSize(65536);
        channel.socket().setSendBufferSize(65536);
        int ops = 1;
        if (connect) {
            ops = 1 | 8;
        }
        return selector.register(channel, ops);
    }
}
