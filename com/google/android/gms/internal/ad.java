package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.C0112b;
import com.google.android.gms.dynamic.C0112b.C0655a;
import com.google.android.gms.internal.aw.C0681a;

public interface ad extends IInterface {

    public static abstract class C0670a extends Binder implements ad {

        private static class C0669a implements ad {
            private IBinder dG;

            C0669a(IBinder iBinder) {
                this.dG = iBinder;
            }

            public IBinder mo768a(C0112b c0112b, C0771x c0771x, String str, aw awVar, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    if (c0771x != null) {
                        obtain.writeInt(1);
                        c0771x.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    if (awVar != null) {
                        iBinder = awVar.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    iBinder = obtain2.readStrongBinder();
                    return iBinder;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.dG;
            }
        }

        public static ad m843g(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ad)) ? new C0669a(iBinder) : (ad) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    IBinder a = mo768a(C0655a.m828z(data.readStrongBinder()), data.readInt() != 0 ? C0771x.CREATOR.m701b(data) : null, data.readString(), C0681a.m873i(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(a);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IBinder mo768a(C0112b c0112b, C0771x c0771x, String str, aw awVar, int i) throws RemoteException;
}
