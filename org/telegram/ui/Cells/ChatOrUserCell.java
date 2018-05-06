package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import java.lang.ref.WeakReference;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ApplicationLoader;
import org.telegram.ui.Views.ImageReceiver;

public class ChatOrUserCell extends BaseCell {
    private static Paint linePaint;
    private static Drawable lockDrawable;
    private static TextPaint nameEncryptedPaint;
    private static TextPaint namePaint;
    private static TextPaint offlinePaint;
    private static TextPaint onlinePaint;
    private ImageReceiver avatarImage;
    private ChatOrUserCellLayout cellLayout;
    private Chat chat = null;
    private CharSequence currentName;
    public float drawAlpha = 1.0f;
    private EncryptedChat encryptedChat = null;
    private FileLocation lastAvatar = null;
    private String lastName = null;
    private int lastStatus = 0;
    private String subLabel;
    public boolean useBoldFont = false;
    public boolean usePadding = true;
    public boolean useSeparator = false;
    private User user = null;

    private class ChatOrUserCellLayout {
        private int avatarLeft;
        private int avatarTop;
        private boolean drawNameLock;
        private StaticLayout nameLayout;
        private int nameLeft;
        private int nameLockLeft;
        private int nameLockTop;
        private int nameTop;
        private int nameWidth;
        private StaticLayout onlineLayout;
        private int onlineLeft;
        private int onlineTop;
        private int onlineWidth;

        private ChatOrUserCellLayout() {
            this.nameLockTop = Utilities.dp(15);
            this.onlineTop = Utilities.dp(36);
            this.avatarTop = Utilities.dp(7);
        }

        public void build(int width, int height) {
            int i;
            TextPaint currentNamePaint;
            CharSequence nameString = BuildConfig.FLAVOR;
            if (ChatOrUserCell.this.encryptedChat != null) {
                this.drawNameLock = true;
                if (Utilities.isRTL) {
                    this.nameLockLeft = (width - Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 63)) - ChatOrUserCell.lockDrawable.getIntrinsicWidth();
                    this.nameLeft = ChatOrUserCell.this.usePadding ? Utilities.dp(11) : 0;
                } else {
                    this.nameLockLeft = Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 61);
                    if (ChatOrUserCell.this.usePadding) {
                        i = 11;
                    } else {
                        i = 0;
                    }
                    this.nameLeft = Utilities.dp(i + 65) + ChatOrUserCell.lockDrawable.getIntrinsicWidth();
                }
            } else {
                this.drawNameLock = false;
                if (Utilities.isRTL) {
                    this.nameLeft = ChatOrUserCell.this.usePadding ? Utilities.dp(11) : 0;
                } else {
                    this.nameLeft = Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 61);
                }
            }
            if (ChatOrUserCell.this.currentName != null) {
                nameString = ChatOrUserCell.this.currentName;
            } else if (!ChatOrUserCell.this.useBoldFont) {
                String nameString2 = BuildConfig.FLAVOR;
                if (ChatOrUserCell.this.chat != null) {
                    nameString2 = ChatOrUserCell.this.chat.title;
                } else if (ChatOrUserCell.this.user != null) {
                    if (ChatOrUserCell.this.user.id == 333000 || MessagesController.Instance.contactsDict.get(ChatOrUserCell.this.user.id) != null) {
                        nameString2 = Utilities.formatName(ChatOrUserCell.this.user.first_name, ChatOrUserCell.this.user.last_name);
                    } else if (MessagesController.Instance.contactsDict.size() == 0 && MessagesController.Instance.loadingContacts) {
                        nameString2 = Utilities.formatName(ChatOrUserCell.this.user.first_name, ChatOrUserCell.this.user.last_name);
                    } else if (ChatOrUserCell.this.user.phone == null || ChatOrUserCell.this.user.phone.length() == 0) {
                        nameString2 = Utilities.formatName(ChatOrUserCell.this.user.first_name, ChatOrUserCell.this.user.last_name);
                    } else {
                        nameString2 = PhoneFormat.Instance.format("+" + ChatOrUserCell.this.user.phone);
                    }
                }
                nameString = nameString2.replace("\n", " ");
            } else if (ChatOrUserCell.this.user.first_name.length() != 0 && ChatOrUserCell.this.user.last_name.length() != 0) {
                nameString = Html.fromHtml(ChatOrUserCell.this.user.first_name + " <b>" + ChatOrUserCell.this.user.last_name + "</b>");
            } else if (ChatOrUserCell.this.user.first_name.length() != 0) {
                nameString = Html.fromHtml("<b>" + ChatOrUserCell.this.user.first_name + "</b>");
            } else {
                nameString = Html.fromHtml("<b>" + ChatOrUserCell.this.user.last_name + "</b>");
            }
            if (nameString.length() == 0) {
                nameString = ApplicationLoader.applicationContext.getString(C0419R.string.HiddenName);
            }
            if (ChatOrUserCell.this.encryptedChat != null) {
                currentNamePaint = ChatOrUserCell.nameEncryptedPaint;
            } else {
                currentNamePaint = ChatOrUserCell.namePaint;
            }
            if (Utilities.isRTL) {
                i = (width - this.nameLeft) - Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 61);
                this.nameWidth = i;
                this.onlineWidth = i;
            } else {
                i = (width - this.nameLeft) - Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 3);
                this.nameWidth = i;
                this.onlineWidth = i;
            }
            if (this.drawNameLock) {
                this.nameWidth -= Utilities.dp(6) + ChatOrUserCell.lockDrawable.getIntrinsicWidth();
            }
            this.nameLayout = new StaticLayout(TextUtils.ellipsize(nameString, currentNamePaint, (float) (this.nameWidth - Utilities.dp(12)), TruncateAt.END), currentNamePaint, this.nameWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (ChatOrUserCell.this.chat == null) {
                if (Utilities.isRTL) {
                    this.onlineLeft = ChatOrUserCell.this.usePadding ? Utilities.dp(11) : 0;
                } else {
                    this.onlineLeft = Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 61);
                }
                String onlineString = BuildConfig.FLAVOR;
                TextPaint currentOnlinePaint = ChatOrUserCell.offlinePaint;
                if (ChatOrUserCell.this.subLabel != null) {
                    onlineString = ChatOrUserCell.this.subLabel;
                } else if (ChatOrUserCell.this.user != null) {
                    if (ChatOrUserCell.this.user.status == null) {
                        onlineString = ChatOrUserCell.this.getResources().getString(C0419R.string.Offline);
                    } else {
                        int currentTime = ConnectionsManager.Instance.getCurrentTime();
                        if (ChatOrUserCell.this.user.status.expires > currentTime || ChatOrUserCell.this.user.status.was_online > currentTime) {
                            currentOnlinePaint = ChatOrUserCell.onlinePaint;
                            onlineString = ChatOrUserCell.this.getResources().getString(C0419R.string.Online);
                        } else if (ChatOrUserCell.this.user.status.was_online > FileLoader.FileDidUpload || ChatOrUserCell.this.user.status.expires > FileLoader.FileDidUpload) {
                            int value = ChatOrUserCell.this.user.status.was_online;
                            if (value == 0) {
                                value = ChatOrUserCell.this.user.status.expires;
                            }
                            onlineString = ChatOrUserCell.this.getResources().getString(C0419R.string.LastSeen) + " " + Utilities.formatDateOnline((long) value);
                        } else {
                            onlineString = ChatOrUserCell.this.getResources().getString(C0419R.string.Invisible);
                        }
                    }
                }
                this.onlineLayout = new StaticLayout(TextUtils.ellipsize(onlineString, currentOnlinePaint, (float) (this.nameWidth - Utilities.dp(12)), TruncateAt.END), currentOnlinePaint, this.nameWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.nameTop = Utilities.dp(12);
            } else {
                this.onlineLayout = null;
                this.nameTop = Utilities.dp(22);
            }
            if (Utilities.isRTL) {
                this.avatarLeft = width - Utilities.dp((ChatOrUserCell.this.usePadding ? 11 : 0) + 50);
            } else {
                this.avatarLeft = ChatOrUserCell.this.usePadding ? Utilities.dp(11) : 0;
            }
            ChatOrUserCell.this.avatarImage.imageX = this.avatarLeft;
            ChatOrUserCell.this.avatarImage.imageY = this.avatarTop;
            ChatOrUserCell.this.avatarImage.imageW = Utilities.dp(50);
            ChatOrUserCell.this.avatarImage.imageH = Utilities.dp(50);
            double widthpx;
            if (Utilities.isRTL) {
                if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineLeft(0) == 0.0f) {
                    widthpx = Math.ceil((double) this.nameLayout.getLineWidth(0));
                    if (widthpx < ((double) this.nameWidth)) {
                        this.nameLeft = (int) (((double) this.nameLeft) + (((double) this.nameWidth) - widthpx));
                    }
                }
                if (this.onlineLayout != null && this.onlineLayout.getLineCount() > 0 && this.onlineLayout.getLineLeft(0) == 0.0f) {
                    widthpx = Math.ceil((double) this.onlineLayout.getLineWidth(0));
                    if (widthpx < ((double) this.onlineWidth)) {
                        this.onlineLeft = (int) (((double) this.onlineLeft) + (((double) this.onlineWidth) - widthpx));
                        return;
                    }
                    return;
                }
                return;
            }
            if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineRight(0) == ((float) this.nameWidth)) {
                widthpx = Math.ceil((double) this.nameLayout.getLineWidth(0));
                if (widthpx < ((double) this.nameWidth)) {
                    this.nameLeft = (int) (((double) this.nameLeft) - (((double) this.nameWidth) - widthpx));
                }
            }
            if (this.onlineLayout != null && this.onlineLayout.getLineCount() > 0 && this.onlineLayout.getLineRight(0) == ((float) this.onlineWidth)) {
                widthpx = Math.ceil((double) this.onlineLayout.getLineWidth(0));
                if (widthpx < ((double) this.onlineWidth)) {
                    this.onlineLeft = (int) (((double) this.onlineLeft) - (((double) this.onlineWidth) - widthpx));
                }
            }
        }
    }

    public ChatOrUserCell(Context context) {
        super(context);
        init();
    }

    public ChatOrUserCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatOrUserCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (namePaint == null) {
            namePaint = new TextPaint(1);
            namePaint.setTextSize((float) Utilities.dp(18));
            namePaint.setColor(-14540254);
        }
        if (nameEncryptedPaint == null) {
            nameEncryptedPaint = new TextPaint(1);
            nameEncryptedPaint.setTextSize((float) Utilities.dp(18));
            nameEncryptedPaint.setColor(-16734706);
        }
        if (onlinePaint == null) {
            onlinePaint = new TextPaint(1);
            onlinePaint.setTextSize((float) Utilities.dp(15));
            onlinePaint.setColor(-13537377);
        }
        if (offlinePaint == null) {
            offlinePaint = new TextPaint(1);
            offlinePaint.setTextSize((float) Utilities.dp(15));
            offlinePaint.setColor(-6710887);
        }
        if (lockDrawable == null) {
            lockDrawable = getResources().getDrawable(C0419R.drawable.ic_lock_green);
        }
        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setColor(-2302756);
        }
        if (this.avatarImage == null) {
            this.avatarImage = new ImageReceiver();
            this.avatarImage.parentView = new WeakReference(this);
        }
        if (this.cellLayout == null) {
            this.cellLayout = new ChatOrUserCellLayout();
        }
    }

    public void setData(User u, Chat c, EncryptedChat ec, CharSequence n, String s) {
        this.currentName = n;
        this.user = u;
        this.chat = c;
        this.encryptedChat = ec;
        this.subLabel = s;
        update(0);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Utilities.dp(64));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.user == null && this.chat == null && this.encryptedChat == null) {
            super.onLayout(changed, left, top, right, bottom);
        } else if (changed) {
            buildLayout();
        }
    }

    public void buildLayout() {
        this.cellLayout.build(getMeasuredWidth(), getMeasuredHeight());
    }

    public void update(int mask) {
        int placeHolderId = 0;
        FileLocation photo = null;
        if (this.user != null) {
            if (this.user.photo != null) {
                photo = this.user.photo.photo_small;
            }
            placeHolderId = Utilities.getUserAvatarForId(this.user.id);
        } else if (this.chat != null) {
            if (this.chat.photo != null) {
                photo = this.chat.photo.photo_small;
            }
            placeHolderId = Utilities.getGroupAvatarForId(this.chat.id);
        }
        if (mask != 0) {
            boolean continueUpdate = false;
            if (!(((mask & 2) == 0 || this.user == null) && ((mask & 8) == 0 || this.chat == null)) && ((this.lastAvatar != null && photo == null) || !(this.lastAvatar != null || photo == null || this.lastAvatar == null || photo == null || (this.lastAvatar.volume_id == photo.volume_id && this.lastAvatar.local_id == photo.local_id)))) {
                continueUpdate = true;
            }
            if (!(continueUpdate || (mask & 4) == 0 || this.user == null)) {
                int newStatus = 0;
                if (this.user.status != null) {
                    newStatus = this.user.status.expires;
                    if (this.lastStatus == 0) {
                        this.lastStatus = this.user.status.was_online;
                    }
                }
                if (newStatus != this.lastStatus) {
                    continueUpdate = true;
                }
            }
            if (!((continueUpdate || (mask & 1) == 0 || this.user == null) && ((mask & 16) == 0 || this.chat == null))) {
                String newName;
                if (this.user != null) {
                    newName = this.user.first_name + this.user.last_name;
                } else {
                    newName = this.chat.title;
                }
                if (!newName.equals(this.lastName)) {
                    continueUpdate = true;
                }
            }
            if (!continueUpdate) {
                return;
            }
        }
        if (this.user != null) {
            if (this.user.status != null) {
                this.lastStatus = this.user.status.expires;
                if (this.lastStatus == 0) {
                    this.lastStatus = this.user.status.was_online;
                }
            } else {
                this.lastStatus = 0;
            }
            this.lastName = this.user.first_name + this.user.last_name;
        } else if (this.chat != null) {
            this.lastName = this.chat.title;
        }
        this.lastAvatar = photo;
        this.avatarImage.setImage(photo, "50_50", placeHolderId == 0 ? null : getResources().getDrawable(placeHolderId));
        if (getMeasuredWidth() == 0 && getMeasuredHeight() == 0) {
            requestLayout();
        } else {
            buildLayout();
        }
        postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (this.user != null || this.chat != null || this.encryptedChat != null) {
            if (this.cellLayout == null) {
                requestLayout();
                return;
            }
            if (this.drawAlpha != 1.0f) {
                canvas.saveLayerAlpha(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), (int) (255.0f * this.drawAlpha), 4);
            }
            if (this.cellLayout.drawNameLock) {
                setDrawableBounds(lockDrawable, this.cellLayout.nameLockLeft, this.cellLayout.nameLockTop);
                lockDrawable.draw(canvas);
            }
            canvas.save();
            canvas.translate((float) this.cellLayout.nameLeft, (float) this.cellLayout.nameTop);
            this.cellLayout.nameLayout.draw(canvas);
            canvas.restore();
            if (this.cellLayout.onlineLayout != null) {
                canvas.save();
                canvas.translate((float) this.cellLayout.onlineLeft, (float) this.cellLayout.onlineTop);
                this.cellLayout.onlineLayout.draw(canvas);
                canvas.restore();
            }
            this.avatarImage.draw(canvas, this.cellLayout.avatarLeft, this.cellLayout.avatarTop, Utilities.dp(50), Utilities.dp(50));
            if (this.useSeparator) {
                int h = getMeasuredHeight();
                if (this.usePadding) {
                    canvas.drawLine((float) Utilities.dp(11), (float) (h - 1), (float) (getMeasuredWidth() - Utilities.dp(11)), (float) h, linePaint);
                    return;
                }
                canvas.drawLine(0.0f, (float) (h - 1), (float) getMeasuredWidth(), (float) h, linePaint);
            }
        }
    }
}
