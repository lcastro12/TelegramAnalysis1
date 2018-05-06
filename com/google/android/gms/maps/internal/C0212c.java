package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.C0112b;
import com.google.android.gms.dynamic.C0112b.C0655a;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate.C0782a;
import com.google.android.gms.maps.internal.IMapFragmentDelegate.C0788a;
import com.google.android.gms.maps.internal.IMapViewDelegate.C0790a;
import com.google.android.gms.maps.model.internal.C0237a;
import com.google.android.gms.maps.model.internal.C0237a.C0827a;

public interface C0212c extends IInterface {

    public static abstract class C0798a extends Binder implements C0212c {

        private static class C0797a implements C0212c {
            private IBinder dG;

            C0797a(IBinder iBinder) {
                this.dG = iBinder;
            }

            public IMapViewDelegate mo1176a(C0112b c0112b, GoogleMapOptions googleMapOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    if (googleMapOptions != null) {
                        obtain.writeInt(1);
                        googleMapOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.dG.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapViewDelegate O = C0790a.m1258O(obtain2.readStrongBinder());
                    return O;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo1177a(C0112b c0112b, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    obtain.writeInt(i);
                    this.dG.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.dG;
            }

            public ICameraUpdateFactoryDelegate cG() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.dG.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    ICameraUpdateFactoryDelegate H = C0782a.m1254H(obtain2.readStrongBinder());
                    return H;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public C0237a cH() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.dG.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    C0237a ac = C0827a.ac(obtain2.readStrongBinder());
                    return ac;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo1180e(C0112b c0112b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IMapFragmentDelegate mo1181f(C0112b c0112b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0112b != null ? c0112b.asBinder() : null);
                    this.dG.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapFragmentDelegate N = C0788a.m1257N(obtain2.readStrongBinder());
                    return N;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static C0212c m1265J(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof C0212c)) ? new C0797a(iBinder) : (C0212c) queryLocalInterface;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    mo1180e(C0655a.m828z(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapFragmentDelegate f = mo1181f(C0655a.m828z(data.readStrongBinder()));
                    reply.writeNoException();
                    if (f != null) {
                        iBinder = f.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 3:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapViewDelegate a = mo1176a(C0655a.m828z(data.readStrongBinder()), data.readInt() != 0 ? GoogleMapOptions.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    if (a != null) {
                        iBinder = a.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 4:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    ICameraUpdateFactoryDelegate cG = cG();
                    reply.writeNoException();
                    if (cG != null) {
                        iBinder = cG.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 5:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    C0237a cH = cH();
                    reply.writeNoException();
                    if (cH != null) {
                        iBinder = cH.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 6:
                    data.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    mo1177a(C0655a.m828z(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.ICreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IMapViewDelegate mo1176a(C0112b c0112b, GoogleMapOptions googleMapOptions) throws RemoteException;

    void mo1177a(C0112b c0112b, int i) throws RemoteException;

    ICameraUpdateFactoryDelegate cG() throws RemoteException;

    C0237a cH() throws RemoteException;

    void mo1180e(C0112b c0112b) throws RemoteException;

    IMapFragmentDelegate mo1181f(C0112b c0112b) throws RemoteException;
}
