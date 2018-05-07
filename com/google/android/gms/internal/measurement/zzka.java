package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

public final class zzka extends zzabd<zzka> {
    private static volatile zzka[] zzarp;
    public zzkd zzarq;
    public zzkb zzarr;
    public Boolean zzars;
    public String zzart;

    public zzka() {
        this.zzarq = null;
        this.zzarr = null;
        this.zzars = null;
        this.zzart = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzka[] zzky() {
        if (zzarp == null) {
            synchronized (zzabh.zzbzr) {
                if (zzarp == null) {
                    zzarp = new zzka[0];
                }
            }
        }
        return zzarp;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzka)) {
            return false;
        }
        zzka com_google_android_gms_internal_measurement_zzka = (zzka) obj;
        if (this.zzarq == null) {
            if (com_google_android_gms_internal_measurement_zzka.zzarq != null) {
                return false;
            }
        } else if (!this.zzarq.equals(com_google_android_gms_internal_measurement_zzka.zzarq)) {
            return false;
        }
        if (this.zzarr == null) {
            if (com_google_android_gms_internal_measurement_zzka.zzarr != null) {
                return false;
            }
        } else if (!this.zzarr.equals(com_google_android_gms_internal_measurement_zzka.zzarr)) {
            return false;
        }
        if (this.zzars == null) {
            if (com_google_android_gms_internal_measurement_zzka.zzars != null) {
                return false;
            }
        } else if (!this.zzars.equals(com_google_android_gms_internal_measurement_zzka.zzars)) {
            return false;
        }
        if (this.zzart == null) {
            if (com_google_android_gms_internal_measurement_zzka.zzart != null) {
                return false;
            }
        } else if (!this.zzart.equals(com_google_android_gms_internal_measurement_zzka.zzart)) {
            return false;
        }
        return (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzka.zzbzh == null || com_google_android_gms_internal_measurement_zzka.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzka.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = getClass().getName().hashCode() + 527;
        zzkd com_google_android_gms_internal_measurement_zzkd = this.zzarq;
        hashCode = (com_google_android_gms_internal_measurement_zzkd == null ? 0 : com_google_android_gms_internal_measurement_zzkd.hashCode()) + (hashCode * 31);
        zzkb com_google_android_gms_internal_measurement_zzkb = this.zzarr;
        hashCode = ((this.zzart == null ? 0 : this.zzart.hashCode()) + (((this.zzars == null ? 0 : this.zzars.hashCode()) + (((com_google_android_gms_internal_measurement_zzkb == null ? 0 : com_google_android_gms_internal_measurement_zzkb.hashCode()) + (hashCode * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzarq != null) {
            zza += zzabb.zzb(1, this.zzarq);
        }
        if (this.zzarr != null) {
            zza += zzabb.zzb(2, this.zzarr);
        }
        if (this.zzars != null) {
            this.zzars.booleanValue();
            zza += zzabb.zzas(3) + 1;
        }
        return this.zzart != null ? zza + zzabb.zzd(4, this.zzart) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzarq != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(1, this.zzarq);
        }
        if (this.zzarr != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(2, this.zzarr);
        }
        if (this.zzars != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(3, this.zzars.booleanValue());
        }
        if (this.zzart != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(4, this.zzart);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    break;
                case 10:
                    if (this.zzarq == null) {
                        this.zzarq = new zzkd();
                    }
                    com_google_android_gms_internal_measurement_zzaba.zza(this.zzarq);
                    continue;
                case 18:
                    if (this.zzarr == null) {
                        this.zzarr = new zzkb();
                    }
                    com_google_android_gms_internal_measurement_zzaba.zza(this.zzarr);
                    continue;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzars = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
                    continue;
                case 34:
                    this.zzart = com_google_android_gms_internal_measurement_zzaba.readString();
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
