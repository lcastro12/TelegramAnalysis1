package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import java.io.IOException;

public class C0969g extends C0920f {

    class C0191a {
        private String dt;
        private boolean du;
        final /* synthetic */ C0969g dv;

        public C0191a(C0969g c0969g, String str, boolean z) {
            this.dv = c0969g;
            this.dt = str;
            this.du = z;
        }

        public String getId() {
            return this.dt;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.du;
        }
    }

    private C0969g(Context context, C0195j c0195j, C0196k c0196k) {
        super(context, c0195j, c0196k);
    }

    public static C0969g m1635a(String str, Context context) {
        C0195j c0664a = new C0664a();
        C0920f.m1447a(str, context, c0664a);
        return new C0969g(context, c0664a, new C0764m(239));
    }

    protected void mo1543b(Context context) {
        long j = 1;
        super.mo1543b(context);
        try {
            C0191a f = m1637f(context);
            try {
                if (!f.isLimitAdTrackingEnabled()) {
                    j = 0;
                }
                m1014a(28, j);
                String id = f.getId();
                if (id != null) {
                    m1015a(30, id);
                }
            } catch (IOException e) {
            }
        } catch (GooglePlayServicesNotAvailableException e2) {
        } catch (IOException e3) {
            m1014a(28, 1);
        }
    }

    C0191a m1637f(Context context) throws IOException, GooglePlayServicesNotAvailableException {
        int i = 0;
        try {
            String str;
            Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            String id = advertisingIdInfo.getId();
            if (id == null || !id.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")) {
                str = id;
            } else {
                byte[] bArr = new byte[16];
                int i2 = 0;
                while (i < id.length()) {
                    if (i == 8 || i == 13 || i == 18 || i == 23) {
                        i++;
                    }
                    bArr[i2] = (byte) ((Character.digit(id.charAt(i), 16) << 4) + Character.digit(id.charAt(i + 1), 16));
                    i2++;
                    i += 2;
                }
                str = this.di.mo751a(bArr, true);
            }
            return new C0191a(this, str, advertisingIdInfo.isLimitAdTrackingEnabled());
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }
}
