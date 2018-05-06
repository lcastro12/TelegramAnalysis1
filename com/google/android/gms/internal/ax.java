package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.C0112b;
import com.google.android.gms.dynamic.C0112b.C0655a;
import com.google.android.gms.internal.ay.C0685a;

public interface ax extends IInterface {

    public static abstract class C0683a extends Binder implements ax {

        private static class C0682a implements ax {
            private IBinder dG;

            C0682a(IBinder iBinder) {
                this.dG = iBinder;
            }

            public void mo775a(C0112b c0112b, C0770v c0770v, String str, ay ayVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    if (c0770v != null) {
                        obtain.writeInt(1);
                        c0770v.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    if (ayVar != null) {
                        iBinder = ayVar.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.dG.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo776a(C0112b c0112b, C0771x c0771x, C0770v c0770v, String str, ay ayVar) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    if (c0771x != null) {
                        obtain.writeInt(1);
                        c0771x.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (c0770v != null) {
                        obtain.writeInt(1);
                        c0770v.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    if (ayVar != null) {
                        iBinder = ayVar.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.dG;
            }

            public void destroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    this.dG.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public C0112b getView() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    this.dG.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    C0112b z = C0655a.m828z(obtain2.readStrongBinder());
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showInterstitial() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    this.dG.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public C0683a() {
            attachInterface(this, "com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
        }

        public static ax m876j(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ax)) ? new C0682a(iBinder) : (ax) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            C0770v c0770v = null;
            C0112b view;
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    mo776a(C0655a.m828z(data.readStrongBinder()), data.readInt() != 0 ? C0771x.CREATOR.m701b(data) : null, data.readInt() != 0 ? C0770v.CREATOR.m698a(data) : null, data.readString(), C0685a.m878k(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    IBinder asBinder;
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    view = getView();
                    reply.writeNoException();
                    if (view != null) {
                        asBinder = view.asBinder();
                    }
                    reply.writeStrongBinder(asBinder);
                    return true;
                case 3:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    view = C0655a.m828z(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        c0770v = C0770v.CREATOR.m698a(data);
                    }
                    mo775a(view, c0770v, data.readString(), C0685a.m878k(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    showInterstitial();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    destroy();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void mo775a(C0112b c0112b, C0770v c0770v, String str, ay ayVar) throws RemoteException;

    void mo776a(C0112b c0112b, C0771x c0771x, C0770v c0770v, String str, ay ayVar) throws RemoteException;

    void destroy() throws RemoteException;

    C0112b getView() throws RemoteException;

    void showInterstitial() throws RemoteException;
}
