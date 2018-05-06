package jawnae.pyronet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PyroSelector {
    public static final int BUFFER_SIZE = 65536;
    public static boolean DO_NOT_CHECK_NETWORK_THREAD = true;
    final ByteBuffer networkBuffer = ByteBuffer.allocateDirect(65536);
    Thread networkThread;
    final Selector nioSelector;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue();

    class C02671 implements Runnable {
        C02671() {
        }

        public void run() {
            PyroSelector.this.networkThread = Thread.currentThread();
            while (true) {
                try {
                    PyroSelector.this.select();
                } catch (Exception exc) {
                    throw new IllegalStateException(exc);
                }
            }
        }
    }

    public PyroSelector() {
        try {
            this.nioSelector = Selector.open();
            this.networkThread = Thread.currentThread();
        } catch (IOException exc) {
            throw new PyroException("Failed to open a selector?!", exc);
        }
    }

    public ByteBuffer malloc(int size) {
        return ByteBuffer.allocate(size);
    }

    public ByteBuffer malloc(byte[] array) {
        ByteBuffer copy = malloc(array.length);
        copy.put(array);
        copy.flip();
        return copy;
    }

    public ByteBuffer copy(ByteBuffer buffer) {
        ByteBuffer copy = malloc(buffer.remaining());
        copy.put(buffer);
        buffer.position(buffer.position() - copy.remaining());
        copy.flip();
        return copy;
    }

    public final boolean isNetworkThread() {
        if (DO_NOT_CHECK_NETWORK_THREAD || this.networkThread == Thread.currentThread()) {
            return true;
        }
        return false;
    }

    public final Thread networkThread() {
        return this.networkThread;
    }

    public final void checkThread() {
        if (!DO_NOT_CHECK_NETWORK_THREAD && !isNetworkThread()) {
            throw new PyroException("call from outside the network-thread, you must schedule tasks");
        }
    }

    public PyroClient connect(InetSocketAddress host) throws IOException {
        return connect(host, null);
    }

    public PyroClient connect(InetSocketAddress host, InetSocketAddress bind) throws IOException {
        try {
            return new PyroClient(this, bind, host);
        } catch (IOException exc) {
            throw exc;
        }
    }

    public void select() {
        select(10);
    }

    public void select(long eventTimeout) {
        checkThread();
        executePendingTasks();
        performNioSelect(eventTimeout);
        long now = System.currentTimeMillis();
        handleSelectedKeys(now);
        handleSocketTimeouts(now);
    }

    private void executePendingTasks() {
        while (true) {
            Runnable task = (Runnable) this.tasks.poll();
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable cause) {
                    cause.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    private final void performNioSelect(long timeout) {
        try {
            int select = this.nioSelector.select(timeout);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private final void handleSelectedKeys(long now) {
        Iterator<SelectionKey> keys = this.nioSelector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = (SelectionKey) keys.next();
            keys.remove();
            if (key.channel() instanceof SocketChannel) {
                ((PyroClient) key.attachment()).onInterestOp(now);
            }
        }
    }

    private final void handleSocketTimeouts(long now) {
        for (SelectionKey key : this.nioSelector.keys()) {
            if (key.channel() instanceof SocketChannel) {
                PyroClient client = (PyroClient) key.attachment();
                if (client.didTimeout(now)) {
                    try {
                        throw new SocketTimeoutException("PyroNet detected NIO timeout");
                    } catch (SocketTimeoutException exc) {
                        client.onConnectionError(exc);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public void spawnNetworkThread(String name) {
        this.networkThread = null;
        new Thread(new C02671(), name).start();
    }

    public void scheduleTask(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        try {
            this.tasks.put(task);
            wakeup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void wakeup() {
        this.nioSelector.wakeup();
    }

    final SelectionKey register(SelectableChannel channel, int ops) throws IOException {
        return channel.register(this.nioSelector, ops);
    }

    final boolean adjustInterestOp(SelectionKey key, int op, boolean state) {
        boolean changed = true;
        checkThread();
        try {
            boolean z;
            int ops = key.interestOps();
            if ((ops & op) == op) {
                z = true;
            } else {
                z = false;
            }
            if (state == z) {
                changed = false;
            }
            if (!changed) {
                return changed;
            }
            key.interestOps(state ? ops | op : (op ^ -1) & ops);
            return changed;
        } catch (CancelledKeyException e) {
            return false;
        }
    }
}
