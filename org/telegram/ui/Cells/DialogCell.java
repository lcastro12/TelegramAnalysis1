package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
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
import org.telegram.TL.TLRPC.TL_dialog;
import org.telegram.TL.TLRPC.TL_encryptedChat;
import org.telegram.TL.TLRPC.TL_encryptedChatDiscarded;
import org.telegram.TL.TLRPC.TL_encryptedChatRequested;
import org.telegram.TL.TLRPC.TL_encryptedChatWaiting;
import org.telegram.TL.TLRPC.TL_messageMediaEmpty;
import org.telegram.TL.TLRPC.TL_messageService;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.objects.MessageObject;
import org.telegram.ui.ApplicationLoader;
import org.telegram.ui.Views.ImageReceiver;

public class DialogCell extends BaseCell {
    private static Drawable checkDrawable;
    private static Drawable clockDrawable;
    private static Drawable countDrawable;
    private static TextPaint countPaint;
    private static Drawable errorDrawable;
    private static Drawable halfCheckDrawable;
    private static Drawable lockDrawable;
    private static TextPaint messagePaint;
    private static TextPaint messagePrintingPaint;
    private static TextPaint nameEncryptedPaint;
    private static TextPaint namePaint;
    private static TextPaint nameUnknownPaint;
    private static TextPaint timePaint;
    private ImageReceiver avatarImage;
    private DialogCellLayout cellLayout;
    private Chat chat = null;
    private TL_dialog currentDialog;
    private EncryptedChat encryptedChat = null;
    private CharSequence lastPrintString = null;
    private User user = null;

    private class DialogCellLayout {
        private int avatarLeft;
        private int avatarTop;
        private int checkDrawLeft;
        private int checkDrawTop;
        private StaticLayout countLayout;
        private int countLeft;
        private int countTop;
        private int countWidth;
        private boolean drawCheck1;
        private boolean drawCheck2;
        private boolean drawClock;
        private boolean drawCount;
        private boolean drawError;
        private boolean drawNameLock;
        private int errorLeft;
        private int errorTop;
        private int halfCheckDrawLeft;
        private StaticLayout messageLayout;
        private int messageLeft;
        private int messageTop;
        private int messageWidth;
        private StaticLayout nameLayout;
        private int nameLeft;
        private int nameLockLeft;
        private int nameLockTop;
        private int nameTop;
        private int nameWidth;
        private StaticLayout timeLayout;
        private int timeLeft;
        private int timeTop;
        private int timeWidth;

        private DialogCellLayout() {
            this.nameTop = Utilities.dp(10);
            this.nameLockTop = Utilities.dp(13);
            this.timeTop = Utilities.dp(13);
            this.checkDrawTop = Utilities.dp(15);
            this.messageTop = Utilities.dp(40);
            this.errorTop = Utilities.dp(37);
            this.countTop = Utilities.dp(37);
            this.avatarTop = Utilities.dp(8);
        }

        public void build(int width, int height) {
            TextPaint currentNamePaint;
            int w;
            MessageObject message = (MessageObject) MessagesController.Instance.dialogMessage.get(DialogCell.this.currentDialog.top_message);
            String nameString = BuildConfig.FLAVOR;
            String timeString = BuildConfig.FLAVOR;
            String countString = null;
            CharSequence messageString = BuildConfig.FLAVOR;
            CharSequence printingString = (CharSequence) MessagesController.Instance.printingStrings.get(Long.valueOf(DialogCell.this.currentDialog.id));
            TextPaint currentNamePaint2 = DialogCell.namePaint;
            TextPaint currentMessagePaint = DialogCell.messagePaint;
            boolean checkMessage = true;
            if (DialogCell.this.encryptedChat != null) {
                this.drawNameLock = true;
                if (Utilities.isRTL) {
                    this.nameLockLeft = (width - Utilities.dp(77)) - DialogCell.lockDrawable.getIntrinsicWidth();
                    this.nameLeft = Utilities.dp(14);
                } else {
                    this.nameLockLeft = Utilities.dp(77);
                    this.nameLeft = Utilities.dp(81) + DialogCell.lockDrawable.getIntrinsicWidth();
                }
            } else {
                this.drawNameLock = false;
                if (Utilities.isRTL) {
                    this.nameLeft = Utilities.dp(14);
                } else {
                    this.nameLeft = Utilities.dp(77);
                }
            }
            if (message == null) {
                if (printingString != null) {
                    messageString = printingString;
                    DialogCell.this.lastPrintString = printingString;
                    currentMessagePaint = DialogCell.messagePrintingPaint;
                } else {
                    DialogCell.this.lastPrintString = null;
                    if (DialogCell.this.encryptedChat != null) {
                        currentMessagePaint = DialogCell.messagePrintingPaint;
                        if (DialogCell.this.encryptedChat instanceof TL_encryptedChatRequested) {
                            messageString = ApplicationLoader.applicationContext.getString(C0419R.string.EncryptionProcessing);
                        } else if (DialogCell.this.encryptedChat instanceof TL_encryptedChatWaiting) {
                            messageString = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.AwaitingEncryption), new Object[]{DialogCell.this.user.first_name});
                        } else if (DialogCell.this.encryptedChat instanceof TL_encryptedChatDiscarded) {
                            messageString = ApplicationLoader.applicationContext.getString(C0419R.string.EncryptionRejected);
                        } else if (DialogCell.this.encryptedChat instanceof TL_encryptedChat) {
                            if (DialogCell.this.encryptedChat.admin_id == UserConfig.clientUserId) {
                                if (DialogCell.this.user != null) {
                                    messageString = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.EncryptedChatStartedOutgoing), new Object[]{DialogCell.this.user.first_name});
                                }
                            } else if (DialogCell.this.user != null) {
                                messageString = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.EncryptedChatStartedIncoming), new Object[]{DialogCell.this.user.first_name});
                            }
                        }
                    }
                }
                if (DialogCell.this.currentDialog.last_message_date != 0) {
                    timeString = Utilities.stringForMessageListDate((long) DialogCell.this.currentDialog.last_message_date);
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawClock = false;
                this.drawCount = false;
                this.drawError = false;
            } else {
                User fromUser = (User) MessagesController.Instance.users.get(Integer.valueOf(message.messageOwner.from_id));
                if (DialogCell.this.currentDialog.last_message_date != 0) {
                    timeString = Utilities.stringForMessageListDate((long) DialogCell.this.currentDialog.last_message_date);
                } else {
                    timeString = Utilities.stringForMessageListDate((long) message.messageOwner.date);
                }
                if (printingString != null) {
                    messageString = printingString;
                    DialogCell.this.lastPrintString = printingString;
                    currentMessagePaint = DialogCell.messagePrintingPaint;
                } else {
                    DialogCell.this.lastPrintString = null;
                    if (message.messageOwner instanceof TL_messageService) {
                        messageString = message.messageText;
                        currentMessagePaint = DialogCell.messagePrintingPaint;
                    } else if (DialogCell.this.chat != null) {
                        String name = BuildConfig.FLAVOR;
                        if (message.messageOwner.from_id == UserConfig.clientUserId) {
                            name = ApplicationLoader.applicationContext.getString(C0419R.string.FromYou);
                        } else if (fromUser != null) {
                            if (fromUser.first_name.length() > 0) {
                                name = fromUser.first_name;
                            } else {
                                name = fromUser.last_name;
                            }
                        }
                        if (message.messageOwner.media == null || (message.messageOwner.media instanceof TL_messageMediaEmpty)) {
                            checkMessage = false;
                            messageString = Emoji.replaceEmoji(Html.fromHtml(String.format("<font color=#316f9f>%s:</font> <font color=#808080>%s</font>", new Object[]{name, message.messageOwner.message.replace("\n", " ")})));
                        } else {
                            messageString = message.messageText;
                            currentMessagePaint = DialogCell.messagePrintingPaint;
                        }
                    } else {
                        messageString = message.messageText;
                        if (!(message.messageOwner.media == null || (message.messageOwner.media instanceof TL_messageMediaEmpty))) {
                            currentMessagePaint = DialogCell.messagePrintingPaint;
                        }
                    }
                }
                if (DialogCell.this.currentDialog.unread_count != 0) {
                    this.drawCount = true;
                    countString = String.format("%d", new Object[]{Integer.valueOf(DialogCell.this.currentDialog.unread_count)});
                } else {
                    this.drawCount = false;
                }
                if (message.messageOwner.id < 0 && message.messageOwner.send_state != 0 && MessagesController.Instance.sendingMessages.get(message.messageOwner.id) == null) {
                    message.messageOwner.send_state = 2;
                }
                if (message.messageOwner.from_id != UserConfig.clientUserId) {
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawClock = false;
                    this.drawError = false;
                } else if (message.messageOwner.send_state == 1) {
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawClock = true;
                    this.drawError = false;
                } else if (message.messageOwner.send_state == 2) {
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawClock = false;
                    this.drawError = true;
                    this.drawCount = false;
                } else if (message.messageOwner.send_state == 0) {
                    if (message.messageOwner.unread) {
                        this.drawCheck1 = false;
                        this.drawCheck2 = true;
                    } else {
                        this.drawCheck1 = true;
                        this.drawCheck2 = true;
                    }
                    this.drawClock = false;
                    this.drawError = false;
                }
            }
            this.timeWidth = (int) Math.ceil((double) DialogCell.timePaint.measureText(timeString));
            this.timeLayout = new StaticLayout(timeString, DialogCell.timePaint, this.timeWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (Utilities.isRTL) {
                this.timeLeft = Utilities.dp(11);
            } else {
                this.timeLeft = (width - Utilities.dp(11)) - this.timeWidth;
            }
            if (DialogCell.this.chat != null) {
                nameString = DialogCell.this.chat.title;
                currentNamePaint = currentNamePaint2;
            } else if (DialogCell.this.user != null) {
                if (DialogCell.this.user.id == 333000 || MessagesController.Instance.contactsDict.get(DialogCell.this.user.id) != null) {
                    nameString = Utilities.formatName(DialogCell.this.user.first_name, DialogCell.this.user.last_name);
                    currentNamePaint = currentNamePaint2;
                } else if (MessagesController.Instance.contactsDict.size() == 0 && MessagesController.Instance.loadingContacts) {
                    nameString = Utilities.formatName(DialogCell.this.user.first_name, DialogCell.this.user.last_name);
                    currentNamePaint = currentNamePaint2;
                } else if (DialogCell.this.user.phone == null || DialogCell.this.user.phone.length() == 0) {
                    currentNamePaint = DialogCell.nameUnknownPaint;
                    nameString = Utilities.formatName(DialogCell.this.user.first_name, DialogCell.this.user.last_name);
                } else {
                    nameString = PhoneFormat.Instance.format("+" + DialogCell.this.user.phone);
                    currentNamePaint = currentNamePaint2;
                }
                if (DialogCell.this.encryptedChat != null) {
                    currentNamePaint = DialogCell.nameEncryptedPaint;
                }
            } else {
                currentNamePaint = currentNamePaint2;
            }
            if (nameString.length() == 0) {
                nameString = ApplicationLoader.applicationContext.getString(C0419R.string.HiddenName);
            }
            if (Utilities.isRTL) {
                this.nameWidth = ((width - this.nameLeft) - Utilities.dp(77)) - this.timeWidth;
                this.nameLeft += this.timeWidth;
            } else {
                this.nameWidth = ((width - this.nameLeft) - Utilities.dp(14)) - this.timeWidth;
            }
            if (this.drawNameLock) {
                this.nameWidth -= Utilities.dp(4) + DialogCell.lockDrawable.getIntrinsicWidth();
            }
            if (this.drawClock) {
                w = DialogCell.clockDrawable.getIntrinsicWidth() + Utilities.dp(2);
                this.nameWidth -= w;
                if (Utilities.isRTL) {
                    this.checkDrawLeft = (this.timeLeft + this.timeWidth) + Utilities.dp(2);
                    this.nameLeft += w;
                } else {
                    this.checkDrawLeft = this.timeLeft - w;
                }
            } else if (this.drawCheck2) {
                w = DialogCell.checkDrawable.getIntrinsicWidth() + Utilities.dp(2);
                this.nameWidth -= w;
                if (this.drawCheck1) {
                    this.nameWidth -= DialogCell.halfCheckDrawable.getIntrinsicWidth() - Utilities.dp(5);
                    if (Utilities.isRTL) {
                        this.checkDrawLeft = (this.timeLeft + this.timeWidth) + Utilities.dp(2);
                        this.halfCheckDrawLeft = this.checkDrawLeft + Utilities.dp(5);
                        this.nameLeft += (DialogCell.halfCheckDrawable.getIntrinsicWidth() + w) - Utilities.dp(5);
                    } else {
                        this.halfCheckDrawLeft = this.timeLeft - w;
                        this.checkDrawLeft = this.halfCheckDrawLeft - Utilities.dp(5);
                    }
                } else if (Utilities.isRTL) {
                    this.checkDrawLeft = (this.timeLeft + this.timeWidth) + Utilities.dp(2);
                    this.nameLeft += w;
                } else {
                    this.checkDrawLeft = this.timeLeft - w;
                }
            }
            this.nameLayout = new StaticLayout(TextUtils.ellipsize(nameString.replace("\n", " "), currentNamePaint, (float) (this.nameWidth - Utilities.dp(12)), TruncateAt.END), currentNamePaint, this.nameWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.messageWidth = width - Utilities.dp(88);
            if (Utilities.isRTL) {
                this.messageLeft = Utilities.dp(11);
                this.avatarLeft = width - Utilities.dp(65);
            } else {
                this.messageLeft = Utilities.dp(77);
                this.avatarLeft = Utilities.dp(11);
            }
            DialogCell.this.avatarImage.imageX = this.avatarLeft;
            DialogCell.this.avatarImage.imageY = this.avatarTop;
            DialogCell.this.avatarImage.imageW = Utilities.dp(54);
            DialogCell.this.avatarImage.imageH = Utilities.dp(54);
            if (this.drawError) {
                w = DialogCell.errorDrawable.getIntrinsicWidth() + Utilities.dp(8);
                this.messageWidth -= w;
                if (Utilities.isRTL) {
                    this.errorLeft = Utilities.dp(11);
                    this.messageLeft += w;
                } else {
                    this.errorLeft = (width - DialogCell.errorDrawable.getIntrinsicWidth()) - Utilities.dp(11);
                }
            } else if (countString != null) {
                this.countWidth = Math.max(Utilities.dp(12), (int) Math.ceil((double) DialogCell.countPaint.measureText(countString)));
                this.countLayout = new StaticLayout(countString, DialogCell.countPaint, this.countWidth, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                w = this.countWidth + Utilities.dp(18);
                this.messageWidth -= w;
                if (Utilities.isRTL) {
                    this.countLeft = Utilities.dp(16);
                    this.messageLeft += w;
                } else {
                    this.countLeft = (width - this.countWidth) - Utilities.dp(16);
                }
                this.drawCount = true;
            } else {
                this.drawCount = false;
            }
            if (checkMessage) {
                String mess = messageString.toString().replace("\n", " ");
                if (mess.length() > 150) {
                    mess = mess.substring(0, 150);
                }
                messageString = Emoji.replaceEmoji(mess);
            }
            TextPaint textPaint = currentMessagePaint;
            this.messageLayout = new StaticLayout(TextUtils.ellipsize(messageString, currentMessagePaint, (float) (this.messageWidth - Utilities.dp(12)), TruncateAt.END), textPaint, this.messageWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            double widthpx;
            if (Utilities.isRTL) {
                if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineLeft(0) == 0.0f) {
                    widthpx = Math.ceil((double) this.nameLayout.getLineWidth(0));
                    if (widthpx < ((double) this.nameWidth)) {
                        this.nameLeft = (int) (((double) this.nameLeft) + (((double) this.nameWidth) - widthpx));
                    }
                }
                if (this.messageLayout.getLineCount() > 0 && this.messageLayout.getLineLeft(0) == 0.0f) {
                    widthpx = Math.ceil((double) this.messageLayout.getLineWidth(0));
                    if (widthpx < ((double) this.messageWidth)) {
                        this.messageLeft = (int) (((double) this.messageLeft) + (((double) this.messageWidth) - widthpx));
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
            if (this.messageLayout.getLineCount() > 0 && this.messageLayout.getLineRight(0) == ((float) this.messageWidth)) {
                widthpx = Math.ceil((double) this.messageLayout.getLineWidth(0));
                if (widthpx < ((double) this.messageWidth)) {
                    this.messageLeft = (int) (((double) this.messageLeft) - (((double) this.messageWidth) - widthpx));
                }
            }
        }
    }

    private void init() {
        if (namePaint == null) {
            namePaint = new TextPaint(1);
            namePaint.setTextSize((float) Utilities.dp(19));
            namePaint.setColor(-14540254);
            namePaint.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
        }
        if (nameEncryptedPaint == null) {
            nameEncryptedPaint = new TextPaint(1);
            nameEncryptedPaint.setTextSize((float) Utilities.dp(19));
            nameEncryptedPaint.setColor(-16734706);
            nameEncryptedPaint.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
        }
        if (nameUnknownPaint == null) {
            nameUnknownPaint = new TextPaint(1);
            nameUnknownPaint.setTextSize((float) Utilities.dp(19));
            nameUnknownPaint.setColor(-13537377);
            nameUnknownPaint.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
        }
        if (messagePaint == null) {
            messagePaint = new TextPaint(1);
            messagePaint.setTextSize((float) Utilities.dp(16));
            messagePaint.setColor(-8355712);
        }
        if (messagePrintingPaint == null) {
            messagePrintingPaint = new TextPaint(1);
            messagePrintingPaint.setTextSize((float) Utilities.dp(16));
            messagePrintingPaint.setColor(-13537377);
        }
        if (timePaint == null) {
            timePaint = new TextPaint(1);
            timePaint.setTextSize((float) Utilities.dp(14));
            timePaint.setColor(-6381922);
        }
        if (countPaint == null) {
            countPaint = new TextPaint(1);
            countPaint.setTextSize((float) Utilities.dp(13));
            countPaint.setColor(-1);
        }
        if (lockDrawable == null) {
            lockDrawable = getResources().getDrawable(C0419R.drawable.ic_lock_green);
        }
        if (checkDrawable == null) {
            checkDrawable = getResources().getDrawable(C0419R.drawable.dialogs_check);
        }
        if (halfCheckDrawable == null) {
            halfCheckDrawable = getResources().getDrawable(C0419R.drawable.dialogs_halfcheck);
        }
        if (clockDrawable == null) {
            clockDrawable = getResources().getDrawable(C0419R.drawable.msg_clock);
        }
        if (errorDrawable == null) {
            errorDrawable = getResources().getDrawable(C0419R.drawable.dialogs_warning);
        }
        if (countDrawable == null) {
            countDrawable = getResources().getDrawable(C0419R.drawable.dialogs_badge);
        }
        if (this.avatarImage == null) {
            this.avatarImage = new ImageReceiver();
            this.avatarImage.parentView = new WeakReference(this);
        }
        if (this.cellLayout == null) {
            this.cellLayout = new DialogCellLayout();
        }
    }

    public DialogCell(Context context) {
        super(context);
        init();
    }

    public DialogCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialogCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDialog(TL_dialog dialog) {
        this.currentDialog = dialog;
        update(0);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Utilities.dp(70));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.currentDialog == null) {
            super.onLayout(changed, left, top, right, bottom);
        } else if (changed) {
            buildLayout();
        }
    }

    public void buildLayout() {
        this.cellLayout.build(getMeasuredWidth(), getMeasuredHeight());
    }

    public void update(int mask) {
        if (mask != 0) {
            boolean continueUpdate = false;
            if ((mask & 64) != 0) {
                CharSequence printString = (CharSequence) MessagesController.Instance.printingStrings.get(Long.valueOf(this.currentDialog.id));
                if ((this.lastPrintString != null && printString == null) || ((this.lastPrintString == null && printString != null) || !(this.lastPrintString == null || printString == null || this.lastPrintString.equals(printString)))) {
                    continueUpdate = true;
                }
            }
            if ((mask & 2) != 0 && this.chat == null) {
                continueUpdate = true;
            }
            if ((mask & 1) != 0 && this.chat == null) {
                continueUpdate = true;
            }
            if ((mask & 8) != 0 && this.user == null) {
                continueUpdate = true;
            }
            if ((mask & 16) != 0 && this.user == null) {
                continueUpdate = true;
            }
            if (!continueUpdate) {
                return;
            }
        }
        this.user = null;
        this.chat = null;
        this.encryptedChat = null;
        int lower_id = (int) this.currentDialog.id;
        if (lower_id == 0) {
            this.encryptedChat = (EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf((int) (this.currentDialog.id >> 32)));
            if (this.encryptedChat != null) {
                this.user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.encryptedChat.user_id));
            }
        } else if (lower_id < 0) {
            this.chat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(-lower_id));
        } else {
            this.user = (User) MessagesController.Instance.users.get(Integer.valueOf(lower_id));
        }
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
        this.avatarImage.setImage(photo, "50_50", placeHolderId == 0 ? null : getResources().getDrawable(placeHolderId));
        if (getMeasuredWidth() == 0 && getMeasuredHeight() == 0) {
            requestLayout();
        } else {
            buildLayout();
        }
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (this.currentDialog != null) {
            if (this.cellLayout == null) {
                requestLayout();
                return;
            }
            if (this.cellLayout.drawNameLock) {
                setDrawableBounds(lockDrawable, this.cellLayout.nameLockLeft, this.cellLayout.nameLockTop);
                lockDrawable.draw(canvas);
            }
            canvas.save();
            canvas.translate((float) this.cellLayout.nameLeft, (float) this.cellLayout.nameTop);
            this.cellLayout.nameLayout.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate((float) this.cellLayout.timeLeft, (float) this.cellLayout.timeTop);
            this.cellLayout.timeLayout.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate((float) this.cellLayout.messageLeft, (float) this.cellLayout.messageTop);
            this.cellLayout.messageLayout.draw(canvas);
            canvas.restore();
            if (this.cellLayout.drawClock) {
                setDrawableBounds(clockDrawable, this.cellLayout.checkDrawLeft, this.cellLayout.checkDrawTop);
                clockDrawable.draw(canvas);
            } else if (this.cellLayout.drawCheck2) {
                if (this.cellLayout.drawCheck1) {
                    setDrawableBounds(halfCheckDrawable, this.cellLayout.halfCheckDrawLeft, this.cellLayout.checkDrawTop);
                    halfCheckDrawable.draw(canvas);
                    setDrawableBounds(checkDrawable, this.cellLayout.checkDrawLeft, this.cellLayout.checkDrawTop);
                    checkDrawable.draw(canvas);
                } else {
                    setDrawableBounds(checkDrawable, this.cellLayout.checkDrawLeft, this.cellLayout.checkDrawTop);
                    checkDrawable.draw(canvas);
                }
            }
            if (this.cellLayout.drawError) {
                setDrawableBounds(errorDrawable, this.cellLayout.errorLeft, this.cellLayout.errorTop);
                errorDrawable.draw(canvas);
            } else if (this.cellLayout.drawCount) {
                setDrawableBounds(countDrawable, this.cellLayout.countLeft - Utilities.dp(5), this.cellLayout.countTop, Utilities.dp(10) + this.cellLayout.countWidth, countDrawable.getIntrinsicHeight());
                countDrawable.draw(canvas);
                canvas.save();
                canvas.translate((float) this.cellLayout.countLeft, (float) (this.cellLayout.countTop + Utilities.dp(3)));
                this.cellLayout.countLayout.draw(canvas);
                canvas.restore();
            }
            this.avatarImage.draw(canvas, this.cellLayout.avatarLeft, this.cellLayout.avatarTop, Utilities.dp(54), Utilities.dp(54));
        }
    }
}
