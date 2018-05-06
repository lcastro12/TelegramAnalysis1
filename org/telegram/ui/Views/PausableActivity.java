package org.telegram.ui.Views;

import android.support.v7.app.ActionBarActivity;
import org.telegram.ui.ApplicationLoader;

public class PausableActivity extends ActionBarActivity {
    protected void onPause() {
        super.onPause();
        ApplicationLoader.lastPauseTime = System.currentTimeMillis();
    }

    protected void onResume() {
        super.onResume();
        ApplicationLoader.resetLastPauseTime();
    }
}
