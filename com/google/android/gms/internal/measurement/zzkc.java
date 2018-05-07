package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkc extends zzabd<zzkc> {
    private static volatile zzkc[] zzarz;
    public Integer zzark;
    public String zzasa;
    public zzka zzasb;

    public zzkc() {
        this.zzark = null;
        this.zzasa = null;
        this.zzasb = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzkc[] zzkz() {
        if (zzarz == null) {
            synchronized (zzabh.zzbzr) {
                if (zzarz == null) {
                    zzarz = new zzkc[0];
                }
            }
        }
        return zzarz;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkc)) {
            return false;
        }
        zzkc com_google_android_gms_internal_measurement_zzkc = (zzkc) obj;
        if (this.zzark == null) {
            if (com_google_android_gms_internal_measurement_zzkc.zzark != null) {
                return false;
            }
        } else if (!this.zzark.equals(com_google_android_gms_internal_measurement_zzkc.zzark)) {
            return false;
        }
        if (this.zzasa == null) {
            if (com_google_android_gms_internal_measurement_zzkc.zzasa != null) {
                return false;
            }
        } else if (!this.zzasa.equals(com_google_android_gms_internal_measurement_zzkc.zzasa)) {
            return false;
        }
        if (this.zzasb == null) {
            if (com_google_android_gms_internal_measurement_zzkc.zzasb != null) {
                return false;
            }
        } else if (!this.zzasb.equals(com_google_android_gms_internal_measurement_zzkc.zzasb)) {
            return false;
        }
        return (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkc.zzbzh == null || com_google_android_gms_internal_measurement_zzkc.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkc.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (this.zzasa == null ? 0 : this.zzasa.hashCode()) + (((this.zzark == null ? 0 : this.zzark.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31);
        zzka com_google_android_gms_internal_measurement_zzka = this.zzasb;
        hashCode = ((com_google_android_gms_internal_measurement_zzka == null ? 0 : com_google_android_gms_internal_measurement_zzka.hashCode()) + (hashCode * 31)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzark != null) {
            zza += zzabb.zzf(1, this.zzark.intValue());
        }
        if (this.zzasa != null) {
            zza += zzabb.zzd(2, this.zzasa);
        }
        return this.zzasb != null ? zza + zzabb.zzb(3, this.zzasb) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzark != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzark.intValue());
        }
        if (this.zzasa != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.zzasa);
        }
        if (this.zzasb != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(3, this.zzasb);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    break;
                case 8:
                    this.zzark = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    continue;
                case 18:
                    this.zzasa = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case 26:
                    if (this.zzasb == null) {
                        this.zzasb = new zzka();
                    }
                    com_google_android_gms_internal_measurement_zzaba.zza(this.zzasb);
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
