package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.dynamic.C0115e;
import com.google.android.gms.dynamic.C0115e.C0114a;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.internal.dk.C0720a;

public final class dn extends C0115e<dk> {
    private static final dn ll = new dn();

    private dn() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View m982d(Context context, int i, int i2) throws C0114a {
        return ll.m983e(context, i, i2);
    }

    private View m983e(Context context, int i, int i2) throws C0114a {
        try {
            return (View) C0898c.m1317b(((dk) m145t(context)).mo848a(C0898c.m1318g(context), i, i2));
        } catch (Throwable e) {
            throw new C0114a("Could not get button with size " + i + " and color " + i2, e);
        }
    }

    public /* synthetic */ Object mo799d(IBinder iBinder) {
        return m985y(iBinder);
    }

    public dk m985y(IBinder iBinder) {
        return C0720a.m981x(iBinder);
    }
}
