package org.telegram.objects;

import android.graphics.Bitmap;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.Document;
import org.telegram.TL.TLRPC.Message;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_message;
import org.telegram.TL.TLRPC.TL_messageActionChatAddUser;
import org.telegram.TL.TLRPC.TL_messageActionChatCreate;
import org.telegram.TL.TLRPC.TL_messageActionChatDeletePhoto;
import org.telegram.TL.TLRPC.TL_messageActionChatDeleteUser;
import org.telegram.TL.TLRPC.TL_messageActionChatEditPhoto;
import org.telegram.TL.TLRPC.TL_messageActionChatEditTitle;
import org.telegram.TL.TLRPC.TL_messageActionLoginUnknownLocation;
import org.telegram.TL.TLRPC.TL_messageActionTTLChange;
import org.telegram.TL.TLRPC.TL_messageActionUserJoined;
import org.telegram.TL.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.TL.TLRPC.TL_messageForwarded;
import org.telegram.TL.TLRPC.TL_messageMediaAudio;
import org.telegram.TL.TLRPC.TL_messageMediaContact;
import org.telegram.TL.TLRPC.TL_messageMediaDocument;
import org.telegram.TL.TLRPC.TL_messageMediaEmpty;
import org.telegram.TL.TLRPC.TL_messageMediaGeo;
import org.telegram.TL.TLRPC.TL_messageMediaPhoto;
import org.telegram.TL.TLRPC.TL_messageMediaUnsupported;
import org.telegram.TL.TLRPC.TL_messageMediaVideo;
import org.telegram.TL.TLRPC.TL_messageService;
import org.telegram.TL.TLRPC.User;
import org.telegram.TL.TLRPC.Video;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ApplicationLoader;

public class MessageObject {
    public Object TAG;
    public String dateKey;
    public boolean deleted = false;
    public Bitmap imagePreview;
    public Message messageOwner;
    public CharSequence messageText;
    public ArrayList<PhotoObject> photoThumbs;
    public PhotoObject previewPhoto;
    public int type;

    public MessageObject(Message message, AbstractMap<Integer, User> users) {
        this.messageOwner = message;
        Iterator i$;
        if (message instanceof TL_messageService) {
            if (message.action != null) {
                User fromUser = (User) users.get(Integer.valueOf(message.from_id));
                if (fromUser == null) {
                    fromUser = (User) MessagesController.Instance.users.get(Integer.valueOf(message.from_id));
                }
                if (message.action instanceof TL_messageActionChatCreate) {
                    if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionCreateGroup).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou));
                    } else if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionCreateGroup).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionCreateGroup).replace("un1", BuildConfig.FLAVOR);
                    }
                } else if (message.action instanceof TL_messageActionChatDeleteUser) {
                    if (message.action.user_id != message.from_id) {
                        User who = (User) users.get(Integer.valueOf(message.action.user_id));
                        if (who == null) {
                            MessagesController.Instance.users.get(Integer.valueOf(message.action.user_id));
                        }
                        str = ApplicationLoader.applicationContext.getString(C0419R.string.ActionKickUser);
                        if (who == null || fromUser == null) {
                            this.messageText = str.replace("un2", BuildConfig.FLAVOR).replace("un1", BuildConfig.FLAVOR);
                        } else if (message.from_id == UserConfig.clientUserId) {
                            this.messageText = str.replace("un2", Utilities.formatName(who.first_name, who.last_name)).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou));
                        } else if (message.action.user_id == UserConfig.clientUserId) {
                            this.messageText = str.replace("un2", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou)).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                        } else {
                            this.messageText = str.replace("un2", Utilities.formatName(who.first_name, who.last_name)).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                        }
                    } else if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionLeftUser).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou));
                    } else if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionLeftUser).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionLeftUser).replace("un1", BuildConfig.FLAVOR);
                    }
                } else if (message.action instanceof TL_messageActionChatAddUser) {
                    User whoUser = (User) users.get(Integer.valueOf(message.action.user_id));
                    if (whoUser == null) {
                        MessagesController.Instance.users.get(Integer.valueOf(message.action.user_id));
                    }
                    str = ApplicationLoader.applicationContext.getString(C0419R.string.ActionAddUser);
                    if (whoUser == null || fromUser == null) {
                        this.messageText = str.replace("un2", BuildConfig.FLAVOR).replace("un1", BuildConfig.FLAVOR);
                    } else if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = str.replace("un2", Utilities.formatName(whoUser.first_name, whoUser.last_name)).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou));
                    } else if (message.action.user_id == UserConfig.clientUserId) {
                        this.messageText = str.replace("un2", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou)).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                    } else {
                        this.messageText = str.replace("un2", Utilities.formatName(whoUser.first_name, whoUser.last_name)).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                    }
                } else if (message.action instanceof TL_messageActionChatEditPhoto) {
                    this.photoThumbs = new ArrayList();
                    i$ = message.action.photo.sizes.iterator();
                    while (i$.hasNext()) {
                        this.photoThumbs.add(new PhotoObject((PhotoSize) i$.next()));
                    }
                    if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionChangedPhoto).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou));
                    } else if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionChangedPhoto).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionChangedPhoto).replace("un1", BuildConfig.FLAVOR);
                    }
                } else if (message.action instanceof TL_messageActionChatEditTitle) {
                    if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionChangedTitle).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou)).replace("un2", message.action.title);
                    } else if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionChangedTitle).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name)).replace("un2", message.action.title);
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionChangedTitle).replace("un1", BuildConfig.FLAVOR).replace("un2", message.action.title);
                    }
                } else if (message.action instanceof TL_messageActionChatDeletePhoto) {
                    if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionRemovedPhoto).replace("un1", ApplicationLoader.applicationContext.getString(C0419R.string.FromYou));
                    } else if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionRemovedPhoto).replace("un1", Utilities.formatName(fromUser.first_name, fromUser.last_name));
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.ActionRemovedPhoto).replace("un1", BuildConfig.FLAVOR);
                    }
                } else if (message.action instanceof TL_messageActionTTLChange) {
                    if (message.action.ttl != 0) {
                        String timeString;
                        if (message.action.ttl == 2) {
                            timeString = ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetime2s);
                        } else if (message.action.ttl == 5) {
                            timeString = ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetime5s);
                        } else if (message.action.ttl == 60) {
                            timeString = ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetime1m);
                        } else if (message.action.ttl == ConnectionsManager.DC_UPDATE_TIME) {
                            timeString = ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetime1h);
                        } else if (message.action.ttl == 86400) {
                            timeString = ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetime1d);
                        } else if (message.action.ttl == 604800) {
                            timeString = ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetime1w);
                        } else {
                            timeString = String.format("%d", new Object[]{Integer.valueOf(message.action.ttl)});
                        }
                        if (message.from_id == UserConfig.clientUserId) {
                            this.messageText = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetimeChangedOutgoing), new Object[]{timeString});
                        } else if (fromUser != null) {
                            this.messageText = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetimeChanged), new Object[]{fromUser.first_name, timeString});
                        } else {
                            this.messageText = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetimeChanged), new Object[]{BuildConfig.FLAVOR, timeString});
                        }
                    } else if (message.from_id == UserConfig.clientUserId) {
                        this.messageText = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetimeRemoved), new Object[]{ApplicationLoader.applicationContext.getString(C0419R.string.FromYou)});
                    } else if (fromUser != null) {
                        this.messageText = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetimeRemoved), new Object[]{fromUser.first_name});
                    } else {
                        this.messageText = String.format(ApplicationLoader.applicationContext.getString(C0419R.string.MessageLifetimeRemoved), new Object[]{BuildConfig.FLAVOR});
                    }
                } else if (message.action instanceof TL_messageActionLoginUnknownLocation) {
                    this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationUnrecognizedDevice, new Object[]{message.action.title, message.action.address});
                } else if (message.action instanceof TL_messageActionUserJoined) {
                    if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationContactJoined, new Object[]{Utilities.formatName(fromUser.first_name, fromUser.last_name)});
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationContactJoined, new Object[]{BuildConfig.FLAVOR});
                    }
                } else if (message.action instanceof TL_messageActionUserUpdatedPhoto) {
                    if (fromUser != null) {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationContactNewPhoto, new Object[]{Utilities.formatName(fromUser.first_name, fromUser.last_name)});
                    } else {
                        this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.NotificationContactNewPhoto, new Object[]{BuildConfig.FLAVOR});
                    }
                }
            }
        } else if (message.media == null || (message.media instanceof TL_messageMediaEmpty)) {
            this.messageText = message.message;
        } else if (message.media instanceof TL_messageMediaPhoto) {
            this.photoThumbs = new ArrayList();
            i$ = message.media.photo.sizes.iterator();
            while (i$.hasNext()) {
                obj = new PhotoObject((PhotoSize) i$.next());
                this.photoThumbs.add(obj);
                if (this.imagePreview == null && obj.image != null) {
                    this.imagePreview = obj.image;
                }
            }
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.AttachPhoto);
        } else if (message.media instanceof TL_messageMediaVideo) {
            this.photoThumbs = new ArrayList();
            obj = new PhotoObject(message.media.video.thumb);
            this.photoThumbs.add(obj);
            if (this.imagePreview == null && obj.image != null) {
                this.imagePreview = obj.image;
            }
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.AttachVideo);
        } else if (message.media instanceof TL_messageMediaGeo) {
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.AttachLocation);
        } else if (message.media instanceof TL_messageMediaContact) {
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.AttachContact);
        } else if (message.media instanceof TL_messageMediaUnsupported) {
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.UnsuppotedMedia);
        } else if (message.media instanceof TL_messageMediaDocument) {
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.AttachDocument);
        } else if (message.media instanceof TL_messageMediaAudio) {
            this.messageText = ApplicationLoader.applicationContext.getString(C0419R.string.AttachAudio);
        }
        this.messageText = Emoji.replaceEmoji(this.messageText);
        if ((message instanceof TL_message) || ((message instanceof TL_messageForwarded) && (message.media == null || !(message.media instanceof TL_messageMediaEmpty)))) {
            if (message.media == null || (message.media instanceof TL_messageMediaEmpty)) {
                if (message.from_id == UserConfig.clientUserId) {
                    this.type = 0;
                } else {
                    this.type = 1;
                }
            } else if (message.media == null || !(message.media instanceof TL_messageMediaPhoto)) {
                if (message.media == null || !(message.media instanceof TL_messageMediaGeo)) {
                    if (message.media == null || !(message.media instanceof TL_messageMediaVideo)) {
                        if (message.media == null || !(message.media instanceof TL_messageMediaContact)) {
                            if (message.media == null || !(message.media instanceof TL_messageMediaUnsupported)) {
                                if (message.media == null || !(message.media instanceof TL_messageMediaDocument)) {
                                    if (message.media != null && (message.media instanceof TL_messageMediaAudio)) {
                                        if (message.from_id == UserConfig.clientUserId) {
                                            this.type = 0;
                                        } else {
                                            this.type = 1;
                                        }
                                    }
                                } else if (message.from_id == UserConfig.clientUserId) {
                                    this.type = 16;
                                } else {
                                    this.type = 17;
                                }
                            } else if (message.from_id == UserConfig.clientUserId) {
                                this.type = 0;
                            } else {
                                this.type = 1;
                            }
                        } else if (message.from_id == UserConfig.clientUserId) {
                            this.type = 12;
                        } else {
                            this.type = 13;
                        }
                    } else if (message.from_id == UserConfig.clientUserId) {
                        this.type = 6;
                    } else {
                        this.type = 7;
                    }
                } else if (message.from_id == UserConfig.clientUserId) {
                    this.type = 4;
                } else {
                    this.type = 5;
                }
            } else if (message.from_id == UserConfig.clientUserId) {
                this.type = 2;
            } else {
                this.type = 3;
            }
        } else if (message instanceof TL_messageService) {
            if (message.action instanceof TL_messageActionLoginUnknownLocation) {
                this.type = 1;
            } else if ((message.action instanceof TL_messageActionChatEditPhoto) || (message.action instanceof TL_messageActionUserUpdatedPhoto)) {
                this.type = 11;
            } else {
                this.type = 10;
            }
        } else if (message instanceof TL_messageForwarded) {
            if (message.from_id == UserConfig.clientUserId) {
                this.type = 8;
            } else {
                this.type = 9;
            }
        }
        Calendar rightNow = new GregorianCalendar();
        rightNow.setTimeInMillis(((long) this.messageOwner.date) * 1000);
        int dateDay = rightNow.get(6);
        int dateYear = rightNow.get(1);
        int dateMonth = rightNow.get(2);
        this.dateKey = String.format("%d_%02d_%02d", new Object[]{Integer.valueOf(dateYear), Integer.valueOf(dateMonth), Integer.valueOf(dateDay)});
    }

    public String getFileName() {
        if (this.messageOwner.media instanceof TL_messageMediaVideo) {
            return getAttachFileName(this.messageOwner.media.video);
        }
        if (this.messageOwner.media instanceof TL_messageMediaDocument) {
            return getAttachFileName(this.messageOwner.media.document);
        }
        return BuildConfig.FLAVOR;
    }

    public static String getAttachFileName(TLObject attach) {
        if (attach instanceof Video) {
            Video video = (Video) attach;
            return video.dc_id + "_" + video.id + ".mp4";
        } else if (attach instanceof Document) {
            Document document = (Document) attach;
            String ext = document.file_name;
            if (ext != null) {
                int idx = ext.lastIndexOf(".");
                if (idx != -1) {
                    ext = ext.substring(idx);
                    if (ext.length() <= 1) {
                        return document.dc_id + "_" + document.id + ext;
                    }
                    return document.dc_id + "_" + document.id;
                }
            }
            ext = BuildConfig.FLAVOR;
            if (ext.length() <= 1) {
                return document.dc_id + "_" + document.id;
            }
            return document.dc_id + "_" + document.id + ext;
        } else if (!(attach instanceof PhotoSize)) {
            return BuildConfig.FLAVOR;
        } else {
            PhotoSize photo = (PhotoSize) attach;
            return photo.location.volume_id + "_" + photo.location.local_id + ".jpg";
        }
    }
}
