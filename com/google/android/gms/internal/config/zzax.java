package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzax extends zzbb<zzax> {
    private static volatile zzax[] zzbv;
    public String namespace;
    public int resourceId;
    public long zzbw;

    public zzax() {
        this.resourceId = 0;
        this.zzbw = 0;
        this.namespace = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzci = null;
        this.zzcr = -1;
    }

    public static zzax[] zzx() {
        if (zzbv == null) {
            synchronized (zzbf.zzcq) {
                if (zzbv == null) {
                    zzbv = new zzax[0];
                }
            }
        }
        return zzbv;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzax)) {
            return false;
        }
        zzax com_google_android_gms_internal_config_zzax = (zzax) obj;
        if (this.resourceId != com_google_android_gms_internal_config_zzax.resourceId) {
            return false;
        }
        if (this.zzbw != com_google_android_gms_internal_config_zzax.zzbw) {
            return false;
        }
        if (this.namespace == null) {
            if (com_google_android_gms_internal_config_zzax.namespace != null) {
                return false;
            }
        } else if (!this.namespace.equals(com_google_android_gms_internal_config_zzax.namespace)) {
            return false;
        }
        return (this.zzci == null || this.zzci.isEmpty()) ? com_google_android_gms_internal_config_zzax.zzci == null || com_google_android_gms_internal_config_zzax.zzci.isEmpty() : this.zzci.equals(com_google_android_gms_internal_config_zzax.zzci);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.namespace == null ? 0 : this.namespace.hashCode()) + ((((((getClass().getName().hashCode() + 527) * 31) + this.resourceId) * 31) + ((int) (this.zzbw ^ (this.zzbw >>> 32)))) * 31)) * 31;
        if (!(this.zzci == null || this.zzci.isEmpty())) {
            i = this.zzci.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzy = com_google_android_gms_internal_config_zzay.zzy();
            switch (zzy) {
                case 0:
                    break;
                case 8:
                    this.resourceId = com_google_android_gms_internal_config_zzay.zzz();
                    continue;
                case 17:
                    this.zzbw = com_google_android_gms_internal_config_zzay.zzaa();
                    continue;
                case 26:
                    this.namespace = com_google_android_gms_internal_config_zzay.readString();
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }

    public final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.resourceId != 0) {
            com_google_android_gms_internal_config_zzaz.zzc(1, this.resourceId);
        }
        if (this.zzbw != 0) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.zzbw);
        }
        if (!(this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_config_zzaz.zza(3, this.namespace);
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzu() {
        int zzu = super.zzu();
        if (this.resourceId != 0) {
            zzu += zzaz.zzd(1, this.resourceId);
        }
        if (this.zzbw != 0) {
            zzu += zzaz.zzl(2) + 8;
        }
        return (this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID)) ? zzu : zzu + zzaz.zzb(3, this.namespace);
    }
}
