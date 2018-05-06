package com.google.android.gms.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;

public class dc implements OnClickListener {
    private final Activity fD;
    private final int ky;
    private final Intent mIntent;

    public dc(Activity activity, Intent intent, int i) {
        this.fD = activity;
        this.mIntent = intent;
        this.ky = i;
    }

    public void onClick(DialogInterface dialog, int which) {
        try {
            if (this.mIntent != null) {
                this.fD.startActivityForResult(this.mIntent, this.ky);
            }
            dialog.dismiss();
        } catch (ActivityNotFoundException e) {
            Log.e("SettingsRedirect", "Can't redirect to app settings for Google Play services");
        }
    }
}
