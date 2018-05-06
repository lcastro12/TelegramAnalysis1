package org.telegram.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.objects.MessageObject;
import org.telegram.ui.ApplicationLoader;

public class NotificationView extends LinearLayout {
    private Animation animHide;
    private Animation animShow;
    public BackupImageView avatarImage;
    public ImageView closeButton;
    private int currentChatId = 0;
    private int currentEncId = 0;
    private int currentUserId = 0;
    private Timer hideTimer;
    private boolean isVisible;
    public TextView messageTextView;
    public TextView nameTextView;
    private LayoutParams notificationLayoutParams;
    private ViewGroup notificationParentView;
    private boolean onScreen;
    public FrameLayout textLayout;
    private final Integer timerSync = Integer.valueOf(1);

    class C06121 implements OnClickListener {
        C06121() {
        }

        public void onClick(View v) {
            try {
                synchronized (NotificationView.this.timerSync) {
                    if (NotificationView.this.hideTimer != null) {
                        NotificationView.this.hideTimer.cancel();
                        NotificationView.this.hideTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            NotificationView.this.hide(true);
        }
    }

    class C06132 implements OnClickListener {
        C06132() {
        }

        public void onClick(View v) {
            try {
                synchronized (NotificationView.this.timerSync) {
                    if (NotificationView.this.hideTimer != null) {
                        NotificationView.this.hideTimer.cancel();
                        NotificationView.this.hideTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            NotificationView.this.hide(true);
            if (NotificationView.this.currentChatId != 0) {
                NotificationCenter.Instance.addToMemCache("push_chat_id", Integer.valueOf(NotificationView.this.currentChatId));
            }
            if (NotificationView.this.currentUserId != 0) {
                NotificationCenter.Instance.addToMemCache("push_user_id", Integer.valueOf(NotificationView.this.currentUserId));
            }
            if (NotificationView.this.currentEncId != 0) {
                NotificationCenter.Instance.addToMemCache("push_enc_id", Integer.valueOf(NotificationView.this.currentEncId));
            }
            NotificationCenter.Instance.postNotificationName(658, new Object[0]);
        }
    }

    class C06143 implements AnimationListener {
        C06143() {
        }

        public void onAnimationStart(Animation animation) {
            NotificationView.this.onScreen = false;
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            NotificationView.this.setVisibility(8);
            WindowManager wm = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
            NotificationView.this.isVisible = false;
            NotificationView.this.notificationParentView.setVisibility(4);
        }
    }

    class C06154 implements AnimationListener {
        C06154() {
        }

        public void onAnimationStart(Animation animation) {
            NotificationView.this.setVisibility(0);
            NotificationView.this.onScreen = true;
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        }
    }

    class C06175 extends TimerTask {

        class C06161 implements Runnable {
            C06161() {
            }

            public void run() {
                NotificationView.this.hide(true);
            }
        }

        C06175() {
        }

        public void run() {
            Utilities.RunOnUIThread(new C06161());
            try {
                synchronized (NotificationView.this.timerSync) {
                    if (NotificationView.this.hideTimer != null) {
                        NotificationView.this.hideTimer.cancel();
                        NotificationView.this.hideTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
    }

    public NotificationView(Context context) {
        super(context);
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.avatarImage = (BackupImageView) findViewById(C0419R.id.avatar_image);
        this.nameTextView = (TextView) findViewById(C0419R.id.name_text_view);
        this.messageTextView = (TextView) findViewById(C0419R.id.message_text_view);
        this.closeButton = (ImageView) findViewById(C0419R.id.close_button);
        this.textLayout = (FrameLayout) findViewById(C0419R.id.text_layout);
        this.closeButton.setOnClickListener(new C06121());
        setOnClickListener(new C06132());
        this.notificationParentView = new FrameLayout(getContext());
        this.notificationParentView.addView(this);
        this.notificationParentView.setFocusable(false);
        setFocusable(false);
        WindowManager wm = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
        this.notificationLayoutParams = new LayoutParams();
        this.notificationLayoutParams.height = 90;
        this.notificationLayoutParams.format = -3;
        this.notificationLayoutParams.width = -1;
        this.notificationLayoutParams.gravity = 56;
        this.notificationLayoutParams.type = 2010;
        this.notificationLayoutParams.flags = 262920;
        this.isVisible = false;
        wm.addView(this.notificationParentView, this.notificationLayoutParams);
        this.notificationParentView.setVisibility(4);
        this.animHide = AnimationUtils.loadAnimation(ApplicationLoader.applicationContext, C0419R.anim.slide_up);
        this.animHide.setAnimationListener(new C06143());
        this.animShow = AnimationUtils.loadAnimation(ApplicationLoader.applicationContext, C0419R.anim.slide_down);
        this.animShow.setAnimationListener(new C06154());
    }

    public void show(MessageObject object) {
        User user = (User) MessagesController.Instance.users.get(Integer.valueOf(object.messageOwner.from_id));
        Chat chat = null;
        long dialog_id = object.messageOwner.dialog_id;
        if (object.messageOwner.to_id.chat_id != 0) {
            chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(object.messageOwner.to_id.chat_id));
            if (chat == null) {
                return;
            }
        }
        if (user != null) {
            if (chat != null) {
                this.currentChatId = chat.id;
                this.currentUserId = 0;
                this.currentEncId = 0;
                this.nameTextView.setText(Utilities.formatName(user.first_name, user.last_name) + " @ " + chat.title);
            } else {
                if (((int) dialog_id) != 0 || dialog_id == 0) {
                    this.currentUserId = user.id;
                    this.currentEncId = 0;
                } else {
                    this.currentUserId = 0;
                    this.currentEncId = (int) (dialog_id >> 32);
                }
                this.currentChatId = 0;
                this.nameTextView.setText(Utilities.formatName(user.first_name, user.last_name));
            }
            this.nameTextView.setTextColor(Utilities.getColorForId(user.id));
            this.messageTextView.setText(object.messageText);
            FileLocation photo = null;
            if (user.photo != null) {
                photo = user.photo.photo_small;
            }
            this.avatarImage.setImage(photo, "50_50", Utilities.getUserAvatarForId(user.id));
            try {
                synchronized (this.timerSync) {
                    if (this.hideTimer != null) {
                        this.hideTimer.cancel();
                        this.hideTimer = null;
                    }
                }
                this.hideTimer = new Timer();
                this.hideTimer.schedule(new C06175(), 3000);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
            if (!this.onScreen) {
                WindowManager wm = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
                this.isVisible = true;
                this.notificationParentView.setVisibility(0);
                startAnimation(this.animShow);
            }
        }
    }

    public void hide(boolean animation) {
        if (!this.onScreen) {
            return;
        }
        if (animation) {
            startAnimation(this.animHide);
            return;
        }
        try {
            synchronized (this.timerSync) {
                if (this.hideTimer != null) {
                    this.hideTimer.cancel();
                    this.hideTimer = null;
                }
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
        this.onScreen = false;
        setVisibility(8);
        if (this.notificationParentView != null && this.notificationParentView.getParent() != null) {
            WindowManager wm = (WindowManager) ApplicationLoader.applicationContext.getSystemService("window");
            this.isVisible = false;
            this.notificationParentView.setVisibility(4);
        }
    }

    public void destroy() {
        try {
            if (this.notificationParentView != null) {
                this.notificationParentView.removeView(this);
                try {
                    if (this.notificationParentView.getParent() != null) {
                        ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).removeViewImmediate(this.notificationParentView);
                    }
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                }
            }
            this.notificationParentView = null;
            this.notificationLayoutParams = null;
        } catch (Exception e2) {
            FileLog.m799e("tmessages", e2);
        }
    }

    public void applyOrientationPaddings(boolean isLandscape, int height) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.avatarImage.getLayoutParams();
        params.width = height;
        params.height = height;
        this.avatarImage.setLayoutParams(params);
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) this.textLayout.getLayoutParams();
        if (isLandscape) {
            this.nameTextView.setTextSize(2, 14.0f);
            this.messageTextView.setTextSize(2, 14.0f);
            this.nameTextView.setPadding(0, Utilities.dp(2), 0, 0);
            this.messageTextView.setPadding(0, Utilities.dp(18), 0, 0);
            if (Utilities.isRTL) {
                params1.setMargins(Utilities.dp(40), 0, Utilities.dp(6) + height, 0);
            } else {
                params1.setMargins(Utilities.dp(6) + height, 0, Utilities.dp(40), 0);
            }
        } else {
            this.nameTextView.setTextSize(2, 15.0f);
            this.messageTextView.setTextSize(2, 15.0f);
            this.nameTextView.setPadding(0, Utilities.dp(4), 0, 0);
            this.messageTextView.setPadding(0, Utilities.dp(24), 0, 0);
            if (Utilities.isRTL) {
                params1.setMargins(Utilities.dp(40), 0, Utilities.dp(8) + height, 0);
            } else {
                params1.setMargins(Utilities.dp(8) + height, 0, Utilities.dp(40), 0);
            }
        }
        this.textLayout.setLayoutParams(params1);
        if (this.notificationParentView != null) {
            this.notificationLayoutParams.height = height;
            if (this.notificationParentView.getParent() != null) {
                ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).updateViewLayout(this.notificationParentView, this.notificationLayoutParams);
            }
        }
    }
}
