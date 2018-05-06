package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.dm;

public abstract class C0115e<T> {
    private final String mi;
    private T mj;

    public static class C0114a extends Exception {
        public C0114a(String str) {
            super(str);
        }

        public C0114a(String str, Throwable th) {
            super(str, th);
        }
    }

    protected C0115e(String str) {
        this.mi = str;
    }

    protected abstract T mo799d(IBinder iBinder);

    protected final T m145t(Context context) throws C0114a {
        if (this.mj == null) {
            dm.m392e(context);
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext == null) {
                throw new C0114a("Could not get remote context.");
            }
            try {
                this.mj = mo799d((IBinder) remoteContext.getClassLoader().loadClass(this.mi).newInstance());
            } catch (ClassNotFoundException e) {
                throw new C0114a("Could not load creator class.");
            } catch (InstantiationException e2) {
                throw new C0114a("Could not instantiate creator.");
            } catch (IllegalAccessException e3) {
                throw new C0114a("Could not access creator.");
            }
        }
        return this.mj;
    }
}
