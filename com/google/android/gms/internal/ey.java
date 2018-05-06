package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.location.C0208a;
import com.google.android.gms.location.C0208a.C0773a;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.HashMap;

public class ey {
    private final Context mContext;
    private final fc<ex> oO;
    private ContentProviderClient oP = null;
    private boolean oQ = false;
    private HashMap<LocationListener, C0918b> oR = new HashMap();

    private static class C0186a extends Handler {
        private final LocationListener oS;

        public C0186a(LocationListener locationListener) {
            this.oS = locationListener;
        }

        public C0186a(LocationListener locationListener, Looper looper) {
            super(looper);
            this.oS = locationListener;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.oS.onLocationChanged(new Location((Location) msg.obj));
                    return;
                default:
                    Log.e("LocationClientHelper", "unknown message in LocationHandler.handleMessage");
                    return;
            }
        }
    }

    private static class C0918b extends C0773a {
        private Handler oT;

        C0918b(LocationListener locationListener, Looper looper) {
            this.oT = looper == null ? new C0186a(locationListener) : new C0186a(locationListener, looper);
        }

        public void onLocationChanged(Location location) {
            if (this.oT == null) {
                Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
                return;
            }
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = location;
            this.oT.sendMessage(obtain);
        }

        public void release() {
            this.oT = null;
        }
    }

    public ey(Context context, fc<ex> fcVar) {
        this.mContext = context;
        this.oO = fcVar;
    }

    public void cm() {
        if (this.oQ) {
            setMockMode(false);
        }
    }

    public Location getLastLocation() {
        this.oO.bc();
        try {
            return ((ex) this.oO.bd()).cl();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeAllListeners() {
        try {
            synchronized (this.oR) {
                for (C0208a c0208a : this.oR.values()) {
                    if (c0208a != null) {
                        ((ex) this.oO.bd()).mo1004a(c0208a);
                    }
                }
                this.oR.clear();
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(PendingIntent callbackIntent) {
        this.oO.bc();
        try {
            ((ex) this.oO.bd()).mo998a(callbackIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(LocationListener listener) {
        this.oO.bc();
        dm.m388a((Object) listener, (Object) "Invalid null listener");
        synchronized (this.oR) {
            C0208a c0208a = (C0918b) this.oR.remove(listener);
            if (this.oP != null && this.oR.isEmpty()) {
                this.oP.release();
                this.oP = null;
            }
            if (c0208a != null) {
                c0208a.release();
                try {
                    ((ex) this.oO.bd()).mo1004a(c0208a);
                } catch (Throwable e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent callbackIntent) {
        this.oO.bc();
        try {
            ((ex) this.oO.bd()).mo1001a(request, callbackIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        this.oO.bc();
        if (looper == null) {
            dm.m388a(Looper.myLooper(), (Object) "Can't create handler inside thread that has not called Looper.prepare()");
        }
        synchronized (this.oR) {
            C0208a c0918b;
            C0918b c0918b2 = (C0918b) this.oR.get(listener);
            if (c0918b2 == null) {
                c0918b = new C0918b(listener, looper);
            } else {
                Object obj = c0918b2;
            }
            this.oR.put(listener, c0918b);
            try {
                ((ex) this.oO.bd()).mo1003a(request, c0918b, this.mContext.getPackageName());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void setMockLocation(Location mockLocation) {
        this.oO.bc();
        try {
            ((ex) this.oO.bd()).setMockLocation(mockLocation);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void setMockMode(boolean isMockMode) {
        this.oO.bc();
        try {
            ((ex) this.oO.bd()).setMockMode(isMockMode);
            this.oQ = isMockMode;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }
}
