package com.google.android.gms.plus.model.people;

import com.google.android.gms.common.data.C0644c;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.internal.fv;
import com.google.android.gms.internal.gg;

public final class PersonBuffer extends DataBuffer<Person> {
    private final C0644c<fv> tt;

    public PersonBuffer(C0646d dataHolder) {
        super(dataHolder);
        if (dataHolder.aM() == null || !dataHolder.aM().getBoolean("com.google.android.gms.plus.IsSafeParcelable", false)) {
            this.tt = null;
        } else {
            this.tt = new C0644c(dataHolder, fv.CREATOR);
        }
    }

    public Person get(int position) {
        return this.tt != null ? (Person) this.tt.m802p(position) : new gg(this.jf, position);
    }
}
