package org.telegram.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Views.SlideView;
import org.telegram.ui.Views.SlideView.SlideViewDelegate;

public class LoginActivity extends ActionBarActivity implements SlideViewDelegate {
    private int currentViewNum = 0;
    private SlideView[] views = new SlideView[3];

    class C05262 implements OnClickListener {
        C05262() {
        }

        public void onClick(View view) {
            LoginActivity.this.onNextAction();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.currentViewNum == 0 && resultCode == -1) {
            ((LoginActivityPhoneView) this.views[0]).selectCountry(data.getStringExtra("country"));
        }
    }

    protected void onResume() {
        super.onResume();
        ApplicationLoader.resetLastPauseTime();
    }

    protected void onPause() {
        super.onPause();
        ApplicationLoader.lastPauseTime = System.currentTimeMillis();
    }

    public void ShowAlertDialog(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (!LoginActivity.this.isFinishing()) {
                    Builder builder = new Builder(activity);
                    builder.setTitle(LoginActivity.this.getString(C0419R.string.AppName));
                    builder.setMessage(message);
                    builder.setPositiveButton(ApplicationLoader.applicationContext.getString(C0419R.string.OK), null);
                    builder.show().setCanceledOnTouchOutside(true);
                }
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0419R.layout.login_layout);
        ApplicationLoader.applicationContext = getApplicationContext();
        getSupportActionBar().setLogo((int) C0419R.drawable.ab_icon_fixed2);
        getSupportActionBar().show();
        ImageView view = (ImageView) findViewById(16908332);
        if (view == null) {
            view = (ImageView) findViewById(C0419R.id.home);
        }
        if (view != null) {
            view.setPadding(Utilities.dp(6), 0, Utilities.dp(6), 0);
        }
        this.views[0] = (SlideView) findViewById(C0419R.id.login_page1);
        this.views[1] = (SlideView) findViewById(C0419R.id.login_page2);
        this.views[2] = (SlideView) findViewById(C0419R.id.login_page3);
        getSupportActionBar().setTitle(this.views[0].getHeaderName());
        if (savedInstanceState != null) {
            this.currentViewNum = savedInstanceState.getInt("currentViewNum", 0);
        }
        int a = 0;
        while (a < this.views.length) {
            SlideView v = this.views[a];
            if (v != null) {
                v.delegate = this;
                v.setVisibility(this.currentViewNum == a ? 0 : 8);
            }
            a++;
        }
        getWindow().setBackgroundDrawableResource(C0419R.drawable.transparent);
        getWindow().setFormat(4);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0419R.menu.group_create_menu, menu);
        ((TextView) ((SupportMenuItem) menu.findItem(C0419R.id.done_menu_item)).getActionView().findViewById(C0419R.id.done_button)).setOnClickListener(new C05262());
        return super.onCreateOptionsMenu(menu);
    }

    public void onBackPressed() {
        if (this.currentViewNum == 0) {
            for (SlideView v : this.views) {
                if (v != null) {
                    v.onDestroyActivity();
                }
            }
            super.onBackPressed();
        } else if (this.currentViewNum != 1 && this.currentViewNum != 2) {
            setPage(0, true, null, true);
        }
    }

    public void needShowAlert(String text) {
        ShowAlertDialog(this, text);
    }

    public void needShowProgress() {
        Utilities.ShowProgressDialog(this, getResources().getString(C0419R.string.Loading));
    }

    public void needHideProgress() {
        Utilities.HideProgressDialog(this);
    }

    public void setPage(int page, boolean animated, Bundle params, boolean back) {
        if (VERSION.SDK_INT > 13) {
            float f;
            Point displaySize = new Point();
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            final SlideView outView = this.views[this.currentViewNum];
            final SlideView newView = this.views[page];
            this.currentViewNum = page;
            newView.setParams(params);
            getSupportActionBar().setTitle(newView.getHeaderName());
            newView.onShow();
            newView.setX(back ? (float) (-displaySize.x) : (float) displaySize.x);
            ViewPropertyAnimator duration = outView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    outView.setVisibility(8);
                    outView.setX(0.0f);
                }

                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }
            }).setDuration(300);
            if (back) {
                f = (float) displaySize.x;
            } else {
                f = (float) (-displaySize.x);
            }
            duration.translationX(f).start();
            newView.animate().setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new AnimatorListener() {
                public void onAnimationStart(Animator animator) {
                    newView.setVisibility(0);
                }

                public void onAnimationEnd(Animator animator) {
                }

                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }
            }).setDuration(300).translationX(0.0f).start();
            return;
        }
        this.views[this.currentViewNum].setVisibility(8);
        this.currentViewNum = page;
        this.views[page].setParams(params);
        this.views[page].setVisibility(0);
        getSupportActionBar().setTitle(this.views[page].getHeaderName());
        this.views[page].onShow();
    }

    public void onNextAction() {
        this.views[this.currentViewNum].onNextPressed();
    }

    protected void onDestroy() {
        super.onDestroy();
        for (SlideView v : this.views) {
            if (v != null) {
                v.onDestroyActivity();
            }
        }
        Utilities.HideProgressDialog(this);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentViewNum", this.currentViewNum);
    }

    public void needFinishActivity() {
        startActivity(new Intent(this, LaunchActivity.class));
        finish();
    }
}
