package com.google.android.gms.internal;

import android.util.Base64;

class C0664a implements C0195j {
    C0664a() {
    }

    public String mo751a(byte[] bArr, boolean z) {
        return Base64.encodeToString(bArr, z ? 11 : 2);
    }

    public byte[] mo752a(String str, boolean z) throws IllegalArgumentException {
        return Base64.decode(str, z ? 11 : 2);
    }
}
