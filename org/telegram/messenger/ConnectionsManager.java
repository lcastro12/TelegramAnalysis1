package org.telegram.messenger;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Base64;
import com.google.android.gms.location.LocationStatusCodes;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.TL.TLClassStore;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.DestroySessionRes;
import org.telegram.TL.TLRPC.MsgDetailedInfo;
import org.telegram.TL.TLRPC.RpcError;
import org.telegram.TL.TLRPC.TL_account_registerDevice;
import org.telegram.TL.TLRPC.TL_account_updateStatus;
import org.telegram.TL.TLRPC.TL_auth_exportAuthorization;
import org.telegram.TL.TLRPC.TL_auth_exportedAuthorization;
import org.telegram.TL.TLRPC.TL_auth_importAuthorization;
import org.telegram.TL.TLRPC.TL_bad_msg_notification;
import org.telegram.TL.TLRPC.TL_bad_server_salt;
import org.telegram.TL.TLRPC.TL_config;
import org.telegram.TL.TLRPC.TL_dcOption;
import org.telegram.TL.TLRPC.TL_destroy_session;
import org.telegram.TL.TLRPC.TL_destroy_session_ok;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_futuresalts;
import org.telegram.TL.TLRPC.TL_get_future_salts;
import org.telegram.TL.TLRPC.TL_gzip_packed;
import org.telegram.TL.TLRPC.TL_help_getConfig;
import org.telegram.TL.TLRPC.TL_invokeAfterMsg;
import org.telegram.TL.TLRPC.TL_messages_forwardMessages;
import org.telegram.TL.TLRPC.TL_messages_sendEncrypted;
import org.telegram.TL.TLRPC.TL_messages_sendMedia;
import org.telegram.TL.TLRPC.TL_messages_sendMessage;
import org.telegram.TL.TLRPC.TL_msg_container;
import org.telegram.TL.TLRPC.TL_msg_detailed_info;
import org.telegram.TL.TLRPC.TL_msg_resend_req;
import org.telegram.TL.TLRPC.TL_msgs_ack;
import org.telegram.TL.TLRPC.TL_new_session_created;
import org.telegram.TL.TLRPC.TL_ping;
import org.telegram.TL.TLRPC.TL_pong;
import org.telegram.TL.TLRPC.TL_protoMessage;
import org.telegram.TL.TLRPC.TL_rpc_drop_answer;
import org.telegram.TL.TLRPC.TL_rpc_result;
import org.telegram.TL.TLRPC.Updates;
import org.telegram.TL.TLRPC.initConnection;
import org.telegram.TL.TLRPC.invokeWithLayer11;
import org.telegram.messenger.Action.ActionDelegate;
import org.telegram.messenger.RPCRequest.RPCProgressDelegate;
import org.telegram.messenger.RPCRequest.RPCQuickAckDelegate;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;
import org.telegram.messenger.TcpConnection.TcpConnectionDelegate;
import org.telegram.ui.ApplicationLoader;

public class ConnectionsManager implements ActionDelegate, TcpConnectionDelegate {
    public static String APP_HASH = "eb06d4abfb49dc3eeb1aeb98ae0f581e";
    public static int APP_ID = 6;
    public static final int DC_UPDATE_TIME = 3600;
    public static boolean DEBUG_VERSION = false;
    public static final int DEFAULT_DATACENTER_ID = Integer.MAX_VALUE;
    public static String HOCKEY_APP_HASH = "a5b5c4f551dadedc9918d9766a22ca7c";
    public static ConnectionsManager Instance = new ConnectionsManager();
    public static volatile long nextCallToken = 0;
    static long nextPingId = 0;
    private final int SESSION_VERSION;
    private ArrayList<Action> actionQueue;
    public volatile int connectionState;
    private int currentAppVersion;
    public int currentDatacenterId;
    public int currentPingTime;
    private HashMap<Integer, Datacenter> datacenters;
    private ArrayList<Long> destroyingSessions;
    private final boolean isDebugSession;
    int lastClassGuid;
    private int lastDcUpdateTime;
    private int lastDestroySessionRequestTime;
    private long lastOutgoingMessageId;
    private long lastPingTime;
    private HashMap<Long, ArrayList<Long>> messagesIdsForConfirmation;
    private TL_auth_exportedAuthorization movingAuthorization;
    public int movingToDatacenterId;
    private HashMap<Long, Integer> nextSeqNoInSession;
    private int nextSleepTimeout;
    private int nextWakeUpTimeout;
    private boolean paused;
    private HashMap<Long, Integer> pingIdToDate;
    private Runnable pingRunnable;
    private HashMap<Long, ArrayList<Long>> processedMessageIdsSet;
    private HashMap<Long, ArrayList<Long>> processedSessionChanges;
    private HashMap<Integer, ArrayList<Long>> quickAckIdToRequestIds;
    private ArrayList<RPCRequest> requestQueue;
    private ConcurrentHashMap<Long, Integer> requestsByClass;
    private ConcurrentHashMap<Integer, ArrayList<Long>> requestsByGuids;
    private ArrayList<RPCRequest> runningRequests;
    private ArrayList<Long> sessionsToDestroy;
    private Runnable stageRunnable;
    public int timeDifference;
    private boolean updatingDcSettings;
    private int updatingDcStartTime;
    private int useDifferentBackend;

    class C02961 extends TimerTask {

        class C02941 implements Runnable {
            C02941() {
            }

            public void run() {
                long currentTime = System.currentTimeMillis();
                if (ApplicationLoader.lastPauseTime != 0 && ApplicationLoader.lastPauseTime < currentTime - ((long) ConnectionsManager.this.nextSleepTimeout)) {
                    RPCRequest request;
                    boolean dontSleep = false;
                    Iterator i$ = ConnectionsManager.this.runningRequests.iterator();
                    while (i$.hasNext()) {
                        request = (RPCRequest) i$.next();
                        if (request.retryCount < 10 && request.runningStartTime + 60 > ((int) (currentTime / 1000))) {
                            if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0 || (request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                                dontSleep = true;
                                break;
                            }
                        }
                    }
                    if (!dontSleep) {
                        i$ = ConnectionsManager.this.requestQueue.iterator();
                        while (i$.hasNext()) {
                            request = (RPCRequest) i$.next();
                            if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) == 0) {
                                if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                                }
                            }
                            dontSleep = true;
                        }
                    }
                    if (dontSleep) {
                        ApplicationLoader.lastPauseTime += 30000;
                        FileLog.m800e("tmessages", "don't sleep 30 seconds because of upload or download request");
                    } else {
                        if (!ConnectionsManager.this.paused) {
                            FileLog.m800e("tmessages", "pausing network and timers by sleep time = " + ConnectionsManager.this.nextSleepTimeout);
                            for (Datacenter datacenter : ConnectionsManager.this.datacenters.values()) {
                                if (datacenter.connection != null) {
                                    datacenter.connection.suspendConnection(true);
                                }
                                if (datacenter.uploadConnection != null) {
                                    datacenter.uploadConnection.suspendConnection(true);
                                }
                                if (datacenter.downloadConnection != null) {
                                    datacenter.downloadConnection.suspendConnection(true);
                                }
                            }
                        }
                        try {
                            ConnectionsManager.this.paused = true;
                            if (ApplicationLoader.lastPauseTime < (currentTime - ((long) ConnectionsManager.this.nextSleepTimeout)) - ((long) ConnectionsManager.this.nextWakeUpTimeout)) {
                                ApplicationLoader.lastPauseTime = currentTime;
                                ConnectionsManager.this.nextSleepTimeout = 30000;
                                FileLog.m800e("tmessages", "wakeup network in background by wakeup time = " + ConnectionsManager.this.nextWakeUpTimeout);
                                if (ConnectionsManager.this.nextWakeUpTimeout < 1800000) {
                                    ConnectionsManager.access$528(ConnectionsManager.this, 2);
                                }
                            } else {
                                Thread.sleep(500);
                                return;
                            }
                        } catch (Exception e) {
                            FileLog.m799e("tmessages", e);
                        }
                    }
                }
                if (ConnectionsManager.this.paused) {
                    ConnectionsManager.this.paused = false;
                    FileLog.m800e("tmessages", "resume network and timers");
                }
                if (ConnectionsManager.this.datacenters != null) {
                    MessagesController.Instance.updateTimerProc();
                    if (ConnectionsManager.this.datacenterWithId(ConnectionsManager.this.currentDatacenterId).authKey != null) {
                        if (ConnectionsManager.this.lastPingTime < System.currentTimeMillis() - 30000) {
                            ConnectionsManager.this.lastPingTime = System.currentTimeMillis();
                            ConnectionsManager.this.generatePing();
                        }
                        if (!ConnectionsManager.this.updatingDcSettings && ConnectionsManager.this.lastDcUpdateTime < ((int) (System.currentTimeMillis() / 1000)) - 3600) {
                            ConnectionsManager.this.updateDcSettings();
                        }
                        ConnectionsManager.this.processRequestQueue(0, 0);
                    }
                }
            }
        }

        C02961() {
        }

        public void run() {
            Utilities.stageQueue.postRunnable(new C02941());
        }
    }

    class C02972 implements Runnable {
        C02972() {
        }

        public void run() {
            if (ConnectionsManager.this.paused) {
                ApplicationLoader.lastPauseTime = System.currentTimeMillis();
                ConnectionsManager.this.nextWakeUpTimeout = 60000;
                ConnectionsManager.this.nextSleepTimeout = 30000;
                FileLog.m800e("tmessages", "wakeup network in background by recieved push");
            } else if (ApplicationLoader.lastPauseTime != 0) {
                ApplicationLoader.lastPauseTime = System.currentTimeMillis();
                FileLog.m800e("tmessages", "reset sleep timeout by recieved push");
            }
        }
    }

    class C02983 implements Runnable {
        C02983() {
        }

        public void run() {
            if (ConnectionsManager.this.paused) {
                ConnectionsManager.this.nextSleepTimeout = 60000;
                ConnectionsManager.this.nextWakeUpTimeout = 60000;
                FileLog.m800e("tmessages", "reset timers by application moved to foreground");
            }
        }
    }

    class C02994 implements Runnable {
        C02994() {
        }

        public void run() {
            Datacenter datacenter;
            File configFile = new File(ApplicationLoader.applicationContext.getFilesDir(), "config.dat");
            SerializedData data;
            int count;
            int a;
            if (configFile.exists()) {
                try {
                    data = new SerializedData(configFile);
                    int datacenterSetId = data.readInt32();
                    int version = data.readInt32();
                    if (datacenterSetId == ConnectionsManager.this.useDifferentBackend && version == 2) {
                        ConnectionsManager.this.sessionsToDestroy.clear();
                        count = data.readInt32();
                        for (a = 0; a < count; a++) {
                            ConnectionsManager.this.sessionsToDestroy.add(Long.valueOf(data.readInt64()));
                        }
                        ConnectionsManager.this.timeDifference = data.readInt32();
                        count = data.readInt32();
                        for (a = 0; a < count; a++) {
                            datacenter = new Datacenter(data, 0);
                            ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                        }
                        ConnectionsManager.this.currentDatacenterId = data.readInt32();
                    } else {
                        UserConfig.clearConfig();
                    }
                } catch (Exception e) {
                    UserConfig.clearConfig();
                }
            } else {
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("dataconfig", 0);
                if (preferences.getInt("datacenterSetId", 0) == ConnectionsManager.this.useDifferentBackend) {
                    ConnectionsManager.this.currentDatacenterId = preferences.getInt("currentDatacenterId", 0);
                    ConnectionsManager.this.timeDifference = preferences.getInt("timeDifference", 0);
                    ConnectionsManager.this.lastDcUpdateTime = preferences.getInt("lastDcUpdateTime", 0);
                    try {
                        ConnectionsManager.this.sessionsToDestroy.clear();
                        String sessionsString = preferences.getString("sessionsToDestroy", null);
                        if (sessionsString != null) {
                            byte[] sessionsBytes = Base64.decode(sessionsString, 0);
                            if (sessionsBytes != null) {
                                data = new SerializedData(sessionsBytes);
                                count = data.readInt32();
                                for (a = 0; a < count; a++) {
                                    ConnectionsManager.this.sessionsToDestroy.add(Long.valueOf(data.readInt64()));
                                }
                            }
                        }
                    } catch (Exception e2) {
                        FileLog.m799e("tmessages", e2);
                    }
                    try {
                        String datacentersString = preferences.getString("datacenters", null);
                        if (datacentersString != null) {
                            byte[] datacentersBytes = Base64.decode(datacentersString, 0);
                            if (datacentersBytes != null) {
                                data = new SerializedData(datacentersBytes);
                                count = data.readInt32();
                                for (a = 0; a < count; a++) {
                                    datacenter = new Datacenter(data, 1);
                                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                                }
                            }
                        }
                    } catch (Exception e22) {
                        FileLog.m799e("tmessages", e22);
                    }
                }
            }
            if (ConnectionsManager.this.currentDatacenterId != 0 && UserConfig.clientActivated && ConnectionsManager.this.datacenterWithId(ConnectionsManager.this.currentDatacenterId).authKey == null) {
                ConnectionsManager.this.currentDatacenterId = 0;
                ConnectionsManager.this.datacenters.clear();
                UserConfig.clearConfig();
            }
            if (ConnectionsManager.this.datacenters.size() == 0) {
                if (ConnectionsManager.this.useDifferentBackend == 0) {
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 1;
                    datacenter.addAddressAndPort("173.240.5.1", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 2;
                    datacenter.addAddressAndPort("95.142.192.66", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 3;
                    datacenter.addAddressAndPort("174.140.142.6", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 4;
                    datacenter.addAddressAndPort("31.210.235.12", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 5;
                    datacenter.addAddressAndPort("116.51.22.2", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                } else {
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 1;
                    datacenter.addAddressAndPort("173.240.5.253", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 2;
                    datacenter.addAddressAndPort("95.142.192.65", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                    datacenter = new Datacenter();
                    datacenter.datacenterId = 3;
                    datacenter.addAddressAndPort("174.140.142.5", 443);
                    ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                }
            } else if (ConnectionsManager.this.datacenters.size() == 1) {
                datacenter = new Datacenter();
                datacenter.datacenterId = 2;
                datacenter.addAddressAndPort("95.142.192.66", 443);
                ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                datacenter = new Datacenter();
                datacenter.datacenterId = 3;
                datacenter.addAddressAndPort("174.140.142.6", 443);
                ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                datacenter = new Datacenter();
                datacenter.datacenterId = 4;
                datacenter.addAddressAndPort("31.210.235.12", 443);
                ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                datacenter = new Datacenter();
                datacenter.datacenterId = 5;
                datacenter.addAddressAndPort("116.51.22.2", 443);
                ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
            }
            for (Datacenter datacenter2 : ConnectionsManager.this.datacenters.values()) {
                datacenter2.authSessionId = ConnectionsManager.this.getNewSessionId();
            }
            if (ConnectionsManager.this.datacenters.size() != 0 && ConnectionsManager.this.currentDatacenterId == 0) {
                ConnectionsManager.this.currentDatacenterId = 1;
                ConnectionsManager.this.saveSession();
            }
            ConnectionsManager.this.movingToDatacenterId = Integer.MAX_VALUE;
        }
    }

    class C03005 implements Runnable {
        C03005() {
        }

        public void run() {
            try {
                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("dataconfig", 0).edit();
                editor.putInt("datacenterSetId", ConnectionsManager.this.useDifferentBackend);
                Datacenter currentDatacenter = ConnectionsManager.this.datacenterWithId(ConnectionsManager.this.currentDatacenterId);
                if (currentDatacenter != null) {
                    SerializedData data;
                    Iterator i$;
                    editor.putInt("currentDatacenterId", ConnectionsManager.this.currentDatacenterId);
                    editor.putInt("timeDifference", ConnectionsManager.this.timeDifference);
                    editor.putInt("lastDcUpdateTime", ConnectionsManager.this.lastDcUpdateTime);
                    ArrayList<Long> sessions = new ArrayList();
                    if (currentDatacenter.authSessionId != 0) {
                        sessions.add(Long.valueOf(currentDatacenter.authSessionId));
                    }
                    if (currentDatacenter.authDownloadSessionId != 0) {
                        sessions.add(Long.valueOf(currentDatacenter.authDownloadSessionId));
                    }
                    if (currentDatacenter.authUploadSessionId != 0) {
                        sessions.add(Long.valueOf(currentDatacenter.authUploadSessionId));
                    }
                    if (sessions.isEmpty()) {
                        editor.remove("sessionsToDestroy");
                    } else {
                        data = new SerializedData((sessions.size() * 8) + 4);
                        data.writeInt32(sessions.size());
                        i$ = sessions.iterator();
                        while (i$.hasNext()) {
                            data.writeInt64(((Long) i$.next()).longValue());
                        }
                        editor.putString("sessionsToDestroy", Base64.encodeToString(data.toByteArray(), 0));
                    }
                    if (ConnectionsManager.this.datacenters.isEmpty()) {
                        editor.remove("datacenters");
                    } else {
                        data = new SerializedData();
                        data.writeInt32(ConnectionsManager.this.datacenters.size());
                        for (Datacenter datacenter : ConnectionsManager.this.datacenters.values()) {
                            datacenter.SerializeToStream(data);
                        }
                        editor.putString("datacenters", Base64.encodeToString(data.toByteArray(), 0));
                    }
                } else {
                    editor.remove("datacenters");
                    editor.remove("sessionsToDestroy");
                    editor.remove("currentDatacenterId");
                    editor.remove("timeDifference");
                }
                editor.commit();
                File configFile = new File(ApplicationLoader.applicationContext.getFilesDir(), "config.dat");
                if (configFile.exists()) {
                    configFile.delete();
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    class C03016 implements Runnable {
        C03016() {
        }

        public void run() {
            Datacenter datacenter = ConnectionsManager.this.datacenterWithId(ConnectionsManager.this.currentDatacenterId);
            ConnectionsManager.this.recreateSession(datacenter.authSessionId, datacenter);
        }
    }

    class C08439 implements RPCRequestDelegate {
        C08439() {
        }

        public void run(TLObject response, TL_error error) {
            if (ConnectionsManager.this.updatingDcSettings) {
                if (error == null) {
                    ConnectionsManager.this.lastDcUpdateTime = (int) (System.currentTimeMillis() / 1000);
                    TL_config config = (TL_config) response;
                    ArrayList<Datacenter> datacentersArr = new ArrayList();
                    HashMap<Integer, Datacenter> datacenterMap = new HashMap();
                    Iterator i$ = config.dc_options.iterator();
                    while (i$.hasNext()) {
                        TL_dcOption datacenterDesc = (TL_dcOption) i$.next();
                        Datacenter existing = (Datacenter) datacenterMap.get(Integer.valueOf(datacenterDesc.id));
                        if (existing == null) {
                            existing = new Datacenter();
                            existing.datacenterId = datacenterDesc.id;
                            existing.authSessionId = (long) (MessagesController.random.nextDouble() * 9.223372036854776E18d);
                            datacentersArr.add(existing);
                            datacenterMap.put(Integer.valueOf(existing.datacenterId), existing);
                        }
                        existing.addAddressAndPort(datacenterDesc.ip_address, datacenterDesc.port);
                    }
                    if (!datacentersArr.isEmpty()) {
                        i$ = datacentersArr.iterator();
                        while (i$.hasNext()) {
                            Datacenter datacenter = (Datacenter) i$.next();
                            Datacenter exist = ConnectionsManager.this.datacenterWithId(datacenter.datacenterId);
                            if (exist == null) {
                                ConnectionsManager.this.datacenters.put(Integer.valueOf(datacenter.datacenterId), datacenter);
                            } else {
                                exist.replaceAddressesAndPorts(datacenter.addresses, datacenter.ports);
                            }
                            if (datacenter.datacenterId == ConnectionsManager.this.movingToDatacenterId) {
                                ConnectionsManager.this.movingToDatacenterId = Integer.MAX_VALUE;
                                ConnectionsManager.this.moveToDatacenter(datacenter.datacenterId);
                            }
                        }
                        ConnectionsManager.this.saveSession();
                        ConnectionsManager.this.processRequestQueue(RPCRequest.RPCRequestClassTransportMask, 0);
                    }
                }
                ConnectionsManager.this.updatingDcSettings = false;
            }
        }
    }

    static /* synthetic */ int access$528(ConnectionsManager x0, int x1) {
        int i = x0.nextWakeUpTimeout * x1;
        x0.nextWakeUpTimeout = i;
        return i;
    }

    public ConnectionsManager() {
        this.datacenters = new HashMap();
        this.processedMessageIdsSet = new HashMap();
        this.nextSeqNoInSession = new HashMap();
        this.sessionsToDestroy = new ArrayList();
        this.destroyingSessions = new ArrayList();
        this.quickAckIdToRequestIds = new HashMap();
        this.messagesIdsForConfirmation = new HashMap();
        this.processedSessionChanges = new HashMap();
        this.pingIdToDate = new HashMap();
        this.requestsByGuids = new ConcurrentHashMap(100, 1.0f, 2);
        this.requestsByClass = new ConcurrentHashMap(100, 1.0f, 2);
        this.connectionState = 2;
        this.requestQueue = new ArrayList();
        this.runningRequests = new ArrayList();
        this.actionQueue = new ArrayList();
        this.lastOutgoingMessageId = 0;
        this.useDifferentBackend = 0;
        this.SESSION_VERSION = 2;
        this.timeDifference = 0;
        this.isDebugSession = false;
        this.updatingDcSettings = false;
        this.updatingDcStartTime = 0;
        this.lastDcUpdateTime = 0;
        this.currentAppVersion = 0;
        this.paused = false;
        this.lastPingTime = System.currentTimeMillis();
        this.nextWakeUpTimeout = 60000;
        this.nextSleepTimeout = 60000;
        this.lastClassGuid = 1;
        this.currentAppVersion = ApplicationLoader.getAppVersion();
        this.lastOutgoingMessageId = 0;
        this.movingToDatacenterId = Integer.MAX_VALUE;
        loadSession();
        if (!isNetworkOnline()) {
            this.connectionState = 1;
        }
        new Timer().schedule(new C02961(), 1000, 1000);
    }

    public void resumeNetworkMaybe() {
        Utilities.stageQueue.postRunnable(new C02972());
    }

    public void applicationMovedToForeground() {
        Utilities.stageQueue.postRunnable(new C02983());
    }

    public Datacenter datacenterWithId(int datacenterId) {
        if (datacenterId == Integer.MAX_VALUE) {
            return (Datacenter) this.datacenters.get(Integer.valueOf(this.currentDatacenterId));
        }
        return (Datacenter) this.datacenters.get(Integer.valueOf(datacenterId));
    }

    void setTimeDifference(int diff) {
        boolean store = Math.abs(diff - this.timeDifference) > 25;
        this.timeDifference = diff;
        if (store) {
            saveSession();
        }
    }

    void loadSession() {
        Utilities.stageQueue.postRunnable(new C02994());
    }

    void saveSession() {
        Utilities.stageQueue.postRunnable(new C03005());
    }

    void clearRequestsForRequestClass(int requestClass, Datacenter datacenter) {
        Iterator i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            RPCRequest request = (RPCRequest) i$.next();
            Datacenter dcenter = datacenterWithId(request.runningDatacenterId);
            if (!((request.flags & requestClass) == 0 || dcenter == null || dcenter.datacenterId != datacenter.datacenterId)) {
                request.runningMessageId = 0;
                request.runningMessageSeqNo = 0;
                request.runningStartTime = 0;
                request.runningMinStartTime = 0;
                request.transportChannelToken = 0;
            }
        }
    }

    public void cleanUp() {
        Utilities.stageQueue.postRunnable(new C03016());
    }

    void recreateSession(long sessionId, Datacenter datacenter) {
        this.messagesIdsForConfirmation.remove(Long.valueOf(sessionId));
        this.processedMessageIdsSet.remove(Long.valueOf(sessionId));
        this.nextSeqNoInSession.remove(Long.valueOf(sessionId));
        this.processedSessionChanges.remove(Long.valueOf(sessionId));
        this.pingIdToDate.remove(Long.valueOf(sessionId));
        if (sessionId == datacenter.authSessionId) {
            clearRequestsForRequestClass(RPCRequest.RPCRequestClassGeneric, datacenter);
            FileLog.m798d("tmessages", "***** Recreate generic session");
            datacenter.authSessionId = getNewSessionId();
        }
    }

    long getNewSessionId() {
        return (long) (MessagesController.random.nextDouble() * 9.223372036854776E18d);
    }

    long generateMessageId() {
        long messageId = (long) (((((double) System.currentTimeMillis()) + (((double) this.timeDifference) * 1000.0d)) * 4.294967296E9d) / 1000.0d);
        if (messageId <= this.lastOutgoingMessageId) {
            messageId = this.lastOutgoingMessageId + 1;
        }
        while (messageId % 4 != 0) {
            messageId++;
        }
        this.lastOutgoingMessageId = messageId;
        return messageId;
    }

    long getTimeFromMsgId(long messageId) {
        return (long) ((((double) messageId) / 4.294967296E9d) * 1000.0d);
    }

    int generateMessageSeqNo(long session, boolean increment) {
        int value = 0;
        if (this.nextSeqNoInSession.containsKey(Long.valueOf(session))) {
            value = ((Integer) this.nextSeqNoInSession.get(Long.valueOf(session))).intValue();
        }
        if (increment) {
            this.nextSeqNoInSession.put(Long.valueOf(session), Integer.valueOf(value + 1));
        }
        return (increment ? 1 : 0) + (value * 2);
    }

    boolean isMessageIdProcessed(long sessionId, long messageId) {
        ArrayList<Long> set = (ArrayList) this.processedMessageIdsSet.get(Long.valueOf(sessionId));
        return set != null && set.contains(Long.valueOf(messageId));
    }

    void addProcessedMessageId(long sessionId, long messageId) {
        ArrayList<Long> set = (ArrayList) this.processedMessageIdsSet.get(Long.valueOf(sessionId));
        if (set != null) {
            if (set.size() > 1224) {
                for (int a = 0; a < Math.min(set.size(), 225); a++) {
                    set.remove(0);
                }
            }
            set.add(Long.valueOf(messageId));
            return;
        }
        ArrayList<Long> sessionMap = new ArrayList();
        sessionMap.add(Long.valueOf(messageId));
        this.processedMessageIdsSet.put(Long.valueOf(sessionId), sessionMap);
    }

    public int generateClassGuid() {
        int guid = this.lastClassGuid;
        this.lastClassGuid = guid + 1;
        this.requestsByGuids.put(Integer.valueOf(guid), new ArrayList());
        return guid;
    }

    public void cancelRpcsForClassGuid(int guid) {
        ArrayList<Long> requests = (ArrayList) this.requestsByGuids.get(Integer.valueOf(guid));
        if (requests != null) {
            Iterator i$ = requests.iterator();
            while (i$.hasNext()) {
                cancelRpc(((Long) i$.next()).longValue(), true);
            }
            this.requestsByGuids.remove(Integer.valueOf(guid));
        }
    }

    public void bindRequestToGuid(final Long request, final int guid) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                ArrayList<Long> requests = (ArrayList) ConnectionsManager.this.requestsByGuids.get(Integer.valueOf(guid));
                if (requests != null) {
                    requests.add(request);
                    ConnectionsManager.this.requestsByClass.put(request, Integer.valueOf(guid));
                }
            }
        });
    }

    public void removeRequestInClass(final Long request) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                Integer guid = (Integer) ConnectionsManager.this.requestsByClass.get(request);
                if (guid != null) {
                    ArrayList<Long> requests = (ArrayList) ConnectionsManager.this.requestsByGuids.get(guid);
                    if (requests != null) {
                        requests.remove(request);
                    }
                }
            }
        });
    }

    public void updateDcSettings() {
        if (!this.updatingDcSettings) {
            this.updatingDcStartTime = (int) (System.currentTimeMillis() / 1000);
            this.updatingDcSettings = true;
            Instance.performRpc(new TL_help_getConfig(), new C08439(), null, true, RPCRequest.RPCRequestClassEnableUnauthorized | RPCRequest.RPCRequestClassGeneric, this.currentDatacenterId);
        }
    }

    public long performRpc(TLObject rpc, RPCRequestDelegate completionBlock, RPCProgressDelegate progressBlock, boolean requiresCompletion, int requestClass) {
        return performRpc(rpc, completionBlock, progressBlock, requiresCompletion, requestClass, Integer.MAX_VALUE);
    }

    public long performRpc(TLObject rpc, RPCRequestDelegate completionBlock, RPCProgressDelegate progressBlock, boolean requiresCompletion, int requestClass, int datacenterId) {
        return performRpc(rpc, completionBlock, progressBlock, null, requiresCompletion, requestClass, datacenterId);
    }

    TLObject wrapInLayer(TLObject object, int datacenterId, RPCRequest request) {
        if (object.layer() <= 0) {
            return object;
        }
        Datacenter datacenter = datacenterWithId(datacenterId);
        if (datacenter == null || datacenter.lastInitVersion != this.currentAppVersion) {
            request.initRequest = true;
            TLObject invoke = new initConnection();
            invoke.query = object;
            invoke.api_id = APP_ID;
            try {
                invoke.lang_code = Locale.getDefault().getCountry();
                invoke.device_model = Build.MANUFACTURER + Build.MODEL;
                if (invoke.device_model == null) {
                    invoke.device_model = "Android unknown";
                }
                invoke.app_version = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0).versionName;
                if (invoke.app_version == null) {
                    invoke.app_version = "App version unknown";
                }
                invoke.system_version = "SDK " + VERSION.SDK_INT;
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                invoke.lang_code = "en";
                invoke.device_model = "Android unknown";
                invoke.app_version = "App version unknown";
                invoke.system_version = "SDK " + VERSION.SDK_INT;
            }
            if (invoke.lang_code == null || invoke.lang_code.length() == 0) {
                invoke.lang_code = "en";
            }
            if (invoke.device_model == null || invoke.device_model.length() == 0) {
                invoke.device_model = "Android unknown";
            }
            if (invoke.app_version == null || invoke.app_version.length() == 0) {
                invoke.app_version = "App version unknown";
            }
            if (invoke.system_version == null || invoke.system_version.length() == 0) {
                invoke.system_version = "SDK Unknown";
            }
            object = invoke;
        }
        initConnection invoke2 = new invokeWithLayer11();
        invoke2.query = object;
        FileLog.m798d("wrap in layer", BuildConfig.FLAVOR + object);
        return invoke2;
    }

    long performRpc(TLObject rpc, RPCRequestDelegate completionBlock, RPCProgressDelegate progressBlock, RPCQuickAckDelegate quickAckBlock, boolean requiresCompletion, int requestClass, int datacenterId) {
        final long requestToken = nextCallToken;
        nextCallToken = 1 + requestToken;
        final int i = requestClass;
        final int i2 = datacenterId;
        final TLObject tLObject = rpc;
        final RPCRequestDelegate rPCRequestDelegate = completionBlock;
        final RPCProgressDelegate rPCProgressDelegate = progressBlock;
        final RPCQuickAckDelegate rPCQuickAckDelegate = quickAckBlock;
        final boolean z = requiresCompletion;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                RPCRequest request = new RPCRequest();
                request.token = requestToken;
                request.flags = i;
                request.runningDatacenterId = i2;
                request.rawRequest = tLObject;
                request.rpcRequest = ConnectionsManager.this.wrapInLayer(tLObject, i2, request);
                request.completionBlock = rPCRequestDelegate;
                request.progressBlock = rPCProgressDelegate;
                request.quickAckBlock = rPCQuickAckDelegate;
                request.requiresCompletion = z;
                ConnectionsManager.this.requestQueue.add(request);
                if (ConnectionsManager.this.paused && !((request.flags & RPCRequest.RPCRequestClassDownloadMedia) == 0 && (request.flags & RPCRequest.RPCRequestClassUploadMedia) == 0)) {
                    ApplicationLoader.lastPauseTime = System.currentTimeMillis();
                    ConnectionsManager.this.nextSleepTimeout = 30000;
                    FileLog.m800e("tmessages", "wakeup by download or upload request");
                }
                ConnectionsManager.this.processRequestQueue(0, 0);
            }
        });
        return requestToken;
    }

    public void cancelRpc(final long token, final boolean notifyServer) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int i;
                boolean found = false;
                for (i = 0; i < ConnectionsManager.this.requestQueue.size(); i++) {
                    RPCRequest request = (RPCRequest) ConnectionsManager.this.requestQueue.get(i);
                    if (request.token == token) {
                        found = true;
                        request.cancelled = true;
                        FileLog.m798d("tmessages", "===== Cancelled queued rpc request " + request.rawRequest);
                        ConnectionsManager.this.requestQueue.remove(i);
                        break;
                    }
                }
                for (i = 0; i < ConnectionsManager.this.runningRequests.size(); i++) {
                    request = (RPCRequest) ConnectionsManager.this.runningRequests.get(i);
                    if (request.token == token) {
                        found = true;
                        FileLog.m798d("tmessages", "===== Cancelled running rpc request " + request.rawRequest);
                        if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0 && notifyServer) {
                            TL_rpc_drop_answer dropAnswer = new TL_rpc_drop_answer();
                            dropAnswer.req_msg_id = request.runningMessageId;
                            ConnectionsManager.this.performRpc(dropAnswer, null, null, false, request.flags);
                        }
                        request.cancelled = true;
                        ConnectionsManager.this.runningRequests.remove(i);
                        if (!found) {
                            FileLog.m798d("tmessages", "***** Warning: cancelling unknown request");
                        }
                    }
                }
                if (!found) {
                    FileLog.m798d("tmessages", "***** Warning: cancelling unknown request");
                }
            }
        });
    }

    public static boolean isNetworkOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) ApplicationLoader.applicationContext.getSystemService("connectivity");
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == State.CONNECTED) {
                return true;
            }
            netInfo = cm.getNetworkInfo(1);
            if (netInfo == null || netInfo.getState() != State.CONNECTED) {
                return false;
            }
            return true;
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return false;
        }
    }

    public int getCurrentTime() {
        return ((int) (System.currentTimeMillis() / 1000)) + this.timeDifference;
    }

    private void processRequestQueue(int requestClass, int _datacenterId) {
        Datacenter datacenter;
        Iterator it;
        boolean notFound;
        Action actor;
        HashMap<Integer, Integer> activeTransportTokens = new HashMap();
        ArrayList<Integer> transportsToResume = new ArrayList();
        HashMap<Integer, Integer> activeDownloadTransportTokens = new HashMap();
        ArrayList<Integer> downloadTransportsToResume = new ArrayList();
        HashMap<Integer, Integer> activeUploadTransportTokens = new HashMap();
        ArrayList<Integer> uploadTransportsToResume = new ArrayList();
        for (Datacenter datacenter2 : this.datacenters.values()) {
            int channelToken;
            if (datacenter2.connection != null) {
                channelToken = datacenter2.connection.channelToken;
                if (channelToken != 0) {
                    activeTransportTokens.put(Integer.valueOf(datacenter2.datacenterId), Integer.valueOf(channelToken));
                }
            }
            if (datacenter2.downloadConnection != null) {
                channelToken = datacenter2.downloadConnection.channelToken;
                if (channelToken != 0) {
                    activeDownloadTransportTokens.put(Integer.valueOf(datacenter2.datacenterId), Integer.valueOf(channelToken));
                }
            }
            if (datacenter2.uploadConnection != null) {
                channelToken = datacenter2.uploadConnection.channelToken;
                if (channelToken != 0) {
                    activeUploadTransportTokens.put(Integer.valueOf(datacenter2.datacenterId), Integer.valueOf(channelToken));
                }
            }
        }
        Iterator i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            Datacenter requestDatacenter;
            RPCRequest request = (RPCRequest) i$.next();
            if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                requestDatacenter = datacenterWithId(request.runningDatacenterId);
                if (requestDatacenter != null) {
                    if (!activeTransportTokens.containsKey(Integer.valueOf(requestDatacenter.datacenterId))) {
                        if (!transportsToResume.contains(Integer.valueOf(requestDatacenter.datacenterId))) {
                            transportsToResume.add(Integer.valueOf(requestDatacenter.datacenterId));
                        }
                    }
                }
            } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                requestDatacenter = datacenterWithId(request.runningDatacenterId);
                if (!(requestDatacenter == null || activeDownloadTransportTokens.containsKey(Integer.valueOf(requestDatacenter.datacenterId)))) {
                    if (!downloadTransportsToResume.contains(Integer.valueOf(requestDatacenter.datacenterId))) {
                        downloadTransportsToResume.add(Integer.valueOf(requestDatacenter.datacenterId));
                    }
                }
            } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                requestDatacenter = datacenterWithId(request.runningDatacenterId);
                if (requestDatacenter != null) {
                    if (!activeUploadTransportTokens.containsKey(Integer.valueOf(requestDatacenter.datacenterId))) {
                        if (!uploadTransportsToResume.contains(Integer.valueOf(requestDatacenter.datacenterId))) {
                            uploadTransportsToResume.add(Integer.valueOf(requestDatacenter.datacenterId));
                        }
                    }
                }
            }
        }
        i$ = this.requestQueue.iterator();
        while (i$.hasNext()) {
            request = (RPCRequest) i$.next();
            if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                requestDatacenter = datacenterWithId(request.runningDatacenterId);
                if (requestDatacenter != null) {
                    if (!activeTransportTokens.containsKey(Integer.valueOf(requestDatacenter.datacenterId))) {
                        if (!transportsToResume.contains(Integer.valueOf(requestDatacenter.datacenterId))) {
                            transportsToResume.add(Integer.valueOf(requestDatacenter.datacenterId));
                        }
                    }
                }
            } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                requestDatacenter = datacenterWithId(request.runningDatacenterId);
                if (!(requestDatacenter == null || activeDownloadTransportTokens.containsKey(Integer.valueOf(requestDatacenter.datacenterId)))) {
                    if (!downloadTransportsToResume.contains(Integer.valueOf(requestDatacenter.datacenterId))) {
                        downloadTransportsToResume.add(Integer.valueOf(requestDatacenter.datacenterId));
                    }
                }
            } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                requestDatacenter = datacenterWithId(request.runningDatacenterId);
                if (requestDatacenter != null) {
                    if (!activeUploadTransportTokens.containsKey(Integer.valueOf(requestDatacenter.datacenterId))) {
                        if (!uploadTransportsToResume.contains(Integer.valueOf(requestDatacenter.datacenterId))) {
                            uploadTransportsToResume.add(Integer.valueOf(requestDatacenter.datacenterId));
                        }
                    }
                }
            }
        }
        if (!activeTransportTokens.containsKey(Integer.valueOf(this.currentDatacenterId))) {
            if (!transportsToResume.contains(Integer.valueOf(this.currentDatacenterId))) {
                transportsToResume.add(Integer.valueOf(this.currentDatacenterId));
            }
        }
        i$ = transportsToResume.iterator();
        while (i$.hasNext()) {
            datacenter2 = datacenterWithId(((Integer) i$.next()).intValue());
            if (datacenter2.authKey != null) {
                if (datacenter2.connection == null) {
                    datacenter2.connection = new TcpConnection(datacenter2.datacenterId);
                    datacenter2.connection.delegate = this;
                    datacenter2.connection.transportRequestClass = RPCRequest.RPCRequestClassGeneric;
                }
                datacenter2.connection.connect();
            }
        }
        i$ = downloadTransportsToResume.iterator();
        while (i$.hasNext()) {
            datacenter2 = datacenterWithId(((Integer) i$.next()).intValue());
            if (datacenter2.authKey != null) {
                if (datacenter2.downloadConnection == null) {
                    datacenter2.downloadConnection = new TcpConnection(datacenter2.datacenterId);
                    datacenter2.downloadConnection.delegate = this;
                    datacenter2.downloadConnection.transportRequestClass = RPCRequest.RPCRequestClassDownloadMedia;
                    datacenter2.authDownloadSessionId = getNewSessionId();
                }
                datacenter2.downloadConnection.connect();
            }
        }
        i$ = uploadTransportsToResume.iterator();
        while (i$.hasNext()) {
            datacenter2 = datacenterWithId(((Integer) i$.next()).intValue());
            if (datacenter2.authKey != null) {
                if (datacenter2.uploadConnection == null) {
                    datacenter2.uploadConnection = new TcpConnection(datacenter2.datacenterId);
                    datacenter2.uploadConnection.delegate = this;
                    datacenter2.uploadConnection.transportRequestClass = RPCRequest.RPCRequestClassUploadMedia;
                    datacenter2.authUploadSessionId = getNewSessionId();
                }
                datacenter2.uploadConnection.connect();
            }
        }
        HashMap<Integer, ArrayList<NetworkMessage>> genericMessagesToDatacenters = new HashMap();
        ArrayList<Integer> unknownDatacenterIds = new ArrayList();
        ArrayList<Integer> neededDatacenterIds = new ArrayList();
        ArrayList<Integer> unauthorizedDatacenterIds = new ArrayList();
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            ArrayList<Datacenter> arrayList;
            int a;
            long sessionId;
            NetworkMessage networkMessage;
            ArrayList<NetworkMessage> arr;
            request = (RPCRequest) i$.next();
            if (this.updatingDcSettings && this.datacenters.size() > 1 && (request.rawRequest instanceof TL_help_getConfig) && this.updatingDcStartTime < currentTime - 60) {
                this.updatingDcStartTime = currentTime;
                arrayList = new ArrayList(this.datacenters.values());
                for (a = 0; a < arrayList.size(); a++) {
                    if (((Datacenter) arrayList.get(a)).datacenterId == request.runningDatacenterId) {
                        arrayList.remove(a);
                        break;
                    }
                }
                request.runningDatacenterId = ((Datacenter) arrayList.get(Math.abs(MessagesController.random.nextInt()) % arrayList.size())).datacenterId;
            }
            int datacenterId = request.runningDatacenterId;
            if (datacenterId == Integer.MAX_VALUE) {
                if (this.movingToDatacenterId == Integer.MAX_VALUE) {
                    datacenterId = this.currentDatacenterId;
                }
            }
            requestDatacenter = datacenterWithId(datacenterId);
            if (requestDatacenter == null) {
                if (!unknownDatacenterIds.contains(Integer.valueOf(datacenterId))) {
                    unknownDatacenterIds.add(Integer.valueOf(datacenterId));
                }
            } else if (requestDatacenter.authKey == null) {
                if (!neededDatacenterIds.contains(Integer.valueOf(datacenterId))) {
                    neededDatacenterIds.add(Integer.valueOf(datacenterId));
                }
            } else if (requestDatacenter.authorized || request.runningDatacenterId == Integer.MAX_VALUE || request.runningDatacenterId == this.currentDatacenterId || (request.flags & RPCRequest.RPCRequestClassEnableUnauthorized) != 0) {
                Integer tokenIt = (Integer) activeTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId));
                int datacenterTransportToken = tokenIt != null ? tokenIt.intValue() : 0;
                Integer uploadTokenIt = (Integer) activeUploadTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId));
                int datacenterUploadTransportToken = uploadTokenIt != null ? uploadTokenIt.intValue() : 0;
                Integer downloadTokenIt = (Integer) activeDownloadTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId));
                int datacenterDownloadTransportToken = downloadTokenIt != null ? downloadTokenIt.intValue() : 0;
                double maxTimeout = 8.0d;
                if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                    if (datacenterTransportToken == 0) {
                    }
                } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                    if (1 == null) {
                        FileLog.m798d("tmessages", "Don't have any network connection, skipping download request");
                    } else if (datacenterDownloadTransportToken != 0) {
                        maxTimeout = 40.0d;
                    }
                } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                    if (1 == null) {
                        FileLog.m798d("tmessages", "Don't have any network connection, skipping upload request");
                    } else if (datacenterUploadTransportToken != 0) {
                        maxTimeout = 30.0d;
                    }
                }
                sessionId = 0;
                if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                    sessionId = requestDatacenter.authSessionId;
                } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                    sessionId = requestDatacenter.authDownloadSessionId;
                } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                    sessionId = requestDatacenter.authUploadSessionId;
                }
                boolean forceThisRequest = (request.flags & requestClass) != 0 && (_datacenterId == Integer.MIN_VALUE || requestDatacenter.datacenterId == _datacenterId);
                if ((request.rawRequest instanceof TL_get_future_salts) || (request.rawRequest instanceof TL_destroy_session)) {
                    if (request.runningMessageId != 0) {
                        request.addRespondMessageId(request.runningMessageId);
                    }
                    request.runningMessageId = 0;
                    request.runningMessageSeqNo = 0;
                    request.transportChannelToken = 0;
                    forceThisRequest = false;
                }
                if ((((double) Math.abs(currentTime - request.runningStartTime)) > maxTimeout && (currentTime > request.runningMinStartTime || ((double) Math.abs(currentTime - request.runningMinStartTime)) > 60.0d)) || forceThisRequest) {
                    if (!forceThisRequest && request.transportChannelToken > 0) {
                        if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0 && datacenterTransportToken == request.transportChannelToken) {
                            FileLog.m798d("tmessages", "Request token is valid, not retrying " + request.rawRequest);
                        } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                            if (datacenterDownloadTransportToken != 0 && request.transportChannelToken == datacenterDownloadTransportToken) {
                                FileLog.m798d("tmessages", "Request download token is valid, not retrying " + request.rawRequest);
                            }
                        } else if (!((request.flags & RPCRequest.RPCRequestClassUploadMedia) == 0 || datacenterUploadTransportToken == 0 || request.transportChannelToken != datacenterUploadTransportToken)) {
                            FileLog.m798d("tmessages", "Request upload token is valid, not retrying " + request.rawRequest);
                        }
                    }
                    request.retryCount++;
                    networkMessage = new NetworkMessage();
                    networkMessage.protoMessage = new TL_protoMessage();
                    if (request.runningMessageSeqNo == 0) {
                        request.runningMessageSeqNo = generateMessageSeqNo(sessionId, true);
                        request.runningMessageId = generateMessageId();
                    }
                    networkMessage.protoMessage.msg_id = request.runningMessageId;
                    networkMessage.protoMessage.seqno = request.runningMessageSeqNo;
                    networkMessage.protoMessage.bytes = request.serializedLength;
                    networkMessage.protoMessage.body = request.rpcRequest;
                    networkMessage.rawRequest = request.rawRequest;
                    networkMessage.requestId = request.token;
                    request.runningStartTime = currentTime;
                    if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                        request.transportChannelToken = datacenterTransportToken;
                        addMessageToDatacenter(genericMessagesToDatacenters, requestDatacenter.datacenterId, networkMessage);
                    } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                        request.transportChannelToken = datacenterDownloadTransportToken;
                        arr = new ArrayList();
                        arr.add(networkMessage);
                        proceedToSendingMessages(arr, sessionId, requestDatacenter.downloadConnection, false, false);
                    } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                        request.transportChannelToken = datacenterUploadTransportToken;
                        arr = new ArrayList();
                        arr.add(networkMessage);
                        proceedToSendingMessages(arr, sessionId, requestDatacenter.uploadConnection, false, false);
                    }
                }
            } else {
                if (!unauthorizedDatacenterIds.contains(Integer.valueOf(datacenterId))) {
                    unauthorizedDatacenterIds.add(Integer.valueOf(datacenterId));
                }
            }
        }
        boolean updatingState = MessagesController.Instance.updatingState;
        if (!(activeTransportTokens.get(Integer.valueOf(this.currentDatacenterId)) == null || updatingState)) {
            Datacenter currentDatacenter = datacenterWithId(this.currentDatacenterId);
            i$ = this.sessionsToDestroy.iterator();
            while (i$.hasNext()) {
                Long it2 = (Long) i$.next();
                if (!this.destroyingSessions.contains(it2) && ((double) ((System.currentTimeMillis() / 1000) - ((long) this.lastDestroySessionRequestTime))) > 2.0d) {
                    this.lastDestroySessionRequestTime = (int) (System.currentTimeMillis() / 1000);
                    TLObject destroySession = new TL_destroy_session();
                    destroySession.session_id = it2.longValue();
                    this.destroyingSessions.add(it2);
                    networkMessage = new NetworkMessage();
                    networkMessage.protoMessage = wrapMessage(destroySession, currentDatacenter.authSessionId, false);
                    if (networkMessage.protoMessage != null) {
                        addMessageToDatacenter(genericMessagesToDatacenters, currentDatacenter.datacenterId, networkMessage);
                    }
                }
            }
        }
        int genericRunningRequestCount = 0;
        int uploadRunningRequestCount = 0;
        int downloadRunningRequestCount = 0;
        i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            request = (RPCRequest) i$.next();
            if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                genericRunningRequestCount++;
            } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                uploadRunningRequestCount++;
            } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                downloadRunningRequestCount++;
            }
        }
        int i = 0;
        while (i < this.requestQueue.size()) {
            request = (RPCRequest) this.requestQueue.get(i);
            if (request.cancelled) {
                this.requestQueue.remove(i);
                i--;
            } else {
                if (this.updatingDcSettings && this.datacenters.size() > 1 && (request.rawRequest instanceof TL_help_getConfig) && this.updatingDcStartTime < currentTime - 60) {
                    this.updatingDcStartTime = currentTime;
                    arrayList = new ArrayList(this.datacenters.values());
                    for (a = 0; a < arrayList.size(); a++) {
                        if (((Datacenter) arrayList.get(a)).datacenterId == request.runningDatacenterId) {
                            arrayList.remove(a);
                            break;
                        }
                    }
                    request.runningDatacenterId = ((Datacenter) arrayList.get(Math.abs(MessagesController.random.nextInt()) % arrayList.size())).datacenterId;
                }
                datacenterId = request.runningDatacenterId;
                if (datacenterId == Integer.MAX_VALUE) {
                    if (this.movingToDatacenterId == Integer.MAX_VALUE || (request.flags & RPCRequest.RPCRequestClassEnableUnauthorized) != 0) {
                        datacenterId = this.currentDatacenterId;
                    }
                }
                requestDatacenter = datacenterWithId(datacenterId);
                if (requestDatacenter == null) {
                    unknownDatacenterIds.add(Integer.valueOf(datacenterId));
                } else if (requestDatacenter.authKey == null) {
                    neededDatacenterIds.add(Integer.valueOf(datacenterId));
                } else if (requestDatacenter.authorized || request.runningDatacenterId == Integer.MAX_VALUE || request.runningDatacenterId == this.currentDatacenterId || (request.flags & RPCRequest.RPCRequestClassEnableUnauthorized) != 0) {
                    if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                        if (activeTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId)) == null) {
                        }
                    }
                    if (!(updatingState && ((request.rawRequest instanceof TL_account_updateStatus) || (request.rawRequest instanceof TL_account_registerDevice)))) {
                        if (request.requiresCompletion) {
                            if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                                if (genericRunningRequestCount < 60) {
                                    int intValue;
                                    genericRunningRequestCount++;
                                    tokenIt = (Integer) activeTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId));
                                    if (tokenIt != null) {
                                        intValue = tokenIt.intValue();
                                    } else {
                                        intValue = 0;
                                    }
                                    request.transportChannelToken = intValue;
                                }
                            } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                                if (uploadRunningRequestCount < 20) {
                                    if (1 == null) {
                                        FileLog.m798d("tmessages", "Don't have any network connection, skipping upload request");
                                    } else if (uploadRunningRequestCount < 5) {
                                        uploadTokenIt = (Integer) activeUploadTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId));
                                        request.transportChannelToken = uploadTokenIt != null ? uploadTokenIt.intValue() : 0;
                                        uploadRunningRequestCount++;
                                    }
                                }
                            } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                                if (1 == null) {
                                    FileLog.m798d("tmessages", "Don't have any network connection, skipping download request");
                                } else if (downloadRunningRequestCount < 5) {
                                    downloadTokenIt = (Integer) activeDownloadTransportTokens.get(Integer.valueOf(requestDatacenter.datacenterId));
                                    request.transportChannelToken = downloadTokenIt != null ? downloadTokenIt.intValue() : 0;
                                    downloadRunningRequestCount++;
                                }
                            }
                        }
                        long messageId = generateMessageId();
                        SerializedData os = new SerializedData();
                        request.rpcRequest.serializeToStream(os);
                        if (os.length() != 0) {
                            sessionId = 0;
                            if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                                sessionId = requestDatacenter.authSessionId;
                            } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                                sessionId = requestDatacenter.authDownloadSessionId;
                            } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                                sessionId = requestDatacenter.authUploadSessionId;
                            }
                            networkMessage = new NetworkMessage();
                            networkMessage.protoMessage = new TL_protoMessage();
                            networkMessage.protoMessage.msg_id = messageId;
                            networkMessage.protoMessage.seqno = generateMessageSeqNo(sessionId, true);
                            networkMessage.protoMessage.bytes = os.length();
                            networkMessage.protoMessage.body = request.rpcRequest;
                            networkMessage.rawRequest = request.rawRequest;
                            networkMessage.requestId = request.token;
                            request.runningMessageId = messageId;
                            request.runningMessageSeqNo = networkMessage.protoMessage.seqno;
                            request.serializedLength = os.length();
                            request.runningStartTime = (int) (System.currentTimeMillis() / 1000);
                            if (request.requiresCompletion) {
                                this.runningRequests.add(request);
                            }
                            if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0) {
                                addMessageToDatacenter(genericMessagesToDatacenters, requestDatacenter.datacenterId, networkMessage);
                            } else if ((request.flags & RPCRequest.RPCRequestClassDownloadMedia) != 0) {
                                arr = new ArrayList();
                                arr.add(networkMessage);
                                proceedToSendingMessages(arr, sessionId, requestDatacenter.downloadConnection, false, false);
                            } else if ((request.flags & RPCRequest.RPCRequestClassUploadMedia) != 0) {
                                arr = new ArrayList();
                                arr.add(networkMessage);
                                proceedToSendingMessages(arr, sessionId, requestDatacenter.uploadConnection, false, false);
                            } else {
                                FileLog.m800e("tmessages", "***** Error: request " + request.rawRequest + " has undefined session");
                            }
                        } else {
                            FileLog.m800e("tmessages", "***** Couldn't serialize " + request.rawRequest);
                        }
                        this.requestQueue.remove(i);
                        i--;
                    }
                } else {
                    unauthorizedDatacenterIds.add(Integer.valueOf(datacenterId));
                }
            }
            i++;
        }
        for (Datacenter datacenter22 : this.datacenters.values()) {
            if (!(genericMessagesToDatacenters.get(Integer.valueOf(datacenter22.datacenterId)) != null || datacenter22.connection == null || datacenter22.connection.channelToken == 0)) {
                ArrayList<Long> arr2 = (ArrayList) this.messagesIdsForConfirmation.get(Long.valueOf(datacenter22.authSessionId));
                if (!(arr2 == null || arr2.size() == 0)) {
                    genericMessagesToDatacenters.put(Integer.valueOf(datacenter22.datacenterId), new ArrayList());
                }
            }
        }
        for (Integer intValue2 : genericMessagesToDatacenters.keySet()) {
            int iter = intValue2.intValue();
            datacenter22 = datacenterWithId(iter);
            if (datacenter22 != null) {
                boolean scannedPreviousRequests = false;
                long lastSendMessageRpcId = 0;
                boolean hasSendMessage = false;
                arr = (ArrayList) genericMessagesToDatacenters.get(Integer.valueOf(iter));
                it = arr.iterator();
                while (it.hasNext()) {
                    networkMessage = (NetworkMessage) it.next();
                    TL_protoMessage message = networkMessage.protoMessage;
                    Object rawRequest = networkMessage.rawRequest;
                    if (rawRequest != null && ((rawRequest instanceof TL_messages_sendMessage) || (rawRequest instanceof TL_messages_sendMedia) || (rawRequest instanceof TL_messages_forwardMessages) || (rawRequest instanceof TL_messages_sendEncrypted))) {
                        if (rawRequest instanceof TL_messages_sendMessage) {
                            hasSendMessage = true;
                        }
                        if (!scannedPreviousRequests) {
                            scannedPreviousRequests = true;
                            ArrayList<Long> currentRequests = new ArrayList();
                            Iterator i$2 = arr.iterator();
                            while (i$2.hasNext()) {
                                NetworkMessage currentNetworkMessage = (NetworkMessage) i$2.next();
                                TL_protoMessage currentMessage = currentNetworkMessage.protoMessage;
                                Object currentRawRequest = currentNetworkMessage.rawRequest;
                                if ((currentRawRequest instanceof TL_messages_sendMessage) || (currentRawRequest instanceof TL_messages_sendMedia) || (currentRawRequest instanceof TL_messages_forwardMessages) || (currentRawRequest instanceof TL_messages_sendEncrypted)) {
                                    currentRequests.add(Long.valueOf(currentMessage.msg_id));
                                }
                            }
                            long maxRequestId = 0;
                            i$2 = this.runningRequests.iterator();
                            while (i$2.hasNext()) {
                                request = (RPCRequest) i$2.next();
                                if ((request.rawRequest instanceof TL_messages_sendMessage) || (request.rawRequest instanceof TL_messages_sendMedia) || (request.rawRequest instanceof TL_messages_forwardMessages) || (request.rawRequest instanceof TL_messages_sendEncrypted)) {
                                    if (!currentRequests.contains(Long.valueOf(request.runningMessageId))) {
                                        maxRequestId = Math.max(maxRequestId, request.runningMessageId);
                                    }
                                }
                            }
                            lastSendMessageRpcId = maxRequestId;
                        }
                        if (!(lastSendMessageRpcId == 0 || lastSendMessageRpcId == message.msg_id)) {
                            TLObject invokeAfterMsg = new TL_invokeAfterMsg();
                            invokeAfterMsg.msg_id = lastSendMessageRpcId;
                            invokeAfterMsg.query = message.body;
                            message.body = invokeAfterMsg;
                            message.bytes = (message.bytes + 4) + 8;
                        }
                        lastSendMessageRpcId = message.msg_id;
                    }
                }
                if (datacenter22.connection == null) {
                    datacenter22.connection = new TcpConnection(datacenter22.datacenterId);
                    datacenter22.connection.delegate = this;
                    datacenter22.connection.transportRequestClass = RPCRequest.RPCRequestClassGeneric;
                }
                proceedToSendingMessages(arr, datacenter22.authSessionId, datacenter22.connection, hasSendMessage, arr.size() != 0);
            }
        }
        if ((RPCRequest.RPCRequestClassGeneric & requestClass) != 0) {
            ArrayList<NetworkMessage> messagesIt;
            if (_datacenterId == Integer.MIN_VALUE) {
                for (Datacenter datacenter222 : this.datacenters.values()) {
                    messagesIt = (ArrayList) genericMessagesToDatacenters.get(Integer.valueOf(datacenter222.datacenterId));
                    if (messagesIt == null || messagesIt.size() == 0) {
                        generatePing(datacenter222);
                    }
                }
            } else {
                messagesIt = (ArrayList) genericMessagesToDatacenters.get(Integer.valueOf(_datacenterId));
                if (messagesIt == null || messagesIt.size() == 0) {
                    generatePing();
                }
            }
        }
        if (!(unknownDatacenterIds.isEmpty() || this.updatingDcSettings)) {
            updateDcSettings();
        }
        i$ = neededDatacenterIds.iterator();
        while (i$.hasNext()) {
            int num = ((Integer) i$.next()).intValue();
            if (num != this.movingToDatacenterId) {
                notFound = true;
                it = this.actionQueue.iterator();
                while (it.hasNext()) {
                    actor = (Action) it.next();
                    if ((actor instanceof HandshakeAction) && ((HandshakeAction) actor).datacenter.datacenterId == num) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    Action handshakeAction = new HandshakeAction(datacenterWithId(num));
                    handshakeAction.delegate = this;
                    dequeueActor(handshakeAction, true);
                }
            }
        }
        i$ = unauthorizedDatacenterIds.iterator();
        while (i$.hasNext()) {
            num = ((Integer) i$.next()).intValue();
            if (!(num == this.currentDatacenterId || num == this.movingToDatacenterId || UserConfig.clientUserId == 0)) {
                notFound = true;
                it = this.actionQueue.iterator();
                while (it.hasNext()) {
                    actor = (Action) it.next();
                    if ((actor instanceof ExportAuthorizationAction) && ((ExportAuthorizationAction) actor).datacenter.datacenterId == num) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    handshakeAction = new ExportAuthorizationAction(datacenterWithId(num));
                    handshakeAction.delegate = this;
                    dequeueActor(handshakeAction, true);
                }
            }
        }
    }

    void addMessageToDatacenter(HashMap<Integer, ArrayList<NetworkMessage>> pMap, int datacenterId, NetworkMessage message) {
        ArrayList<NetworkMessage> arr = (ArrayList) pMap.get(Integer.valueOf(datacenterId));
        if (arr == null) {
            arr = new ArrayList();
            pMap.put(Integer.valueOf(datacenterId), arr);
        }
        arr.add(message);
    }

    TL_protoMessage wrapMessage(TLObject message, long sessionId, boolean meaningful) {
        SerializedData os = new SerializedData();
        message.serializeToStream(os);
        if (os.length() != 0) {
            TL_protoMessage protoMessage = new TL_protoMessage();
            protoMessage.msg_id = generateMessageId();
            protoMessage.bytes = os.length();
            protoMessage.body = message;
            protoMessage.seqno = generateMessageSeqNo(sessionId, meaningful);
            return protoMessage;
        }
        FileLog.m800e("tmessages", "***** Couldn't serialize " + message);
        return null;
    }

    void proceedToSendingMessages(ArrayList<NetworkMessage> messageList, long sessionId, TcpConnection connection, boolean reportAck, boolean requestShortTimeout) {
        if (sessionId != 0) {
            ArrayList<NetworkMessage> messages = new ArrayList();
            if (messageList != null) {
                messages.addAll(messageList);
            }
            ArrayList<Long> arr = (ArrayList) this.messagesIdsForConfirmation.get(Long.valueOf(sessionId));
            if (!(arr == null || arr.size() == 0)) {
                TL_msgs_ack msgAck = new TL_msgs_ack();
                msgAck.msg_ids = new ArrayList();
                msgAck.msg_ids.addAll(arr);
                SerializedData os = new SerializedData();
                msgAck.serializeToStream(os);
                if (os.length() != 0) {
                    NetworkMessage networkMessage = new NetworkMessage();
                    networkMessage.protoMessage = new TL_protoMessage();
                    networkMessage.protoMessage.msg_id = generateMessageId();
                    networkMessage.protoMessage.seqno = generateMessageSeqNo(sessionId, false);
                    networkMessage.protoMessage.bytes = os.length();
                    networkMessage.protoMessage.body = msgAck;
                    messages.add(networkMessage);
                } else {
                    FileLog.m800e("tmessages", "***** Couldn't serialize ");
                }
                arr.clear();
            }
            sendMessagesToTransport(messages, connection, sessionId, reportAck, requestShortTimeout);
        }
    }

    void sendMessagesToTransport(ArrayList<NetworkMessage> messagesToSend, TcpConnection connection, long sessionId, boolean reportAck, boolean requestShortTimeout) {
        if (messagesToSend.size() != 0) {
            if (connection == null) {
                FileLog.m800e("tmessages", String.format("***** Transport for session 0x%x not found", new Object[]{Long.valueOf(sessionId)}));
                return;
            }
            ArrayList<NetworkMessage> currentMessages = new ArrayList();
            int currentSize = 0;
            int a = 0;
            while (a < messagesToSend.size()) {
                NetworkMessage networkMessage = (NetworkMessage) messagesToSend.get(a);
                currentMessages.add(networkMessage);
                currentSize += networkMessage.protoMessage.bytes;
                if (currentSize >= 3072 || a == messagesToSend.size() - 1) {
                    ArrayList<Integer> quickAckId = new ArrayList();
                    byte[] transportData = createConnectionData(currentMessages, sessionId, quickAckId, connection);
                    if (transportData != null) {
                        if (reportAck && quickAckId.size() != 0) {
                            ArrayList<Long> requestIds = new ArrayList();
                            Iterator i$ = messagesToSend.iterator();
                            while (i$.hasNext()) {
                                NetworkMessage message = (NetworkMessage) i$.next();
                                if (message.requestId != 0) {
                                    requestIds.add(Long.valueOf(message.requestId));
                                }
                            }
                            if (requestIds.size() != 0) {
                                int ack = ((Integer) quickAckId.get(0)).intValue();
                                ArrayList<Long> arr = (ArrayList) this.quickAckIdToRequestIds.get(Integer.valueOf(ack));
                                if (arr == null) {
                                    arr = new ArrayList();
                                    this.quickAckIdToRequestIds.put(Integer.valueOf(ack), arr);
                                }
                                arr.addAll(requestIds);
                            }
                        }
                        connection.sendData(transportData, reportAck, requestShortTimeout);
                    } else {
                        FileLog.m800e("tmessages", "***** Transport data is nil");
                    }
                    currentSize = 0;
                    currentMessages.clear();
                }
                a++;
            }
        }
    }

    byte[] createConnectionData(ArrayList<NetworkMessage> messages, long sessionId, ArrayList<Integer> quickAckId, TcpConnection connection) {
        Datacenter datacenter = datacenterWithId(connection.getDatacenterId());
        if (datacenter.authKey == null) {
            return null;
        }
        long messageId;
        TLObject messageBody;
        int messageSeqNo;
        TL_protoMessage message;
        TLObject messageContainer;
        if (messages.size() == 1) {
            message = ((NetworkMessage) messages.get(0)).protoMessage;
            FileLog.m798d("tmessages", sessionId + ":Send message " + datacenter.datacenterId + "> Send message (" + message.seqno + ", " + message.msg_id + "): " + message.body);
            long msg_time = getTimeFromMsgId(message.msg_id);
            long currentTime = System.currentTimeMillis() + (((long) this.timeDifference) * 1000);
            if (msg_time < currentTime - 30000 || msg_time > 25000 + currentTime) {
                FileLog.m798d("tmessages", "wrap in messages continaer");
                messageContainer = new TL_msg_container();
                messageContainer.messages = new ArrayList();
                messageContainer.messages.add(message);
                messageId = generateMessageId();
                messageBody = messageContainer;
                messageSeqNo = generateMessageSeqNo(sessionId, false);
            } else {
                messageId = message.msg_id;
                messageBody = message.body;
                messageSeqNo = message.seqno;
            }
        } else {
            messageContainer = new TL_msg_container();
            ArrayList<TL_protoMessage> containerMessages = new ArrayList(messages.size());
            Iterator i$ = messages.iterator();
            while (i$.hasNext()) {
                message = ((NetworkMessage) i$.next()).protoMessage;
                containerMessages.add(message);
                FileLog.m798d("tmessages", sessionId + ":DC" + datacenter.datacenterId + "> Send message (" + message.seqno + ", " + message.msg_id + "): " + message.body);
            }
            messageContainer.messages = containerMessages;
            messageId = generateMessageId();
            messageBody = messageContainer;
            messageSeqNo = generateMessageSeqNo(sessionId, false);
        }
        SerializedData innerMessageOs = new SerializedData();
        messageBody.serializeToStream(innerMessageOs);
        byte[] messageData = innerMessageOs.toByteArray();
        SerializedData serializedData = new SerializedData(messageData.length + 32);
        long serverSalt = datacenter.selectServerSalt(getCurrentTime());
        if (serverSalt == 0) {
            serializedData.writeInt64(0);
        } else {
            serializedData.writeInt64(serverSalt);
        }
        serializedData.writeInt64(sessionId);
        serializedData.writeInt64(messageId);
        serializedData.writeInt32(messageSeqNo);
        serializedData.writeInt32(messageData.length);
        serializedData.writeRaw(messageData);
        byte[] innerData = serializedData.toByteArray();
        Object messageKeyFull = Utilities.computeSHA1(innerData);
        Object messageKey = new byte[16];
        System.arraycopy(messageKeyFull, messageKeyFull.length - 16, messageKey, 0, 16);
        if (quickAckId != null) {
            quickAckId.add(Integer.valueOf(new SerializedData((byte[]) messageKeyFull).readInt32() & Integer.MAX_VALUE));
        }
        MessageKeyData keyData = Utilities.generateMessageKeyData(datacenter.authKey, messageKey, false);
        SerializedData dataForEncryption = new SerializedData(innerData.length + (innerData.length % 16));
        dataForEncryption.writeRaw(innerData);
        byte[] b = new byte[1];
        while (dataForEncryption.length() % 16 != 0) {
            MessagesController.random.nextBytes(b);
            dataForEncryption.writeByte(b[0]);
        }
        byte[] encryptedData = Utilities.aesIgeEncryption(dataForEncryption.toByteArray(), keyData.aesKey, keyData.aesIv, true, false);
        SerializedData data;
        try {
            data = new SerializedData((datacenter.authKeyId.length + messageKey.length) + encryptedData.length);
            data.writeRaw(datacenter.authKeyId);
            data.writeRaw(messageKey);
            data.writeRaw(encryptedData);
            return data.toByteArray();
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            System.gc();
            data = new SerializedData();
            data.writeRaw(datacenter.authKeyId);
            data.writeRaw(messageKey);
            data.writeRaw(encryptedData);
            return data.toByteArray();
        }
    }

    void refillSaltSet(final Datacenter datacenter) {
        Iterator i$ = this.requestQueue.iterator();
        while (i$.hasNext()) {
            if (((RPCRequest) i$.next()).rawRequest instanceof TL_get_future_salts) {
                return;
            }
        }
        i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            if (((RPCRequest) i$.next()).rawRequest instanceof TL_get_future_salts) {
                return;
            }
        }
        TL_get_future_salts getFutureSalts = new TL_get_future_salts();
        getFutureSalts.num = 64;
        performRpc(getFutureSalts, new RPCRequestDelegate() {
            public void run(TLObject response, TL_error error) {
                TL_futuresalts res = (TL_futuresalts) response;
                if (error == null) {
                    datacenter.mergeServerSalts(ConnectionsManager.this.getCurrentTime(), res.salts);
                    ConnectionsManager.this.saveSession();
                }
            }
        }, null, true, RPCRequest.RPCRequestClassGeneric, datacenter.datacenterId);
    }

    void messagesConfirmed(final long requestMsgId) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                Iterator i$ = ConnectionsManager.this.runningRequests.iterator();
                while (i$.hasNext()) {
                    RPCRequest request = (RPCRequest) i$.next();
                    if (requestMsgId == request.runningMessageId) {
                        request.confirmed = true;
                    }
                }
            }
        });
    }

    void rpcCompleted(final long requestMsgId) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int i = 0;
                while (i < ConnectionsManager.this.runningRequests.size()) {
                    RPCRequest request = (RPCRequest) ConnectionsManager.this.runningRequests.get(i);
                    ConnectionsManager.this.removeRequestInClass(Long.valueOf(request.token));
                    if (request.respondsToMessageId(requestMsgId)) {
                        ConnectionsManager.this.runningRequests.remove(i);
                        i--;
                    }
                    i++;
                }
            }
        });
    }

    void processMessage(TLObject message, long messageId, int messageSeqNo, long messageSalt, TcpConnection connection, long sessionId, long innerMsgId, long containerMessageId) {
        if (message == null) {
            FileLog.m800e("tmessages", "message is null");
            return;
        }
        Datacenter datacenter = datacenterWithId(connection.getDatacenterId());
        ServerSalt serverSaltDesc;
        Iterator i$;
        RPCRequest request;
        if (message instanceof TL_new_session_created) {
            TL_new_session_created newSession = (TL_new_session_created) message;
            ArrayList<Long> arr = (ArrayList) this.processedSessionChanges.get(Long.valueOf(sessionId));
            if (arr == null) {
                arr = new ArrayList();
                this.processedSessionChanges.put(Long.valueOf(sessionId), arr);
            }
            if (!arr.contains(Long.valueOf(newSession.unique_id))) {
                FileLog.m798d("tmessages", "New session:");
                FileLog.m798d("tmessages", String.format("    first message id: %d", new Object[]{Long.valueOf(newSession.first_msg_id)}));
                FileLog.m798d("tmessages", String.format("    server salt: %d", new Object[]{Long.valueOf(newSession.server_salt)}));
                FileLog.m798d("tmessages", String.format("    unique id: %d", new Object[]{Long.valueOf(newSession.unique_id)}));
                long serverSalt = newSession.server_salt;
                serverSaltDesc = new ServerSalt();
                serverSaltDesc.validSince = getCurrentTime();
                serverSaltDesc.validUntil = getCurrentTime() + 1800;
                serverSaltDesc.value = serverSalt;
                datacenter.addServerSalt(serverSaltDesc);
                i$ = this.runningRequests.iterator();
                while (i$.hasNext()) {
                    request = (RPCRequest) i$.next();
                    Datacenter dcenter = datacenterWithId(request.runningDatacenterId);
                    if (request.runningMessageId < newSession.first_msg_id && (request.flags & connection.transportRequestClass) != 0 && dcenter != null && dcenter.datacenterId == datacenter.datacenterId) {
                        request.runningMessageId = 0;
                        request.runningMessageSeqNo = 0;
                        request.runningStartTime = 0;
                        request.runningMinStartTime = 0;
                        request.transportChannelToken = 0;
                    }
                }
                saveSession();
                if (sessionId == datacenter.authSessionId && datacenter.datacenterId == this.currentDatacenterId && UserConfig.clientActivated) {
                    MessagesController.Instance.getDifference();
                }
                arr.add(Long.valueOf(newSession.unique_id));
            }
        } else if (message instanceof TL_msg_container) {
            i$ = ((TL_msg_container) message).messages.iterator();
            while (i$.hasNext()) {
                TL_protoMessage innerMessage = (TL_protoMessage) i$.next();
                long innerMessageId = innerMessage.msg_id;
                if (innerMessage.seqno % 2 != 0) {
                    set = (ArrayList) this.messagesIdsForConfirmation.get(Long.valueOf(sessionId));
                    if (set == null) {
                        set = new ArrayList();
                        this.messagesIdsForConfirmation.put(Long.valueOf(sessionId), set);
                    }
                    set.add(Long.valueOf(innerMessageId));
                }
                if (!isMessageIdProcessed(sessionId, innerMessageId)) {
                    processMessage(innerMessage.body, 0, innerMessage.seqno, messageSalt, connection, sessionId, innerMessageId, messageId);
                    addProcessedMessageId(sessionId, innerMessageId);
                }
            }
        } else if (message instanceof TL_pong) {
            long pingId = ((TL_pong) message).ping_id;
            ArrayList<Long> itemsToDelete = new ArrayList();
            for (Long pid : this.pingIdToDate.keySet()) {
                if (pid.longValue() == pingId) {
                    int pingTime = ((int) (System.currentTimeMillis() / 1000)) - ((Integer) this.pingIdToDate.get(pid)).intValue();
                    if (Math.abs(pingTime) < 10) {
                        this.currentPingTime = (this.currentPingTime + pingTime) / 2;
                        if (messageId != 0) {
                            this.timeDifference = (int) (((double) ((getTimeFromMsgId(messageId) - System.currentTimeMillis()) / 1000)) - (((double) this.currentPingTime) / 2.0d));
                        }
                    }
                    itemsToDelete.add(pid);
                } else if (pid.longValue() < pingId) {
                    itemsToDelete.add(pid);
                }
            }
            i$ = itemsToDelete.iterator();
            while (i$.hasNext()) {
                this.pingIdToDate.remove((Long) i$.next());
            }
        } else if (message instanceof TL_futuresalts) {
            TLObject futureSalts = (TL_futuresalts) message;
            requestMid = futureSalts.req_msg_id;
            i$ = this.runningRequests.iterator();
            while (i$.hasNext()) {
                request = (RPCRequest) i$.next();
                if (request.respondsToMessageId(requestMid)) {
                    if (request.completionBlock != null) {
                        request.completionBlock.run(futureSalts, null);
                    }
                    messagesConfirmed(requestMid);
                    rpcCompleted(requestMid);
                    return;
                }
            }
        } else if (message instanceof DestroySessionRes) {
            DestroySessionRes res = (DestroySessionRes) message;
            ArrayList<Long> lst = new ArrayList();
            lst.addAll(this.sessionsToDestroy);
            this.destroyingSessions.remove(Long.valueOf(res.session_id));
            i$ = lst.iterator();
            while (i$.hasNext()) {
                long session = ((Long) i$.next()).longValue();
                if (session == res.session_id) {
                    this.sessionsToDestroy.remove(Long.valueOf(session));
                    String str = "tmessages";
                    String str2 = "Destroyed session %d (%s)";
                    Object[] objArr = new Object[2];
                    objArr[0] = Long.valueOf(res.session_id);
                    objArr[1] = res instanceof TL_destroy_session_ok ? "ok" : "not found";
                    FileLog.m798d(str, String.format(str2, objArr));
                    return;
                }
            }
        } else if (message instanceof TL_rpc_result) {
            String errorMessage;
            String errorMsg;
            Matcher matcher;
            Integer val;
            TL_rpc_result resultContainer = (TL_rpc_result) message;
            long resultMid = resultContainer.req_msg_id;
            boolean ignoreResult = false;
            FileLog.m798d("tmessages", "object in rpc_result is " + resultContainer.result);
            if (resultContainer.result instanceof RpcError) {
                errorMessage = ((RpcError) resultContainer.result).error_message;
                FileLog.m800e("tmessages", String.format("***** RPC error %d: %s", new Object[]{Integer.valueOf(((RpcError) resultContainer.result).error_code), errorMessage}));
                int migrateToDatacenterId = Integer.MAX_VALUE;
                if (((RpcError) resultContainer.result).error_code == 303) {
                    ArrayList<String> migrateErrors = new ArrayList();
                    migrateErrors.add("NETWORK_MIGRATE_");
                    migrateErrors.add("PHONE_MIGRATE_");
                    migrateErrors.add("USER_MIGRATE_");
                    i$ = migrateErrors.iterator();
                    while (i$.hasNext()) {
                        String possibleError = (String) i$.next();
                        if (errorMessage.contains(possibleError)) {
                            errorMsg = errorMessage.replace(possibleError, BuildConfig.FLAVOR);
                            matcher = Pattern.compile("[0-9]+").matcher(errorMsg);
                            if (matcher.find()) {
                                errorMsg = matcher.group(0);
                            }
                            try {
                                val = Integer.valueOf(Integer.parseInt(errorMsg));
                            } catch (Exception e) {
                                val = null;
                            }
                            if (val != null) {
                                migrateToDatacenterId = val.intValue();
                            } else {
                                migrateToDatacenterId = Integer.MAX_VALUE;
                            }
                        }
                    }
                }
                if (migrateToDatacenterId != Integer.MAX_VALUE) {
                    ignoreResult = true;
                    moveToDatacenter(migrateToDatacenterId);
                }
            }
            int retryRequestsFromDatacenter = -1;
            int retryRequestsClass = 0;
            if (!ignoreResult) {
                boolean found = false;
                i$ = this.runningRequests.iterator();
                while (i$.hasNext()) {
                    request = (RPCRequest) i$.next();
                    if (request.respondsToMessageId(resultMid)) {
                        found = true;
                        boolean discardResponse = false;
                        boolean isError = false;
                        if (request.completionBlock != null) {
                            TL_error implicitError = null;
                            if (resultContainer.result instanceof TL_gzip_packed) {
                                TL_gzip_packed packet = (TL_gzip_packed) resultContainer.result;
                                TLObject uncomressed = Utilities.decompress(packet.packed_data, request.rawRequest);
                                if (uncomressed == null) {
                                    System.gc();
                                    uncomressed = Utilities.decompress(packet.packed_data, request.rawRequest);
                                }
                                if (uncomressed == null) {
                                    throw new RuntimeException("failed to decomress responce for " + request.rawRequest);
                                }
                                resultContainer.result = uncomressed;
                            }
                            if (resultContainer.result instanceof RpcError) {
                                errorMessage = ((RpcError) resultContainer.result).error_message;
                                FileLog.m800e("tmessages", String.format("***** RPC error %d: %s", new Object[]{Integer.valueOf(((RpcError) resultContainer.result).error_code), errorMessage}));
                                int errorCode = ((RpcError) resultContainer.result).error_code;
                                if (errorCode == 500 || errorCode < 0) {
                                    if ((request.flags & RPCRequest.RPCRequestClassFailOnServerErrors) == 0) {
                                        discardResponse = true;
                                        request.runningMinStartTime = request.runningStartTime + 1;
                                        request.confirmed = false;
                                    } else if (request.serverFailureCount < 1) {
                                        discardResponse = true;
                                        request.runningMinStartTime = request.runningStartTime + 1;
                                        request.serverFailureCount++;
                                    }
                                } else if (errorCode == 420 && (request.flags & RPCRequest.RPCRequestClassFailOnServerErrors) == 0) {
                                    double waitTime = 2.0d;
                                    if (errorMessage.contains("FLOOD_WAIT_")) {
                                        errorMsg = errorMessage.replace("FLOOD_WAIT_", BuildConfig.FLAVOR);
                                        matcher = Pattern.compile("[0-9]+").matcher(errorMsg);
                                        if (matcher.find()) {
                                            errorMsg = matcher.group(0);
                                        }
                                        try {
                                            val = Integer.valueOf(Integer.parseInt(errorMsg));
                                        } catch (Exception e2) {
                                            val = null;
                                        }
                                        if (val != null) {
                                            waitTime = (double) val.intValue();
                                        }
                                    }
                                    discardResponse = true;
                                    request.runningMinStartTime = (int) (((double) (System.currentTimeMillis() / 1000)) + Math.min(30.0d, waitTime));
                                    request.confirmed = false;
                                }
                                implicitError = new TL_error();
                                implicitError.code = ((RpcError) resultContainer.result).error_code;
                                implicitError.text = ((RpcError) resultContainer.result).error_message;
                            } else if (!(resultContainer.result instanceof TL_error) && (request.rawRequest == null || !request.rawRequest.responseClass().isAssignableFrom(resultContainer.result.getClass()))) {
                                if (request.rawRequest == null) {
                                    FileLog.m800e("tmessages", "rawRequest is null");
                                } else {
                                    FileLog.m800e("tmessages", "***** RPC error: invalid response class " + resultContainer.result + " (" + request.rawRequest.responseClass() + " expected)");
                                }
                                implicitError = new TL_error();
                                implicitError.code = -1000;
                            }
                            if (!discardResponse) {
                                if (implicitError != null || (resultContainer.result instanceof TL_error)) {
                                    isError = true;
                                    request.completionBlock.run(null, implicitError != null ? implicitError : (TL_error) resultContainer.result);
                                } else {
                                    request.completionBlock.run(resultContainer.result, null);
                                }
                            }
                            if (implicitError != null && implicitError.code == 401) {
                                isError = true;
                                if (datacenter.datacenterId != this.currentDatacenterId && datacenter.datacenterId != this.movingToDatacenterId) {
                                    datacenter.authorized = false;
                                    saveSession();
                                    discardResponse = true;
                                    if (!((request.flags & RPCRequest.RPCRequestClassDownloadMedia) == 0 && (request.flags & RPCRequest.RPCRequestClassUploadMedia) == 0)) {
                                        retryRequestsFromDatacenter = datacenter.datacenterId;
                                        retryRequestsClass = request.flags;
                                    }
                                } else if ((request.flags & RPCRequest.RPCRequestClassGeneric) != 0 && UserConfig.clientActivated) {
                                    UserConfig.clearConfig();
                                    Utilities.RunOnUIThread(new Runnable() {
                                        public void run() {
                                            NotificationCenter.Instance.postNotificationName(1234, new Object[0]);
                                        }
                                    });
                                }
                            }
                        }
                        if (discardResponse) {
                            request.runningMessageId = 0;
                            request.runningMessageSeqNo = 0;
                            request.transportChannelToken = 0;
                        } else {
                            if (request.initRequest && !isError) {
                                if (datacenter.lastInitVersion != this.currentAppVersion) {
                                    datacenter.lastInitVersion = this.currentAppVersion;
                                    saveSession();
                                    FileLog.m800e("tmessages", "init connection completed");
                                } else {
                                    FileLog.m800e("tmessages", "rpc is init, but init connection already completed");
                                }
                            }
                            rpcCompleted(resultMid);
                        }
                        if (!found) {
                            FileLog.m798d("tmessages", "Response received, but request wasn't found.");
                            rpcCompleted(resultMid);
                        }
                        messagesConfirmed(resultMid);
                    }
                }
                if (found) {
                    FileLog.m798d("tmessages", "Response received, but request wasn't found.");
                    rpcCompleted(resultMid);
                }
                messagesConfirmed(resultMid);
            }
            if (retryRequestsFromDatacenter >= 0) {
                processRequestQueue(retryRequestsClass, retryRequestsFromDatacenter);
            } else {
                processRequestQueue(0, 0);
            }
        } else if (!(message instanceof TL_msgs_ack) && !(message instanceof TL_ping)) {
            if (message instanceof TL_bad_msg_notification) {
                TL_bad_msg_notification badMsgNotification = (TL_bad_msg_notification) message;
                FileLog.m800e("tmessages", String.format("***** Bad message: %d", new Object[]{Integer.valueOf(badMsgNotification.error_code)}));
                if (badMsgNotification.error_code == 16 || badMsgNotification.error_code == 17 || badMsgNotification.error_code == 19 || badMsgNotification.error_code == 32 || badMsgNotification.error_code == 33 || badMsgNotification.error_code == 64) {
                    long realId;
                    if (messageId != 0) {
                        realId = messageId;
                    } else {
                        realId = containerMessageId;
                    }
                    if (realId == 0) {
                        realId = innerMsgId;
                    }
                    if (realId != 0) {
                        this.timeDifference = (int) (((double) ((getTimeFromMsgId(messageId) - System.currentTimeMillis()) / 1000)) - (((double) this.currentPingTime) / 2.0d));
                    }
                    recreateSession(datacenter.authSessionId, datacenter);
                    saveSession();
                    this.lastOutgoingMessageId = 0;
                    clearRequestsForRequestClass(connection.transportRequestClass, datacenter);
                }
            } else if (message instanceof TL_bad_server_salt) {
                if (messageId != 0) {
                    this.timeDifference = (int) (((double) ((getTimeFromMsgId(messageId) - System.currentTimeMillis()) / 1000)) - (((double) this.currentPingTime) / 2.0d));
                    this.lastOutgoingMessageId = Math.max(messageId, this.lastOutgoingMessageId);
                }
                datacenter.clearServerSalts();
                serverSaltDesc = new ServerSalt();
                serverSaltDesc.validSince = getCurrentTime();
                serverSaltDesc.validUntil = getCurrentTime() + 1800;
                serverSaltDesc.value = messageSalt;
                datacenter.addServerSalt(serverSaltDesc);
                saveSession();
                refillSaltSet(datacenter);
                if (datacenter.authKey != null) {
                    processRequestQueue(RPCRequest.RPCRequestClassTransportMask, datacenter.datacenterId);
                }
            } else if (message instanceof MsgDetailedInfo) {
                MsgDetailedInfo detailedInfo = (MsgDetailedInfo) message;
                boolean requestResend = false;
                if (detailedInfo instanceof TL_msg_detailed_info) {
                    requestMid = ((TL_msg_detailed_info) detailedInfo).msg_id;
                    i$ = this.runningRequests.iterator();
                    while (i$.hasNext()) {
                        if (((RPCRequest) i$.next()).respondsToMessageId(requestMid)) {
                            requestResend = true;
                            break;
                        }
                    }
                } else if (!isMessageIdProcessed(sessionId, messageId)) {
                    requestResend = true;
                }
                if (requestResend) {
                    TLObject resendReq = new TL_msg_resend_req();
                    resendReq.msg_ids.add(Long.valueOf(detailedInfo.answer_msg_id));
                    NetworkMessage networkMessage = new NetworkMessage();
                    networkMessage.protoMessage = wrapMessage(resendReq, sessionId, false);
                    ArrayList<NetworkMessage> arr2 = new ArrayList();
                    arr2.add(networkMessage);
                    sendMessagesToTransport(arr2, connection, sessionId, false, true);
                    return;
                }
                set = (ArrayList) this.messagesIdsForConfirmation.get(Long.valueOf(sessionId));
                if (set == null) {
                    set = new ArrayList();
                    this.messagesIdsForConfirmation.put(Long.valueOf(sessionId), set);
                }
                set.add(Long.valueOf(detailedInfo.answer_msg_id));
            } else if (message instanceof TL_gzip_packed) {
                processMessage(Utilities.decompress(((TL_gzip_packed) message).packed_data, getRequestWithMessageId(messageId)), messageId, messageSeqNo, messageSalt, connection, sessionId, innerMsgId, containerMessageId);
            } else if (message instanceof Updates) {
                MessagesController.Instance.processUpdates((Updates) message, false);
            } else {
                FileLog.m800e("tmessages", "***** Error: unknown message class " + message);
            }
        }
    }

    void generatePing() {
        for (Datacenter datacenter : this.datacenters.values()) {
            if (datacenter.datacenterId == this.currentDatacenterId) {
                generatePing(datacenter);
            }
        }
    }

    byte[] generatePingData(Datacenter datacenter, boolean recordTime) {
        long sessionId = datacenter.authSessionId;
        if (sessionId == 0) {
            return null;
        }
        TL_ping ping = new TL_ping();
        long j = nextPingId;
        nextPingId = 1 + j;
        ping.ping_id = j;
        if (recordTime && sessionId == datacenter.authSessionId) {
            this.pingIdToDate.put(Long.valueOf(ping.ping_id), Integer.valueOf((int) (System.currentTimeMillis() / 1000)));
        }
        NetworkMessage networkMessage = new NetworkMessage();
        networkMessage.protoMessage = wrapMessage(ping, sessionId, false);
        ArrayList<NetworkMessage> arr = new ArrayList();
        arr.add(networkMessage);
        return createConnectionData(arr, sessionId, null, datacenter.connection);
    }

    void generatePing(Datacenter datacenter) {
        if (datacenter.connection != null && datacenter.connection.channelToken != 0) {
            byte[] transportData = generatePingData(datacenter, true);
            if (transportData != null) {
                datacenter.connection.sendData(transportData, false, true);
            }
        }
    }

    public long needsToDecodeMessageIdFromPartialData(TcpConnection connection, byte[] data) {
        if (data == null) {
            return -1;
        }
        Datacenter datacenter = (Datacenter) this.datacenters.get(Integer.valueOf(connection.getDatacenterId()));
        SerializedData is = new SerializedData(data);
        byte[] keyId = is.readData(8);
        if (new SerializedData(keyId).readInt64() == 0) {
            return -1;
        }
        if (datacenter.authKeyId == null || !Arrays.equals(keyId, datacenter.authKeyId)) {
            FileLog.m800e("tmessages", "Error: invalid auth key id " + connection);
            return -1;
        }
        MessageKeyData keyData = Utilities.generateMessageKeyData(datacenter.authKey, is.readData(16), true);
        byte[] messageData = Utilities.aesIgeEncryption(is.readData(data.length - 24), keyData.aesKey, keyData.aesIv, false, false);
        if (messageData == null) {
            return -1;
        }
        SerializedData messageIs = new SerializedData(messageData);
        long messageServerSalt = messageIs.readInt64();
        long messageSessionId = messageIs.readInt64();
        if (messageSessionId == datacenter.authSessionId || messageSessionId == datacenter.authDownloadSessionId || messageSessionId == datacenter.authUploadSessionId) {
            long messageId = messageIs.readInt64();
            int messageSeqNo = messageIs.readInt32();
            int messageLength = messageIs.readInt32();
            boolean[] stop = new boolean[1];
            long[] reqMsgId = new long[]{false};
            reqMsgId[0] = 0;
            while (!stop[0] && reqMsgId[0] == 0) {
                int signature = messageIs.readInt32(stop);
                if (stop[0]) {
                    break;
                }
                findReqMsgId(messageIs, signature, reqMsgId, stop);
            }
            return reqMsgId[0];
        }
        FileLog.m800e("tmessages", String.format("***** Error: invalid message session ID (%d instead of %d)", new Object[]{Long.valueOf(messageSessionId), Long.valueOf(datacenter.authSessionId)}));
        finishUpdatingState(connection);
        return -1;
    }

    private void findReqMsgId(SerializedData is, int signature, long[] reqMsgId, boolean[] failed) {
        int count;
        int i;
        if (signature == 1945237724) {
            if (is.length() < 4) {
                failed[0] = true;
                return;
            }
            count = is.readInt32(failed);
            if (!failed[0]) {
                i = 0;
                while (i < count) {
                    is.readInt64(failed);
                    if (!failed[0]) {
                        is.readInt32(failed);
                        if (!failed[0]) {
                            is.readInt32(failed);
                            if (!failed[0]) {
                                int innerSignature = is.readInt32(failed);
                                if (!failed[0]) {
                                    findReqMsgId(is, innerSignature, reqMsgId, failed);
                                    if (!failed[0] && reqMsgId[0] == 0) {
                                        i++;
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
        } else if (signature == -212046591) {
            long value = is.readInt64(failed);
            if (!failed[0]) {
                reqMsgId[0] = value;
            }
        } else if (signature == 1658238041) {
            is.readInt32(failed);
            if (!failed[0]) {
                count = is.readInt32(failed);
                if (!failed[0]) {
                    i = 0;
                    while (i < count) {
                        is.readInt32(failed);
                        if (!failed[0]) {
                            i++;
                        } else {
                            return;
                        }
                    }
                }
            }
        } else if (signature == 880243653) {
            is.readInt64(failed);
            if (!failed[0]) {
                is.readInt64(failed);
            }
        }
    }

    public void tcpConnectionProgressChanged(TcpConnection connection, long messageId, int currentSize, int length) {
        Iterator i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            RPCRequest request = (RPCRequest) i$.next();
            if (request.respondsToMessageId(messageId)) {
                if (request.progressBlock != null) {
                    request.progressBlock.progress(length, currentSize);
                    return;
                }
                return;
            }
        }
    }

    public void tcpConnectionClosed(TcpConnection connection) {
        if (connection.getDatacenterId() == this.currentDatacenterId && (connection.transportRequestClass & RPCRequest.RPCRequestClassGeneric) != 0) {
            if (isNetworkOnline()) {
                this.connectionState = 2;
            } else {
                this.connectionState = 1;
            }
            final int stateCopy = this.connectionState;
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.Instance.postNotificationName(703, Integer.valueOf(stateCopy));
                }
            });
        }
    }

    public void tcpConnectionConnected(TcpConnection connection) {
        if (datacenterWithId(connection.getDatacenterId()).authKey != null) {
            processRequestQueue(connection.transportRequestClass, connection.getDatacenterId());
        }
    }

    public void tcpConnectionQuiackAckReceived(TcpConnection connection, int ack) {
        ArrayList<Long> arr = (ArrayList) this.quickAckIdToRequestIds.get(Integer.valueOf(ack));
        if (arr != null) {
            Iterator i$ = this.runningRequests.iterator();
            while (i$.hasNext()) {
                RPCRequest request = (RPCRequest) i$.next();
                if (arr.contains(Long.valueOf(request.token)) && request.quickAckBlock != null) {
                    request.quickAckBlock.quickAck();
                }
            }
            this.quickAckIdToRequestIds.remove(Integer.valueOf(ack));
        }
    }

    private void finishUpdatingState(TcpConnection connection) {
        if (connection.getDatacenterId() == this.currentDatacenterId && (connection.transportRequestClass & RPCRequest.RPCRequestClassGeneric) != 0 && Instance.connectionState == 3 && !MessagesController.Instance.gettingDifference && !MessagesController.Instance.gettingDifferenceAgain) {
            Instance.connectionState = 0;
            final int stateCopy = Instance.connectionState;
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.Instance.postNotificationName(703, Integer.valueOf(stateCopy));
                }
            });
        }
    }

    public void tcpConnectionReceivedData(TcpConnection connection, byte[] data) {
        if (connection.getDatacenterId() == this.currentDatacenterId && (connection.transportRequestClass & RPCRequest.RPCRequestClassGeneric) != 0 && (this.connectionState == 1 || this.connectionState == 2)) {
            this.connectionState = 3;
            final int i = this.connectionState;
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.Instance.postNotificationName(703, Integer.valueOf(i));
                }
            });
        }
        Datacenter datacenter = datacenterWithId(connection.getDatacenterId());
        SerializedData serializedData = new SerializedData(data);
        byte[] keyId = serializedData.readData(8);
        long messageId;
        if (new SerializedData(keyId).readInt64() == 0) {
            messageId = serializedData.readInt64();
            if (isMessageIdProcessed(0, messageId)) {
                finishUpdatingState(connection);
                return;
            }
            int messageLength = serializedData.readInt32();
            int constructor = serializedData.readInt32();
            TLObject object = TLClassStore.Instance().TLdeserialize(serializedData, constructor, getRequestWithMessageId(messageId));
            processMessage(object, messageId, 0, 0, connection, 0, 0, 0);
            if (object != null) {
                addProcessedMessageId(0, messageId);
                return;
            }
            return;
        }
        if (datacenter.authKeyId != null) {
            if (Arrays.equals(keyId, datacenter.authKeyId)) {
                byte[] messageKey = serializedData.readData(16);
                MessageKeyData keyData = Utilities.generateMessageKeyData(datacenter.authKey, messageKey, true);
                byte[] messageData = Utilities.aesIgeEncryption(serializedData.readData(data.length - 24), keyData.aesKey, keyData.aesIv, false, false);
                if (messageData == null) {
                    FileLog.m800e("tmessages", "Error: can't decrypt message data " + connection);
                    return;
                }
                serializedData = new SerializedData(messageData);
                long messageServerSalt = serializedData.readInt64();
                long messageSessionId = serializedData.readInt64();
                if (messageSessionId == datacenter.authSessionId || messageSessionId == datacenter.authDownloadSessionId || messageSessionId == datacenter.authUploadSessionId) {
                    boolean doNotProcess = false;
                    messageId = serializedData.readInt64();
                    int messageSeqNo = serializedData.readInt32();
                    messageLength = serializedData.readInt32();
                    if (isMessageIdProcessed(messageSessionId, messageId)) {
                        doNotProcess = true;
                    }
                    if (messageSeqNo % 2 != 0) {
                        ArrayList<Long> set = (ArrayList) this.messagesIdsForConfirmation.get(Long.valueOf(messageSessionId));
                        if (set == null) {
                            set = new ArrayList();
                            this.messagesIdsForConfirmation.put(Long.valueOf(messageSessionId), set);
                        }
                        set.add(Long.valueOf(messageId));
                    }
                    Object realMessageKeyFull = Utilities.computeSHA1(messageData, 0, Math.min(messageLength + 32, messageData.length));
                    if (realMessageKeyFull != null) {
                        Object realMessageKey = new byte[16];
                        System.arraycopy(realMessageKeyFull, realMessageKeyFull.length - 16, realMessageKey, 0, 16);
                        if (Arrays.equals(messageKey, realMessageKey)) {
                            if (doNotProcess) {
                                proceedToSendingMessages(null, messageSessionId, connection, false, false);
                            } else {
                                constructor = serializedData.readInt32();
                                TLObject message = TLClassStore.Instance().TLdeserialize(serializedData, constructor, getRequestWithMessageId(messageId));
                                if (message == null) {
                                    FileLog.m800e("tmessages", "***** Error parsing message: " + constructor);
                                } else {
                                    processMessage(message, messageId, messageSeqNo, messageServerSalt, connection, messageSessionId, 0, 0);
                                    addProcessedMessageId(messageSessionId, messageId);
                                }
                            }
                            finishUpdatingState(connection);
                            return;
                        }
                        FileLog.m800e("tmessages", "***** Error: invalid message key");
                        return;
                    }
                    return;
                }
                FileLog.m800e("tmessages", String.format("***** Error: invalid message session ID (%d instead of %d)", new Object[]{Long.valueOf(messageSessionId), Long.valueOf(datacenter.authSessionId)}));
                finishUpdatingState(connection);
                return;
            }
        }
        FileLog.m800e("tmessages", "Error: invalid auth key id " + connection);
    }

    public TLObject getRequestWithMessageId(long msgId) {
        Iterator i$ = this.runningRequests.iterator();
        while (i$.hasNext()) {
            RPCRequest request = (RPCRequest) i$.next();
            if (msgId == request.runningMessageId) {
                return request.rawRequest;
            }
        }
        return null;
    }

    void moveToDatacenter(final int datacenterId) {
        if (this.movingToDatacenterId != datacenterId) {
            this.movingToDatacenterId = datacenterId;
            Datacenter currentDatacenter = datacenterWithId(this.currentDatacenterId);
            clearRequestsForRequestClass(RPCRequest.RPCRequestClassGeneric, currentDatacenter);
            clearRequestsForRequestClass(RPCRequest.RPCRequestClassDownloadMedia, currentDatacenter);
            clearRequestsForRequestClass(RPCRequest.RPCRequestClassUploadMedia, currentDatacenter);
            if (UserConfig.clientUserId != 0) {
                TL_auth_exportAuthorization exportAuthorization = new TL_auth_exportAuthorization();
                exportAuthorization.dc_id = datacenterId;
                performRpc(exportAuthorization, new RPCRequestDelegate() {

                    class C02951 implements Runnable {
                        C02951() {
                        }

                        public void run() {
                            ConnectionsManager.this.moveToDatacenter(datacenterId);
                        }
                    }

                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            ConnectionsManager.this.movingAuthorization = (TL_auth_exportedAuthorization) response;
                            ConnectionsManager.this.authorizeOnMovingDatacenter();
                            return;
                        }
                        Utilities.globalQueue.postRunnable(new C02951(), LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric, this.currentDatacenterId);
                return;
            }
            authorizeOnMovingDatacenter();
        }
    }

    void authorizeOnMovingDatacenter() {
        Datacenter datacenter = datacenterWithId(this.movingToDatacenterId);
        if (datacenter != null) {
            recreateSession(datacenter.authSessionId, datacenter);
            if (datacenter.authKey == null) {
                datacenter.clearServerSalts();
                HandshakeAction actor = new HandshakeAction(datacenter);
                actor.delegate = this;
                dequeueActor(actor, true);
            }
            if (this.movingAuthorization != null) {
                TL_auth_importAuthorization importAuthorization = new TL_auth_importAuthorization();
                importAuthorization.id = UserConfig.clientUserId;
                importAuthorization.bytes = this.movingAuthorization.bytes;
                performRpc(importAuthorization, new RPCRequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        ConnectionsManager.this.movingAuthorization = null;
                        if (error == null) {
                            ConnectionsManager.this.authorizedOnMovingDatacenter();
                        } else {
                            ConnectionsManager.this.moveToDatacenter(ConnectionsManager.this.movingToDatacenterId);
                        }
                    }
                }, null, true, RPCRequest.RPCRequestClassGeneric, datacenter.datacenterId);
                return;
            }
            authorizedOnMovingDatacenter();
        } else if (!this.updatingDcSettings) {
            updateDcSettings();
        }
    }

    void authorizedOnMovingDatacenter() {
        Datacenter datacenter = datacenterWithId(this.currentDatacenterId);
        if (!(datacenter == null || datacenter.connection == null)) {
            datacenter.connection.suspendConnection(true);
        }
        this.movingAuthorization = null;
        this.currentDatacenterId = this.movingToDatacenterId;
        this.movingToDatacenterId = Integer.MAX_VALUE;
        saveSession();
        processRequestQueue(0, 0);
    }

    public void dequeueActor(Action actor, boolean execute) {
        if (this.actionQueue.size() == 0 || execute) {
            actor.execute(null);
        }
        this.actionQueue.add(actor);
    }

    public void cancelActor(Action actor) {
        if (actor != null) {
            this.actionQueue.remove(actor);
        }
    }

    public void ActionDidFinishExecution(final Action action, HashMap<String, Object> params) {
        if (action instanceof HandshakeAction) {
            HandshakeAction eactor = (HandshakeAction) action;
            eactor.datacenter.connection.delegate = this;
            saveSession();
            if (eactor.datacenter.datacenterId == this.currentDatacenterId || eactor.datacenter.datacenterId == this.movingToDatacenterId) {
                this.timeDifference = ((Integer) params.get("timeDifference")).intValue();
                recreateSession(eactor.datacenter.authSessionId, eactor.datacenter);
            }
            processRequestQueue(RPCRequest.RPCRequestClassTransportMask, eactor.datacenter.datacenterId);
        } else if (action instanceof ExportAuthorizationAction) {
            Datacenter datacenter = ((ExportAuthorizationAction) action).datacenter;
            datacenter.authorized = true;
            saveSession();
            processRequestQueue(RPCRequest.RPCRequestClassTransportMask, datacenter.datacenterId);
        }
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ConnectionsManager.this.actionQueue.remove(action);
                action.delegate = null;
            }
        });
    }

    public void ActionDidFailExecution(final Action action) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ConnectionsManager.this.actionQueue.remove(action);
                action.delegate = null;
            }
        });
    }
}
