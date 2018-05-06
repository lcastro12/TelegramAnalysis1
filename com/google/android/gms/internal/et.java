package com.google.android.gms.internal;

import android.net.LocalSocket;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class et implements RealTimeSocket {
    private ParcelFileDescriptor jN;
    private final String nd;
    private final LocalSocket nt;

    et(LocalSocket localSocket, String str) {
        this.nt = localSocket;
        this.nd = str;
    }

    public void close() throws IOException {
        this.nt.close();
    }

    public InputStream getInputStream() throws IOException {
        return this.nt.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.nt.getOutputStream();
    }

    public ParcelFileDescriptor getParcelFileDescriptor() throws IOException {
        if (this.jN == null && !isClosed()) {
            Parcel obtain = Parcel.obtain();
            obtain.writeFileDescriptor(this.nt.getFileDescriptor());
            obtain.setDataPosition(0);
            this.jN = obtain.readFileDescriptor();
        }
        return this.jN;
    }

    public boolean isClosed() {
        return (this.nt.isConnected() || this.nt.isBound()) ? false : true;
    }
}
