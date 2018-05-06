package org.telegram.messenger;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;
import jawnae.pyronet.PyroClient;
import jawnae.pyronet.PyroClientAdapter;
import jawnae.pyronet.PyroSelector;

public class TcpConnection extends PyroClientAdapter {
    static volatile Integer nextChannelToken = Integer.valueOf(1);
    private static PyroSelector selector;
    public volatile int channelToken = 0;
    private PyroClient client;
    public TcpConnectionState connectionState;
    private int datacenterId;
    public TcpConnectionDelegate delegate;
    private int failedConnectionCount;
    private boolean firstPacket;
    private boolean hasSomeDataSinceLastConnect = false;
    private String hostAddress;
    private int hostPort;
    private boolean isNextPort = false;
    private long lastMessageId = 0;
    private Timer reconnectTimer;
    private ByteBuffer restOfTheData;
    private final Integer timerSync = Integer.valueOf(1);
    public int transportRequestClass;
    private int willRetryConnectCount = 5;

    class C04231 implements Runnable {

        class C04201 implements Runnable {
            C04201() {
            }

            public void run() {
                TcpConnection.this.delegate.tcpConnectionClosed(TcpConnection.this);
            }
        }

        class C04222 extends TimerTask {

            class C04211 implements Runnable {
                C04211() {
                }

                public void run() {
                    try {
                        synchronized (TcpConnection.this.timerSync) {
                            if (TcpConnection.this.reconnectTimer != null) {
                                TcpConnection.this.reconnectTimer.cancel();
                                TcpConnection.this.reconnectTimer = null;
                            }
                        }
                    } catch (Exception e2) {
                        FileLog.m799e("tmessages", e2);
                    }
                    TcpConnection.this.connect();
                }
            }

            C04222() {
            }

            public void run() {
                TcpConnection.selector.scheduleTask(new C04211());
            }
        }

        C04231() {
        }

        public void run() {
            long j = 300;
            if ((TcpConnection.this.connectionState != TcpConnectionState.TcpConnectionStageConnected && TcpConnection.this.connectionState != TcpConnectionState.TcpConnectionStageConnecting) || TcpConnection.this.client == null) {
                TcpConnection.this.connectionState = TcpConnectionState.TcpConnectionStageConnecting;
                try {
                    synchronized (TcpConnection.this.timerSync) {
                        if (TcpConnection.this.reconnectTimer != null) {
                            TcpConnection.this.reconnectTimer.cancel();
                            TcpConnection.this.reconnectTimer = null;
                        }
                    }
                    try {
                        Datacenter datacenter = ConnectionsManager.Instance.datacenterWithId(TcpConnection.this.datacenterId);
                        TcpConnection.this.hostAddress = datacenter.getCurrentAddress();
                        TcpConnection.this.hostPort = datacenter.getCurrentPort();
                        FileLog.m798d("tmessages", String.format(TcpConnection.this + " Connecting (%s:%d)", new Object[]{TcpConnection.this.hostAddress, Integer.valueOf(TcpConnection.this.hostPort)}));
                        TcpConnection.this.firstPacket = true;
                        TcpConnection.this.restOfTheData = null;
                        TcpConnection.this.hasSomeDataSinceLastConnect = false;
                        if (TcpConnection.this.client != null) {
                            TcpConnection.this.client.removeListener(TcpConnection.this);
                            TcpConnection.this.client.dropConnection();
                            TcpConnection.this.client = null;
                        }
                        TcpConnection.this.client = TcpConnection.selector.connect(new InetSocketAddress(TcpConnection.this.hostAddress, TcpConnection.this.hostPort));
                        TcpConnection.this.client.addListener(TcpConnection.this);
                        if (TcpConnection.this.isNextPort) {
                            TcpConnection.this.client.setTimeout(8000);
                        } else {
                            TcpConnection.this.client.setTimeout(35000);
                        }
                        TcpConnection.selector.wakeup();
                    } catch (Exception e) {
                        try {
                            synchronized (TcpConnection.this.timerSync) {
                                if (TcpConnection.this.reconnectTimer != null) {
                                    TcpConnection.this.reconnectTimer.cancel();
                                    TcpConnection.this.reconnectTimer = null;
                                }
                                TcpConnection.this.connectionState = TcpConnectionState.TcpConnectionStageReconnecting;
                                if (TcpConnection.this.delegate != null) {
                                    Utilities.stageQueue.postRunnable(new C04201());
                                }
                                TcpConnection.this.failedConnectionCount = TcpConnection.this.failedConnectionCount + 1;
                                if (TcpConnection.this.failedConnectionCount == 1) {
                                    if (TcpConnection.this.hasSomeDataSinceLastConnect) {
                                        TcpConnection.this.willRetryConnectCount = 5;
                                    } else {
                                        TcpConnection.this.willRetryConnectCount = 1;
                                    }
                                }
                                if (ConnectionsManager.isNetworkOnline()) {
                                    TcpConnection.this.isNextPort = true;
                                    if (TcpConnection.this.failedConnectionCount > TcpConnection.this.willRetryConnectCount) {
                                        ConnectionsManager.Instance.datacenterWithId(TcpConnection.this.datacenterId).nextAddressOrPort();
                                        TcpConnection.this.failedConnectionCount = 0;
                                    }
                                }
                                FileLog.m799e("tmessages", e);
                                FileLog.m798d("tmessages", "Reconnect " + TcpConnection.this.hostAddress + ":" + TcpConnection.this.hostPort + " " + TcpConnection.this);
                                try {
                                    long j2;
                                    TcpConnection.this.reconnectTimer = new Timer();
                                    Timer access$200 = TcpConnection.this.reconnectTimer;
                                    TimerTask c04222 = new C04222();
                                    if (TcpConnection.this.failedConnectionCount >= 3) {
                                        j2 = 500;
                                    } else {
                                        j2 = 300;
                                    }
                                    if (TcpConnection.this.failedConnectionCount >= 3) {
                                        j = 500;
                                    }
                                    access$200.schedule(c04222, j2, j);
                                } catch (Exception e3) {
                                    FileLog.m799e("tmessages", e3);
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.m799e("tmessages", e2);
                        }
                    }
                } catch (Exception e22) {
                    FileLog.m799e("tmessages", e22);
                }
            }
        }
    }

    class C04242 implements Runnable {
        C04242() {
        }

        public void run() {
            TcpConnection.this.delegate.tcpConnectionClosed(TcpConnection.this);
        }
    }

    class C04253 implements Runnable {
        C04253() {
        }

        public void run() {
            TcpConnection.this.suspendConnectionInternal();
        }
    }

    class C04308 implements Runnable {
        C04308() {
        }

        public void run() {
            if (TcpConnection.this.delegate != null) {
                TcpConnection.this.delegate.tcpConnectionClosed(TcpConnection.this);
            }
        }
    }

    class C04329 extends TimerTask {

        class C04311 implements Runnable {
            C04311() {
            }

            public void run() {
                try {
                    synchronized (TcpConnection.this.timerSync) {
                        if (TcpConnection.this.reconnectTimer != null) {
                            TcpConnection.this.reconnectTimer.cancel();
                            TcpConnection.this.reconnectTimer = null;
                        }
                    }
                } catch (Exception e2) {
                    FileLog.m799e("tmessages", e2);
                }
                TcpConnection.this.connect();
            }
        }

        C04329() {
        }

        public void run() {
            TcpConnection.selector.scheduleTask(new C04311());
        }
    }

    public interface TcpConnectionDelegate {
        void tcpConnectionClosed(TcpConnection tcpConnection);

        void tcpConnectionConnected(TcpConnection tcpConnection);

        void tcpConnectionProgressChanged(TcpConnection tcpConnection, long j, int i, int i2);

        void tcpConnectionQuiackAckReceived(TcpConnection tcpConnection, int i);

        void tcpConnectionReceivedData(TcpConnection tcpConnection, byte[] bArr);
    }

    public enum TcpConnectionState {
        TcpConnectionStageIdle,
        TcpConnectionStageConnecting,
        TcpConnectionStageReconnecting,
        TcpConnectionStageConnected,
        TcpConnectionStageSuspended
    }

    public TcpConnection(int did) {
        if (selector == null) {
            selector = new PyroSelector();
            selector.spawnNetworkThread("network thread");
        }
        this.datacenterId = did;
        this.connectionState = TcpConnectionState.TcpConnectionStageIdle;
    }

    static int generateChannelToken() {
        Integer num = nextChannelToken;
        nextChannelToken = Integer.valueOf(nextChannelToken.intValue() + 1);
        return num.intValue();
    }

    public int getDatacenterId() {
        return this.datacenterId;
    }

    public void connect() {
        selector.scheduleTask(new C04231());
    }

    private void suspendConnectionInternal() {
        synchronized (this.timerSync) {
            if (this.reconnectTimer != null) {
                this.reconnectTimer.cancel();
                this.reconnectTimer = null;
            }
        }
        if (this.connectionState != TcpConnectionState.TcpConnectionStageIdle && this.connectionState != TcpConnectionState.TcpConnectionStageSuspended) {
            FileLog.m798d("tmessages", "suspend connnection " + this);
            this.connectionState = TcpConnectionState.TcpConnectionStageSuspended;
            if (this.client != null) {
                this.client.removeListener(this);
                this.client.dropConnection();
                this.client = null;
            }
            if (this.delegate != null) {
                Utilities.stageQueue.postRunnable(new C04242());
            }
            this.firstPacket = true;
            this.restOfTheData = null;
            this.channelToken = 0;
        }
    }

    public void suspendConnection(boolean task) {
        if (task) {
            selector.scheduleTask(new C04253());
        } else {
            suspendConnectionInternal();
        }
    }

    public void resumeConnection() {
    }

    private void reconnect() {
        suspendConnection(false);
        this.connectionState = TcpConnectionState.TcpConnectionStageReconnecting;
        connect();
    }

    public void sendData(final byte[] data, final boolean reportAck, boolean startResponseTimeout) {
        selector.scheduleTask(new Runnable() {
            public void run() {
                if (TcpConnection.this.connectionState == TcpConnectionState.TcpConnectionStageIdle || TcpConnection.this.connectionState == TcpConnectionState.TcpConnectionStageReconnecting || TcpConnection.this.connectionState == TcpConnectionState.TcpConnectionStageSuspended || TcpConnection.this.client == null) {
                    TcpConnection.this.connect();
                }
                if (TcpConnection.this.client != null && !TcpConnection.this.client.isDisconnected()) {
                    int i;
                    int packetLength = data.length / 4;
                    SerializedData buffer = new SerializedData();
                    if (packetLength < 127) {
                        if (reportAck) {
                            packetLength |= 128;
                        }
                        buffer.writeByte(packetLength);
                    } else {
                        packetLength = (packetLength << 8) + 127;
                        if (reportAck) {
                            packetLength |= 128;
                        }
                        buffer.writeInt32(packetLength);
                    }
                    buffer.writeRaw(data);
                    byte[] packet = buffer.toByteArray();
                    if (TcpConnection.this.firstPacket) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    ByteBuffer sendBuffer = ByteBuffer.allocate(i + packet.length);
                    sendBuffer.rewind();
                    sendBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    if (TcpConnection.this.firstPacket) {
                        sendBuffer.put((byte) -17);
                        TcpConnection.this.firstPacket = false;
                    }
                    sendBuffer.put(packet);
                    sendBuffer.rewind();
                    TcpConnection.this.client.write(sendBuffer);
                }
            }
        });
    }

    private void readData(ByteBuffer buffer) throws Exception {
        if (this.restOfTheData != null) {
            ByteBuffer newBuffer = ByteBuffer.allocate(this.restOfTheData.limit() + buffer.limit());
            newBuffer.put(this.restOfTheData);
            newBuffer.put(buffer);
            buffer = newBuffer;
            this.restOfTheData = null;
        }
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();
        while (buffer.hasRemaining()) {
            if (!this.hasSomeDataSinceLastConnect) {
                ConnectionsManager.Instance.datacenterWithId(this.datacenterId).storeCurrentAddressAndPortNum();
                this.isNextPort = false;
                this.client.setTimeout(35000);
            }
            this.hasSomeDataSinceLastConnect = true;
            buffer.mark();
            byte fByte = buffer.get();
            if ((fByte & 128) != 0) {
                buffer.reset();
                if (buffer.remaining() < 4) {
                    this.restOfTheData = ByteBuffer.allocate(buffer.remaining());
                    this.restOfTheData.put(buffer);
                    this.restOfTheData.rewind();
                    return;
                }
                buffer.order(ByteOrder.BIG_ENDIAN);
                final int ackId = buffer.getInt() & Integer.MAX_VALUE;
                if (this.delegate != null) {
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            TcpConnection.this.delegate.tcpConnectionQuiackAckReceived(TcpConnection.this, ackId);
                        }
                    });
                }
                buffer.order(ByteOrder.LITTLE_ENDIAN);
            } else {
                int currentPacketLength;
                if (fByte != Byte.MAX_VALUE) {
                    currentPacketLength = fByte * 4;
                } else {
                    buffer.reset();
                    if (buffer.remaining() < 4) {
                        this.restOfTheData = ByteBuffer.allocate(buffer.remaining());
                        this.restOfTheData.put(buffer);
                        this.restOfTheData.rewind();
                        return;
                    }
                    currentPacketLength = (buffer.getInt() >> 8) * 4;
                }
                if (currentPacketLength % 4 != 0 || currentPacketLength > AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END) {
                    FileLog.m800e("tmessages", "Invalid packet length");
                    reconnect();
                    return;
                }
                if (currentPacketLength < buffer.remaining()) {
                    FileLog.m798d("tmessages", this + " Received message len " + currentPacketLength + " but packet larger " + buffer.remaining());
                    this.lastMessageId = 0;
                } else if (currentPacketLength == buffer.remaining()) {
                    FileLog.m798d("tmessages", this + " Received message len " + currentPacketLength + " equal to packet size");
                    this.lastMessageId = 0;
                } else {
                    FileLog.m798d("tmessages", this + " Received packet size less(" + buffer.remaining() + ") then message size(" + currentPacketLength + ")");
                    if (buffer.remaining() >= 152 && (this.transportRequestClass & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                        if (this.lastMessageId == 0) {
                            byte[] temp = new byte[152];
                            buffer.get(temp);
                            this.lastMessageId = ConnectionsManager.Instance.needsToDecodeMessageIdFromPartialData(this, temp);
                        }
                        if (!(this.lastMessageId == -1 || this.lastMessageId == 0 || this.delegate == null)) {
                            final int arg2 = buffer.remaining();
                            final int arg3 = currentPacketLength;
                            Utilities.stageQueue.postRunnable(new Runnable() {
                                public void run() {
                                    TcpConnection.this.delegate.tcpConnectionProgressChanged(TcpConnection.this, TcpConnection.this.lastMessageId, arg2, arg3);
                                }
                            });
                        }
                    }
                    buffer.reset();
                    this.restOfTheData = ByteBuffer.allocate(buffer.remaining());
                    this.restOfTheData.order(ByteOrder.LITTLE_ENDIAN);
                    this.restOfTheData.put(buffer);
                    this.restOfTheData.rewind();
                    return;
                }
                final byte[] packetData = new byte[currentPacketLength];
                buffer.get(packetData);
                if (this.delegate != null) {
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            TcpConnection.this.delegate.tcpConnectionReceivedData(TcpConnection.this, packetData);
                        }
                    });
                }
            }
        }
    }

    public void handleDisconnect(PyroClient client, Exception e) {
        long j = 300;
        synchronized (this.timerSync) {
            if (this.reconnectTimer != null) {
                this.reconnectTimer.cancel();
                this.reconnectTimer = null;
            }
        }
        if (e != null) {
            FileLog.m798d("tmessages", "Disconnected " + this + " with error " + e);
        } else {
            FileLog.m798d("tmessages", "Disconnected " + this);
        }
        this.firstPacket = true;
        this.restOfTheData = null;
        this.channelToken = 0;
        if (!(this.connectionState == TcpConnectionState.TcpConnectionStageSuspended || this.connectionState == TcpConnectionState.TcpConnectionStageIdle)) {
            this.connectionState = TcpConnectionState.TcpConnectionStageIdle;
        }
        if (this.delegate != null) {
            Utilities.stageQueue.postRunnable(new C04308());
        }
        if (this.connectionState == TcpConnectionState.TcpConnectionStageIdle && (this.transportRequestClass & RPCRequest.RPCRequestClassGeneric) != 0) {
            if (this.datacenterId == ConnectionsManager.Instance.currentDatacenterId || this.datacenterId == ConnectionsManager.Instance.movingToDatacenterId) {
                this.failedConnectionCount++;
                if (this.failedConnectionCount == 1) {
                    if (this.hasSomeDataSinceLastConnect) {
                        this.willRetryConnectCount = 5;
                    } else {
                        this.willRetryConnectCount = 1;
                    }
                }
                if (ConnectionsManager.isNetworkOnline()) {
                    this.isNextPort = true;
                    if (this.failedConnectionCount > this.willRetryConnectCount) {
                        ConnectionsManager.Instance.datacenterWithId(this.datacenterId).nextAddressOrPort();
                        this.failedConnectionCount = 0;
                    }
                }
                FileLog.m798d("tmessages", "Reconnect " + this.hostAddress + ":" + this.hostPort + " " + this);
                try {
                    long j2;
                    this.reconnectTimer = new Timer();
                    Timer timer = this.reconnectTimer;
                    TimerTask c04329 = new C04329();
                    if (this.failedConnectionCount > 3) {
                        j2 = 500;
                    } else {
                        j2 = 300;
                    }
                    if (this.failedConnectionCount > 3) {
                        j = 500;
                    }
                    timer.schedule(c04329, j2, j);
                } catch (Exception e3) {
                    FileLog.m799e("tmessages", e3);
                }
            }
        }
    }

    public void connectedClient(PyroClient client) {
        this.connectionState = TcpConnectionState.TcpConnectionStageConnected;
        this.channelToken = generateChannelToken();
        FileLog.m798d("tmessages", String.format(this + " Connected (%s:%d)", new Object[]{this.hostAddress, Integer.valueOf(this.hostPort)}));
        if (this.delegate != null) {
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    TcpConnection.this.delegate.tcpConnectionConnected(TcpConnection.this);
                }
            });
        }
    }

    public void unconnectableClient(PyroClient client, Exception cause) {
        handleDisconnect(client, cause);
    }

    public void droppedClient(PyroClient client, IOException cause) {
        super.droppedClient(client, cause);
        handleDisconnect(client, cause);
    }

    public void disconnectedClient(PyroClient client) {
        handleDisconnect(client, null);
    }

    public void receivedData(PyroClient client, ByteBuffer data) {
        try {
            this.failedConnectionCount = 0;
            readData(data);
        } catch (Exception e) {
            FileLog.m798d("tmessages", "read data error");
            reconnect();
        }
    }

    public void sentData(PyroClient client, int bytes) {
        this.failedConnectionCount = 0;
        FileLog.m798d("tmessages", this + " bytes sent " + bytes);
    }
}
