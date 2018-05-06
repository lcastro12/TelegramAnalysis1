package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class C0725e implements C0174d {
    protected MotionEvent dg;
    protected DisplayMetrics dh;
    protected C0195j di;
    private C0196k dj;

    protected C0725e(Context context, C0195j c0195j, C0196k c0196k) {
        this.di = c0195j;
        this.dj = c0196k;
        try {
            this.dh = context.getResources().getDisplayMetrics();
        } catch (UnsupportedOperationException e) {
            this.dh = new DisplayMetrics();
            this.dh.density = 1.0f;
        }
    }

    private String m1007a(Context context, String str, boolean z) {
        try {
            byte[] c;
            synchronized (this) {
                m1008b();
                if (z) {
                    mo1544c(context);
                } else {
                    mo1543b(context);
                }
                c = m1009c();
            }
            return c.length == 0 ? Integer.toString(5) : m1012a(c, str);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toString(7);
        } catch (UnsupportedEncodingException e2) {
            return Integer.toString(7);
        } catch (IOException e3) {
            return Integer.toString(3);
        }
    }

    private void m1008b() {
        this.dj.reset();
    }

    private byte[] m1009c() throws IOException {
        return this.dj.mo1057h();
    }

    public String mo852a(Context context) {
        return m1007a(context, null, false);
    }

    public String mo853a(Context context, String str) {
        return m1007a(context, str, true);
    }

    String m1012a(byte[] bArr, String str) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        byte[] bArr2;
        if (bArr.length > 239) {
            m1008b();
            m1014a(20, 1);
            bArr = m1009c();
        }
        if (bArr.length < 239) {
            bArr2 = new byte[(239 - bArr.length)];
            new SecureRandom().nextBytes(bArr2);
            bArr2 = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).put(bArr2).array();
        } else {
            bArr2 = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).array();
        }
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(bArr2);
        bArr2 = ByteBuffer.allocate(256).put(instance.digest()).put(bArr2).array();
        byte[] bArr3 = new byte[256];
        new C0131b().m192a(bArr2, bArr3);
        if (str != null && str.length() > 0) {
            m1017a(str, bArr3);
        }
        return this.di.mo751a(bArr3, true);
    }

    public void mo854a(int i, int i2, int i3) {
        if (this.dg != null) {
            this.dg.recycle();
        }
        this.dg = MotionEvent.obtain(0, (long) i3, 1, ((float) i) * this.dh.density, ((float) i2) * this.dh.density, 0.0f, 0.0f, 0, 0.0f, 0.0f, 0, 0);
    }

    protected void m1014a(int i, long j) throws IOException {
        this.dj.mo1055b(i, j);
    }

    protected void m1015a(int i, String str) throws IOException {
        this.dj.mo1056b(i, str);
    }

    public void mo855a(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (this.dg != null) {
                this.dg.recycle();
            }
            this.dg = MotionEvent.obtain(motionEvent);
        }
    }

    void m1017a(String str, byte[] bArr) throws UnsupportedEncodingException {
        if (str.length() > 32) {
            str = str.substring(0, 32);
        }
        new gk(str.getBytes("UTF-8")).m649f(bArr);
    }

    protected abstract void mo1543b(Context context);

    protected abstract void mo1544c(Context context);
}
