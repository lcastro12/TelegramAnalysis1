package org.telegram.messenger;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.TL.TLRPC.TL_futureSalt;
import org.telegram.ui.ApplicationLoader;

public class Datacenter {
    private final int DATA_VERSION;
    public ArrayList<String> addresses;
    public long authDownloadSessionId;
    public byte[] authKey;
    public byte[] authKeyId;
    private ArrayList<ServerSalt> authServerSaltSet;
    public long authSessionId;
    public long authUploadSessionId;
    public boolean authorized;
    public TcpConnection connection;
    private volatile int currentAddressNum;
    private volatile int currentPortNum;
    public int datacenterId;
    public int[] defaultPorts;
    public TcpConnection downloadConnection;
    public int lastInitVersion;
    public HashMap<String, Integer> ports;
    public TcpConnection uploadConnection;

    class C03041 implements Runnable {
        C03041() {
        }

        public void run() {
            Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("dataconfig", 0).edit();
            editor.putInt("dc" + Datacenter.this.datacenterId + "port", Datacenter.this.currentPortNum);
            editor.putInt("dc" + Datacenter.this.datacenterId + "address", Datacenter.this.currentAddressNum);
            editor.commit();
        }
    }

    private class SaltComparator implements Comparator<ServerSalt> {
        private SaltComparator() {
        }

        public int compare(ServerSalt o1, ServerSalt o2) {
            if (o1.validSince < o2.validSince) {
                return -1;
            }
            if (o1.validSince > o2.validSince) {
                return 1;
            }
            return 0;
        }
    }

    public Datacenter() {
        this.DATA_VERSION = 3;
        this.addresses = new ArrayList();
        this.ports = new HashMap();
        this.defaultPorts = new int[]{-1, 80, -1, 443, -1, 443, -1, 80, -1, 443, -1};
        this.lastInitVersion = 0;
        this.currentPortNum = 0;
        this.currentAddressNum = 0;
        this.authServerSaltSet = new ArrayList();
        this.authServerSaltSet = new ArrayList();
    }

    public Datacenter(SerializedData data, int version) {
        boolean z = true;
        this.DATA_VERSION = 3;
        this.addresses = new ArrayList();
        this.ports = new HashMap();
        this.defaultPorts = new int[]{-1, 80, -1, 443, -1, 443, -1, 80, -1, 443, -1};
        this.lastInitVersion = 0;
        this.currentPortNum = 0;
        this.currentAddressNum = 0;
        this.authServerSaltSet = new ArrayList();
        String address;
        int len;
        int a;
        ServerSalt salt;
        if (version == 0) {
            this.datacenterId = data.readInt32();
            address = data.readString();
            this.addresses.add(address);
            this.ports.put(address, Integer.valueOf(data.readInt32()));
            len = data.readInt32();
            if (len != 0) {
                this.authKey = data.readData(len);
            }
            len = data.readInt32();
            if (len != 0) {
                this.authKeyId = data.readData(len);
            }
            if (data.readInt32() == 0) {
                z = false;
            }
            this.authorized = z;
            len = data.readInt32();
            for (a = 0; a < len; a++) {
                salt = new ServerSalt();
                salt.validSince = data.readInt32();
                salt.validUntil = data.readInt32();
                salt.value = data.readInt64();
                if (this.authServerSaltSet == null) {
                    this.authServerSaltSet = new ArrayList();
                }
                this.authServerSaltSet.add(salt);
            }
        } else if (version == 1) {
            int currentVersion = data.readInt32();
            if (currentVersion == 2 || currentVersion == 3) {
                this.datacenterId = data.readInt32();
                if (currentVersion == 3) {
                    this.lastInitVersion = data.readInt32();
                }
                len = data.readInt32();
                for (a = 0; a < len; a++) {
                    address = data.readString();
                    this.addresses.add(address);
                    this.ports.put(address, Integer.valueOf(data.readInt32()));
                }
                len = data.readInt32();
                if (len != 0) {
                    this.authKey = data.readData(len);
                }
                len = data.readInt32();
                if (len != 0) {
                    this.authKeyId = data.readData(len);
                }
                if (data.readInt32() == 0) {
                    z = false;
                }
                this.authorized = z;
                len = data.readInt32();
                for (a = 0; a < len; a++) {
                    salt = new ServerSalt();
                    salt.validSince = data.readInt32();
                    salt.validUntil = data.readInt32();
                    salt.value = data.readInt64();
                    if (this.authServerSaltSet == null) {
                        this.authServerSaltSet = new ArrayList();
                    }
                    this.authServerSaltSet.add(salt);
                }
            }
        }
        readCurrentAddressAndPortNum();
    }

    public String getCurrentAddress() {
        if (this.addresses.isEmpty()) {
            return null;
        }
        if (this.currentAddressNum >= this.addresses.size()) {
            this.currentAddressNum = 0;
        }
        return (String) this.addresses.get(this.currentAddressNum);
    }

    public int getCurrentPort() {
        if (this.ports.isEmpty()) {
            return 443;
        }
        if (this.currentPortNum >= this.defaultPorts.length) {
            this.currentPortNum = 0;
        }
        int port = this.defaultPorts[this.currentPortNum];
        if (port != -1) {
            return port;
        }
        return ((Integer) this.ports.get(getCurrentAddress())).intValue();
    }

    public void addAddressAndPort(String address, int port) {
        if (!this.addresses.contains(address)) {
            this.addresses.add(address);
            this.ports.put(address, Integer.valueOf(port));
        }
    }

    public void nextAddressOrPort() {
        if (this.currentPortNum + 1 < this.defaultPorts.length) {
            this.currentPortNum++;
            return;
        }
        if (this.currentAddressNum + 1 < this.addresses.size()) {
            this.currentAddressNum++;
        } else {
            this.currentAddressNum = 0;
        }
        this.currentPortNum = 0;
    }

    public void storeCurrentAddressAndPortNum() {
        Utilities.stageQueue.postRunnable(new C03041());
    }

    private void readCurrentAddressAndPortNum() {
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("dataconfig", 0);
        this.currentPortNum = preferences.getInt("dc" + this.datacenterId + "port", 0);
        this.currentAddressNum = preferences.getInt("dc" + this.datacenterId + "address", 0);
    }

    public void replaceAddressesAndPorts(ArrayList<String> newAddresses, HashMap<String, Integer> newPorts) {
        this.addresses = newAddresses;
        this.ports = newPorts;
    }

    public void SerializeToStream(SerializedData stream) {
        int i;
        stream.writeInt32(3);
        stream.writeInt32(this.datacenterId);
        stream.writeInt32(this.lastInitVersion);
        stream.writeInt32(this.addresses.size());
        Iterator i$ = this.addresses.iterator();
        while (i$.hasNext()) {
            String address = (String) i$.next();
            stream.writeString(address);
            stream.writeInt32(((Integer) this.ports.get(address)).intValue());
        }
        if (this.authKey != null) {
            stream.writeInt32(this.authKey.length);
            stream.writeRaw(this.authKey);
        } else {
            stream.writeInt32(0);
        }
        if (this.authKeyId != null) {
            stream.writeInt32(this.authKeyId.length);
            stream.writeRaw(this.authKeyId);
        } else {
            stream.writeInt32(0);
        }
        if (this.authorized) {
            i = 1;
        } else {
            i = 0;
        }
        stream.writeInt32(i);
        stream.writeInt32(this.authServerSaltSet.size());
        i$ = this.authServerSaltSet.iterator();
        while (i$.hasNext()) {
            ServerSalt salt = (ServerSalt) i$.next();
            stream.writeInt32(salt.validSince);
            stream.writeInt32(salt.validUntil);
            stream.writeInt64(salt.value);
        }
    }

    public void clear() {
        this.authKey = null;
        this.authKeyId = null;
        this.authorized = false;
        this.authServerSaltSet.clear();
    }

    public void clearServerSalts() {
        this.authServerSaltSet.clear();
    }

    public long selectServerSalt(int date) {
        boolean cleanupNeeded = false;
        long result = 0;
        int maxRemainingInterval = 0;
        Iterator i$ = this.authServerSaltSet.iterator();
        while (i$.hasNext()) {
            ServerSalt salt = (ServerSalt) i$.next();
            if (salt.validUntil < date || (salt.validSince == 0 && salt.validUntil == Integer.MAX_VALUE)) {
                cleanupNeeded = true;
            } else if (salt.validSince <= date && salt.validUntil > date) {
                if (maxRemainingInterval == 0 || Math.abs(salt.validUntil - date) > maxRemainingInterval) {
                    maxRemainingInterval = Math.abs(salt.validUntil - date);
                    result = salt.value;
                }
            }
        }
        if (cleanupNeeded) {
            int i = 0;
            while (i < this.authServerSaltSet.size()) {
                if (((ServerSalt) this.authServerSaltSet.get(i)).validUntil < date) {
                    this.authServerSaltSet.remove(i);
                    i--;
                }
                i++;
            }
        }
        if (result == 0) {
            FileLog.m800e("tmessages", "Valid salt not found");
        }
        return result;
    }

    public void mergeServerSalts(int date, ArrayList<TL_futureSalt> salts) {
        if (salts != null) {
            ArrayList<Long> existingSalts = new ArrayList(this.authServerSaltSet.size());
            Iterator i$ = this.authServerSaltSet.iterator();
            while (i$.hasNext()) {
                existingSalts.add(Long.valueOf(((ServerSalt) i$.next()).value));
            }
            i$ = salts.iterator();
            while (i$.hasNext()) {
                TL_futureSalt saltDesc = (TL_futureSalt) i$.next();
                long salt = saltDesc.salt;
                if (!existingSalts.contains(Long.valueOf(salt)) && saltDesc.valid_until > date) {
                    ServerSalt serverSalt = new ServerSalt();
                    serverSalt.validSince = saltDesc.valid_since;
                    serverSalt.validUntil = saltDesc.valid_until;
                    serverSalt.value = salt;
                    this.authServerSaltSet.add(serverSalt);
                }
            }
            Collections.sort(this.authServerSaltSet, new SaltComparator());
        }
    }

    public void addServerSalt(ServerSalt serverSalt) {
        Iterator i$ = this.authServerSaltSet.iterator();
        while (i$.hasNext()) {
            if (((ServerSalt) i$.next()).value == serverSalt.value) {
                return;
            }
        }
        this.authServerSaltSet.add(serverSalt);
        Collections.sort(this.authServerSaltSet, new SaltComparator());
    }

    boolean containsServerSalt(long value) {
        Iterator i$ = this.authServerSaltSet.iterator();
        while (i$.hasNext()) {
            if (((ServerSalt) i$.next()).value == value) {
                return true;
            }
        }
        return false;
    }
}
