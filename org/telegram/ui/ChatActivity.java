package org.telegram.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Video.Media;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.google.android.gms.plus.PlusShare;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.TL.TLRPC.Chat;
import org.telegram.TL.TLRPC.ChatParticipants;
import org.telegram.TL.TLRPC.Document;
import org.telegram.TL.TLRPC.EncryptedChat;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.Message;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_chatForbidden;
import org.telegram.TL.TLRPC.TL_chatParticipant;
import org.telegram.TL.TLRPC.TL_chatParticipantsForbidden;
import org.telegram.TL.TLRPC.TL_document;
import org.telegram.TL.TLRPC.TL_documentEncrypted;
import org.telegram.TL.TLRPC.TL_encryptedChat;
import org.telegram.TL.TLRPC.TL_encryptedChatDiscarded;
import org.telegram.TL.TLRPC.TL_encryptedChatRequested;
import org.telegram.TL.TLRPC.TL_encryptedChatWaiting;
import org.telegram.TL.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.TL.TLRPC.TL_messageForwarded;
import org.telegram.TL.TLRPC.TL_messageMediaEmpty;
import org.telegram.TL.TLRPC.TL_photo;
import org.telegram.TL.TLRPC.TL_photoCachedSize;
import org.telegram.TL.TLRPC.TL_photoSize;
import org.telegram.TL.TLRPC.TL_photoSizeEmpty;
import org.telegram.TL.TLRPC.TL_video;
import org.telegram.TL.TLRPC.User;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.objects.MessageObject;
import org.telegram.objects.PhotoObject;
import org.telegram.ui.DocumentSelectActivity.DocumentSelectActivityDelegate;
import org.telegram.ui.MessagesActivity.MessagesActivityDelegate;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.BaseFragment;
import org.telegram.ui.Views.EmojiView;
import org.telegram.ui.Views.EmojiView.Listener;
import org.telegram.ui.Views.LayoutListView;
import org.telegram.ui.Views.MessageActionLayout;
import org.telegram.ui.Views.MessageLayout;
import org.telegram.ui.Views.OnSwipeTouchListener;
import org.telegram.ui.Views.SizeNotifierRelativeLayout;
import org.telegram.ui.Views.SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate;

public class ChatActivity extends BaseFragment implements SizeNotifierRelativeLayoutDelegate, NotificationCenterDelegate, MessagesActivityDelegate, DocumentSelectActivityDelegate {
    private BackupImageView avatarImageView;
    private View bottomOverlay;
    private TextView bottomOverlayText;
    private boolean cacheEndReaced = false;
    private ChatAdapter chatAdapter;
    private LayoutListView chatListView;
    private View contentView;
    private Chat currentChat;
    private EncryptedChat currentEncryptedChat;
    private String currentPicturePath;
    private User currentUser;
    private long dialog_id;
    private Point displaySize = new Point();
    private ImageView emojiButton;
    private PopupWindow emojiPopup;
    private EmojiView emojiView;
    private TextView emptyView;
    private boolean endReached = false;
    boolean first = true;
    private int first_unread_id = 0;
    private int fontSize = 16;
    private MessageObject forwaringMessage;
    private boolean ignoreTextChange = false;
    private ChatParticipants info = null;
    private boolean invalidateAfterAnimation = false;
    private boolean isCustomTheme = false;
    private int keyboardHeight = 0;
    private int keyboardHeightLand = 0;
    private boolean keyboardVisible;
    private CharSequence lastPrintString;
    private long lastTypingTimeSend = 0;
    private int last_unread_id = 0;
    private boolean loading = false;
    private HashMap<String, ArrayList<ProgressBar>> loadingFile = new HashMap();
    private boolean loadingForward = false;
    ActionMode mActionMode = null;
    private Callback mActionModeCallback = new C08561();
    private final Rect mLastTouch = new Rect();
    private int maxDate = ExploreByTouchHelper.INVALID_ID;
    private int maxMessageId = Integer.MAX_VALUE;
    private ArrayList<MessageObject> messages = new ArrayList();
    private HashMap<String, ArrayList<MessageObject>> messagesByDays = new HashMap();
    private HashMap<Integer, MessageObject> messagesDict = new HashMap();
    private EditText messsageEditText;
    private int minDate = 0;
    private int minMessageId = ExploreByTouchHelper.INVALID_ID;
    private int onlineCount = -1;
    private View pagedownButton;
    private boolean paused = true;
    private HashMap<String, ProgressBar> progressBarMap = new HashMap();
    private HashMap<Integer, String> progressByTag = new HashMap();
    private int progressTag = 0;
    private View progressView;
    private boolean readWhenResume = false;
    private int readWithDate = 0;
    private int readWithMid = 0;
    public boolean scrollToTopOnResume = false;
    private boolean scrollToTopUnReadOnResume = false;
    private View secretChatPlaceholder;
    private TextView secretViewStatusTextView;
    private HashMap<Integer, MessageObject> selectedMessagesCanCopyIds = new HashMap();
    private HashMap<Integer, MessageObject> selectedMessagesIds = new HashMap();
    private MessageObject selectedObject;
    private ImageButton sendButton;
    private boolean sendByEnter = false;
    private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    private boolean swipeOpening = false;
    private View topPanel;
    private TextView topPanelText;
    private ImageView topPlaneClose;
    private MessageObject unreadMessageObject = null;
    private boolean unread_end_reached = true;
    private int unread_to_load = 0;
    AlertDialog visibleDialog = null;

    class C04462 implements OnItemLongClickListener {
        C04462() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            ChatActivity.this.createMenu(view, false);
            return true;
        }
    }

    class C04473 implements OnScrollListener {
        C04473() {
        }

        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (visibleItemCount > 0) {
                if (!(firstVisibleItem > 4 || ChatActivity.this.endReached || ChatActivity.this.loading)) {
                    if (ChatActivity.this.messagesByDays.size() != 0) {
                        MessagesController.Instance.loadMessages(ChatActivity.this.dialog_id, 0, 20, ChatActivity.this.maxMessageId, !ChatActivity.this.cacheEndReaced, ChatActivity.this.minDate, ChatActivity.this.classGuid, false, false);
                    } else {
                        MessagesController.Instance.loadMessages(ChatActivity.this.dialog_id, 0, 20, 0, !ChatActivity.this.cacheEndReaced, ChatActivity.this.minDate, ChatActivity.this.classGuid, false, false);
                    }
                    ChatActivity.this.loading = true;
                }
                if (!(firstVisibleItem + visibleItemCount < totalItemCount - 6 || ChatActivity.this.unread_end_reached || ChatActivity.this.loadingForward)) {
                    MessagesController.Instance.loadMessages(ChatActivity.this.dialog_id, 0, 20, ChatActivity.this.minMessageId, true, ChatActivity.this.maxDate, ChatActivity.this.classGuid, false, true);
                    ChatActivity.this.loadingForward = true;
                }
                if (firstVisibleItem + visibleItemCount == totalItemCount && ChatActivity.this.unread_end_reached) {
                    ChatActivity.this.showPagedownButton(false, true);
                    return;
                }
                return;
            }
            ChatActivity.this.showPagedownButton(false, false);
        }
    }

    class C04484 implements OnClickListener {
        C04484() {
        }

        public void onClick(View view) {
            boolean z = true;
            if (ChatActivity.this.emojiPopup == null) {
                ChatActivity.this.showEmojiPopup(true);
                return;
            }
            ChatActivity chatActivity = ChatActivity.this;
            if (ChatActivity.this.emojiPopup.isShowing()) {
                z = false;
            }
            chatActivity.showEmojiPopup(z);
        }
    }

    class C04495 implements OnKeyListener {
        C04495() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (i != 4 || ChatActivity.this.keyboardVisible || ChatActivity.this.emojiPopup == null || !ChatActivity.this.emojiPopup.isShowing()) {
                if (i != 66 || !ChatActivity.this.sendByEnter || keyEvent.getAction() != 0) {
                    return false;
                }
                ChatActivity.this.sendMessage();
                return true;
            } else if (keyEvent.getAction() != 1) {
                return true;
            } else {
                ChatActivity.this.showEmojiPopup(false);
                return true;
            }
        }
    }

    class C04506 implements OnEditorActionListener {
        C04506() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 4) {
                ChatActivity.this.sendMessage();
                return true;
            } else if (!ChatActivity.this.sendByEnter || keyEvent == null || i != 0 || keyEvent.getAction() != 0) {
                return false;
            } else {
                ChatActivity.this.sendMessage();
                return true;
            }
        }
    }

    class C04517 implements OnClickListener {
        C04517() {
        }

        public void onClick(View view) {
            ChatActivity.this.sendMessage();
        }
    }

    class C04528 implements OnClickListener {
        C04528() {
        }

        public void onClick(View view) {
            if (ChatActivity.this.unread_end_reached || ChatActivity.this.first_unread_id == 0) {
                ChatActivity.this.chatListView.setSelectionFromTop(ChatActivity.this.messages.size() - 1, -10000 - ChatActivity.this.chatListView.getPaddingTop());
                return;
            }
            ChatActivity.this.messages.clear();
            ChatActivity.this.messagesByDays.clear();
            ChatActivity.this.messagesDict.clear();
            ChatActivity.this.progressView.setVisibility(0);
            ChatActivity.this.chatListView.setEmptyView(null);
            ChatActivity.this.maxMessageId = Integer.MAX_VALUE;
            ChatActivity.this.minMessageId = ExploreByTouchHelper.INVALID_ID;
            ChatActivity.this.maxDate = ExploreByTouchHelper.INVALID_ID;
            ChatActivity.this.minDate = 0;
            MessagesController.Instance.loadMessages(ChatActivity.this.dialog_id, 0, 30, 0, true, 0, ChatActivity.this.classGuid, true, false);
            ChatActivity.this.loading = true;
            ChatActivity.this.chatAdapter.notifyDataSetChanged();
        }
    }

    class C04539 implements TextWatcher {
        C04539() {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String message = charSequence.toString().trim().replaceAll("\n\n+", "\n\n").replaceAll(" +", " ");
            ChatActivity.this.sendButton.setEnabled(message.length() != 0);
            if (message.length() != 0 && ChatActivity.this.lastTypingTimeSend < System.currentTimeMillis() - 5000 && !ChatActivity.this.ignoreTextChange) {
                int currentTime = ConnectionsManager.Instance.getCurrentTime();
                if (ChatActivity.this.currentUser == null || ChatActivity.this.currentUser.status == null || ChatActivity.this.currentUser.status.expires >= currentTime || ChatActivity.this.currentUser.status.was_online >= currentTime) {
                    ChatActivity.this.lastTypingTimeSend = System.currentTimeMillis();
                    MessagesController.Instance.sendTyping(ChatActivity.this.dialog_id, ChatActivity.this.classGuid);
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            if (ChatActivity.this.sendByEnter && editable.length() > 0 && editable.charAt(editable.length() - 1) == '\n') {
                ChatActivity.this.sendMessage();
            }
            for (Object removeSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                editable.removeSpan(removeSpan);
            }
            Emoji.replaceEmoji(editable);
        }
    }

    private class ChatAdapter extends BaseAdapter {
        private Context mContext;

        public ChatAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return true;
        }

        public boolean isEnabled(int i) {
            return true;
        }

        public int getCount() {
            int count = ChatActivity.this.messages.size();
            if (count == 0) {
                return count;
            }
            if (!ChatActivity.this.endReached) {
                count++;
            }
            if (ChatActivity.this.unread_end_reached) {
                return count;
            }
            return count + 1;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return true;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int offset = 1;
            if (!((ChatActivity.this.endReached && ChatActivity.this.unread_end_reached) || ChatActivity.this.messages.size() == 0)) {
                if (!ChatActivity.this.endReached) {
                    offset = 0;
                }
                if ((i == 0 && !ChatActivity.this.endReached) || (!ChatActivity.this.unread_end_reached && i == (ChatActivity.this.messages.size() + 1) - offset)) {
                    if (view == null) {
                        view = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0419R.layout.chat_loading_layout, viewGroup, false);
                        View progressBar = view.findViewById(C0419R.id.progressLayout);
                        if (ChatActivity.this.isCustomTheme) {
                            progressBar.setBackgroundResource(C0419R.drawable.system_loader2);
                        } else {
                            progressBar.setBackgroundResource(C0419R.drawable.system_loader1);
                        }
                    }
                    return view;
                }
            }
            MessageObject message = (MessageObject) ChatActivity.this.messages.get((ChatActivity.this.messages.size() - i) - offset);
            int type = message.type;
            if (view == null) {
                LayoutInflater li = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
                if (type == 0) {
                    view = li.inflate(C0419R.layout.chat_outgoing_text_layout, viewGroup, false);
                } else if (type == 1) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_text_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_text_layout, viewGroup, false);
                    }
                } else if (type == 8) {
                    view = li.inflate(C0419R.layout.chat_outgoing_forward_layout, viewGroup, false);
                } else if (type == 9) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_forward_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_forward_layout, viewGroup, false);
                    }
                } else if (type == 4) {
                    view = li.inflate(C0419R.layout.chat_outgoing_location_layout, viewGroup, false);
                } else if (type == 5) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_location_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_location_layout, viewGroup, false);
                    }
                } else if (type == 2) {
                    view = li.inflate(C0419R.layout.chat_outgoing_photo_layout, viewGroup, false);
                } else if (type == 3) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_photo_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_photo_layout, viewGroup, false);
                    }
                } else if (type == 6) {
                    view = li.inflate(C0419R.layout.chat_outgoing_video_layout, viewGroup, false);
                } else if (type == 7) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_video_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_video_layout, viewGroup, false);
                    }
                } else if (type == 10) {
                    view = li.inflate(C0419R.layout.chat_action_message_layout, viewGroup, false);
                } else if (type == 11) {
                    view = li.inflate(C0419R.layout.chat_action_change_photo_layout, viewGroup, false);
                } else if (type == 12) {
                    view = li.inflate(C0419R.layout.chat_outgoing_contact_layout, viewGroup, false);
                } else if (type == 13) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_contact_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_contact_layout, viewGroup, false);
                    }
                } else if (type == 15) {
                    view = li.inflate(C0419R.layout.chat_unread_layout, viewGroup, false);
                } else if (type == 16) {
                    view = li.inflate(C0419R.layout.chat_outgoing_document_layout, viewGroup, false);
                } else if (type == 17) {
                    if (ChatActivity.this.currentChat != null) {
                        view = li.inflate(C0419R.layout.chat_group_incoming_document_layout, viewGroup, false);
                    } else {
                        view = li.inflate(C0419R.layout.chat_incoming_document_layout, viewGroup, false);
                    }
                }
            }
            ChatListRowHolderEx holder = (ChatListRowHolderEx) view.getTag();
            if (holder == null) {
                holder = new ChatListRowHolderEx(view, type);
                view.setTag(holder);
            }
            holder.message = message;
            boolean selected = false;
            boolean disableSelection = false;
            if (ChatActivity.this.mActionMode != null) {
                if (ChatActivity.this.selectedMessagesIds.containsKey(Integer.valueOf(holder.message.messageOwner.id))) {
                    view.setBackgroundColor(1714664933);
                    selected = true;
                } else {
                    view.setBackgroundColor(0);
                }
                disableSelection = true;
            } else {
                view.setBackgroundColor(0);
            }
            int messageType = holder.message.type;
            if (disableSelection) {
                if (messageType == 2 || messageType == 4 || messageType == 6) {
                    if (selected) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_photo_selected);
                    } else {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_photo);
                    }
                } else if (messageType == 3 || messageType == 5 || messageType == 7) {
                    if (selected) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_photo_selected);
                    } else {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_photo);
                    }
                } else if (messageType == 0 || messageType == 8) {
                    if (selected) {
                        holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_out_selected);
                    } else {
                        holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_out);
                    }
                    holder.messageLayout.setPadding(Utilities.dp(11), Utilities.dp(7), Utilities.dp(18), 0);
                } else if (messageType == 1 || messageType == 9) {
                    if (selected) {
                        holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_in_selected);
                    } else {
                        holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_in);
                    }
                    holder.messageLayout.setPadding(Utilities.dp(19), Utilities.dp(7), Utilities.dp(9), 0);
                } else if (messageType == 12) {
                    if (selected) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_selected);
                    } else {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out);
                    }
                    holder.chatBubbleView.setPadding(Utilities.dp(6), Utilities.dp(6), Utilities.dp(18), 0);
                } else if (messageType == 13) {
                    if (selected) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_selected);
                    } else {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in);
                    }
                    holder.chatBubbleView.setPadding(Utilities.dp(15), Utilities.dp(6), Utilities.dp(9), 0);
                } else if (messageType == 16) {
                    if (selected) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_selected);
                    } else {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out);
                    }
                    holder.chatBubbleView.setPadding(Utilities.dp(9), Utilities.dp(9), Utilities.dp(18), 0);
                } else if (messageType == 17) {
                    if (selected) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_selected);
                    } else {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in);
                    }
                    holder.chatBubbleView.setPadding(Utilities.dp(18), Utilities.dp(9), Utilities.dp(9), 0);
                }
            } else if (messageType == 2 || messageType == 4 || messageType == 6) {
                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_outgoing_photo_states);
            } else if (messageType == 3 || messageType == 5 || messageType == 7) {
                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_incoming_photo_states);
            } else if (messageType == 0 || messageType == 8) {
                holder.messageLayout.setBackgroundResource(C0419R.drawable.chat_outgoing_text_states);
                holder.messageLayout.setPadding(Utilities.dp(11), Utilities.dp(7), Utilities.dp(18), 0);
            } else if (messageType == 1 || messageType == 9) {
                holder.messageLayout.setBackgroundResource(C0419R.drawable.chat_incoming_text_states);
                holder.messageLayout.setPadding(Utilities.dp(19), Utilities.dp(7), Utilities.dp(9), 0);
            } else if (messageType == 12) {
                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_outgoing_text_states);
                holder.chatBubbleView.setPadding(Utilities.dp(6), Utilities.dp(6), Utilities.dp(18), 0);
            } else if (messageType == 13) {
                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_incoming_text_states);
                holder.chatBubbleView.setPadding(Utilities.dp(15), Utilities.dp(6), Utilities.dp(9), 0);
            } else if (messageType == 16) {
                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_outgoing_text_states);
                holder.chatBubbleView.setPadding(Utilities.dp(9), Utilities.dp(9), Utilities.dp(18), 0);
            } else if (messageType == 17) {
                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_incoming_text_states);
                holder.chatBubbleView.setPadding(Utilities.dp(18), Utilities.dp(9), Utilities.dp(9), 0);
            }
            holder.update();
            return view;
        }

        public int getItemViewType(int i) {
            int offset = 1;
            if (!(ChatActivity.this.endReached || ChatActivity.this.messages.size() == 0)) {
                offset = 0;
                if (i == 0) {
                    return 14;
                }
            }
            if (ChatActivity.this.unread_end_reached || i != (ChatActivity.this.messages.size() + 1) - offset) {
                return ((MessageObject) ChatActivity.this.messages.get((ChatActivity.this.messages.size() - i) - offset)).type;
            }
            return 14;
        }

        public int getViewTypeCount() {
            return 18;
        }

        public boolean isEmpty() {
            int count = ChatActivity.this.messages.size();
            if (count != 0) {
                if (!ChatActivity.this.endReached) {
                    count++;
                }
                if (!ChatActivity.this.unread_end_reached) {
                    count++;
                }
            }
            return count == 0;
        }
    }

    public class ChatListRowHolderEx {
        public TextView actionAttachButton;
        public ImageView actionCancelButton;
        public ProgressBar actionProgress;
        public View actionView;
        public ImageView addContactButton;
        public View addContactView;
        public BackupImageView avatarImageView;
        public View chatBubbleView;
        public ImageView checkImage;
        public BackupImageView contactAvatar;
        public View contactView;
        public TextView foewardedUserName;
        public TextView forwardedUserText;
        public ImageView halfCheckImage;
        public MessageObject message;
        public MessageLayout messageLayout;
        public MessageActionLayout messageLayoutAction;
        public TextView messageTextView;
        public TextView nameTextView;
        public TextView phoneTextView;
        public BackupImageView photoImage;
        public View photoProgressView;
        public TextView timeTextView;
        public TextView videoTimeText;

        public void update() {
            int width;
            FileLocation photo;
            String fileName;
            Float progress;
            User fromUser = (User) MessagesController.Instance.users.get(Integer.valueOf(this.message.messageOwner.from_id));
            int type = this.message.type;
            if (type == 0 || type == 1 || type == 8 || type == 9) {
                if (ChatActivity.this.currentChat == null || !(type == 1 || type == 9)) {
                    width = ChatActivity.this.displaySize.x - Utilities.dp(80);
                } else {
                    width = ChatActivity.this.displaySize.x - Utilities.dp(122);
                }
                this.messageLayout.maxWidth = width;
                this.messageLayout.messageTextView.setText(this.message.messageText);
                this.messageLayout.messageTextView.setMaxWidth(width);
            }
            if (this.timeTextView != null) {
                this.timeTextView.setText(Utilities.formatterDay.format(((long) this.message.messageOwner.date) * 1000));
            }
            if (!(this.avatarImageView == null || fromUser == null)) {
                photo = null;
                if (fromUser.photo != null) {
                    photo = fromUser.photo.photo_small;
                }
                this.avatarImageView.setImage(photo, "50_50", Utilities.getUserAvatarForId(fromUser.id));
            }
            if (!(type == 12 || type == 13 || this.nameTextView == null || fromUser == null || type == 16 || type == 17)) {
                this.nameTextView.setText(Utilities.formatName(fromUser.first_name, fromUser.last_name));
                this.nameTextView.setTextColor(Utilities.getColorForId(this.message.messageOwner.from_id));
            }
            if (type == 8 || type == 9) {
                User fwdUser = (User) MessagesController.Instance.users.get(Integer.valueOf(this.message.messageOwner.fwd_from_id));
                if (fwdUser != null) {
                    this.forwardedUserText.setText(Html.fromHtml(ChatActivity.this.getStringEntry(C0419R.string.From) + " <b>" + Utilities.formatName(fwdUser.first_name, fwdUser.last_name) + "</b>"));
                }
            } else if (type == 2 || type == 3 || type == 6 || type == 7) {
                width = (int) (((float) Math.min(ChatActivity.this.displaySize.x, ChatActivity.this.displaySize.y)) * 0.7f);
                int height = width + Utilities.dp(100);
                if (type == 6 || type == 7) {
                    width = (int) (((float) Math.min(ChatActivity.this.displaySize.x, ChatActivity.this.displaySize.y)) / 2.5f);
                    height = width + 100;
                }
                photo = PhotoObject.getClosestImageWithSize(this.message.photoThumbs, width, height);
                if (type == 3 && this.photoProgressView != null) {
                    this.photoProgressView.setVisibility(8);
                }
                if (photo != null) {
                    float scale = ((float) photo.photoOwner.f60w) / ((float) width);
                    int w = (int) (((float) photo.photoOwner.f60w) / scale);
                    int h = (int) (((float) photo.photoOwner.f59h) / scale);
                    if (h > height) {
                        h = height;
                        w = (int) (((float) w) / (((float) h) / ((float) h)));
                    } else if (h < Utilities.dp(120)) {
                        h = Utilities.dp(120);
                        float hScale = ((float) photo.photoOwner.f59h) / ((float) h);
                        if (((float) photo.photoOwner.f60w) / hScale < ((float) width)) {
                            w = (int) (((float) photo.photoOwner.f60w) / hScale);
                        }
                    }
                    LayoutParams params = (FrameLayout.LayoutParams) this.photoImage.getLayoutParams();
                    params.width = w;
                    params.height = h;
                    this.photoImage.setLayoutParams(params);
                    LayoutParams params2 = (LinearLayout.LayoutParams) this.chatBubbleView.getLayoutParams();
                    params2.width = Utilities.dp(12) + w;
                    params2.height = Utilities.dp(12) + h;
                    this.chatBubbleView.setLayoutParams(params2);
                    if (photo.image != null) {
                        this.photoImage.setImageBitmap(photo.image);
                    } else if (this.message.imagePreview != null) {
                        this.photoImage.setImage(photo.photoOwner.location, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) w) / Utilities.density)), Integer.valueOf((int) (((float) h) / Utilities.density))}), this.message.imagePreview);
                    } else {
                        this.photoImage.setImage(photo.photoOwner.location, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) w) / Utilities.density)), Integer.valueOf((int) (((float) h) / Utilities.density))}), this.message.messageOwner.out ? C0419R.drawable.photo_placeholder_out : C0419R.drawable.photo_placeholder_in);
                    }
                }
                if ((type == 6 || type == 7) && this.videoTimeText != null) {
                    int duration = this.message.messageOwner.media.video.duration;
                    int seconds = duration - ((duration / 60) * 60);
                    this.videoTimeText.setText(String.format("%d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}));
                }
            } else if (type == 4 || type == 5) {
                double lat = this.message.messageOwner.media.geo.lat;
                double lon = this.message.messageOwner.media.geo._long;
                this.photoImage.setImage(String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=13&size=100x100&maptype=roadmap&scale=%d&markers=color:red|size:big|%f,%f&sensor=false", new Object[]{Double.valueOf(lat), Double.valueOf(lon), Integer.valueOf(Math.min(2, (int) Math.ceil((double) Utilities.density))), Double.valueOf(lat), Double.valueOf(lon)}), null, this.message.messageOwner.out ? C0419R.drawable.photo_placeholder_out : C0419R.drawable.photo_placeholder_in);
            } else if (type == 11 || type == 10) {
                width = ChatActivity.this.displaySize.x - Utilities.dp(30);
                this.messageTextView.setText(this.message.messageText);
                this.messageTextView.setMaxWidth(width);
                if (type == 11) {
                    if (this.message.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                        this.photoImage.setImage(this.message.messageOwner.action.newUserPhoto.photo_small, "50_50", Utilities.getUserAvatarForId(ChatActivity.this.currentUser.id));
                    } else {
                        photo = PhotoObject.getClosestImageWithSize(this.message.photoThumbs, Utilities.dp(64), Utilities.dp(64));
                        if (photo.image != null) {
                            this.photoImage.setImageBitmap(photo.image);
                        } else {
                            this.photoImage.setImage(photo.photoOwner.location, "50_50", Utilities.getGroupAvatarForId(ChatActivity.this.currentChat.id));
                        }
                    }
                }
            } else if (type == 12 || type == 13) {
                User contactUser = (User) MessagesController.Instance.users.get(Integer.valueOf(this.message.messageOwner.media.user_id));
                String phone;
                if (contactUser != null) {
                    this.nameTextView.setText(Utilities.formatName(this.message.messageOwner.media.first_name, this.message.messageOwner.media.last_name));
                    this.nameTextView.setTextColor(Utilities.getColorForId(contactUser.id));
                    phone = this.message.messageOwner.media.phone_number;
                    if (phone == null || phone.length() == 0) {
                        this.phoneTextView.setText("Unknown");
                    } else {
                        if (!phone.startsWith("+")) {
                            phone = "+" + phone;
                        }
                        this.phoneTextView.setText(PhoneFormat.Instance.format(phone));
                    }
                    photo = null;
                    if (contactUser.photo != null) {
                        photo = contactUser.photo.photo_small;
                    }
                    this.contactAvatar.setImage(photo, "50_50", Utilities.getUserAvatarForId(contactUser.id));
                    if (contactUser.id == UserConfig.clientUserId || MessagesController.Instance.contactsDict.get(contactUser.id) != null) {
                        this.addContactView.setVisibility(8);
                    } else {
                        this.addContactView.setVisibility(0);
                    }
                } else {
                    this.nameTextView.setText(Utilities.formatName(this.message.messageOwner.media.first_name, this.message.messageOwner.media.last_name));
                    this.nameTextView.setTextColor(Utilities.getColorForId(this.message.messageOwner.media.user_id));
                    phone = this.message.messageOwner.media.phone_number;
                    if (phone == null || phone.length() == 0) {
                        this.phoneTextView.setText("Unknown");
                    } else {
                        if (!phone.startsWith("+")) {
                            phone = "+" + phone;
                        }
                        this.phoneTextView.setText(PhoneFormat.Instance.format(phone));
                    }
                    this.contactAvatar.setImageResource(Utilities.getUserAvatarForId(this.message.messageOwner.media.user_id));
                    this.addContactView.setVisibility(8);
                }
            } else if (type == 15) {
                if (ChatActivity.this.unread_to_load == 1) {
                    this.messageTextView.setText(String.format(ChatActivity.this.getStringEntry(C0419R.string.OneNewMessage), new Object[]{Integer.valueOf(ChatActivity.this.unread_to_load)}));
                } else {
                    this.messageTextView.setText(String.format(ChatActivity.this.getStringEntry(C0419R.string.FewNewMessages), new Object[]{Integer.valueOf(ChatActivity.this.unread_to_load)}));
                }
            } else if (type == 16 || type == 17) {
                Document document = this.message.messageOwner.media.document;
                if ((document instanceof TL_document) || (document instanceof TL_documentEncrypted)) {
                    this.nameTextView.setText(this.message.messageOwner.media.document.file_name);
                    fileName = this.message.getFileName();
                    int idx = fileName.lastIndexOf(".");
                    String ext = null;
                    if (idx != -1) {
                        ext = fileName.substring(idx + 1);
                    }
                    if (ext == null || ext.length() == 0) {
                        ext = this.message.messageOwner.media.document.mime_type;
                    }
                    ext = ext.toUpperCase();
                    if (document.size < 1024) {
                        this.phoneTextView.setText(String.format("%d B %s", new Object[]{Integer.valueOf(document.size), ext}));
                    } else if (document.size < 1048576) {
                        this.phoneTextView.setText(String.format("%.1f KB %s", new Object[]{Float.valueOf(((float) document.size) / 1024.0f), ext}));
                    } else {
                        this.phoneTextView.setText(String.format("%.1f MB %s", new Object[]{Float.valueOf((((float) document.size) / 1024.0f) / 1024.0f), ext}));
                    }
                    if (!((document.thumb instanceof TL_photoSize) || (document.thumb instanceof TL_photoCachedSize))) {
                        if (type == 16) {
                            this.contactAvatar.setImageResource(C0419R.drawable.doc_green);
                        } else {
                            this.contactAvatar.setImageResource(C0419R.drawable.doc_blue);
                        }
                    }
                } else {
                    this.nameTextView.setText("Error");
                    this.phoneTextView.setText("Error");
                    if (type == 16) {
                        this.contactAvatar.setImageResource(C0419R.drawable.doc_green);
                    } else {
                        this.contactAvatar.setImageResource(C0419R.drawable.doc_blue);
                    }
                }
            }
            if (this.message.messageOwner.id < 0 && this.message.messageOwner.send_state != 0 && MessagesController.Instance.sendingMessages.get(this.message.messageOwner.id) == null) {
                this.message.messageOwner.send_state = 2;
            }
            if (this.message.messageOwner.from_id == UserConfig.clientUserId && this.halfCheckImage != null) {
                if (this.message.messageOwner.send_state == 1) {
                    this.checkImage.setVisibility(4);
                    if (type == 2 || type == 6 || type == 4) {
                        this.halfCheckImage.setImageResource(C0419R.drawable.msg_clock_photo);
                    } else {
                        this.halfCheckImage.setImageResource(C0419R.drawable.msg_clock);
                    }
                    this.halfCheckImage.setVisibility(0);
                    if (!(this.actionView == null && this.photoProgressView == null)) {
                        if (this.actionView != null) {
                            this.actionView.setVisibility(0);
                        }
                        if (this.photoProgressView != null) {
                            this.photoProgressView.setVisibility(0);
                        }
                        progress = (Float) FileLoader.Instance.fileProgresses.get(this.message.messageOwner.attachPath);
                        if (progress != null) {
                            this.actionProgress.setProgress((int) (progress.floatValue() * 100.0f));
                        } else {
                            this.actionProgress.setProgress(0);
                        }
                        ChatActivity.this.progressByTag.put((Integer) this.actionProgress.getTag(), this.message.messageOwner.attachPath);
                        ChatActivity.this.progressBarMap.put(this.message.messageOwner.attachPath, this.actionProgress);
                    }
                    if (this.actionAttachButton != null) {
                        this.actionAttachButton.setVisibility(8);
                    }
                } else if (this.message.messageOwner.send_state == 2) {
                    this.halfCheckImage.setVisibility(0);
                    this.halfCheckImage.setImageResource(C0419R.drawable.msg_warning);
                    if (this.checkImage != null) {
                        this.checkImage.setVisibility(4);
                    }
                    if (this.actionView != null) {
                        this.actionView.setVisibility(8);
                    }
                    if (this.photoProgressView != null) {
                        this.photoProgressView.setVisibility(8);
                    }
                    if (this.actionAttachButton != null) {
                        this.actionAttachButton.setVisibility(8);
                    }
                } else if (this.message.messageOwner.send_state == 0) {
                    if (this.message.messageOwner.unread) {
                        this.halfCheckImage.setVisibility(0);
                        this.checkImage.setVisibility(4);
                        if (type == 2 || type == 6 || type == 4) {
                            this.halfCheckImage.setImageResource(C0419R.drawable.msg_check_w);
                        } else {
                            this.halfCheckImage.setImageResource(C0419R.drawable.msg_check);
                        }
                    } else {
                        this.halfCheckImage.setVisibility(0);
                        this.checkImage.setVisibility(0);
                        if (type == 2 || type == 6 || type == 4) {
                            this.halfCheckImage.setImageResource(C0419R.drawable.msg_halfcheck_w);
                        } else {
                            this.halfCheckImage.setImageResource(C0419R.drawable.msg_halfcheck);
                        }
                    }
                    if (this.actionView != null) {
                        this.actionView.setVisibility(8);
                    }
                    if (this.photoProgressView != null) {
                        this.photoProgressView.setVisibility(8);
                    }
                    if (this.actionAttachButton != null) {
                        this.actionAttachButton.setVisibility(0);
                    }
                }
            }
            if (this.message.type == 6 || this.message.type == 7 || this.message.type == 16 || this.message.type == 17) {
                String file = (String) ChatActivity.this.progressByTag.get((Integer) this.actionProgress.getTag());
                if (file != null) {
                    ChatActivity.this.removeFromloadingFile(file, this.actionProgress);
                }
                if (!(this.message.messageOwner.send_state == 1 || this.message.messageOwner.send_state == 2)) {
                    if (file != null) {
                        ChatActivity.this.progressBarMap.remove(file);
                    }
                    fileName = this.message.getFileName();
                    boolean load = false;
                    if (this.message.messageOwner.attachPath == null || this.message.messageOwner.attachPath.length() == 0) {
                        if (new File(Utilities.getCacheDir(), fileName).exists()) {
                            this.actionAttachButton.setVisibility(0);
                            this.actionView.setVisibility(8);
                            if (this.message.type == 6 || this.message.type == 7) {
                                this.actionAttachButton.setText(ChatActivity.this.getStringEntry(C0419R.string.ViewVideo));
                            } else if (this.message.type == 16 || this.message.type == 17) {
                                this.actionAttachButton.setText(ChatActivity.this.getStringEntry(C0419R.string.Open));
                            }
                        } else {
                            load = true;
                        }
                    } else if (new File(this.message.messageOwner.attachPath).exists()) {
                        this.actionAttachButton.setVisibility(0);
                        this.actionView.setVisibility(8);
                        if (this.message.type == 6 || this.message.type == 7) {
                            this.actionAttachButton.setText(ChatActivity.this.getStringEntry(C0419R.string.ViewVideo));
                        } else if (this.message.type == 16 || this.message.type == 17) {
                            this.actionAttachButton.setText(ChatActivity.this.getStringEntry(C0419R.string.Open));
                        }
                    } else {
                        load = true;
                    }
                    if (load) {
                        progress = (Float) FileLoader.Instance.fileProgresses.get(fileName);
                        if (ChatActivity.this.loadingFile.containsKey(fileName) || progress != null) {
                            if (progress != null) {
                                this.actionProgress.setProgress((int) (progress.floatValue() * 100.0f));
                            } else {
                                this.actionProgress.setProgress(0);
                            }
                            ChatActivity.this.progressByTag.put((Integer) this.actionProgress.getTag(), fileName);
                            ChatActivity.this.addToLoadingFile(fileName, this.actionProgress);
                            this.actionView.setVisibility(0);
                            this.actionAttachButton.setVisibility(8);
                        } else {
                            this.actionView.setVisibility(8);
                            this.actionAttachButton.setVisibility(0);
                            if (this.message.type == 6 || this.message.type == 7) {
                                this.actionAttachButton.setText(String.format("%s %.1f MB", new Object[]{ChatActivity.this.getStringEntry(C0419R.string.DOWNLOAD), Float.valueOf((((float) this.message.messageOwner.media.video.size) / 1024.0f) / 1024.0f)}));
                            } else if (this.message.type == 16 || this.message.type == 17) {
                                this.actionAttachButton.setText(ChatActivity.this.getStringEntry(C0419R.string.DOWNLOAD));
                            }
                        }
                    }
                }
            }
            if (this.message.type == 16 || this.message.type == 17) {
                if (ChatActivity.this.currentChat == null || type == 16) {
                    if (this.actionView.getVisibility() == 0) {
                        width = ChatActivity.this.displaySize.x - Utilities.dp(240);
                    } else {
                        width = ChatActivity.this.displaySize.x - Utilities.dp(220);
                    }
                } else if (this.actionView.getVisibility() == 0) {
                    width = ChatActivity.this.displaySize.x - Utilities.dp(290);
                } else {
                    width = ChatActivity.this.displaySize.x - Utilities.dp(270);
                }
                this.nameTextView.setMaxWidth(width);
                this.phoneTextView.setMaxWidth(width);
            }
        }

        public ChatListRowHolderEx(View view, int type) {
            this.avatarImageView = (BackupImageView) view.findViewById(C0419R.id.chat_group_avatar_image);
            this.nameTextView = (TextView) view.findViewById(C0419R.id.chat_user_group_name);
            this.messageLayout = (MessageLayout) view.findViewById(C0419R.id.message_layout);
            this.messageLayoutAction = (MessageActionLayout) view.findViewById(C0419R.id.message_action_layout);
            this.forwardedUserText = (TextView) view.findViewById(C0419R.id.chat_text_forward_name);
            this.foewardedUserName = (TextView) view.findViewById(C0419R.id.chat_text_forward_text);
            this.timeTextView = (TextView) view.findViewById(C0419R.id.chat_time_text);
            this.photoImage = (BackupImageView) view.findViewById(C0419R.id.chat_photo_image);
            this.halfCheckImage = (ImageView) view.findViewById(C0419R.id.chat_row_halfcheck);
            this.checkImage = (ImageView) view.findViewById(C0419R.id.chat_row_check);
            this.actionAttachButton = (TextView) view.findViewById(C0419R.id.chat_view_action_button);
            this.messageTextView = (TextView) view.findViewById(C0419R.id.chat_message_text);
            this.videoTimeText = (TextView) view.findViewById(C0419R.id.chat_video_time);
            this.actionView = view.findViewById(C0419R.id.chat_view_action_layout);
            this.actionProgress = (ProgressBar) view.findViewById(C0419R.id.chat_view_action_progress);
            this.actionCancelButton = (ImageView) view.findViewById(C0419R.id.chat_view_action_cancel_button);
            this.phoneTextView = (TextView) view.findViewById(C0419R.id.phone_text_view);
            this.contactAvatar = (BackupImageView) view.findViewById(C0419R.id.contact_avatar);
            this.contactView = view.findViewById(C0419R.id.shared_layout);
            this.addContactButton = (ImageView) view.findViewById(C0419R.id.add_contact_button);
            this.addContactView = view.findViewById(C0419R.id.add_contact_view);
            this.chatBubbleView = view.findViewById(C0419R.id.chat_bubble_layout);
            this.photoProgressView = view.findViewById(C0419R.id.photo_progress);
            if (this.messageTextView != null) {
                this.messageTextView.setTextSize(2, (float) ChatActivity.this.fontSize);
            }
            if (this.actionProgress != null) {
                this.actionProgress.setTag(Integer.valueOf(ChatActivity.this.progressTag));
                ChatActivity.this.progressTag = ChatActivity.this.progressTag + 1;
            }
            if (!(type == 2 || type == 3 || this.actionView == null)) {
                if (ChatActivity.this.isCustomTheme) {
                    this.actionView.setBackgroundResource(C0419R.drawable.system_black);
                } else {
                    this.actionView.setBackgroundResource(C0419R.drawable.system_blue);
                }
            }
            if (this.messageLayoutAction != null) {
                if (ChatActivity.this.isCustomTheme) {
                    this.messageLayoutAction.setBackgroundResource(C0419R.drawable.system_black);
                } else {
                    this.messageLayoutAction.setBackgroundResource(C0419R.drawable.system_blue);
                }
            }
            if (this.addContactButton != null) {
                this.addContactButton.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        if (ChatActivity.this.mActionMode != null) {
                            ChatActivity.this.processRowSelect(view);
                            return;
                        }
                        ContactAddActivity fragment = new ContactAddActivity();
                        Bundle args = new Bundle();
                        args.putInt("user_id", ChatListRowHolderEx.this.message.messageOwner.media.user_id);
                        args.putString("phone", ChatListRowHolderEx.this.message.messageOwner.media.phone_number);
                        fragment.setArguments(args);
                        ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "add_contact_" + ChatListRowHolderEx.this.message.messageOwner.media.user_id, false);
                    }
                });
                this.addContactButton.setOnLongClickListener(new OnLongClickListener(ChatActivity.this) {
                    public boolean onLongClick(View v) {
                        ChatActivity.this.createMenu(v, false);
                        return true;
                    }
                });
            }
            if (this.contactView != null) {
                this.contactView.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        if (ChatListRowHolderEx.this.message.type == 16 || ChatListRowHolderEx.this.message.type == 17) {
                            ChatListRowHolderEx.this.processOnClick(view);
                        } else if (ChatListRowHolderEx.this.message.type != 12 && ChatListRowHolderEx.this.message.type != 13) {
                        } else {
                            if (ChatActivity.this.mActionMode != null) {
                                ChatActivity.this.processRowSelect(view);
                            } else if (ChatListRowHolderEx.this.message.messageOwner.media.user_id != UserConfig.clientUserId) {
                                UserProfileActivity fragment = new UserProfileActivity();
                                Bundle args = new Bundle();
                                args.putInt("user_id", ChatListRowHolderEx.this.message.messageOwner.media.user_id);
                                fragment.setArguments(args);
                                ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "user_" + ChatListRowHolderEx.this.message.messageOwner.media.user_id, false);
                            }
                        }
                    }
                });
                this.contactView.setOnLongClickListener(new OnLongClickListener(ChatActivity.this) {
                    public boolean onLongClick(View v) {
                        ChatActivity.this.createMenu(v, false);
                        return true;
                    }
                });
            }
            if (this.actionAttachButton != null) {
                this.actionAttachButton.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        ChatListRowHolderEx.this.processOnClick(view);
                    }
                });
            }
            if (this.avatarImageView != null) {
                this.avatarImageView.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        if (ChatActivity.this.mActionMode != null) {
                            ChatActivity.this.processRowSelect(view);
                        } else if (ChatListRowHolderEx.this.message != null) {
                            UserProfileActivity fragment = new UserProfileActivity();
                            Bundle args = new Bundle();
                            args.putInt("user_id", ChatListRowHolderEx.this.message.messageOwner.from_id);
                            fragment.setArguments(args);
                            ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "user_" + ChatListRowHolderEx.this.message.messageOwner.from_id, false);
                        }
                    }
                });
            }
            if (this.actionCancelButton != null) {
                this.actionCancelButton.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        if (ChatListRowHolderEx.this.message != null) {
                            Integer tag = (Integer) ChatListRowHolderEx.this.actionProgress.getTag();
                            String file;
                            if (ChatListRowHolderEx.this.message.messageOwner.send_state != 0) {
                                MessagesController.Instance.cancelSendingMessage(ChatListRowHolderEx.this.message);
                                file = (String) ChatActivity.this.progressByTag.get(tag);
                                if (file != null) {
                                    ChatActivity.this.progressBarMap.remove(file);
                                }
                            } else if (ChatListRowHolderEx.this.message.type == 6 || ChatListRowHolderEx.this.message.type == 7 || ChatListRowHolderEx.this.message.type == 16 || ChatListRowHolderEx.this.message.type == 17) {
                                file = (String) ChatActivity.this.progressByTag.get(tag);
                                if (file != null) {
                                    ChatActivity.this.loadingFile.remove(file);
                                    if (ChatListRowHolderEx.this.message.type == 6 || ChatListRowHolderEx.this.message.type == 7) {
                                        FileLoader.Instance.cancelLoadFile(ChatListRowHolderEx.this.message.messageOwner.media.video, null, null);
                                    } else if (ChatListRowHolderEx.this.message.type == 16 || ChatListRowHolderEx.this.message.type == 17) {
                                        FileLoader.Instance.cancelLoadFile(null, null, ChatListRowHolderEx.this.message.messageOwner.media.document);
                                    }
                                    ChatActivity.this.updateVisibleRows();
                                }
                            }
                        }
                    }
                });
            }
            if (this.photoImage != null) {
                this.photoImage.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        ChatListRowHolderEx.this.processOnClick(view);
                    }
                });
                this.photoImage.setOnLongClickListener(new OnLongClickListener(ChatActivity.this) {
                    public boolean onLongClick(View v) {
                        ChatActivity.this.createMenu(v, false);
                        return true;
                    }
                });
            }
            if (this.forwardedUserText != null) {
                this.forwardedUserText.setOnClickListener(new OnClickListener(ChatActivity.this) {
                    public void onClick(View view) {
                        User fwdUser = (User) MessagesController.Instance.users.get(Integer.valueOf(ChatListRowHolderEx.this.message.messageOwner.fwd_from_id));
                        if (fwdUser != null && fwdUser.id != UserConfig.clientUserId) {
                            UserProfileActivity fragment = new UserProfileActivity();
                            Bundle args = new Bundle();
                            args.putInt("user_id", fwdUser.id);
                            fragment.setArguments(args);
                            ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "user_" + fwdUser.id, false);
                        }
                    }
                });
            }
        }

        private void alertUserOpenError() {
            Builder builder = new Builder(ChatActivity.this.parentActivity);
            builder.setTitle(C0419R.string.AppName);
            builder.setPositiveButton(C0419R.string.OK, null);
            if (this.message.type == 6 || this.message.type == 7) {
                builder.setMessage(C0419R.string.NoPlayerInstalled);
            } else {
                builder.setMessage(String.format(ChatActivity.this.getStringEntry(C0419R.string.NoHandleAppInstalled), new Object[]{this.message.messageOwner.media.document.mime_type}));
            }
            ChatActivity.this.visibleDialog = builder.show();
            ChatActivity.this.visibleDialog.setCanceledOnTouchOutside(true);
            ChatActivity.this.visibleDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    ChatActivity.this.visibleDialog = null;
                }
            });
        }

        private void processOnClick(View view) {
            if (ChatActivity.this.mActionMode != null) {
                ChatActivity.this.processRowSelect(view);
            } else if (this.message == null) {
            } else {
                if (this.message.type == 4 || this.message.type == 5) {
                    if (ChatActivity.this.isGoogleMapsInstalled()) {
                        NotificationCenter.Instance.addToMemCache(0, this.message);
                        ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(new LocationActivity(), "location_view", false);
                    }
                } else if (this.message.type == 2 || this.message.type == 3) {
                    NotificationCenter.Instance.addToMemCache(51, this.message);
                    ChatActivity.this.startActivity(new Intent(ChatActivity.this.parentActivity, GalleryImageViewer.class));
                } else if (this.message.type == 11) {
                    NotificationCenter.Instance.addToMemCache(51, this.message);
                    ChatActivity.this.startActivity(new Intent(ChatActivity.this.parentActivity, GalleryImageViewer.class));
                } else if (this.message.type == 6 || this.message.type == 7 || this.message.type == 16 || this.message.type == 17) {
                    File f;
                    String fileName = this.message.getFileName();
                    if (this.message.messageOwner.attachPath == null || this.message.messageOwner.attachPath.length() == 0) {
                        f = new File(Utilities.getCacheDir(), fileName);
                    } else {
                        f = new File(this.message.messageOwner.attachPath);
                    }
                    if (f != null && f.exists()) {
                        String realMimeType = null;
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            if (this.message.type == 6 || this.message.type == 7) {
                                intent.setDataAndType(Uri.fromFile(f), "video/mp4");
                            } else if (this.message.type == 16 || this.message.type == 17) {
                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                int idx = fileName.lastIndexOf(".");
                                if (idx != -1) {
                                    realMimeType = myMime.getMimeTypeFromExtension(fileName.substring(idx + 1).toLowerCase());
                                    if (realMimeType != null) {
                                        intent.setDataAndType(Uri.fromFile(f), realMimeType);
                                    } else {
                                        intent.setDataAndType(Uri.fromFile(f), "text/plain");
                                    }
                                } else {
                                    intent.setDataAndType(Uri.fromFile(f), "text/plain");
                                }
                            }
                            if (realMimeType != null) {
                                try {
                                    ChatActivity.this.startActivity(intent);
                                    return;
                                } catch (Exception e) {
                                    intent.setDataAndType(Uri.fromFile(f), "text/plain");
                                    ChatActivity.this.startActivity(intent);
                                    return;
                                }
                            }
                            ChatActivity.this.startActivity(intent);
                        } catch (Exception e2) {
                            alertUserOpenError();
                        }
                    } else if ((this.message.messageOwner.send_state == 2 || this.message.messageOwner.send_state == 1) && this.message.messageOwner.out) {
                        if (this.message.messageOwner.send_state == 2) {
                            ChatActivity.this.createMenu(view, false);
                        }
                    } else if (!ChatActivity.this.loadingFile.containsKey(fileName)) {
                        ChatActivity.this.progressByTag.put((Integer) this.actionProgress.getTag(), fileName);
                        ChatActivity.this.addToLoadingFile(fileName, this.actionProgress);
                        if (this.message.type == 6 || this.message.type == 7) {
                            FileLoader.Instance.loadFile(this.message.messageOwner.media.video, null, null);
                        } else if (this.message.type == 16 || this.message.type == 17) {
                            FileLoader.Instance.loadFile(null, null, this.message.messageOwner.media.document);
                        }
                        ChatActivity.this.updateVisibleRows();
                    }
                }
            }
        }
    }

    class C08561 implements Callback {
        C08561() {
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            menu.clear();
            MenuInflater inflater = actionMode.getMenuInflater();
            if (ChatActivity.this.currentEncryptedChat == null) {
                inflater.inflate(C0419R.menu.messages_full_menu, menu);
            } else {
                inflater.inflate(C0419R.menu.messages_encrypted_menu, menu);
            }
            menu.findItem(C0419R.id.copy).setVisible(ChatActivity.this.selectedMessagesCanCopyIds.size() == 1);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case C0419R.id.delete:
                    MessagesController.Instance.deleteMessages(new ArrayList(ChatActivity.this.selectedMessagesIds.keySet()));
                    break;
                case C0419R.id.copy:
                    MessageObject messageObject = ChatActivity.this.selectedMessagesCanCopyIds.values().toArray()[0];
                    if (VERSION.SDK_INT >= 11) {
                        ((ClipboardManager) ChatActivity.this.parentActivity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(PlusShare.KEY_CALL_TO_ACTION_LABEL, messageObject.messageOwner.message));
                        break;
                    }
                    ((android.text.ClipboardManager) ChatActivity.this.parentActivity.getSystemService("clipboard")).setText(messageObject.messageOwner.message);
                    break;
                case C0419R.id.forward:
                    MessagesActivity fragment = new MessagesActivity();
                    fragment.selectAlertString = C0419R.string.ForwardMessagesTo;
                    fragment.animationType = 1;
                    Bundle args = new Bundle();
                    args.putBoolean("onlySelect", true);
                    args.putBoolean("serverOnly", true);
                    fragment.setArguments(args);
                    fragment.delegate = ChatActivity.this;
                    ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "select_chat", false);
                    break;
            }
            actionMode.finish();
            return false;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            ChatActivity.this.mActionMode = null;
            ChatActivity.this.updateVisibleRows();
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        int chatId = getArguments().getInt("chat_id", 0);
        int userId = getArguments().getInt("user_id", 0);
        int encId = getArguments().getInt("enc_id", 0);
        if (chatId != 0) {
            this.currentChat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(chatId));
            if (this.currentChat == null) {
                return false;
            }
            MessagesController.Instance.loadChatInfo(this.currentChat.id);
            this.dialog_id = (long) (-chatId);
        } else if (userId != 0) {
            this.currentUser = (User) MessagesController.Instance.users.get(Integer.valueOf(userId));
            if (this.currentUser == null) {
                return false;
            }
            this.dialog_id = (long) userId;
        } else if (encId == 0) {
            return false;
        } else {
            this.currentEncryptedChat = (EncryptedChat) MessagesController.Instance.encryptedChats.get(Integer.valueOf(encId));
            if (this.currentEncryptedChat == null) {
                return false;
            }
            this.currentUser = (User) MessagesController.Instance.users.get(Integer.valueOf(this.currentEncryptedChat.user_id));
            if (this.currentUser == null) {
                return false;
            }
            this.dialog_id = ((long) encId) << 32;
            this.minMessageId = Integer.MAX_VALUE;
        }
        NotificationCenter.Instance.addObserver(this, 8);
        NotificationCenter.Instance.addObserver(this, 999);
        NotificationCenter.Instance.addObserver(this, 3);
        NotificationCenter.Instance.addObserver(this, 1);
        NotificationCenter.Instance.addObserver(this, 5);
        NotificationCenter.Instance.addObserver(this, 7);
        NotificationCenter.Instance.addObserver(this, 6);
        NotificationCenter.Instance.addObserver(this, 10);
        NotificationCenter.Instance.addObserver(this, 9);
        NotificationCenter.Instance.addObserver(this, 11);
        NotificationCenter.Instance.addObserver(this, 17);
        NotificationCenter.Instance.addObserver(this, 13);
        NotificationCenter.Instance.addObserver(this, 21);
        NotificationCenter.Instance.addObserver(this, 22);
        NotificationCenter.Instance.addObserver(this, 10002);
        NotificationCenter.Instance.addObserver(this, 10005);
        NotificationCenter.Instance.addObserver(this, 10004);
        NotificationCenter.Instance.addObserver(this, 10003);
        NotificationCenter.Instance.addObserver(this, 997);
        this.loading = true;
        MessagesController.Instance.loadMessages(this.dialog_id, 0, 30, 0, true, 0, this.classGuid, true, false);
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        this.fontSize = preferences.getInt("fons_size", 16);
        this.sendByEnter = preferences.getBoolean("send_by_enter", false);
        this.keyboardHeight = ApplicationLoader.applicationContext.getSharedPreferences("emoji", 0).getInt("kbd_height", Emoji.scale(200.0f));
        this.keyboardHeightLand = ApplicationLoader.applicationContext.getSharedPreferences("emoji", 0).getInt("kbd_height_land3", Emoji.scale(200.0f));
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.Instance.removeObserver(this, 8);
        NotificationCenter.Instance.removeObserver(this, 999);
        NotificationCenter.Instance.removeObserver(this, 3);
        NotificationCenter.Instance.removeObserver(this, 1);
        NotificationCenter.Instance.removeObserver(this, 5);
        NotificationCenter.Instance.removeObserver(this, 7);
        NotificationCenter.Instance.removeObserver(this, 6);
        NotificationCenter.Instance.removeObserver(this, 10);
        NotificationCenter.Instance.removeObserver(this, 9);
        NotificationCenter.Instance.removeObserver(this, 11);
        NotificationCenter.Instance.removeObserver(this, 17);
        NotificationCenter.Instance.removeObserver(this, 21);
        NotificationCenter.Instance.removeObserver(this, 22);
        NotificationCenter.Instance.removeObserver(this, 10002);
        NotificationCenter.Instance.removeObserver(this, 10005);
        NotificationCenter.Instance.removeObserver(this, 10004);
        NotificationCenter.Instance.removeObserver(this, 10003);
        NotificationCenter.Instance.removeObserver(this, 13);
        NotificationCenter.Instance.removeObserver(this, 997);
        if (this.sizeNotifierRelativeLayout != null) {
            this.sizeNotifierRelativeLayout.delegate = null;
            this.sizeNotifierRelativeLayout = null;
        }
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Display display = this.parentActivity.getWindowManager().getDefaultDisplay();
        if (VERSION.SDK_INT < 13) {
            this.displaySize.set(display.getWidth(), display.getHeight());
        } else {
            display.getSize(this.displaySize);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.fragmentView == null) {
            this.fragmentView = inflater.inflate(C0419R.layout.chat_layout, container, false);
            this.sizeNotifierRelativeLayout = (SizeNotifierRelativeLayout) this.fragmentView.findViewById(C0419R.id.chat_layout);
            this.sizeNotifierRelativeLayout.delegate = this;
            this.contentView = this.sizeNotifierRelativeLayout;
            this.emptyView = (TextView) this.fragmentView.findViewById(C0419R.id.searchEmptyView);
            this.chatListView = (LayoutListView) this.fragmentView.findViewById(C0419R.id.chat_list_view);
            LayoutListView layoutListView = this.chatListView;
            ListAdapter chatAdapter = new ChatAdapter(this.parentActivity);
            this.chatAdapter = chatAdapter;
            layoutListView.setAdapter(chatAdapter);
            this.topPanel = this.fragmentView.findViewById(C0419R.id.top_panel);
            this.topPlaneClose = (ImageView) this.fragmentView.findViewById(C0419R.id.top_plane_close);
            this.topPanelText = (TextView) this.fragmentView.findViewById(C0419R.id.top_panel_text);
            this.bottomOverlay = this.fragmentView.findViewById(C0419R.id.bottom_overlay);
            this.bottomOverlayText = (TextView) this.fragmentView.findViewById(C0419R.id.bottom_overlay_text);
            View bottomOverlayChat = this.fragmentView.findViewById(C0419R.id.bottom_overlay_chat);
            this.progressView = this.fragmentView.findViewById(C0419R.id.progressLayout);
            this.pagedownButton = this.fragmentView.findViewById(C0419R.id.pagedown_button);
            View progressViewInner = this.progressView.findViewById(C0419R.id.progressLayoutInner);
            updateContactStatus();
            ImageView backgroundImage = (ImageView) this.fragmentView.findViewById(C0419R.id.background_image);
            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            int selectedBackground = preferences.getInt("selectedBackground", 1000001);
            int selectedColor = preferences.getInt("selectedColor", 0);
            if (selectedColor != 0) {
                backgroundImage.setBackgroundColor(selectedColor);
                this.chatListView.setCacheColorHint(selectedColor);
            } else {
                this.chatListView.setCacheColorHint(0);
                if (selectedBackground == 1000001) {
                    backgroundImage.setImageResource(C0419R.drawable.background_hd);
                } else {
                    File toFile = new File(ApplicationLoader.applicationContext.getFilesDir(), "wallpaper.jpg");
                    if (toFile.exists()) {
                        if (ApplicationLoader.cachedWallpaper != null) {
                            backgroundImage.setImageBitmap(ApplicationLoader.cachedWallpaper);
                        } else {
                            backgroundImage.setImageURI(Uri.fromFile(toFile));
                            if (backgroundImage.getDrawable() instanceof BitmapDrawable) {
                                ApplicationLoader.cachedWallpaper = ((BitmapDrawable) backgroundImage.getDrawable()).getBitmap();
                            }
                        }
                        this.isCustomTheme = true;
                    } else {
                        backgroundImage.setImageResource(C0419R.drawable.background_hd);
                    }
                }
            }
            if (this.currentEncryptedChat != null) {
                this.secretChatPlaceholder = this.contentView.findViewById(C0419R.id.secret_placeholder);
                if (this.isCustomTheme) {
                    this.secretChatPlaceholder.setBackgroundResource(C0419R.drawable.system_black);
                } else {
                    this.secretChatPlaceholder.setBackgroundResource(C0419R.drawable.system_blue);
                }
                this.secretViewStatusTextView = (TextView) this.contentView.findViewById(C0419R.id.invite_text);
                this.secretChatPlaceholder.setPadding(Utilities.dp(16), Utilities.dp(12), Utilities.dp(16), Utilities.dp(12));
                this.contentView.findViewById(C0419R.id.secret_placeholder).setVisibility(0);
                if (this.currentEncryptedChat.admin_id == UserConfig.clientUserId) {
                    if (this.currentUser.first_name.length() > 0) {
                        this.secretViewStatusTextView.setText(String.format(getStringEntry(C0419R.string.EncryptedPlaceholderTitleOutgoing), new Object[]{this.currentUser.first_name}));
                    } else {
                        this.secretViewStatusTextView.setText(String.format(getStringEntry(C0419R.string.EncryptedPlaceholderTitleOutgoing), new Object[]{this.currentUser.last_name}));
                    }
                } else if (this.currentUser.first_name.length() > 0) {
                    this.secretViewStatusTextView.setText(String.format(getStringEntry(C0419R.string.EncryptedPlaceholderTitleIncoming), new Object[]{this.currentUser.first_name}));
                } else {
                    this.secretViewStatusTextView.setText(String.format(getStringEntry(C0419R.string.EncryptedPlaceholderTitleIncoming), new Object[]{this.currentUser.last_name}));
                }
                updateSecretStatus();
            }
            if (this.isCustomTheme) {
                progressViewInner.setBackgroundResource(C0419R.drawable.system_loader2);
                this.emptyView.setBackgroundResource(C0419R.drawable.system_black);
            } else {
                progressViewInner.setBackgroundResource(C0419R.drawable.system_loader1);
                this.emptyView.setBackgroundResource(C0419R.drawable.system_blue);
            }
            this.emptyView.setPadding(Utilities.dp(7), Utilities.dp(1), Utilities.dp(7), Utilities.dp(1));
            if (this.currentUser != null && this.currentUser.id == 333000) {
                this.emptyView.setText(C0419R.string.GotAQuestion);
            }
            this.chatListView.setOnItemLongClickListener(new C04462());
            this.chatListView.setOnScrollListener(new C04473());
            this.messsageEditText = (EditText) this.fragmentView.findViewById(C0419R.id.chat_text_edit);
            this.sendButton = (ImageButton) this.fragmentView.findViewById(C0419R.id.chat_send_button);
            this.sendButton.setImageResource(C0419R.drawable.send_button_states);
            this.sendButton.setEnabled(false);
            this.emojiButton = (ImageView) this.fragmentView.findViewById(C0419R.id.chat_smile_button);
            if (this.loading && this.messages.isEmpty()) {
                this.progressView.setVisibility(0);
                this.chatListView.setEmptyView(null);
            } else {
                this.progressView.setVisibility(8);
                if (this.currentEncryptedChat == null) {
                    this.chatListView.setEmptyView(this.emptyView);
                } else {
                    this.chatListView.setEmptyView(this.secretChatPlaceholder);
                }
            }
            this.emojiButton.setOnClickListener(new C04484());
            this.messsageEditText.setOnKeyListener(new C04495());
            this.messsageEditText.setOnEditorActionListener(new C04506());
            this.sendButton.setOnClickListener(new C04517());
            this.pagedownButton.setOnClickListener(new C04528());
            this.messsageEditText.addTextChangedListener(new C04539());
            bottomOverlayChat.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (ChatActivity.this.currentChat != null) {
                        MessagesController.Instance.deleteDialog((long) (-ChatActivity.this.currentChat.id), 0, false);
                        ChatActivity.this.finishFragment();
                    }
                }
            });
            this.chatListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (ChatActivity.this.mActionMode != null) {
                        ChatActivity.this.processRowSelect(view);
                    } else if (!ChatActivity.this.spanClicked(ChatActivity.this.chatListView, view, C0419R.id.chat_message_text)) {
                        ChatActivity.this.createMenu(view, true);
                    }
                }
            });
            this.chatListView.setOnTouchListener(new OnSwipeTouchListener() {
                public void onSwipeRight() {
                    try {
                        if (ChatActivity.this.visibleDialog != null) {
                            ChatActivity.this.visibleDialog.dismiss();
                            ChatActivity.this.visibleDialog = null;
                        }
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                    ChatActivity.this.finishFragment(true);
                }

                public void onSwipeLeft() {
                    if (!ChatActivity.this.swipeOpening) {
                        try {
                            if (ChatActivity.this.visibleDialog != null) {
                                ChatActivity.this.visibleDialog.dismiss();
                                ChatActivity.this.visibleDialog = null;
                            }
                        } catch (Exception e) {
                            FileLog.m799e("tmessages", e);
                        }
                        if (ChatActivity.this.avatarImageView != null) {
                            ChatActivity.this.swipeOpening = true;
                            ChatActivity.this.avatarImageView.performClick();
                        }
                    }
                }

                public void onTouchUp(MotionEvent event) {
                    ChatActivity.this.mLastTouch.right = (int) event.getX();
                    ChatActivity.this.mLastTouch.bottom = (int) event.getY();
                }
            });
            this.emptyView.setOnTouchListener(new OnSwipeTouchListener() {
                public void onSwipeRight() {
                    ChatActivity.this.finishFragment(true);
                }

                public void onSwipeLeft() {
                    if (!ChatActivity.this.swipeOpening && ChatActivity.this.avatarImageView != null) {
                        ChatActivity.this.swipeOpening = true;
                        ChatActivity.this.avatarImageView.performClick();
                    }
                }
            });
            if (this.currentChat == null || !((this.currentChat instanceof TL_chatForbidden) || this.currentChat.left)) {
                bottomOverlayChat.setVisibility(8);
            } else {
                bottomOverlayChat.setVisibility(0);
            }
        } else {
            ViewGroup parent = (ViewGroup) this.fragmentView.getParent();
            if (parent != null) {
                parent.removeView(this.fragmentView);
            }
        }
        return this.fragmentView;
    }

    private void sendMessage() {
        if (processSendingText(this.messsageEditText.getText().toString().trim())) {
            this.messsageEditText.setText(BuildConfig.FLAVOR);
            this.lastTypingTimeSend = 0;
            this.chatListView.post(new Runnable() {
                public void run() {
                    ChatActivity.this.chatListView.setSelectionFromTop(ChatActivity.this.messages.size() - 1, -10000 - ChatActivity.this.chatListView.getPaddingTop());
                }
            });
        }
    }

    private void showPagedownButton(boolean show, boolean animated) {
        if (this.pagedownButton != null) {
            if (show) {
                if (this.pagedownButton.getVisibility() != 8) {
                    return;
                }
                if (VERSION.SDK_INT < 16 || !animated) {
                    this.pagedownButton.setVisibility(0);
                    return;
                }
                this.pagedownButton.setVisibility(0);
                this.pagedownButton.setAlpha(0.0f);
                this.pagedownButton.animate().alpha(1.0f).setDuration(200).start();
            } else if (this.pagedownButton.getVisibility() != 0) {
            } else {
                if (VERSION.SDK_INT < 16 || !animated) {
                    this.pagedownButton.setVisibility(8);
                } else {
                    this.pagedownButton.animate().alpha(0.0f).withEndAction(new Runnable() {
                        public void run() {
                            ChatActivity.this.pagedownButton.setVisibility(8);
                        }
                    }).setDuration(200).start();
                }
            }
        }
    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (this.invalidateAfterAnimation && this.chatListView != null) {
            updateVisibleRows();
        }
    }

    public void willBeHidden() {
        super.willBeHidden();
        this.paused = true;
    }

    private void updateSecretStatus() {
        if (this.bottomOverlay != null) {
            if (this.currentEncryptedChat == null || this.secretViewStatusTextView == null) {
                this.bottomOverlay.setVisibility(8);
                return;
            }
            boolean hideKeyboard = false;
            if (this.currentEncryptedChat instanceof TL_encryptedChatRequested) {
                this.bottomOverlayText.setText(getStringEntry(C0419R.string.EncryptionProcessing));
                this.bottomOverlay.setVisibility(0);
                hideKeyboard = true;
            } else if (this.currentEncryptedChat instanceof TL_encryptedChatWaiting) {
                this.bottomOverlayText.setText(Html.fromHtml(String.format(getStringEntry(C0419R.string.AwaitingEncryption), new Object[]{"<b>" + this.currentUser.first_name + "</b>"})));
                this.bottomOverlay.setVisibility(0);
                hideKeyboard = true;
            } else if (this.currentEncryptedChat instanceof TL_encryptedChatDiscarded) {
                this.bottomOverlayText.setText(getStringEntry(C0419R.string.EncryptionRejected));
                this.bottomOverlay.setVisibility(0);
                hideKeyboard = true;
            } else if (this.currentEncryptedChat instanceof TL_encryptedChat) {
                this.bottomOverlay.setVisibility(8);
            }
            if (hideKeyboard) {
                hideEmojiPopup();
                if (this.parentActivity != null) {
                    Utilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                }
            }
            if (this.parentActivity != null) {
                this.parentActivity.supportInvalidateOptionsMenu();
            }
        }
    }

    private void addToLoadingFile(String path, ProgressBar bar) {
        ArrayList<ProgressBar> arr = (ArrayList) this.loadingFile.get(path);
        if (arr == null) {
            arr = new ArrayList();
            this.loadingFile.put(path, arr);
        }
        arr.add(bar);
    }

    private void removeFromloadingFile(String path, ProgressBar bar) {
        ArrayList<ProgressBar> arr = (ArrayList) this.loadingFile.get(path);
        if (arr != null) {
            arr.remove(bar);
        }
    }

    private void updateOnlineCount() {
        if (this.info != null) {
            this.onlineCount = 0;
            int currentTime = ConnectionsManager.Instance.getCurrentTime();
            Iterator i$ = this.info.participants.iterator();
            while (i$.hasNext()) {
                User user = (User) MessagesController.Instance.users.get(Integer.valueOf(((TL_chatParticipant) i$.next()).user_id));
                if (!(user == null || user.status == null)) {
                    if ((user.status.expires > currentTime || user.status.was_online > currentTime || user.id == UserConfig.clientUserId) && (user.status.expires > FileLoader.FileDidUpload || user.status.was_online > FileLoader.FileDidUpload)) {
                        this.onlineCount++;
                    }
                }
            }
            updateSubtitle();
        }
    }

    private int getMessageType(MessageObject messageObject) {
        if (this.currentEncryptedChat == null) {
            if (messageObject.messageOwner.id > 0 || !messageObject.messageOwner.out) {
                if (messageObject.type == 15) {
                    return -1;
                }
                if (messageObject.type == 10 || messageObject.type == 11) {
                    if (messageObject.messageOwner.id == 0) {
                        return -1;
                    }
                    return 1;
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaEmpty) {
                    return 3;
                } else {
                    return 2;
                }
            } else if (messageObject.messageOwner.send_state == 2) {
                return 0;
            } else {
                return -1;
            }
        } else if (messageObject.type == 15) {
            return -1;
        } else {
            if (messageObject.messageOwner.send_state == 2) {
                return 0;
            }
            if (messageObject.type == 10 || messageObject.type == 11 || messageObject.messageOwner.send_state == 1) {
                if (messageObject.messageOwner.id == 0) {
                    return -1;
                }
                return 1;
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaEmpty) {
                return 3;
            } else {
                return 2;
            }
        }
    }

    private void addToSelectedMessages(MessageObject messageObject) {
        boolean z = true;
        if (this.selectedMessagesIds.containsKey(Integer.valueOf(messageObject.messageOwner.id))) {
            this.selectedMessagesIds.remove(Integer.valueOf(messageObject.messageOwner.id));
            if (messageObject.type == 0 || messageObject.type == 1 || messageObject.type == 8 || messageObject.type == 9) {
                this.selectedMessagesCanCopyIds.remove(Integer.valueOf(messageObject.messageOwner.id));
            }
            if (this.selectedMessagesIds.size() == 1 && this.mActionMode != null && this.mActionMode.getMenu() != null) {
                MenuItem findItem = this.mActionMode.getMenu().findItem(C0419R.id.copy);
                if (this.selectedMessagesCanCopyIds.size() != 1) {
                    z = false;
                }
                findItem.setVisible(z);
                return;
            }
            return;
        }
        boolean update = false;
        if (this.selectedMessagesIds.size() == 1) {
            update = true;
        }
        this.selectedMessagesIds.put(Integer.valueOf(messageObject.messageOwner.id), messageObject);
        if (messageObject.type == 0 || messageObject.type == 1 || messageObject.type == 8 || messageObject.type == 9) {
            this.selectedMessagesCanCopyIds.put(Integer.valueOf(messageObject.messageOwner.id), messageObject);
        }
        if (update && this.mActionMode != null && this.mActionMode.getMenu() != null) {
            this.mActionMode.getMenu().findItem(C0419R.id.copy).setVisible(false);
        }
    }

    private void processRowSelect(View view) {
        View parentView = getRowParentView(view);
        if (parentView != null) {
            MessageObject message = ((ChatListRowHolderEx) parentView.getTag()).message;
            if (getMessageType(message) >= 2) {
                addToSelectedMessages(message);
                updateActionModeTitle();
                updateVisibleRows();
            }
        }
    }

    private void updateActionModeTitle() {
        if (this.mActionMode != null) {
            if (this.selectedMessagesIds.isEmpty()) {
                this.mActionMode.finish();
                return;
            }
            this.mActionMode.setTitle(String.format("%s %d", new Object[]{getStringEntry(C0419R.string.Selected), Integer.valueOf(this.selectedMessagesIds.size())}));
        }
    }

    private void updateSubtitle() {
        if (!this.isFinish && !this.paused && getActivity() != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            TextView title = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_title);
            if (title == null) {
                title = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
            }
            if (this.currentChat != null) {
                actionBar.setTitle(this.currentChat.title);
                if (title != null) {
                    title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    title.setCompoundDrawablePadding(0);
                }
            } else if (this.currentUser != null) {
                if (this.currentUser.id == 333000 || MessagesController.Instance.contactsDict.get(this.currentUser.id) != null || (MessagesController.Instance.contactsDict.size() == 0 && MessagesController.Instance.loadingContacts)) {
                    actionBar.setTitle(Utilities.formatName(this.currentUser.first_name, this.currentUser.last_name));
                } else if (this.currentUser.phone == null || this.currentUser.phone.length() == 0) {
                    actionBar.setTitle(Utilities.formatName(this.currentUser.first_name, this.currentUser.last_name));
                } else {
                    actionBar.setTitle(PhoneFormat.Instance.format("+" + this.currentUser.phone));
                }
                if (title != null) {
                    if (this.currentEncryptedChat != null) {
                        title.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.ic_lock_white, 0, 0, 0);
                        title.setCompoundDrawablePadding(Utilities.dp(4));
                    } else {
                        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        title.setCompoundDrawablePadding(0);
                    }
                }
            }
            CharSequence printString = (CharSequence) MessagesController.Instance.printingStrings.get(Long.valueOf(this.dialog_id));
            if (printString == null || printString.length() == 0) {
                this.lastPrintString = null;
                setTypingAnimation(false);
                if (this.currentChat != null) {
                    if (this.currentChat instanceof TL_chatForbidden) {
                        actionBar.setSubtitle(getStringEntry(C0419R.string.YouWereKicked));
                        return;
                    } else if (this.currentChat.left) {
                        actionBar.setSubtitle(getStringEntry(C0419R.string.YouLeft));
                        return;
                    } else if (this.onlineCount <= 0 || this.currentChat.participants_count == 0) {
                        actionBar.setSubtitle(String.format("%d %s", new Object[]{Integer.valueOf(this.currentChat.participants_count), getStringEntry(C0419R.string.Members)}));
                        return;
                    } else {
                        actionBar.setSubtitle(String.format("%d %s, %d %s", new Object[]{Integer.valueOf(this.currentChat.participants_count), getStringEntry(C0419R.string.Members), Integer.valueOf(this.onlineCount), getStringEntry(C0419R.string.Online)}));
                        return;
                    }
                } else if (this.currentUser == null) {
                    return;
                } else {
                    if (this.currentUser.status == null) {
                        actionBar.setSubtitle(getStringEntry(C0419R.string.Offline));
                        return;
                    }
                    int currentTime = ConnectionsManager.Instance.getCurrentTime();
                    if (this.currentUser.status.expires > currentTime || this.currentUser.status.was_online > currentTime) {
                        actionBar.setSubtitle(getStringEntry(C0419R.string.Online));
                        return;
                    } else if (this.currentUser.status.was_online > FileLoader.FileDidUpload || this.currentUser.status.expires > FileLoader.FileDidUpload) {
                        int value = this.currentUser.status.was_online;
                        if (value == 0) {
                            value = this.currentUser.status.expires;
                        }
                        actionBar.setSubtitle(String.format("%s %s", new Object[]{getStringEntry(C0419R.string.LastSeen), Utilities.formatDateOnline((long) value)}));
                        return;
                    } else {
                        actionBar.setSubtitle(getStringEntry(C0419R.string.Invisible));
                        return;
                    }
                }
            }
            this.lastPrintString = printString;
            actionBar.setSubtitle(printString);
            setTypingAnimation(true);
        }
    }

    private void checkAndUpdateAvatar() {
        FileLocation newPhoto = null;
        int placeHolderId = 0;
        if (this.currentUser != null) {
            this.currentUser = (User) MessagesController.Instance.users.get(Integer.valueOf(this.currentUser.id));
            if (this.currentUser.photo != null) {
                newPhoto = this.currentUser.photo.photo_small;
            }
            placeHolderId = Utilities.getUserAvatarForId(this.currentUser.id);
        } else if (this.currentChat != null) {
            this.currentChat = (Chat) MessagesController.Instance.chats.get(Integer.valueOf(this.currentChat.id));
            if (this.currentChat.photo != null) {
                newPhoto = this.currentChat.photo.photo_small;
            }
            placeHolderId = Utilities.getGroupAvatarForId(this.currentChat.id);
        }
        if (this.avatarImageView != null) {
            this.avatarImageView.setImage(newPhoto, "50_50", placeHolderId);
        }
    }

    public void onSizeChanged(int height) {
        boolean z = true;
        this.parentActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(new Rect());
        int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
        if (height > Emoji.scale(50.0f)) {
            if (rotation == 3 || rotation == 1) {
                this.keyboardHeightLand = height;
                this.parentActivity.getSharedPreferences("emoji", 0).edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            } else {
                this.keyboardHeight = height;
                this.parentActivity.getSharedPreferences("emoji", 0).edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (this.emojiPopup != null && this.emojiPopup.isShowing()) {
            WindowManager wm = (WindowManager) this.parentActivity.getSystemService("window");
            final WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.emojiPopup.getContentView().getLayoutParams();
            layoutParams.width = this.contentView.getWidth();
            if (rotation == 3 || rotation == 1) {
                layoutParams.height = this.keyboardHeightLand;
            } else {
                layoutParams.height = this.keyboardHeight;
            }
            wm.updateViewLayout(this.emojiPopup.getContentView(), layoutParams);
            if (!this.keyboardVisible) {
                this.contentView.post(new Runnable() {
                    public void run() {
                        ChatActivity.this.contentView.setPadding(0, 0, 0, layoutParams.height);
                        ChatActivity.this.contentView.forceLayout();
                    }
                });
            }
        }
        boolean oldValue = this.keyboardVisible;
        if (height <= 0) {
            z = false;
        }
        this.keyboardVisible = z;
        if (this.keyboardVisible && this.contentView.getPaddingBottom() > 0) {
            showEmojiPopup(false);
        } else if (!this.keyboardVisible && this.keyboardVisible != oldValue && this.emojiPopup != null && this.emojiPopup.isShowing()) {
            showEmojiPopup(false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 0) {
            Utilities.addMediaToGallery(this.currentPicturePath);
            processSendingPhoto(this.currentPicturePath);
            this.currentPicturePath = null;
        } else if (requestCode == 1) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                String imageFilePath = null;
                if (imageUri.getScheme().contains("file")) {
                    imageFilePath = imageUri.getPath();
                } else {
                    inflaterActivity = this.parentActivity;
                    if (inflaterActivity == null) {
                        inflaterActivity = (ActionBarActivity) getActivity();
                    }
                    if (inflaterActivity != null) {
                        try {
                            imageFilePath = Utilities.getPath(inflaterActivity, imageUri);
                        } catch (Exception e) {
                            FileLog.m799e("tmessages", e);
                        }
                    } else {
                        return;
                    }
                }
                processSendingPhoto(imageFilePath);
            }
        } else if (requestCode == 2) {
            String videoPath = null;
            if (data != null) {
                Uri uri = data.getData();
                boolean fromCamera = false;
                if (uri != null && uri.getScheme() != null) {
                    fromCamera = uri.getScheme().contains("file");
                } else if (uri == null) {
                    fromCamera = true;
                }
                if (fromCamera) {
                    if (uri != null) {
                        videoPath = uri.getPath();
                    } else {
                        videoPath = this.currentPicturePath;
                    }
                    Utilities.addMediaToGallery(this.currentPicturePath);
                    this.currentPicturePath = null;
                } else {
                    inflaterActivity = this.parentActivity;
                    if (inflaterActivity == null) {
                        inflaterActivity = (ActionBarActivity) getActivity();
                    }
                    if (inflaterActivity != null) {
                        try {
                            videoPath = Utilities.getPath(inflaterActivity, uri);
                        } catch (Exception e2) {
                            FileLog.m799e("tmessages", e2);
                        }
                    } else {
                        return;
                    }
                }
            }
            if (videoPath == null && this.currentPicturePath != null) {
                if (new File(this.currentPicturePath).exists()) {
                    videoPath = this.currentPicturePath;
                }
                this.currentPicturePath = null;
            }
            processSendingVideo(videoPath);
        }
    }

    public boolean processSendingText(String text) {
        text = text.replaceAll("\n\n+", "\n\n").replaceAll(" +", " ");
        if (text.length() == 0) {
            return false;
        }
        int count = (int) Math.ceil((double) (((float) text.length()) / 2048.0f));
        for (int a = 0; a < count; a++) {
            MessagesController.Instance.sendMessage(text.substring(a * 2048, Math.min((a + 1) * 2048, text.length())), this.dialog_id);
        }
        return true;
    }

    public void processSendingPhoto(String imageFilePath) {
        if (imageFilePath != null) {
            TL_photo photo = MessagesController.Instance.generatePhotoSizes(imageFilePath);
            if (photo != null) {
                MessagesController.Instance.sendMessage(photo, this.dialog_id);
                if (this.chatListView != null) {
                    this.chatListView.setSelection(this.messages.size() + 1);
                }
                this.scrollToTopOnResume = true;
            }
        }
    }

    public void processSendingVideo(String videoPath) {
        if (videoPath != null) {
            boolean z;
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoPath, 1);
            if (this.currentEncryptedChat != null) {
                z = true;
            } else {
                z = false;
            }
            PhotoSize size = FileLoader.scaleAndSaveImage(thumb, 90.0f, 90.0f, 55, z);
            if (size != null) {
                size.type = "s";
                TL_video video = new TL_video();
                video.thumb = size;
                video.caption = BuildConfig.FLAVOR;
                video.id = 0;
                video.path = videoPath;
                File temp = new File(videoPath);
                if (temp != null && temp.exists()) {
                    video.size = (int) temp.length();
                }
                UserConfig.lastLocalId--;
                UserConfig.saveConfig(false);
                MediaPlayer mp = MediaPlayer.create(ApplicationLoader.applicationContext, Uri.fromFile(new File(videoPath)));
                if (mp != null) {
                    video.duration = (int) Math.ceil((double) (((float) mp.getDuration()) / 1000.0f));
                    video.w = mp.getVideoWidth();
                    video.h = mp.getVideoHeight();
                    mp.release();
                    Media media = new Media();
                    MessagesController.Instance.sendMessage(video, this.dialog_id);
                    if (this.chatListView != null) {
                        this.chatListView.setSelection(this.messages.size() + 1);
                    }
                    this.scrollToTopOnResume = true;
                }
            }
        }
    }

    private void removeUnreadPlane(boolean reload) {
        if (this.unreadMessageObject != null) {
            this.messages.remove(this.unreadMessageObject);
            this.unread_end_reached = true;
            this.first_unread_id = 0;
            this.last_unread_id = 0;
            this.unread_to_load = 0;
            this.unreadMessageObject = null;
            if (reload) {
                this.chatAdapter.notifyDataSetChanged();
            }
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        MessageObject obj;
        ArrayList<MessageObject> dayArray;
        Message dateMsg;
        MessageObject messageObject;
        if (id == 8) {
            if (((Long) args[0]).longValue() == this.dialog_id) {
                int offset = ((Integer) args[1]).intValue();
                int count = ((Integer) args[2]).intValue();
                boolean isCache = ((Boolean) args[4]).booleanValue();
                int fnid = ((Integer) args[5]).intValue();
                int last_unread_date = ((Integer) args[8]).intValue();
                boolean forwardLoad = ((Boolean) args[9]).booleanValue();
                boolean wasUnread = false;
                boolean positionToUnread = false;
                if (fnid != 0) {
                    this.first_unread_id = ((Integer) args[5]).intValue();
                    this.last_unread_id = ((Integer) args[6]).intValue();
                    this.unread_to_load = ((Integer) args[7]).intValue();
                    positionToUnread = true;
                }
                ArrayList<MessageObject> messArr = args[3];
                int newRowsCount = 0;
                this.unread_end_reached = this.last_unread_id == 0;
                for (int a = 0; a < messArr.size(); a++) {
                    obj = (MessageObject) messArr.get(a);
                    if (!this.messagesDict.containsKey(Integer.valueOf(obj.messageOwner.id))) {
                        if (obj.messageOwner.id > 0) {
                            this.maxMessageId = Math.min(obj.messageOwner.id, this.maxMessageId);
                            this.minMessageId = Math.max(obj.messageOwner.id, this.minMessageId);
                        } else if (this.currentEncryptedChat != null) {
                            this.minMessageId = Math.min(obj.messageOwner.id, this.minMessageId);
                        }
                        this.maxDate = Math.max(this.maxDate, obj.messageOwner.date);
                        if (this.minDate == 0 || obj.messageOwner.date < this.minDate) {
                            this.minDate = obj.messageOwner.date;
                        }
                        if (!obj.messageOwner.out && obj.messageOwner.unread) {
                            wasUnread = true;
                        }
                        this.messagesDict.put(Integer.valueOf(obj.messageOwner.id), obj);
                        dayArray = (ArrayList) this.messagesByDays.get(obj.dateKey);
                        if (dayArray == null) {
                            dayArray = new ArrayList();
                            this.messagesByDays.put(obj.dateKey, dayArray);
                            dateMsg = new Message();
                            dateMsg.message = Utilities.formatDateChat((long) obj.messageOwner.date);
                            dateMsg.id = 0;
                            messageObject = new MessageObject(dateMsg, null);
                            messageObject.type = 10;
                            if (forwardLoad) {
                                this.messages.add(0, messageObject);
                            } else {
                                this.messages.add(messageObject);
                            }
                            newRowsCount++;
                        }
                        newRowsCount++;
                        dayArray.add(obj);
                        if (forwardLoad) {
                            this.messages.add(0, obj);
                        } else {
                            this.messages.add(this.messages.size() - 1, obj);
                        }
                        if (!forwardLoad) {
                            if (obj.messageOwner.id == this.first_unread_id) {
                                dateMsg = new Message();
                                dateMsg.message = BuildConfig.FLAVOR;
                                dateMsg.id = 0;
                                messageObject = new MessageObject(dateMsg, null);
                                messageObject.type = 15;
                                boolean dateAdded = true;
                                if (a != messArr.size() - 1) {
                                    dateAdded = !((MessageObject) messArr.get(a + 1)).dateKey.equals(obj.dateKey);
                                }
                                this.messages.add(this.messages.size() - (dateAdded ? 0 : 1), messageObject);
                                this.unreadMessageObject = messageObject;
                                newRowsCount++;
                            }
                            if (obj.messageOwner.id == this.last_unread_id) {
                                this.unread_end_reached = true;
                            }
                        }
                    }
                }
                if (this.unread_end_reached) {
                    this.first_unread_id = 0;
                    this.last_unread_id = 0;
                }
                if (forwardLoad) {
                    if (messArr.size() != count) {
                        this.unread_end_reached = true;
                        this.first_unread_id = 0;
                        this.last_unread_id = 0;
                    }
                    this.chatAdapter.notifyDataSetChanged();
                    this.loadingForward = false;
                } else {
                    if (messArr.size() != count) {
                        if (isCache) {
                            this.cacheEndReaced = true;
                            if (this.currentEncryptedChat != null) {
                                this.endReached = true;
                            }
                        } else {
                            this.cacheEndReaced = true;
                            this.endReached = true;
                        }
                    }
                    this.loading = false;
                    if (this.chatListView != null) {
                        if (this.first || this.scrollToTopOnResume) {
                            this.chatAdapter.notifyDataSetChanged();
                            if (!positionToUnread || this.unreadMessageObject == null) {
                                this.chatListView.post(new Runnable() {
                                    public void run() {
                                        ChatActivity.this.chatListView.setSelectionFromTop(ChatActivity.this.messages.size() - 1, -10000 - ChatActivity.this.chatListView.getPaddingTop());
                                    }
                                });
                            } else {
                                if (this.messages.get(this.messages.size() - 1) == this.unreadMessageObject) {
                                    this.chatListView.setSelectionFromTop(0, Utilities.dp(-11));
                                } else {
                                    this.chatListView.setSelectionFromTop(this.messages.size() - this.messages.indexOf(this.unreadMessageObject), Utilities.dp(-11));
                                }
                                this.chatListView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                                    public boolean onPreDraw() {
                                        if (ChatActivity.this.messages.get(ChatActivity.this.messages.size() - 1) == ChatActivity.this.unreadMessageObject) {
                                            ChatActivity.this.chatListView.setSelectionFromTop(0, Utilities.dp(-11));
                                        } else {
                                            ChatActivity.this.chatListView.setSelectionFromTop(ChatActivity.this.messages.size() - ChatActivity.this.messages.indexOf(ChatActivity.this.unreadMessageObject), Utilities.dp(-11));
                                        }
                                        ChatActivity.this.chatListView.getViewTreeObserver().removeOnPreDrawListener(this);
                                        return false;
                                    }
                                });
                                this.chatListView.invalidate();
                                showPagedownButton(true, true);
                            }
                        } else {
                            int firstVisPos = this.chatListView.getLastVisiblePosition();
                            View firstVisView = this.chatListView.getChildAt(this.chatListView.getChildCount() - 1);
                            int top = (firstVisView == null ? 0 : firstVisView.getTop()) - this.chatListView.getPaddingTop();
                            this.chatAdapter.notifyDataSetChanged();
                            this.chatListView.setSelectionFromTop(firstVisPos + newRowsCount, top);
                        }
                        if (this.paused) {
                            this.scrollToTopOnResume = true;
                            if (positionToUnread && this.unreadMessageObject != null) {
                                this.scrollToTopUnReadOnResume = true;
                            }
                        }
                        if (this.first && this.chatListView.getEmptyView() == null) {
                            if (this.currentEncryptedChat == null) {
                                this.chatListView.setEmptyView(this.emptyView);
                            } else {
                                this.chatListView.setEmptyView(this.secretChatPlaceholder);
                            }
                        }
                    } else {
                        this.scrollToTopOnResume = true;
                        if (positionToUnread && this.unreadMessageObject != null) {
                            this.scrollToTopUnReadOnResume = true;
                        }
                    }
                }
                if (this.first && this.messages.size() > 0) {
                    if (this.last_unread_id != 0) {
                        MessagesController.Instance.markDialogAsRead(this.dialog_id, ((MessageObject) this.messages.get(0)).messageOwner.id, this.last_unread_id, 0, last_unread_date, wasUnread);
                    } else {
                        MessagesController.Instance.markDialogAsRead(this.dialog_id, ((MessageObject) this.messages.get(0)).messageOwner.id, this.minMessageId, 0, this.maxDate, wasUnread);
                    }
                    this.first = false;
                }
                if (this.progressView != null) {
                    this.progressView.setVisibility(8);
                }
            }
        } else if (id == 999) {
            if (this.animationInProgress) {
                this.invalidateAfterAnimation = true;
            } else if (this.chatListView != null) {
                this.chatListView.invalidateViews();
            }
            if (this.emojiView != null) {
                this.emojiView.invalidateViews();
            }
        } else if (id == 3) {
            int updateMask = ((Integer) args[0]).intValue();
            if (!((updateMask & 1) == 0 && (updateMask & 4) == 0 && (updateMask & 16) == 0)) {
                updateSubtitle();
                updateOnlineCount();
            }
            if (!((updateMask & 2) == 0 && (updateMask & 8) == 0 && (updateMask & 1) == 0)) {
                checkAndUpdateAvatar();
                if (this.animationInProgress) {
                    this.invalidateAfterAnimation = true;
                } else if (this.chatListView != null) {
                    updateVisibleRows();
                }
            }
            if ((updateMask & 64) != 0) {
                CharSequence printString = (CharSequence) MessagesController.Instance.printingStrings.get(Long.valueOf(this.dialog_id));
                if ((this.lastPrintString != null && printString == null) || ((this.lastPrintString == null && printString != null) || (this.lastPrintString != null && printString != null && !this.lastPrintString.equals(printString)))) {
                    updateSubtitle();
                }
            }
        } else if (id == 1) {
            if (((Long) args[0]).longValue() == this.dialog_id) {
                boolean updateChat = false;
                ArrayList<MessageObject> arr = args[1];
                if (this.unread_end_reached) {
                    boolean markAsRead = false;
                    int oldCount = this.messages.size();
                    i$ = arr.iterator();
                    while (i$.hasNext()) {
                        obj = (MessageObject) i$.next();
                        if (!this.messagesDict.containsKey(Integer.valueOf(obj.messageOwner.id))) {
                            if (this.minDate == 0 || obj.messageOwner.date < this.minDate) {
                                this.minDate = obj.messageOwner.date;
                            }
                            if (!(obj.messageOwner.attachPath == null || obj.messageOwner.attachPath.length() == 0)) {
                                this.progressBarMap.put(obj.messageOwner.attachPath, null);
                            }
                            if (obj.messageOwner.out) {
                                removeUnreadPlane(false);
                            }
                            if (!(obj.messageOwner.out || this.unreadMessageObject == null)) {
                                this.unread_to_load++;
                            }
                            if (obj.messageOwner.id > 0) {
                                this.maxMessageId = Math.min(obj.messageOwner.id, this.maxMessageId);
                                this.minMessageId = Math.max(obj.messageOwner.id, this.minMessageId);
                            } else if (this.currentEncryptedChat != null) {
                                this.minMessageId = Math.min(obj.messageOwner.id, this.minMessageId);
                            }
                            this.maxDate = Math.max(this.maxDate, obj.messageOwner.date);
                            this.messagesDict.put(Integer.valueOf(obj.messageOwner.id), obj);
                            dayArray = (ArrayList) this.messagesByDays.get(obj.dateKey);
                            if (dayArray == null) {
                                dayArray = new ArrayList();
                                this.messagesByDays.put(obj.dateKey, dayArray);
                                dateMsg = new Message();
                                dateMsg.message = Utilities.formatDateChat((long) obj.messageOwner.date);
                                dateMsg.id = 0;
                                messageObject = new MessageObject(dateMsg, null);
                                messageObject.type = 10;
                                this.messages.add(0, messageObject);
                            }
                            if (!obj.messageOwner.out && obj.messageOwner.unread) {
                                obj.messageOwner.unread = false;
                                markAsRead = true;
                            }
                            dayArray.add(0, obj);
                            this.messages.add(0, obj);
                            if (obj.type == 10 || obj.type == 11) {
                                updateChat = true;
                            }
                        }
                    }
                    if (this.progressView != null) {
                        this.progressView.setVisibility(8);
                    }
                    if (this.chatAdapter != null) {
                        this.chatAdapter.notifyDataSetChanged();
                    } else {
                        this.scrollToTopOnResume = true;
                    }
                    if (this.chatListView == null || this.chatAdapter == null) {
                        this.scrollToTopOnResume = true;
                    } else {
                        int lastVisible = this.chatListView.getLastVisiblePosition();
                        if (this.endReached) {
                            lastVisible++;
                        }
                        if (lastVisible != oldCount) {
                            showPagedownButton(true, true);
                        } else if (this.paused) {
                            this.scrollToTopOnResume = true;
                        } else {
                            this.chatListView.post(new Runnable() {
                                public void run() {
                                    ChatActivity.this.chatListView.setSelectionFromTop(ChatActivity.this.messages.size() - 1, -10000 - ChatActivity.this.chatListView.getPaddingTop());
                                }
                            });
                        }
                    }
                    if (markAsRead) {
                        if (this.paused) {
                            this.readWhenResume = true;
                            this.readWithDate = this.maxDate;
                            this.readWithMid = this.minMessageId;
                        } else {
                            MessagesController.Instance.markDialogAsRead(this.dialog_id, ((MessageObject) this.messages.get(0)).messageOwner.id, this.minMessageId, 0, this.maxDate, true);
                        }
                    }
                } else {
                    int currentMaxDate = ExploreByTouchHelper.INVALID_ID;
                    int currentMinMsgId = ExploreByTouchHelper.INVALID_ID;
                    if (this.currentEncryptedChat != null) {
                        currentMinMsgId = Integer.MAX_VALUE;
                    }
                    boolean currentMarkAsRead = false;
                    i$ = arr.iterator();
                    while (i$.hasNext()) {
                        obj = (MessageObject) i$.next();
                        if (!this.messagesDict.containsKey(Integer.valueOf(obj.messageOwner.id))) {
                            currentMaxDate = Math.max(currentMaxDate, obj.messageOwner.date);
                            if (obj.messageOwner.id > 0) {
                                currentMinMsgId = Math.max(obj.messageOwner.id, currentMinMsgId);
                            } else if (this.currentEncryptedChat != null) {
                                currentMinMsgId = Math.min(obj.messageOwner.id, currentMinMsgId);
                            }
                            if (!obj.messageOwner.out && obj.messageOwner.unread) {
                                this.unread_to_load++;
                                currentMarkAsRead = true;
                            }
                            if (obj.type == 10 || obj.type == 11) {
                                updateChat = true;
                            }
                        }
                    }
                    if (currentMarkAsRead) {
                        if (this.paused) {
                            this.readWhenResume = true;
                            this.readWithDate = currentMaxDate;
                            this.readWithMid = currentMinMsgId;
                        } else {
                            MessagesController.Instance.markDialogAsRead(this.dialog_id, ((MessageObject) this.messages.get(0)).messageOwner.id, currentMinMsgId, 0, currentMaxDate, true);
                        }
                    }
                    updateVisibleRows();
                }
                if (updateChat) {
                    updateSubtitle();
                    checkAndUpdateAvatar();
                }
            }
        } else if (id == 5) {
            if (this.messsageEditText != null && this.messsageEditText.isFocused()) {
                Utilities.hideKeyboard(this.messsageEditText);
            }
            removeSelfFromStack();
        } else if (id == 7) {
            updated = false;
            i$ = args[0].iterator();
            while (i$.hasNext()) {
                obj = (MessageObject) this.messagesDict.get((Integer) i$.next());
                if (obj != null) {
                    obj.messageOwner.unread = false;
                    updated = true;
                }
            }
            if (!updated) {
                return;
            }
            if (this.animationInProgress) {
                this.invalidateAfterAnimation = true;
            } else if (this.chatListView != null) {
                updateVisibleRows();
            }
        } else if (id == 6) {
            updated = false;
            i$ = args[0].iterator();
            while (i$.hasNext()) {
                Integer ids = (Integer) i$.next();
                obj = (MessageObject) this.messagesDict.get(ids);
                if (obj != null) {
                    int index = this.messages.indexOf(obj);
                    if (index != -1) {
                        this.messages.remove(index);
                        this.messagesDict.remove(ids);
                        ArrayList<MessageObject> dayArr = (ArrayList) this.messagesByDays.get(obj.dateKey);
                        dayArr.remove(obj);
                        if (dayArr.isEmpty()) {
                            this.messagesByDays.remove(obj.dateKey);
                            if (index != -1) {
                                this.messages.remove(index);
                            }
                        }
                        updated = true;
                    }
                }
            }
            if (!(!this.messages.isEmpty() || this.endReached || this.loading)) {
                this.progressView.setVisibility(0);
                this.chatListView.setEmptyView(null);
                this.maxMessageId = Integer.MAX_VALUE;
                this.minMessageId = ExploreByTouchHelper.INVALID_ID;
                this.maxDate = ExploreByTouchHelper.INVALID_ID;
                this.minDate = 0;
                MessagesController.Instance.loadMessages(this.dialog_id, 0, 30, 0, !this.cacheEndReaced, this.minDate, this.classGuid, false, false);
                this.loading = true;
            }
            if (updated && this.chatAdapter != null) {
                removeUnreadPlane(false);
                this.chatAdapter.notifyDataSetChanged();
            }
        } else if (id == 10) {
            Integer msgId = args[0];
            obj = (MessageObject) this.messagesDict.get(msgId);
            if (obj != null) {
                Integer newMsgId = args[1];
                this.messagesDict.remove(msgId);
                this.messagesDict.put(newMsgId, obj);
                obj.messageOwner.id = newMsgId.intValue();
                obj.messageOwner.send_state = 0;
                if (this.animationInProgress) {
                    this.invalidateAfterAnimation = true;
                } else if (this.chatListView != null) {
                    updateVisibleRows();
                }
                if (obj.messageOwner.attachPath != null && obj.messageOwner.attachPath.length() != 0) {
                    this.progressBarMap.remove(obj.messageOwner.attachPath);
                }
            }
        } else if (id == 9) {
            obj = (MessageObject) this.messagesDict.get((Integer) args[0]);
            if (obj != null) {
                if (!(obj.messageOwner.attachPath == null || obj.messageOwner.attachPath.length() == 0)) {
                    this.progressBarMap.remove(obj.messageOwner.attachPath);
                }
                obj.messageOwner.send_state = 0;
                if (this.animationInProgress) {
                    this.invalidateAfterAnimation = true;
                } else if (this.chatListView != null) {
                    updateVisibleRows();
                }
            }
        } else if (id == 11) {
            obj = (MessageObject) this.messagesDict.get((Integer) args[0]);
            if (obj != null) {
                obj.messageOwner.send_state = 2;
                if (this.animationInProgress) {
                    this.invalidateAfterAnimation = true;
                } else if (this.chatListView != null) {
                    updateVisibleRows();
                }
                if (obj.messageOwner.attachPath != null && obj.messageOwner.attachPath.length() != 0) {
                    this.progressBarMap.remove(obj.messageOwner.attachPath);
                }
            }
        } else if (id == 997) {
            MessagesController.Instance.sendMessage(((Double) args[0]).doubleValue(), ((Double) args[1]).doubleValue(), this.dialog_id);
            if (this.chatListView != null) {
                this.chatListView.setSelection(this.messages.size() + 1);
                this.scrollToTopOnResume = true;
            }
        } else if (id == 17) {
            int chatId = ((Integer) args[0]).intValue();
            if (this.currentChat != null && chatId == this.currentChat.id) {
                this.info = (ChatParticipants) args[1];
                updateOnlineCount();
            }
        } else if (id == 10002) {
            ProgressBar bar = (ProgressBar) this.progressBarMap.get(args[0]);
            if (bar != null) {
                bar.setProgress((int) (args[1].floatValue() * 100.0f));
            }
        } else if (id == 10005) {
            location = (String) args[0];
            if (this.loadingFile.containsKey(location)) {
                this.loadingFile.remove(location);
                if (this.animationInProgress) {
                    this.invalidateAfterAnimation = true;
                } else if (this.chatListView != null) {
                    updateVisibleRows();
                }
            }
        } else if (id == 10004) {
            location = (String) args[0];
            if (this.loadingFile.containsKey(location)) {
                this.loadingFile.remove(location);
                if (this.animationInProgress) {
                    this.invalidateAfterAnimation = true;
                } else if (this.chatListView != null) {
                    updateVisibleRows();
                }
            }
        } else if (id == 10003) {
            ArrayList<ProgressBar> arr2 = (ArrayList) this.loadingFile.get((String) args[0]);
            if (arr2 != null) {
                Float progress = (Float) args[1];
                i$ = arr2.iterator();
                while (i$.hasNext()) {
                    ((ProgressBar) i$.next()).setProgress((int) (progress.floatValue() * 100.0f));
                }
            }
        } else if (id == 13) {
            updateContactStatus();
            updateSubtitle();
        } else if (id == 21) {
            EncryptedChat chat = args[0];
            if (this.currentEncryptedChat != null && chat.id == this.currentEncryptedChat.id) {
                this.currentEncryptedChat = chat;
                updateContactStatus();
                updateSecretStatus();
            }
        } else if (id == 22) {
            int encId = ((Integer) args[0]).intValue();
            if (this.currentEncryptedChat != null && this.currentEncryptedChat.id == encId) {
                int date = ((Integer) args[1]).intValue();
                i$ = this.messages.iterator();
                while (i$.hasNext()) {
                    obj = (MessageObject) i$.next();
                    if (obj.messageOwner.out) {
                        if (obj.messageOwner.out && !obj.messageOwner.unread) {
                            break;
                        } else if (obj.messageOwner.date <= date) {
                            obj.messageOwner.unread = false;
                        }
                    }
                }
                if (this.chatListView != null) {
                    updateVisibleRows();
                }
            }
        }
    }

    private void updateContactStatus() {
        if (this.topPanel != null) {
            if (this.currentUser == null) {
                this.topPanel.setVisibility(8);
            } else if ((this.currentEncryptedChat == null || (this.currentEncryptedChat instanceof TL_encryptedChat)) && this.currentUser.id != 333000 && (this.currentUser.phone == null || this.currentUser.phone.length() == 0 || MessagesController.Instance.contactsDict.get(this.currentUser.id) == null || (MessagesController.Instance.contactsDict.size() == 0 && MessagesController.Instance.loadingContacts))) {
                this.topPanel.setVisibility(0);
                this.topPanelText.setShadowLayer(1.0f, 0.0f, (float) Utilities.dp(1), -7891037);
                if (this.isCustomTheme) {
                    this.topPlaneClose.setImageResource(C0419R.drawable.ic_msg_btn_cross_custom);
                    this.topPanel.setBackgroundResource(C0419R.drawable.top_pane_custom);
                } else {
                    this.topPlaneClose.setImageResource(C0419R.drawable.ic_msg_btn_cross_custom);
                    this.topPanel.setBackgroundResource(C0419R.drawable.top_pane);
                }
                if (this.currentUser.phone == null || this.currentUser.phone.length() == 0) {
                    if (MessagesController.Instance.hidenAddToContacts.get(this.currentUser.id) != null) {
                        this.topPanel.setVisibility(4);
                        return;
                    }
                    this.topPanelText.setText(getStringEntry(C0419R.string.ShareMyContactInfo));
                    this.topPlaneClose.setVisibility(8);
                    this.topPanel.setOnClickListener(new OnClickListener() {

                        class C04451 implements Runnable {
                            C04451() {
                            }

                            public void run() {
                                ChatActivity.this.chatListView.setSelectionFromTop(ChatActivity.this.messages.size() - 1, -10000 - ChatActivity.this.chatListView.getPaddingTop());
                            }
                        }

                        public void onClick(View v) {
                            MessagesController.Instance.hidenAddToContacts.put(ChatActivity.this.currentUser.id, ChatActivity.this.currentUser);
                            ChatActivity.this.topPanel.setVisibility(8);
                            MessagesController.Instance.sendMessage(UserConfig.currentUser, ChatActivity.this.dialog_id);
                            ChatActivity.this.chatListView.post(new C04451());
                        }
                    });
                } else if (MessagesController.Instance.hidenAddToContacts.get(this.currentUser.id) != null) {
                    this.topPanel.setVisibility(4);
                } else {
                    this.topPanelText.setText(getStringEntry(C0419R.string.AddToContacts));
                    this.topPlaneClose.setVisibility(0);
                    this.topPlaneClose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            MessagesController.Instance.hidenAddToContacts.put(ChatActivity.this.currentUser.id, ChatActivity.this.currentUser);
                            ChatActivity.this.topPanel.setVisibility(8);
                        }
                    });
                    this.topPanel.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ContactAddActivity fragment = new ContactAddActivity();
                            Bundle args = new Bundle();
                            args.putInt("user_id", ChatActivity.this.currentUser.id);
                            fragment.setArguments(args);
                            ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "add_contact_" + ChatActivity.this.currentUser.id, false);
                        }
                    });
                }
            } else {
                this.topPanel.setVisibility(8);
            }
        }
    }

    private void createEmojiPopup() {
        this.emojiView = new EmojiView(this.parentActivity);
        this.emojiView.setListener(new Listener() {
            public void onBackspace() {
                ChatActivity.this.messsageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
            }

            public void onEmojiSelected(String paramAnonymousString) {
                int i = ChatActivity.this.messsageEditText.getSelectionEnd();
                CharSequence localCharSequence = Emoji.replaceEmoji(paramAnonymousString);
                ChatActivity.this.messsageEditText.setText(ChatActivity.this.messsageEditText.getText().insert(i, localCharSequence));
                int j = i + localCharSequence.length();
                ChatActivity.this.messsageEditText.setSelection(j, j);
            }
        });
        this.emojiPopup = new PopupWindow(this.emojiView);
    }

    private void showEmojiPopup(boolean show) {
        if (this.parentActivity != null) {
            InputMethodManager localInputMethodManager = (InputMethodManager) this.parentActivity.getSystemService("input_method");
            if (show) {
                int currentHeight;
                if (this.emojiPopup == null) {
                    createEmojiPopup();
                }
                int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
                if (this.keyboardHeight <= 0) {
                    this.keyboardHeight = this.parentActivity.getSharedPreferences("emoji", 0).getInt("kbd_height", Emoji.scale(200.0f));
                }
                if (this.keyboardHeightLand <= 0) {
                    this.keyboardHeightLand = this.parentActivity.getSharedPreferences("emoji", 0).getInt("kbd_height_land3", Emoji.scale(200.0f));
                }
                if (rotation == 3 || rotation == 1) {
                    currentHeight = this.keyboardHeightLand;
                } else {
                    currentHeight = this.keyboardHeight;
                }
                this.emojiPopup.setHeight(MeasureSpec.makeMeasureSpec(currentHeight, 1073741824));
                this.emojiPopup.setWidth(MeasureSpec.makeMeasureSpec(this.contentView.getWidth(), 1073741824));
                this.emojiPopup.showAtLocation(this.parentActivity.getWindow().getDecorView(), 83, 0, 0);
                if (this.keyboardVisible) {
                    this.emojiButton.setImageResource(C0419R.drawable.ic_msg_panel_kb);
                    return;
                }
                this.contentView.setPadding(0, 0, 0, currentHeight);
                this.emojiButton.setImageResource(C0419R.drawable.ic_msg_panel_hide);
                return;
            }
            if (this.emojiButton != null) {
                this.emojiButton.setImageResource(C0419R.drawable.ic_msg_panel_smiles);
            }
            if (this.emojiPopup != null) {
                this.emojiPopup.dismiss();
            }
            if (this.contentView != null) {
                this.contentView.post(new Runnable() {
                    public void run() {
                        if (ChatActivity.this.contentView != null) {
                            ChatActivity.this.contentView.setPadding(0, 0, 0, 0);
                        }
                    }
                });
            }
        }
    }

    public void hideEmojiPopup() {
        if (this.emojiPopup != null && this.emojiPopup.isShowing()) {
            showEmojiPopup(false);
        }
    }

    public void applySelfActionBar() {
        if (this.parentActivity != null) {
            ActionBar actionBar = this.parentActivity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setCustomView(null);
            updateSubtitle();
            ((ApplicationActivity) this.parentActivity).fixBackButton();
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.isFinish) {
            if (!(this.firstStart || this.chatAdapter == null)) {
                this.chatAdapter.notifyDataSetChanged();
            }
            MessagesController.Instance.openned_dialog_id = this.dialog_id;
            if (this.scrollToTopOnResume) {
                if (!this.scrollToTopUnReadOnResume || this.unreadMessageObject == null) {
                    if (this.chatListView != null) {
                        this.chatListView.setSelection(this.messages.size() + 1);
                    }
                } else if (this.chatListView != null) {
                    this.chatListView.setSelectionFromTop(this.messages.size() - this.messages.indexOf(this.unreadMessageObject), (-this.chatListView.getPaddingTop()) - Utilities.dp(7));
                }
                this.scrollToTopUnReadOnResume = false;
                this.scrollToTopOnResume = false;
            }
            this.firstStart = false;
            this.swipeOpening = false;
            if (this.emojiView != null) {
                this.emojiView.loadRecents();
            }
            this.paused = false;
            if (this.readWhenResume && !this.messages.isEmpty()) {
                this.readWhenResume = false;
                MessagesController.Instance.markDialogAsRead(this.dialog_id, ((MessageObject) this.messages.get(0)).messageOwner.id, this.readWithMid, 0, this.readWithDate, true);
            }
            if (getActivity() != null) {
                ((ApplicationActivity) this.parentActivity).showActionBar();
                ((ApplicationActivity) this.parentActivity).updateActionBar();
                fixLayout();
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                String lastMessageText = preferences.getString("dialog_" + this.dialog_id, null);
                if (lastMessageText != null) {
                    Editor editor = preferences.edit();
                    editor.remove("dialog_" + this.dialog_id);
                    editor.commit();
                    this.ignoreTextChange = true;
                    this.messsageEditText.setText(lastMessageText);
                    this.messsageEditText.setSelection(this.messsageEditText.getText().length());
                    this.ignoreTextChange = false;
                }
                if (this.messsageEditText != null) {
                    this.messsageEditText.postDelayed(new Runnable() {
                        public void run() {
                            if (ChatActivity.this.messsageEditText != null) {
                                ChatActivity.this.messsageEditText.requestFocus();
                            }
                        }
                    }, 400);
                }
            }
        }
    }

    private void setTypingAnimation(boolean start) {
        TextView subtitle = (TextView) this.parentActivity.findViewById(C0419R.id.action_bar_subtitle);
        if (subtitle == null) {
            subtitle = (TextView) this.parentActivity.findViewById(this.parentActivity.getResources().getIdentifier("action_bar_subtitle", "id", "android"));
        }
        if (subtitle == null) {
            return;
        }
        if (start) {
            try {
                if (this.currentChat != null) {
                    subtitle.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.typing_dots_chat, 0, 0, 0);
                } else {
                    subtitle.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.typing_dots, 0, 0, 0);
                }
                subtitle.setCompoundDrawablePadding(Utilities.dp(4));
                AnimationDrawable mAnim = subtitle.getCompoundDrawables()[0];
                mAnim.setAlpha(200);
                mAnim.start();
                return;
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return;
            }
        }
        subtitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void onPause() {
        super.onPause();
        if (this.mActionMode != null) {
            this.mActionMode.finish();
            this.mActionMode = null;
        }
        hideEmojiPopup();
        this.paused = true;
        MessagesController.Instance.openned_dialog_id = 0;
        if (this.messsageEditText != null && this.messsageEditText.length() != 0) {
            Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit();
            editor.putString("dialog_" + this.dialog_id, this.messsageEditText.getText().toString());
            editor.commit();
        }
    }

    private void fixLayout() {
        if (this.chatListView != null) {
            this.chatListView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (ChatActivity.this.parentActivity == null) {
                        ChatActivity.this.chatListView.getViewTreeObserver().removeOnPreDrawListener(this);
                    } else {
                        int height;
                        int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
                        int currentActionBarHeight = ChatActivity.this.parentActivity.getSupportActionBar().getHeight();
                        if (currentActionBarHeight == Utilities.dp(48) || currentActionBarHeight == Utilities.dp(40)) {
                            height = Utilities.dp(48);
                            if (rotation == 3 || rotation == 1) {
                                height = Utilities.dp(40);
                            }
                        } else {
                            height = currentActionBarHeight;
                        }
                        if (ChatActivity.this.avatarImageView != null) {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ChatActivity.this.avatarImageView.getLayoutParams();
                            params.width = height;
                            params.height = height;
                            ChatActivity.this.avatarImageView.setLayoutParams(params);
                        }
                        ChatActivity.this.chatListView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (ChatActivity.this.currentEncryptedChat != null) {
                            TextView title = (TextView) ChatActivity.this.parentActivity.findViewById(C0419R.id.action_bar_title);
                            if (title == null) {
                                title = (TextView) ChatActivity.this.parentActivity.findViewById(ChatActivity.this.parentActivity.getResources().getIdentifier("action_bar_title", "id", "android"));
                            }
                            if (title != null) {
                                title.setCompoundDrawablesWithIntrinsicBounds(C0419R.drawable.ic_lock_white, 0, 0, 0);
                                title.setCompoundDrawablePadding(Utilities.dp(4));
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
        if (this.parentActivity != null) {
            Display display = this.parentActivity.getWindowManager().getDefaultDisplay();
            if (VERSION.SDK_INT < 13) {
                this.displaySize.set(display.getWidth(), display.getHeight());
            } else {
                display.getSize(this.displaySize);
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(C0419R.menu.chat_menu, menu);
        if (!(this.currentEncryptedChat == null || (this.currentEncryptedChat instanceof TL_encryptedChat)) || (this.currentChat != null && ((this.currentChat instanceof TL_chatForbidden) || this.currentChat.left))) {
            ((SupportMenuItem) menu.findItem(C0419R.id.chat_menu_attach)).setVisible(false);
        }
        this.avatarImageView = (BackupImageView) ((SupportMenuItem) menu.findItem(C0419R.id.chat_menu_avatar)).getActionView().findViewById(C0419R.id.chat_avatar_image);
        this.avatarImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ChatActivity.this.parentActivity != null) {
                    Bundle args;
                    if (ChatActivity.this.currentUser != null) {
                        UserProfileActivity fragment = new UserProfileActivity();
                        args = new Bundle();
                        args.putInt("user_id", ChatActivity.this.currentUser.id);
                        if (ChatActivity.this.currentEncryptedChat != null) {
                            args.putLong("dialog_id", ChatActivity.this.dialog_id);
                        }
                        fragment.setArguments(args);
                        ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment, "user_" + ChatActivity.this.currentUser.id, ChatActivity.this.swipeOpening);
                    } else if (ChatActivity.this.currentChat != null) {
                        if (ChatActivity.this.info != null) {
                            if (!(ChatActivity.this.info instanceof TL_chatParticipantsForbidden)) {
                                NotificationCenter.Instance.addToMemCache(5, ChatActivity.this.info);
                            } else {
                                return;
                            }
                        }
                        if (ChatActivity.this.currentChat.participants_count != 0 && !ChatActivity.this.currentChat.left && !(ChatActivity.this.currentChat instanceof TL_chatForbidden)) {
                            ChatProfileActivity fragment2 = new ChatProfileActivity();
                            args = new Bundle();
                            args.putInt("chat_id", ChatActivity.this.currentChat.id);
                            fragment2.setArguments(args);
                            ((ApplicationActivity) ChatActivity.this.parentActivity).presentFragment(fragment2, "chat_" + ChatActivity.this.currentChat.id, ChatActivity.this.swipeOpening);
                        }
                    }
                }
            }
        });
        FileLocation photo = null;
        int placeHolderId = 0;
        if (this.currentUser != null) {
            if (this.currentUser.photo != null) {
                photo = this.currentUser.photo.photo_small;
            }
            placeHolderId = Utilities.getUserAvatarForId(this.currentUser.id);
        } else if (this.currentChat != null) {
            if (this.currentChat.photo != null) {
                photo = this.currentChat.photo.photo_small;
            }
            placeHolderId = Utilities.getGroupAvatarForId(this.currentChat.id);
        }
        this.avatarImageView.setImage(photo, "50_50", placeHolderId);
    }

    private View getRowParentView(View v) {
        while (!(v.getTag() instanceof ChatListRowHolderEx)) {
            if (!(v.getParent() instanceof View)) {
                return null;
            }
            v = (View) v.getParent();
            if (v == null) {
                return null;
            }
        }
        return v;
    }

    public void createMenu(View v, boolean single) {
        if (this.mActionMode == null && this.parentActivity != null && getActivity() != null && !this.isFinish && !this.swipeOpening) {
            this.selectedMessagesCanCopyIds.clear();
            this.selectedObject = null;
            this.forwaringMessage = null;
            this.selectedMessagesIds.clear();
            View parentView = getRowParentView(v);
            if (parentView != null) {
                MessageObject message = ((ChatListRowHolderEx) parentView.getTag()).message;
                final int type = getMessageType(message);
                if (!single && type >= 2) {
                    addToSelectedMessages(message);
                    this.mActionMode = this.parentActivity.startSupportActionMode(this.mActionModeCallback);
                    updateActionModeTitle();
                    updateVisibleRows();
                } else if (type >= 0) {
                    this.selectedObject = message;
                    Builder builder = new Builder(getActivity());
                    CharSequence[] items = null;
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(UserConfig.clientUserId));
                    if (this.currentEncryptedChat == null) {
                        if (type == 0) {
                            items = new CharSequence[]{getStringEntry(C0419R.string.Retry), getStringEntry(C0419R.string.Delete)};
                        } else if (type == 1) {
                            items = new CharSequence[]{getStringEntry(C0419R.string.Delete)};
                        } else if (type == 2) {
                            items = new CharSequence[]{getStringEntry(C0419R.string.Forward), getStringEntry(C0419R.string.Delete)};
                        } else if (type == 3) {
                            items = new CharSequence[]{getStringEntry(C0419R.string.Forward), getStringEntry(C0419R.string.Copy), getStringEntry(C0419R.string.Delete)};
                        }
                    } else if (type == 0) {
                        items = new CharSequence[]{getStringEntry(C0419R.string.Retry), getStringEntry(C0419R.string.Delete)};
                    } else if (type == 1) {
                        items = new CharSequence[]{getStringEntry(C0419R.string.Delete)};
                    } else if (type == 2) {
                        items = new CharSequence[]{getStringEntry(C0419R.string.Delete)};
                    } else if (type == 3) {
                        items = new CharSequence[]{getStringEntry(C0419R.string.Copy), getStringEntry(C0419R.string.Delete)};
                    }
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (type == 0) {
                                if (i == 0) {
                                    ChatActivity.this.processSelectedOption(0);
                                } else if (i == 1) {
                                    ChatActivity.this.processSelectedOption(1);
                                }
                            } else if (type == 1) {
                                ChatActivity.this.processSelectedOption(1);
                            } else if (type == 2) {
                                if (ChatActivity.this.currentEncryptedChat != null) {
                                    ChatActivity.this.processSelectedOption(1);
                                } else if (i == 0) {
                                    ChatActivity.this.processSelectedOption(2);
                                } else if (i == 1) {
                                    ChatActivity.this.processSelectedOption(1);
                                }
                            } else if (type != 3) {
                            } else {
                                if (ChatActivity.this.currentEncryptedChat == null) {
                                    if (i == 0) {
                                        ChatActivity.this.processSelectedOption(2);
                                    } else if (i == 1) {
                                        ChatActivity.this.processSelectedOption(3);
                                    } else if (i == 2) {
                                        ChatActivity.this.processSelectedOption(1);
                                    }
                                } else if (i == 0) {
                                    ChatActivity.this.processSelectedOption(3);
                                } else if (i == 1) {
                                    ChatActivity.this.processSelectedOption(1);
                                }
                            }
                        }
                    });
                    builder.setTitle(C0419R.string.Message);
                    this.visibleDialog = builder.show();
                    this.visibleDialog.setCanceledOnTouchOutside(true);
                    this.visibleDialog.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            ChatActivity.this.visibleDialog = null;
                        }
                    });
                }
            }
        }
    }

    private void processSelectedOption(int option) {
        if (option == 0) {
            if (this.selectedObject != null && this.selectedObject.messageOwner.id < 0) {
                if (this.selectedObject.type == 0 || this.selectedObject.type == 1) {
                    if (this.selectedObject.messageOwner instanceof TL_messageForwarded) {
                        MessagesController.Instance.sendMessage(this.selectedObject, this.dialog_id);
                    } else {
                        MessagesController.Instance.sendMessage(this.selectedObject.messageOwner.message, this.dialog_id);
                    }
                } else if (this.selectedObject.type == 8 || this.selectedObject.type == 9) {
                    MessagesController.Instance.sendMessage(this.selectedObject, this.dialog_id);
                } else if (this.selectedObject.type == 4 || this.selectedObject.type == 5) {
                    MessagesController.Instance.sendMessage(this.selectedObject.messageOwner.media.geo.lat, this.selectedObject.messageOwner.media.geo._long, this.dialog_id);
                } else if (this.selectedObject.type == 2 || this.selectedObject.type == 3) {
                    if (this.selectedObject.messageOwner instanceof TL_messageForwarded) {
                        MessagesController.Instance.sendMessage(this.selectedObject, this.dialog_id);
                    } else {
                        MessagesController.Instance.sendMessage(this.selectedObject.messageOwner.media.photo, this.dialog_id);
                    }
                } else if (this.selectedObject.type == 6 || this.selectedObject.type == 7) {
                    if (this.selectedObject.messageOwner instanceof TL_messageForwarded) {
                        MessagesController.Instance.sendMessage(this.selectedObject, this.dialog_id);
                    } else {
                        TL_video video = (TL_video) this.selectedObject.messageOwner.media.video;
                        video.path = this.selectedObject.messageOwner.attachPath;
                        MessagesController.Instance.sendMessage(video, this.dialog_id);
                    }
                } else if (this.selectedObject.type == 12 || this.selectedObject.type == 13) {
                    User user = (User) MessagesController.Instance.users.get(Integer.valueOf(this.selectedObject.messageOwner.media.user_id));
                    MessagesController.Instance.sendMessage(user, this.dialog_id);
                } else if (this.selectedObject.type == 16 || this.selectedObject.type == 17) {
                    TL_document document = this.selectedObject.messageOwner.media.document;
                    document.path = this.selectedObject.messageOwner.attachPath;
                    MessagesController.Instance.sendMessage(document, this.dialog_id);
                }
                ArrayList<Integer> arr = new ArrayList();
                arr.add(Integer.valueOf(this.selectedObject.messageOwner.id));
                MessagesController.Instance.deleteMessages(arr);
                this.chatListView.setSelection(this.messages.size() + 1);
            }
        } else if (option == 1) {
            if (this.selectedObject != null) {
                ArrayList<Integer> ids = new ArrayList();
                ids.add(Integer.valueOf(this.selectedObject.messageOwner.id));
                removeUnreadPlane(true);
                MessagesController.Instance.deleteMessages(ids);
                this.selectedObject = null;
            }
        } else if (option == 2) {
            if (this.selectedObject != null) {
                this.forwaringMessage = this.selectedObject;
                this.selectedObject = null;
                MessagesActivity fragment = new MessagesActivity();
                fragment.selectAlertString = C0419R.string.ForwardMessagesTo;
                fragment.animationType = 1;
                Bundle args = new Bundle();
                args.putBoolean("onlySelect", true);
                args.putBoolean("serverOnly", true);
                fragment.setArguments(args);
                fragment.delegate = this;
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "select_chat", false);
            }
        } else if (option == 3 && this.selectedObject != null) {
            if (VERSION.SDK_INT < 11) {
                ((android.text.ClipboardManager) this.parentActivity.getSystemService("clipboard")).setText(this.selectedObject.messageText);
            } else {
                ((ClipboardManager) this.parentActivity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(PlusShare.KEY_CALL_TO_ACTION_LABEL, this.selectedObject.messageText));
            }
            this.selectedObject = null;
        }
    }

    public void didSelectFile(DocumentSelectActivity activity, String path, String name, String ext, long size) {
        activity.finishFragment();
        TL_document document = new TL_document();
        document.thumb = new TL_photoSizeEmpty();
        document.thumb.type = "s";
        document.id = 0;
        document.user_id = UserConfig.clientUserId;
        document.date = ConnectionsManager.Instance.getCurrentTime();
        document.file_name = name;
        document.size = (int) size;
        document.dc_id = 0;
        document.path = path;
        if (ext.length() != 0) {
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
            if (mimeType != null) {
                document.mime_type = mimeType;
            } else {
                document.mime_type = "application/octet-stream";
            }
        } else {
            document.mime_type = "application/octet-stream";
        }
        MessagesController.Instance.sendMessage(document, this.dialog_id);
    }

    public void didSelectDialog(MessagesActivity activity, long did) {
        if (this.dialog_id == 0) {
            return;
        }
        if (this.forwaringMessage != null || !this.selectedMessagesIds.isEmpty()) {
            ArrayList<Integer> ids;
            Iterator i$;
            if (did != this.dialog_id) {
                int lower_part = (int) did;
                if (lower_part != 0) {
                    ActionBarActivity inflaterActivity = this.parentActivity;
                    if (inflaterActivity == null) {
                        inflaterActivity = (ActionBarActivity) getActivity();
                    }
                    activity.removeSelfFromStack();
                    ChatActivity fragment = new ChatActivity();
                    Bundle bundle = new Bundle();
                    if (lower_part > 0) {
                        bundle.putInt("user_id", lower_part);
                        fragment.setArguments(bundle);
                        fragment.scrollToTopOnResume = true;
                        ActionBarActivity act = (ActionBarActivity) getActivity();
                        if (inflaterActivity != null) {
                            ((ApplicationActivity) inflaterActivity).presentFragment(fragment, "chat" + Math.random(), false);
                        }
                    } else if (lower_part < 0) {
                        bundle.putInt("chat_id", -lower_part);
                        fragment.setArguments(bundle);
                        fragment.scrollToTopOnResume = true;
                        if (inflaterActivity != null) {
                            ((ApplicationActivity) inflaterActivity).presentFragment(fragment, "chat" + Math.random(), false);
                        }
                    }
                    removeSelfFromStack();
                    if (this.forwaringMessage != null) {
                        if (this.forwaringMessage.messageOwner.id > 0) {
                            MessagesController.Instance.sendMessage(this.forwaringMessage, did);
                        }
                        this.forwaringMessage = null;
                        return;
                    }
                    ids = new ArrayList(this.selectedMessagesIds.keySet());
                    Collections.sort(ids);
                    i$ = ids.iterator();
                    while (i$.hasNext()) {
                        Integer id = (Integer) i$.next();
                        if (id.intValue() > 0) {
                            MessagesController.Instance.sendMessage((MessageObject) this.selectedMessagesIds.get(id), did);
                        }
                    }
                    this.selectedMessagesIds.clear();
                    return;
                }
                activity.finishFragment();
                return;
            }
            activity.finishFragment();
            if (this.forwaringMessage != null) {
                MessagesController.Instance.sendMessage(this.forwaringMessage, did);
                this.forwaringMessage = null;
            } else {
                ids = new ArrayList(this.selectedMessagesIds.keySet());
                Collections.sort(ids, new Comparator<Integer>() {
                    public int compare(Integer lhs, Integer rhs) {
                        return lhs.compareTo(rhs);
                    }
                });
                i$ = ids.iterator();
                while (i$.hasNext()) {
                    MessagesController.Instance.sendMessage((MessageObject) this.selectedMessagesIds.get((Integer) i$.next()), did);
                }
                this.selectedMessagesIds.clear();
            }
            this.chatListView.setSelection(this.messages.size() + 1);
            this.scrollToTopOnResume = true;
        }
    }

    public boolean onBackPressed() {
        if (this.emojiPopup == null || !this.emojiPopup.isShowing()) {
            return true;
        }
        hideEmojiPopup();
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishFragment();
                break;
            case C0419R.id.attach_photo:
                try {
                    Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File image = Utilities.generatePicturePath();
                    if (image != null) {
                        takePictureIntent.putExtra("output", Uri.fromFile(image));
                        this.currentPicturePath = image.getAbsolutePath();
                    }
                    startActivityForResult(takePictureIntent, 0);
                    break;
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                    break;
                }
            case C0419R.id.attach_gallery:
                try {
                    Intent photoPickerIntent = new Intent("android.intent.action.PICK");
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                    break;
                } catch (Exception e2) {
                    FileLog.m799e("tmessages", e2);
                    break;
                }
            case C0419R.id.attach_video:
                try {
                    Intent pickIntent = new Intent();
                    pickIntent.setType("video/*");
                    pickIntent.setAction("android.intent.action.GET_CONTENT");
                    pickIntent.putExtra("android.intent.extra.sizeLimit", 1048576000);
                    Intent takeVideoIntent = new Intent("android.media.action.VIDEO_CAPTURE");
                    File video = Utilities.generateVideoPath();
                    if (video != null) {
                        if (VERSION.SDK_INT > 16) {
                            takeVideoIntent.putExtra("output", Uri.fromFile(video));
                        }
                        takeVideoIntent.putExtra("android.intent.extra.sizeLimit", 1048576000);
                        this.currentPicturePath = video.getAbsolutePath();
                    }
                    Intent chooserIntent = Intent.createChooser(pickIntent, BuildConfig.FLAVOR);
                    chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", new Intent[]{takeVideoIntent});
                    startActivityForResult(chooserIntent, 2);
                    break;
                } catch (Exception e22) {
                    FileLog.m799e("tmessages", e22);
                    break;
                }
            case C0419R.id.attach_document:
                DocumentSelectActivity fragment = new DocumentSelectActivity();
                fragment.delegate = this;
                ((ApplicationActivity) this.parentActivity).presentFragment(fragment, "document", false);
                break;
            case C0419R.id.attach_location:
                if (isGoogleMapsInstalled()) {
                    ((ApplicationActivity) this.parentActivity).presentFragment(new LocationActivity(), "location", false);
                    break;
                }
                break;
        }
        return true;
    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo applicationInfo = ApplicationLoader.applicationContext.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (NameNotFoundException e) {
            Builder builder = new Builder(this.parentActivity);
            builder.setMessage("Install Google Maps?");
            builder.setCancelable(true);
            builder.setPositiveButton(getStringEntry(C0419R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        ChatActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.maps")));
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                    }
                }
            });
            builder.setNegativeButton(C0419R.string.Cancel, null);
            this.visibleDialog = builder.create();
            this.visibleDialog.setCanceledOnTouchOutside(true);
            this.visibleDialog.show();
            return false;
        }
    }

    private boolean spanClicked(ListView list, View view, int textViewId) {
        TextView widget = (TextView) view.findViewById(textViewId);
        if (widget == null) {
            return false;
        }
        try {
            list.offsetRectIntoDescendantCoords(widget, this.mLastTouch);
            int x = this.mLastTouch.right;
            x = (x - widget.getTotalPaddingLeft()) + widget.getScrollX();
            int y = (this.mLastTouch.bottom - widget.getTotalPaddingTop()) + widget.getScrollY();
            Layout layout = widget.getLayout();
            if (layout == null) {
                return false;
            }
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, (float) x);
            float left = layout.getLineLeft(line);
            if (left > ((float) x) || layout.getLineWidth(line) + left < ((float) x)) {
                return false;
            }
            Editable buffer = new SpannableStringBuilder(widget.getText());
            if (buffer == null) {
                return false;
            }
            ClickableSpan[] link = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan.class);
            if (link.length == 0) {
                return false;
            }
            link[0].onClick(widget);
            return true;
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return false;
        }
    }

    private void updateVisibleRows() {
        if (this.chatListView != null) {
            int count = this.chatListView.getChildCount();
            for (int a = 0; a < count; a++) {
                View view = this.chatListView.getChildAt(a);
                ChatListRowHolderEx tag = view.getTag();
                if (tag instanceof ChatListRowHolderEx) {
                    ChatListRowHolderEx holder = tag;
                    holder.update();
                    boolean disableSelection = false;
                    boolean selected = false;
                    if (this.mActionMode != null) {
                        if (this.selectedMessagesIds.containsKey(Integer.valueOf(holder.message.messageOwner.id))) {
                            view.setBackgroundColor(1714664933);
                            selected = true;
                        } else {
                            view.setBackgroundColor(0);
                        }
                        disableSelection = true;
                    } else {
                        view.setBackgroundColor(0);
                    }
                    int messageType = holder.message.type;
                    if (disableSelection) {
                        if (messageType == 2 || messageType == 4 || messageType == 6) {
                            if (selected) {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_photo_selected);
                            } else {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_photo);
                            }
                        } else if (messageType == 3 || messageType == 5 || messageType == 7) {
                            if (selected) {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_photo_selected);
                            } else {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_photo);
                            }
                        } else if (messageType == 0 || messageType == 8) {
                            if (selected) {
                                holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_out_selected);
                            } else {
                                holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_out);
                            }
                            holder.messageLayout.setPadding(Utilities.dp(11), Utilities.dp(7), Utilities.dp(18), 0);
                        } else if (messageType == 1 || messageType == 9) {
                            if (selected) {
                                holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_in_selected);
                            } else {
                                holder.messageLayout.setBackgroundResource(C0419R.drawable.msg_in);
                            }
                            holder.messageLayout.setPadding(Utilities.dp(19), Utilities.dp(7), Utilities.dp(9), 0);
                        } else if (messageType == 12) {
                            if (selected) {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_selected);
                            } else {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out);
                            }
                            holder.chatBubbleView.setPadding(Utilities.dp(6), Utilities.dp(6), Utilities.dp(18), 0);
                        } else if (messageType == 13) {
                            if (selected) {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_selected);
                            } else {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in);
                            }
                            holder.chatBubbleView.setPadding(Utilities.dp(15), Utilities.dp(6), Utilities.dp(9), 0);
                        } else if (messageType == 16) {
                            if (selected) {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out_selected);
                            } else {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_out);
                            }
                            holder.chatBubbleView.setPadding(Utilities.dp(9), Utilities.dp(9), Utilities.dp(18), 0);
                        } else if (messageType == 17) {
                            if (selected) {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in_selected);
                            } else {
                                holder.chatBubbleView.setBackgroundResource(C0419R.drawable.msg_in);
                            }
                            holder.chatBubbleView.setPadding(Utilities.dp(18), Utilities.dp(9), Utilities.dp(9), 0);
                        }
                    } else if (messageType == 2 || messageType == 4 || messageType == 6) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_outgoing_photo_states);
                    } else if (messageType == 3 || messageType == 5 || messageType == 7) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_incoming_photo_states);
                    } else if (messageType == 0 || messageType == 8) {
                        holder.messageLayout.setBackgroundResource(C0419R.drawable.chat_outgoing_text_states);
                        holder.messageLayout.setPadding(Utilities.dp(11), Utilities.dp(7), Utilities.dp(18), 0);
                    } else if (messageType == 1 || messageType == 9) {
                        holder.messageLayout.setBackgroundResource(C0419R.drawable.chat_incoming_text_states);
                        holder.messageLayout.setPadding(Utilities.dp(19), Utilities.dp(7), Utilities.dp(9), 0);
                    } else if (messageType == 12) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_outgoing_text_states);
                        holder.chatBubbleView.setPadding(Utilities.dp(6), Utilities.dp(6), Utilities.dp(18), 0);
                    } else if (messageType == 13) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_incoming_text_states);
                        holder.chatBubbleView.setPadding(Utilities.dp(15), Utilities.dp(6), Utilities.dp(9), 0);
                    } else if (messageType == 16) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_outgoing_text_states);
                        holder.chatBubbleView.setPadding(Utilities.dp(9), Utilities.dp(9), Utilities.dp(18), 0);
                    } else if (messageType == 17) {
                        holder.chatBubbleView.setBackgroundResource(C0419R.drawable.chat_incoming_text_states);
                        holder.chatBubbleView.setPadding(Utilities.dp(18), Utilities.dp(9), Utilities.dp(9), 0);
                    }
                }
            }
        }
    }
}
