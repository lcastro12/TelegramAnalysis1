package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de.C0177e;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public final class df implements Callback {
    private static final Object kU = new Object();
    private static df kV;
    private final Context kW;
    private final HashMap<String, C0179a> kX = new HashMap();
    private final Handler mHandler;

    final class C0179a {
        private final String kY;
        private final C0178a kZ = new C0178a(this);
        private final HashSet<C0177e> la = new HashSet();
        private boolean lb;
        private IBinder lc;
        private ComponentName ld;
        final /* synthetic */ df le;
        private int mState = 0;

        public class C0178a implements ServiceConnection {
            final /* synthetic */ C0179a lf;

            public C0178a(C0179a c0179a) {
                this.lf = c0179a;
            }

            public void onServiceConnected(ComponentName component, IBinder binder) {
                synchronized (this.lf.le.kX) {
                    this.lf.lc = binder;
                    this.lf.ld = component;
                    Iterator it = this.lf.la.iterator();
                    while (it.hasNext()) {
                        ((C0177e) it.next()).onServiceConnected(component, binder);
                    }
                    this.lf.mState = 1;
                }
            }

            public void onServiceDisconnected(ComponentName component) {
                synchronized (this.lf.le.kX) {
                    this.lf.lc = null;
                    this.lf.ld = component;
                    Iterator it = this.lf.la.iterator();
                    while (it.hasNext()) {
                        ((C0177e) it.next()).onServiceDisconnected(component);
                    }
                    this.lf.mState = 2;
                }
            }
        }

        public C0179a(df dfVar, String str) {
            this.le = dfVar;
            this.kY = str;
        }

        public void m351a(C0177e c0177e) {
            this.la.add(c0177e);
        }

        public void m352b(C0177e c0177e) {
            this.la.remove(c0177e);
        }

        public C0178a bg() {
            return this.kZ;
        }

        public String bh() {
            return this.kY;
        }

        public boolean bi() {
            return this.la.isEmpty();
        }

        public boolean m353c(C0177e c0177e) {
            return this.la.contains(c0177e);
        }

        public IBinder getBinder() {
            return this.lc;
        }

        public ComponentName getComponentName() {
            return this.ld;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.lb;
        }

        public void m354l(boolean z) {
            this.lb = z;
        }
    }

    private df(Context context) {
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.kW = context.getApplicationContext();
    }

    public static df m356s(Context context) {
        synchronized (kU) {
            if (kV == null) {
                kV = new df(context.getApplicationContext());
            }
        }
        return kV;
    }

    public boolean m357a(String str, C0177e c0177e) {
        boolean isBound;
        synchronized (this.kX) {
            C0179a c0179a = (C0179a) this.kX.get(str);
            if (c0179a != null) {
                this.mHandler.removeMessages(0, c0179a);
                if (!c0179a.m353c(c0177e)) {
                    c0179a.m351a((C0177e) c0177e);
                    switch (c0179a.getState()) {
                        case 1:
                            c0177e.onServiceConnected(c0179a.getComponentName(), c0179a.getBinder());
                            break;
                        case 2:
                            c0179a.m354l(this.kW.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), c0179a.bg(), 129));
                            break;
                        default:
                            break;
                    }
                }
                throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  startServiceAction=" + str);
            }
            c0179a = new C0179a(this, str);
            c0179a.m351a((C0177e) c0177e);
            c0179a.m354l(this.kW.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), c0179a.bg(), 129));
            this.kX.put(str, c0179a);
            isBound = c0179a.isBound();
        }
        return isBound;
    }

    public void m358b(String str, C0177e c0177e) {
        synchronized (this.kX) {
            C0179a c0179a = (C0179a) this.kX.get(str);
            if (c0179a == null) {
                throw new IllegalStateException("Nonexistent connection status for service action: " + str);
            } else if (c0179a.m353c(c0177e)) {
                c0179a.m352b(c0177e);
                if (c0179a.bi()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, c0179a), 5000);
                }
            } else {
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  startServiceAction=" + str);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                C0179a c0179a = (C0179a) msg.obj;
                synchronized (this.kX) {
                    if (c0179a.bi()) {
                        this.kW.unbindService(c0179a.bg());
                        this.kX.remove(c0179a.bh());
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
