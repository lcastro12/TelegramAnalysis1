package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class C0920f extends C0725e {
    private static Method dk;
    private static Method dl;
    private static Method dm;
    private static Method dn;
    private static Method f83do;
    private static Method dp;
    private static String dq;
    private static C0198l dr;
    static boolean ds = false;
    private static long startTime = 0;

    static class C0188a extends Exception {
        public C0188a(Throwable th) {
            super(th);
        }
    }

    protected C0920f(Context context, C0195j c0195j, C0196k c0196k) {
        super(context, c0195j, c0196k);
    }

    static String m1445a(Context context, C0195j c0195j) throws C0188a {
        if (dm == null) {
            throw new C0188a();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) dm.invoke(null, new Object[]{context});
            if (byteBuffer != null) {
                return c0195j.mo751a(byteBuffer.array(), true);
            }
            throw new C0188a();
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    static ArrayList<Long> m1446a(MotionEvent motionEvent, DisplayMetrics displayMetrics) throws C0188a {
        if (dn == null || motionEvent == null) {
            throw new C0188a();
        }
        try {
            return (ArrayList) dn.invoke(null, new Object[]{motionEvent, displayMetrics});
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    protected static synchronized void m1447a(String str, Context context, C0195j c0195j) {
        synchronized (C0920f.class) {
            if (!ds) {
                try {
                    dr = new C0198l(c0195j, null);
                    dq = str;
                    C0920f.m1453e(context);
                    startTime = C0920f.m1452e().longValue();
                    ds = true;
                } catch (C0188a e) {
                } catch (UnsupportedOperationException e2) {
                }
            }
        }
    }

    static String m1448b(Context context, C0195j c0195j) throws C0188a {
        if (dp == null) {
            throw new C0188a();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) dp.invoke(null, new Object[]{context});
            if (byteBuffer != null) {
                return c0195j.mo751a(byteBuffer.array(), true);
            }
            throw new C0188a();
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    private static String m1449b(byte[] bArr, String str) throws C0188a {
        try {
            return new String(dr.m674c(bArr, str), "UTF-8");
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    static String m1450d() throws C0188a {
        if (dq != null) {
            return dq;
        }
        throw new C0188a();
    }

    static String m1451d(Context context) throws C0188a {
        if (f83do == null) {
            throw new C0188a();
        }
        try {
            String str = (String) f83do.invoke(null, new Object[]{context});
            if (str != null) {
                return str;
            }
            throw new C0188a();
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    static Long m1452e() throws C0188a {
        if (dk == null) {
            throw new C0188a();
        }
        try {
            return (Long) dk.invoke(null, new Object[0]);
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    private static void m1453e(Context context) throws C0188a {
        try {
            byte[] b = dr.m673b(C0199n.getKey());
            byte[] c = dr.m674c(b, C0199n.m675i());
            File cacheDir = context.getCacheDir();
            if (cacheDir == null) {
                cacheDir = context.getDir("dex", 0);
                if (cacheDir == null) {
                    throw new C0188a();
                }
            }
            File createTempFile = File.createTempFile("ads", ".jar", cacheDir);
            FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
            fileOutputStream.write(c, 0, c.length);
            fileOutputStream.close();
            DexClassLoader dexClassLoader = new DexClassLoader(createTempFile.getAbsolutePath(), cacheDir.getAbsolutePath(), null, context.getClassLoader());
            Class loadClass = dexClassLoader.loadClass(C0920f.m1449b(b, C0199n.m676j()));
            Class loadClass2 = dexClassLoader.loadClass(C0920f.m1449b(b, C0199n.m682p()));
            Class loadClass3 = dexClassLoader.loadClass(C0920f.m1449b(b, C0199n.m680n()));
            Class loadClass4 = dexClassLoader.loadClass(C0920f.m1449b(b, C0199n.m686t()));
            Class loadClass5 = dexClassLoader.loadClass(C0920f.m1449b(b, C0199n.m678l()));
            Class loadClass6 = dexClassLoader.loadClass(C0920f.m1449b(b, C0199n.m684r()));
            dk = loadClass.getMethod(C0920f.m1449b(b, C0199n.m677k()), new Class[0]);
            dl = loadClass2.getMethod(C0920f.m1449b(b, C0199n.m683q()), new Class[0]);
            dm = loadClass3.getMethod(C0920f.m1449b(b, C0199n.m681o()), new Class[]{Context.class});
            dn = loadClass4.getMethod(C0920f.m1449b(b, C0199n.m687u()), new Class[]{MotionEvent.class, DisplayMetrics.class});
            f83do = loadClass5.getMethod(C0920f.m1449b(b, C0199n.m679m()), new Class[]{Context.class});
            dp = loadClass6.getMethod(C0920f.m1449b(b, C0199n.m685s()), new Class[]{Context.class});
            String name = createTempFile.getName();
            createTempFile.delete();
            new File(cacheDir, name.replace(".jar", ".dex")).delete();
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        } catch (Throwable e22) {
            throw new C0188a(e22);
        } catch (Throwable e222) {
            throw new C0188a(e222);
        } catch (Throwable e2222) {
            throw new C0188a(e2222);
        } catch (Throwable e22222) {
            throw new C0188a(e22222);
        }
    }

    static String m1454f() throws C0188a {
        if (dl == null) {
            throw new C0188a();
        }
        try {
            return (String) dl.invoke(null, new Object[0]);
        } catch (Throwable e) {
            throw new C0188a(e);
        } catch (Throwable e2) {
            throw new C0188a(e2);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void mo1543b(android.content.Context r4) {
        /*
        r3 = this;
        r0 = 1;
        r1 = com.google.android.gms.internal.C0920f.m1454f();	 Catch:{ a -> 0x002f, IOException -> 0x0027 }
        r3.m1015a(r0, r1);	 Catch:{ a -> 0x002f, IOException -> 0x0027 }
    L_0x0008:
        r0 = 2;
        r1 = com.google.android.gms.internal.C0920f.m1450d();	 Catch:{ a -> 0x002d, IOException -> 0x0027 }
        r3.m1015a(r0, r1);	 Catch:{ a -> 0x002d, IOException -> 0x0027 }
    L_0x0010:
        r0 = 25;
        r1 = com.google.android.gms.internal.C0920f.m1452e();	 Catch:{ a -> 0x002b, IOException -> 0x0027 }
        r1 = r1.longValue();	 Catch:{ a -> 0x002b, IOException -> 0x0027 }
        r3.m1014a(r0, r1);	 Catch:{ a -> 0x002b, IOException -> 0x0027 }
    L_0x001d:
        r0 = 24;
        r1 = com.google.android.gms.internal.C0920f.m1451d(r4);	 Catch:{ a -> 0x0029, IOException -> 0x0027 }
        r3.m1015a(r0, r1);	 Catch:{ a -> 0x0029, IOException -> 0x0027 }
    L_0x0026:
        return;
    L_0x0027:
        r0 = move-exception;
        goto L_0x0026;
    L_0x0029:
        r0 = move-exception;
        goto L_0x0026;
    L_0x002b:
        r0 = move-exception;
        goto L_0x001d;
    L_0x002d:
        r0 = move-exception;
        goto L_0x0010;
    L_0x002f:
        r0 = move-exception;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.f.b(android.content.Context):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void mo1544c(android.content.Context r7) {
        /*
        r6 = this;
        r0 = 2;
        r1 = com.google.android.gms.internal.C0920f.m1450d();	 Catch:{ a -> 0x0097, IOException -> 0x008a }
        r6.m1015a(r0, r1);	 Catch:{ a -> 0x0097, IOException -> 0x008a }
    L_0x0008:
        r0 = 1;
        r1 = com.google.android.gms.internal.C0920f.m1454f();	 Catch:{ a -> 0x0094, IOException -> 0x008a }
        r6.m1015a(r0, r1);	 Catch:{ a -> 0x0094, IOException -> 0x008a }
    L_0x0010:
        r0 = com.google.android.gms.internal.C0920f.m1452e();	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r0 = r0.longValue();	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r2 = 25;
        r6.m1014a(r2, r0);	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r2 = startTime;	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r4 = 0;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 == 0) goto L_0x0034;
    L_0x0025:
        r2 = 17;
        r3 = startTime;	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r0 = r0 - r3;
        r6.m1014a(r2, r0);	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r0 = 23;
        r1 = startTime;	 Catch:{ a -> 0x0092, IOException -> 0x008a }
        r6.m1014a(r0, r1);	 Catch:{ a -> 0x0092, IOException -> 0x008a }
    L_0x0034:
        r0 = r6.dg;	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r1 = r6.dh;	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r1 = com.google.android.gms.internal.C0920f.m1446a(r0, r1);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r2 = 14;
        r0 = 0;
        r0 = r1.get(r0);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r0 = (java.lang.Long) r0;	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r3 = r0.longValue();	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r6.m1014a(r2, r3);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r2 = 15;
        r0 = 1;
        r0 = r1.get(r0);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r0 = (java.lang.Long) r0;	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r3 = r0.longValue();	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r6.m1014a(r2, r3);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r0 = r1.size();	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r2 = 3;
        if (r0 < r2) goto L_0x0073;
    L_0x0063:
        r2 = 16;
        r0 = 2;
        r0 = r1.get(r0);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r0 = (java.lang.Long) r0;	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r0 = r0.longValue();	 Catch:{ a -> 0x0090, IOException -> 0x008a }
        r6.m1014a(r2, r0);	 Catch:{ a -> 0x0090, IOException -> 0x008a }
    L_0x0073:
        r0 = 27;
        r1 = r6.di;	 Catch:{ a -> 0x008e, IOException -> 0x008a }
        r1 = com.google.android.gms.internal.C0920f.m1445a(r7, r1);	 Catch:{ a -> 0x008e, IOException -> 0x008a }
        r6.m1015a(r0, r1);	 Catch:{ a -> 0x008e, IOException -> 0x008a }
    L_0x007e:
        r0 = 29;
        r1 = r6.di;	 Catch:{ a -> 0x008c, IOException -> 0x008a }
        r1 = com.google.android.gms.internal.C0920f.m1448b(r7, r1);	 Catch:{ a -> 0x008c, IOException -> 0x008a }
        r6.m1015a(r0, r1);	 Catch:{ a -> 0x008c, IOException -> 0x008a }
    L_0x0089:
        return;
    L_0x008a:
        r0 = move-exception;
        goto L_0x0089;
    L_0x008c:
        r0 = move-exception;
        goto L_0x0089;
    L_0x008e:
        r0 = move-exception;
        goto L_0x007e;
    L_0x0090:
        r0 = move-exception;
        goto L_0x0073;
    L_0x0092:
        r0 = move-exception;
        goto L_0x0034;
    L_0x0094:
        r0 = move-exception;
        goto L_0x0010;
    L_0x0097:
        r0 = move-exception;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.f.c(android.content.Context):void");
    }
}
