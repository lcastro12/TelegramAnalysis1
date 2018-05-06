package org.telegram.ui.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.ui.ApplicationActivity;
import org.telegram.ui.ApplicationLoader;

public class BaseFragment extends Fragment {
    public boolean animationInProgress = false;
    public int animationType = 0;
    public int classGuid = 0;
    public boolean firstStart = true;
    public View fragmentView;
    public boolean isFinish = false;
    public ActionBarActivity parentActivity;
    private boolean removeParentOnAnimationEnd = true;
    private boolean removeParentOnDestroy = false;

    class C06021 implements AnimationListener {
        C06021() {
        }

        public void onAnimationStart(Animation animation) {
            BaseFragment.this.onAnimationStart();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            BaseFragment.this.onAnimationEnd();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = (ActionBarActivity) getActivity();
    }

    public void willBeHidden() {
    }

    public void finishFragment() {
        finishFragment(false);
    }

    public void finishFragment(boolean bySwipe) {
        if (!this.isFinish && !this.animationInProgress) {
            this.isFinish = true;
            if (this.parentActivity == null) {
                ApplicationLoader.fragmentsStack.remove(this);
                onFragmentDestroy();
                return;
            }
            ((ApplicationActivity) this.parentActivity).finishFragment(bySwipe);
            if (getActivity() == null) {
                if (this.fragmentView != null) {
                    ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
                    if (parent != null) {
                        parent.removeView(this.fragmentView);
                    }
                    this.fragmentView = null;
                }
                this.parentActivity = null;
                return;
            }
            this.removeParentOnDestroy = true;
        }
    }

    public void removeSelfFromStack() {
        if (!this.isFinish) {
            this.isFinish = true;
            if (this.parentActivity == null) {
                ApplicationLoader.fragmentsStack.remove(this);
                onFragmentDestroy();
                return;
            }
            ((ApplicationActivity) this.parentActivity).removeFromStack(this);
            if (getActivity() == null) {
                if (this.fragmentView != null) {
                    ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
                    if (parent != null) {
                        parent.removeView(this.fragmentView);
                    }
                    this.fragmentView = null;
                }
                this.parentActivity = null;
                return;
            }
            this.removeParentOnDestroy = true;
        }
    }

    public boolean onFragmentCreate() {
        this.classGuid = ConnectionsManager.Instance.generateClassGuid();
        return true;
    }

    public void onFragmentDestroy() {
        ConnectionsManager.Instance.cancelRpcsForClassGuid(this.classGuid);
        this.removeParentOnDestroy = true;
        this.isFinish = true;
    }

    public String getStringEntry(int res) {
        return ApplicationLoader.applicationContext.getString(res);
    }

    public void onAnimationStart() {
        this.animationInProgress = true;
    }

    public void onAnimationEnd() {
        this.animationInProgress = false;
    }

    public boolean onBackPressed() {
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.removeParentOnDestroy) {
            if (this.fragmentView != null) {
                ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
                if (parent != null) {
                    parent.removeView(this.fragmentView);
                }
                this.fragmentView = null;
            }
            this.parentActivity = null;
        }
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        anim.setAnimationListener(new C06021());
        return anim;
    }

    public boolean canApplyUpdateStatus() {
        return true;
    }

    public void applySelfActionBar() {
    }
}
