package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

final class zzfo implements Runnable {
    private final String packageName;
    private final URL url;
    private final byte[] zzajl;
    private final zzfm zzajm;
    private final Map<String, String> zzajn;
    private final /* synthetic */ zzfk zzajo;

    public zzfo(zzfk com_google_android_gms_internal_measurement_zzfk, String str, URL url, byte[] bArr, Map<String, String> map, zzfm com_google_android_gms_internal_measurement_zzfm) {
        this.zzajo = com_google_android_gms_internal_measurement_zzfk;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzfm);
        this.url = url;
        this.zzajl = bArr;
        this.zzajm = com_google_android_gms_internal_measurement_zzfm;
        this.packageName = str;
        this.zzajn = map;
    }

    public final void run() {
        HttpURLConnection zzb;
        Throwable e;
        Map map;
        int i;
        HttpURLConnection httpURLConnection;
        Throwable th;
        Map map2;
        OutputStream outputStream;
        this.zzajo.zzfr();
        int i2 = 0;
        OutputStream outputStream2;
        try {
            zzb = this.zzajo.zzb(this.url);
            try {
                if (this.zzajn != null) {
                    for (Entry entry : this.zzajn.entrySet()) {
                        zzb.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if (this.zzajl != null) {
                    byte[] zza = this.zzajo.zzgc().zza(this.zzajl);
                    this.zzajo.zzgg().zzir().zzg("Uploading data. size", Integer.valueOf(zza.length));
                    zzb.setDoOutput(true);
                    zzb.addRequestProperty("Content-Encoding", "gzip");
                    zzb.setFixedLengthStreamingMode(zza.length);
                    zzb.connect();
                    outputStream2 = zzb.getOutputStream();
                    try {
                        outputStream2.write(zza);
                        outputStream2.close();
                    } catch (IOException e2) {
                        e = e2;
                        map = null;
                        i = 0;
                        httpURLConnection = zzb;
                        if (outputStream2 != null) {
                            try {
                                outputStream2.close();
                            } catch (IOException e3) {
                                this.zzajo.zzgg().zzil().zze("Error closing HTTP compressed POST connection output stream. appId", zzfg.zzbh(this.packageName), e3);
                            }
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i, e, null, map));
                    } catch (Throwable th2) {
                        th = th2;
                        map2 = null;
                        outputStream = outputStream2;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e32) {
                                this.zzajo.zzgg().zzil().zze("Error closing HTTP compressed POST connection output stream. appId", zzfg.zzbh(this.packageName), e32);
                            }
                        }
                        if (zzb != null) {
                            zzb.disconnect();
                        }
                        this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i2, null, null, map2));
                        throw th;
                    }
                }
                i2 = zzb.getResponseCode();
                map2 = zzb.getHeaderFields();
            } catch (IOException e4) {
                e = e4;
                map = null;
                i = i2;
                outputStream2 = null;
                httpURLConnection = zzb;
                if (outputStream2 != null) {
                    outputStream2.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i, e, null, map));
            } catch (Throwable th3) {
                th = th3;
                map2 = null;
                outputStream = null;
                if (outputStream != null) {
                    outputStream.close();
                }
                if (zzb != null) {
                    zzb.disconnect();
                }
                this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i2, null, null, map2));
                throw th;
            }
            try {
                byte[] zza2 = zzfk.zzb(zzb);
                if (zzb != null) {
                    zzb.disconnect();
                }
                this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i2, null, zza2, map2));
            } catch (IOException e5) {
                e = e5;
                map = map2;
                i = i2;
                outputStream2 = null;
                httpURLConnection = zzb;
                if (outputStream2 != null) {
                    outputStream2.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i, e, null, map));
            } catch (Throwable th32) {
                th = th32;
                outputStream = null;
                if (outputStream != null) {
                    outputStream.close();
                }
                if (zzb != null) {
                    zzb.disconnect();
                }
                this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i2, null, null, map2));
                throw th;
            }
        } catch (IOException e6) {
            e = e6;
            map = null;
            i = 0;
            outputStream2 = null;
            httpURLConnection = null;
            if (outputStream2 != null) {
                outputStream2.close();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i, e, null, map));
        } catch (Throwable th322) {
            th = th322;
            map2 = null;
            outputStream = null;
            zzb = null;
            if (outputStream != null) {
                outputStream.close();
            }
            if (zzb != null) {
                zzb.disconnect();
            }
            this.zzajo.zzgf().zzc(new zzfn(this.packageName, this.zzajm, i2, null, null, map2));
            throw th;
        }
    }
}
