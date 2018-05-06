package org.telegram.TL;

import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.SerializedData;

public class TLRPC {

    public static class Audio extends TLObject {
        public long access_hash;
        public int date;
        public int dc_id;
        public int duration;
        public long id;
        public int size;
        public int user_id;
    }

    public static class BadMsgNotification extends TLObject {
        public long bad_msg_id;
        public int bad_msg_seqno;
        public int error_code;
        public long new_server_salt;
    }

    public static class Bool extends TLObject {
    }

    public static class Chat extends TLObject {
        public long access_hash;
        public String address;
        public boolean checked_in;
        public int date;
        public GeoPoint geo;
        public int id;
        public boolean left;
        public int participants_count;
        public ChatPhoto photo;
        public String title;
        public String venue;
        public int version;
    }

    public static class ChatParticipants extends TLObject {
        public int admin_id;
        public int chat_id;
        public ArrayList<TL_chatParticipant> participants = new ArrayList();
        public int version;
    }

    public static class ChatPhoto extends TLObject {
        public FileLocation photo_big;
        public FileLocation photo_small;
    }

    public static class DecryptedMessage extends TLObject {
        public TL_decryptedMessageActionSetMessageTTL action;
        public DecryptedMessageMedia media;
        public String message;
        public byte[] random_bytes;
        public long random_id;
    }

    public static class DecryptedMessageMedia extends TLObject {
        public double _long;
        public int duration;
        public String file_name;
        public String first_name;
        public int f55h;
        public byte[] iv;
        public byte[] key;
        public String last_name;
        public double lat;
        public String mime_type;
        public String phone_number;
        public int size;
        public byte[] thumb;
        public int thumb_h;
        public int thumb_w;
        public int user_id;
        public int f56w;
    }

    public static class DestroySessionRes extends TLObject {
        public long session_id;
    }

    public static class Document extends TLObject {
        public long access_hash;
        public int date;
        public int dc_id;
        public String file_name;
        public long id;
        public byte[] iv;
        public byte[] key;
        public String mime_type;
        public String path;
        public int size;
        public PhotoSize thumb;
        public int user_id;
    }

    public static class EncryptedChat extends TLObject {
        public byte[] a_or_b;
        public long access_hash;
        public int admin_id;
        public byte[] auth_key;
        public int date;
        public byte[] g_a;
        public byte[] g_a_or_b;
        public int id;
        public long key_fingerprint;
        public int participant_id;
        public int ttl;
        public int user_id;
    }

    public static class EncryptedFile extends TLObject {
        public long access_hash;
        public int dc_id;
        public long id;
        public int key_fingerprint;
        public int size;
    }

    public static class EncryptedMessage extends TLObject {
        public byte[] bytes;
        public int chat_id;
        public int date;
        public EncryptedFile file;
        public long random_id;
    }

    public static class FileLocation extends TLObject {
        public int dc_id;
        public byte[] iv;
        public byte[] key;
        public int local_id;
        public long secret;
        public long volume_id;
    }

    public static class GeoChatMessage extends TLObject {
        public MessageAction action;
        public int chat_id;
        public int date;
        public int from_id;
        public int id;
        public MessageMedia media;
        public String message;
    }

    public static class GeoPoint extends TLObject {
        public double _long;
        public double lat;
    }

    public static class InputAudio extends TLObject {
        public long access_hash;
        public long id;
    }

    public static class InputChatPhoto extends TLObject {
        public InputPhotoCrop crop;
        public InputFile file;
        public InputPhoto id;
    }

    public static class InputDocument extends TLObject {
        public long access_hash;
        public long id;
    }

    public static class InputEncryptedFile extends TLObject {
        public long access_hash;
        public long id;
        public int key_fingerprint;
        public String md5_checksum;
        public int parts;
    }

    public static class InputFile extends TLObject {
        public long id;
        public String md5_checksum;
        public String name;
        public int parts;
    }

    public static class InputFileLocation extends TLObject {
        public long access_hash;
        public long id;
        public int local_id;
        public long secret;
        public long volume_id;
    }

    public static class InputGeoPoint extends TLObject {
        public double _long;
        public double lat;
    }

    public static class InputMedia extends TLObject {
        public int duration;
        public InputFile file;
        public String file_name;
        public String first_name;
        public InputGeoPoint geo_point;
        public int f57h;
        public String last_name;
        public String mime_type;
        public String phone_number;
        public InputFile thumb;
        public int f58w;
    }

    public static class InputNotifyPeer extends TLObject {
    }

    public static class InputPeer extends TLObject {
        public long access_hash;
        public int chat_id;
        public int user_id;
    }

    public static class InputPeerNotifyEvents extends TLObject {
    }

    public static class InputPhoto extends TLObject {
        public long access_hash;
        public long id;
    }

    public static class InputPhotoCrop extends TLObject {
        public double crop_left;
        public double crop_top;
        public double crop_width;
    }

    public static class InputUser extends TLObject {
        public long access_hash;
        public int user_id;
    }

    public static class InputVideo extends TLObject {
        public long access_hash;
        public long id;
    }

    public static class Message extends TLObject {
        public MessageAction action;
        public String attachPath = BuildConfig.FLAVOR;
        public int date;
        public long dialog_id;
        public int from_id;
        public int fwd_date;
        public int fwd_from_id;
        public int fwd_msg_id = 0;
        public int id;
        public int local_id = 0;
        public MessageMedia media;
        public String message;
        public boolean out;
        public long random_id;
        public int send_state = 0;
        public Peer to_id;
        public int ttl;
        public boolean unread;
    }

    public static class MessageAction extends TLObject {
        public String address;
        public UserProfilePhoto newUserPhoto;
        public Photo photo;
        public String title;
        public int ttl;
        public int user_id;
        public ArrayList<Integer> users = new ArrayList();
    }

    public static class MessageMedia extends TLObject {
        public Audio audio;
        public byte[] bytes;
        public Document document;
        public String first_name;
        public GeoPoint geo;
        public String last_name;
        public String phone_number;
        public Photo photo;
        public int user_id;
        public Video video;
    }

    public static class MessagesFilter extends TLObject {
    }

    public static class MsgDetailedInfo extends TLObject {
        public long answer_msg_id;
        public int bytes;
        public long msg_id;
        public int status;
    }

    public static class Peer extends TLObject {
        public int chat_id;
        public int user_id;
    }

    public static class PeerNotifyEvents extends TLObject {
    }

    public static class PeerNotifySettings extends TLObject {
        public int events_mask;
        public int mute_until;
        public boolean show_previews;
        public String sound;
    }

    public static class Photo extends TLObject {
        public long access_hash;
        public String caption;
        public int date;
        public GeoPoint geo;
        public long id;
        public ArrayList<PhotoSize> sizes = new ArrayList();
        public int user_id;
    }

    public static class PhotoSize extends TLObject {
        public byte[] bytes;
        public int f59h;
        public FileLocation location;
        public int size;
        public String type;
        public int f60w;
    }

    public static class RpcDropAnswer extends TLObject {
        public int bytes;
        public long msg_id;
        public int seq_no;
    }

    public static class RpcError extends TLObject {
        public int error_code;
        public String error_message;
        public long query_id;
    }

    public static class Server_DH_Params extends TLObject {
        public byte[] encrypted_answer;
        public byte[] new_nonce_hash;
        public byte[] nonce;
        public byte[] server_nonce;
    }

    public static class Set_client_DH_params_answer extends TLObject {
        public byte[] new_nonce_hash1;
        public byte[] new_nonce_hash2;
        public byte[] new_nonce_hash3;
        public byte[] nonce;
        public byte[] server_nonce;
    }

    public static class TL_account_getNotifySettings extends TLObject {
        public static int constructor = 313765169;
        public InputNotifyPeer peer;

        public Class responseClass() {
            return PeerNotifySettings.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputNotifyPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
        }
    }

    public static class TL_account_getWallPapers extends TLObject {
        public static int constructor = -1068696894;

        public Class responseClass() {
            return Vector.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }

        public void parseVector(Vector vector, SerializedData data) {
            int size = data.readInt32();
            for (int a = 0; a < size; a++) {
                vector.objects.add(TLClassStore.Instance().TLdeserialize(data, data.readInt32()));
            }
        }
    }

    public static class TL_account_registerDevice extends TLObject {
        public static int constructor = 1147957548;
        public boolean app_sandbox;
        public String app_version;
        public String device_model;
        public String lang_code;
        public String system_version;
        public String token;
        public int token_type;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.token_type = stream.readInt32();
            this.token = stream.readString();
            this.device_model = stream.readString();
            this.system_version = stream.readString();
            this.app_version = stream.readString();
            this.app_sandbox = stream.readBool();
            this.lang_code = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.token_type);
            stream.writeString(this.token);
            stream.writeString(this.device_model);
            stream.writeString(this.system_version);
            stream.writeString(this.app_version);
            stream.writeBool(this.app_sandbox);
            stream.writeString(this.lang_code);
        }
    }

    public static class TL_account_resetNotifySettings extends TLObject {
        public static int constructor = -612493497;

        public Class responseClass() {
            return Bool.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_account_unregisterDevice extends TLObject {
        public static int constructor = 1707432768;
        public String token;
        public int token_type;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.token_type = stream.readInt32();
            this.token = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.token_type);
            stream.writeString(this.token);
        }
    }

    public static class TL_account_updateNotifySettings extends TLObject {
        public static int constructor = -2067899501;
        public InputNotifyPeer peer;
        public TL_inputPeerNotifySettings settings;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputNotifyPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.settings = (TL_inputPeerNotifySettings) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            this.settings.serializeToStream(stream);
        }
    }

    public static class TL_account_updateProfile extends TLObject {
        public static int constructor = -259486360;
        public String first_name;
        public String last_name;

        public Class responseClass() {
            return User.class;
        }

        public void readParams(SerializedData stream) {
            this.first_name = stream.readString();
            this.last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
        }
    }

    public static class TL_account_updateStatus extends TLObject {
        public static int constructor = 1713919532;
        public boolean offline;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.offline = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeBool(this.offline);
        }
    }

    public static class TL_auth_authorization extends TLObject {
        public static int constructor = -155815004;
        public int expires;
        public User user;

        public void readParams(SerializedData stream) {
            this.expires = stream.readInt32();
            this.user = (User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.expires);
            this.user.serializeToStream(stream);
        }
    }

    public static class TL_auth_checkPhone extends TLObject {
        public static int constructor = 1877286395;
        public String phone_number;

        public Class responseClass() {
            return TL_auth_checkedPhone.class;
        }

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
        }
    }

    public static class TL_auth_checkedPhone extends TLObject {
        public static int constructor = -486486981;
        public boolean phone_invited;
        public boolean phone_registered;

        public void readParams(SerializedData stream) {
            this.phone_registered = stream.readBool();
            this.phone_invited = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeBool(this.phone_registered);
            stream.writeBool(this.phone_invited);
        }
    }

    public static class TL_auth_exportAuthorization extends TLObject {
        public static int constructor = -440401971;
        public int dc_id;

        public Class responseClass() {
            return TL_auth_exportedAuthorization.class;
        }

        public void readParams(SerializedData stream) {
            this.dc_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.dc_id);
        }
    }

    public static class TL_auth_exportedAuthorization extends TLObject {
        public static int constructor = -543777747;
        public byte[] bytes;
        public int id;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_auth_importAuthorization extends TLObject {
        public static int constructor = -470837741;
        public byte[] bytes;
        public int id;

        public Class responseClass() {
            return TL_auth_authorization.class;
        }

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_auth_logOut extends TLObject {
        public static int constructor = 1461180992;

        public Class responseClass() {
            return Bool.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_auth_resetAuthorizations extends TLObject {
        public static int constructor = -1616179942;

        public Class responseClass() {
            return Bool.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_auth_sendCall extends TLObject {
        public static int constructor = 63247716;
        public String phone_code_hash;
        public String phone_number;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.phone_code_hash = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeString(this.phone_code_hash);
        }
    }

    public static class TL_auth_sendCode extends TLObject {
        public static int constructor = 1988976461;
        public String api_hash;
        public int api_id;
        public String lang_code;
        public String phone_number;
        public int sms_type;

        public Class responseClass() {
            return TL_auth_sentCode.class;
        }

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.sms_type = stream.readInt32();
            this.api_id = stream.readInt32();
            this.api_hash = stream.readString();
            this.lang_code = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeInt32(this.sms_type);
            stream.writeInt32(this.api_id);
            stream.writeString(this.api_hash);
            stream.writeString(this.lang_code);
        }
    }

    public static class TL_auth_sendInvites extends TLObject {
        public static int constructor = 1998331287;
        public String message;
        public ArrayList<String> phone_numbers = new ArrayList();

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.phone_numbers.add(stream.readString());
            }
            this.message = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.phone_numbers.size());
            Iterator i$ = this.phone_numbers.iterator();
            while (i$.hasNext()) {
                stream.writeString((String) i$.next());
            }
            stream.writeString(this.message);
        }
    }

    public static class TL_auth_sentCode extends TLObject {
        public static int constructor = 571849917;
        public String phone_code_hash;
        public boolean phone_registered;

        public void readParams(SerializedData stream) {
            this.phone_registered = stream.readBool();
            this.phone_code_hash = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeBool(this.phone_registered);
            stream.writeString(this.phone_code_hash);
        }
    }

    public static class TL_auth_signIn extends TLObject {
        public static int constructor = -1126886015;
        public String phone_code;
        public String phone_code_hash;
        public String phone_number;

        public Class responseClass() {
            return TL_auth_authorization.class;
        }

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.phone_code_hash = stream.readString();
            this.phone_code = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeString(this.phone_code_hash);
            stream.writeString(this.phone_code);
        }
    }

    public static class TL_auth_signUp extends TLObject {
        public static int constructor = 453408308;
        public String first_name;
        public String last_name;
        public String phone_code;
        public String phone_code_hash;
        public String phone_number;

        public Class responseClass() {
            return TL_auth_authorization.class;
        }

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.phone_code_hash = stream.readString();
            this.phone_code = stream.readString();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeString(this.phone_code_hash);
            stream.writeString(this.phone_code);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
        }
    }

    public static class TL_chatFull extends TLObject {
        public static int constructor = 1661886910;
        public Photo chat_photo;
        public int id;
        public PeerNotifySettings notify_settings;
        public ChatParticipants participants;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.participants = (ChatParticipants) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.chat_photo = (Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.notify_settings = (PeerNotifySettings) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            this.participants.serializeToStream(stream);
            this.chat_photo.serializeToStream(stream);
            this.notify_settings.serializeToStream(stream);
        }
    }

    public static class TL_chatLocated extends TLObject {
        public static int constructor = 909233996;
        public int chat_id;
        public int distance;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.distance = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.distance);
        }
    }

    public static class TL_chatParticipant extends TLObject {
        public static int constructor = -925415106;
        public int date;
        public int inviter_id;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.inviter_id = stream.readInt32();
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.inviter_id);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_client_DH_inner_data extends TLObject {
        public static int constructor = 1715713620;
        public byte[] g_b;
        public byte[] nonce;
        public long retry_id;
        public byte[] server_nonce;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.retry_id = stream.readInt64();
            this.g_b = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeInt64(this.retry_id);
            stream.writeByteArray(this.g_b);
        }
    }

    public static class TL_config extends TLObject {
        public static int constructor = 590174469;
        public int chat_size_max;
        public int date;
        public ArrayList<TL_dcOption> dc_options = new ArrayList();
        public boolean test_mode;
        public int this_dc;

        public void readParams(SerializedData stream) {
            this.date = stream.readInt32();
            this.test_mode = stream.readBool();
            this.this_dc = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.dc_options.add((TL_dcOption) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.chat_size_max = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.date);
            stream.writeBool(this.test_mode);
            stream.writeInt32(this.this_dc);
            stream.writeInt32(481674261);
            stream.writeInt32(this.dc_options.size());
            Iterator i$ = this.dc_options.iterator();
            while (i$.hasNext()) {
                ((TL_dcOption) i$.next()).serializeToStream(stream);
            }
            stream.writeInt32(this.chat_size_max);
        }
    }

    public static class TL_contact extends TLObject {
        public static int constructor = -116274796;
        public boolean mutual;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.mutual = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeBool(this.mutual);
        }
    }

    public static class TL_contactBlocked extends TLObject {
        public static int constructor = 1444661369;
        public int date;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_contactFound extends TLObject {
        public static int constructor = -360210539;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_contactStatus extends TLObject {
        public static int constructor = -1434994573;
        public int expires;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.expires = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.expires);
        }
    }

    public static class TL_contactSuggested extends TLObject {
        public static int constructor = 1038193057;
        public int mutual_contacts;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.mutual_contacts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.mutual_contacts);
        }
    }

    public static class TL_contacts_block extends TLObject {
        public static int constructor = 858475004;
        public InputUser id;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_contacts_deleteContact extends TLObject {
        public static int constructor = -1902823612;
        public InputUser id;

        public Class responseClass() {
            return TL_contacts_link.class;
        }

        public void readParams(SerializedData stream) {
            this.id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_contacts_deleteContacts extends TLObject {
        public static int constructor = 1504393374;
        public ArrayList<InputUser> id = new ArrayList();

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add((InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                ((InputUser) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_found extends TLObject {
        public static int constructor = 90570766;
        public ArrayList<TL_contactFound> results = new ArrayList();
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.results.add((TL_contactFound) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.results.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contactFound) this.results.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_getBlocked extends TLObject {
        public static int constructor = -176409329;
        public int limit;
        public int offset;

        public Class responseClass() {
            return contacts_Blocked.class;
        }

        public void readParams(SerializedData stream) {
            this.offset = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_contacts_getContacts extends TLObject {
        public static int constructor = 583445000;
        public String hash;

        public Class responseClass() {
            return contacts_Contacts.class;
        }

        public void readParams(SerializedData stream) {
            this.hash = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.hash);
        }
    }

    public static class TL_contacts_getStatuses extends TLObject {
        public static int constructor = -995929106;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_getSuggested extends TLObject {
        public static int constructor = -847825880;
        public int limit;

        public Class responseClass() {
            return TL_contacts_suggested.class;
        }

        public void readParams(SerializedData stream) {
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_contacts_importContacts extends TLObject {
        public static int constructor = -634342611;
        public ArrayList<TL_inputPhoneContact> contacts = new ArrayList();
        public boolean replace;

        public Class responseClass() {
            return TL_contacts_importedContacts.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.contacts.add((TL_inputPhoneContact) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.replace = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.contacts.size());
            Iterator i$ = this.contacts.iterator();
            while (i$.hasNext()) {
                ((TL_inputPhoneContact) i$.next()).serializeToStream(stream);
            }
            stream.writeBool(this.replace);
        }
    }

    public static class TL_contacts_importedContacts extends TLObject {
        public static int constructor = -775091636;
        public ArrayList<TL_importedContact> imported = new ArrayList();
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.imported.add((TL_importedContact) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.imported.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_importedContact) this.imported.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_link extends TLObject {
        public static int constructor = -322001931;
        public contacts_ForeignLink foreign_link;
        public contacts_MyLink my_link;
        public User user;

        public void readParams(SerializedData stream) {
            this.my_link = (contacts_MyLink) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.foreign_link = (contacts_ForeignLink) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.user = (User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.my_link.serializeToStream(stream);
            this.foreign_link.serializeToStream(stream);
            this.user.serializeToStream(stream);
        }
    }

    public static class TL_contacts_search extends TLObject {
        public static int constructor = 301470424;
        public int limit;
        public String f61q;

        public Class responseClass() {
            return TL_contacts_found.class;
        }

        public void readParams(SerializedData stream) {
            this.f61q = stream.readString();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.f61q);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_contacts_suggested extends TLObject {
        public static int constructor = 1447681221;
        public ArrayList<TL_contactSuggested> results = new ArrayList();
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.results.add((TL_contactSuggested) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.results.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contactSuggested) this.results.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_unblock extends TLObject {
        public static int constructor = -448724803;
        public InputUser id;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_dcOption extends TLObject {
        public static int constructor = 784507964;
        public String hostname;
        public int id;
        public String ip_address;
        public int port;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.hostname = stream.readString();
            this.ip_address = stream.readString();
            this.port = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.hostname);
            stream.writeString(this.ip_address);
            stream.writeInt32(this.port);
        }
    }

    public static class TL_decryptedMessageActionSetMessageTTL extends TLObject {
        public static int constructor = -1586283796;
        public int ttl_seconds;

        public void readParams(SerializedData stream) {
            this.ttl_seconds = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.ttl_seconds);
        }
    }

    public static class TL_decryptedMessageLayer extends TLObject {
        public static int constructor = -1717290801;
        public int layer;
        public DecryptedMessage message;

        public void readParams(SerializedData stream) {
            this.layer = stream.readInt32();
            this.message = (DecryptedMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.layer);
            this.message.serializeToStream(stream);
        }
    }

    public static class TL_destroy_session extends TLObject {
        public static int constructor = -414113498;
        public long session_id;

        public Class responseClass() {
            return DestroySessionRes.class;
        }

        public int layer() {
            return 0;
        }

        public void readParams(SerializedData stream) {
            this.session_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.session_id);
        }
    }

    public static class TL_destroy_sessions extends TLObject {
        public static int constructor = -1589787345;
        public ArrayList<Long> session_ids = new ArrayList();

        public Class responseClass() {
            return TL_destroy_sessions_res.class;
        }

        public int layer() {
            return 0;
        }

        public void readParams(SerializedData stream) {
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.session_ids.add(Long.valueOf(stream.readInt64()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.session_ids.size());
            Iterator i$ = this.session_ids.iterator();
            while (i$.hasNext()) {
                stream.writeInt64(((Long) i$.next()).longValue());
            }
        }
    }

    public static class TL_destroy_sessions_res extends TLObject {
        public static int constructor = -74077235;
        public ArrayList<DestroySessionRes> destroy_results = new ArrayList();

        public void readParams(SerializedData stream) {
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.destroy_results.add((DestroySessionRes) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.destroy_results.size());
            Iterator i$ = this.destroy_results.iterator();
            while (i$.hasNext()) {
                ((DestroySessionRes) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_dialog extends TLObject {
        public static int constructor = 558533855;
        public long id;
        public int last_message_date;
        public int last_read;
        public Peer peer;
        public int top_message;
        public int unread_count;

        public void readParams(SerializedData stream) {
            this.peer = (Peer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.top_message = stream.readInt32();
            this.unread_count = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.top_message);
            stream.writeInt32(this.unread_count);
        }
    }

    public static class TL_error extends TLObject {
        public static int constructor = -994444869;
        public int code;
        public String text;

        public void readParams(SerializedData stream) {
            this.code = stream.readInt32();
            this.text = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.code);
            stream.writeString(this.text);
        }
    }

    public static class TL_futureSalt extends TLObject {
        public static int constructor = 155834844;
        public long salt;
        public int valid_since;
        public int valid_until;

        public void readParams(SerializedData stream) {
            this.valid_since = stream.readInt32();
            this.valid_until = stream.readInt32();
            this.salt = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.valid_since);
            stream.writeInt32(this.valid_until);
            stream.writeInt64(this.salt);
        }
    }

    public static class TL_futuresalts extends TLObject {
        public static int constructor = -1370486635;
        public int now;
        public long req_msg_id;
        public ArrayList<TL_futureSalt> salts = new ArrayList();

        public void readParams(SerializedData stream) {
            this.req_msg_id = stream.readInt64();
            this.now = stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                new TL_futureSalt().readParams(stream);
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.req_msg_id);
            stream.writeInt32(this.now);
            stream.writeInt32(this.salts.size());
            Iterator i$ = this.salts.iterator();
            while (i$.hasNext()) {
                ((TL_futureSalt) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_geochats_checkin extends TLObject {
        public static int constructor = 1437853947;
        public TL_inputGeoChat peer;

        public Class responseClass() {
            return TL_geochats_statedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
        }
    }

    public static class TL_geochats_createGeoChat extends TLObject {
        public static int constructor = 235482646;
        public String address;
        public InputGeoPoint geo_point;
        public String title;
        public String venue;

        public Class responseClass() {
            return TL_geochats_statedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.title = stream.readString();
            this.geo_point = (InputGeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.address = stream.readString();
            this.venue = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.title);
            this.geo_point.serializeToStream(stream);
            stream.writeString(this.address);
            stream.writeString(this.venue);
        }
    }

    public static class TL_geochats_editChatPhoto extends TLObject {
        public static int constructor = 903355029;
        public TL_inputGeoChat peer;
        public InputChatPhoto photo;

        public Class responseClass() {
            return TL_geochats_statedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.photo = (InputChatPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            this.photo.serializeToStream(stream);
        }
    }

    public static class TL_geochats_editChatTitle extends TLObject {
        public static int constructor = 1284383347;
        public String address;
        public TL_inputGeoChat peer;
        public String title;

        public Class responseClass() {
            return TL_geochats_statedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.title = stream.readString();
            this.address = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeString(this.title);
            stream.writeString(this.address);
        }
    }

    public static class TL_geochats_getFullChat extends TLObject {
        public static int constructor = 1730338159;
        public TL_inputGeoChat peer;

        public Class responseClass() {
            return TL_messages_chatFull.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
        }
    }

    public static class TL_geochats_getHistory extends TLObject {
        public static int constructor = -1254131096;
        public int limit;
        public int max_id;
        public int offset;
        public TL_inputGeoChat peer;

        public Class responseClass() {
            return geochats_Messages.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.offset = stream.readInt32();
            this.max_id = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_geochats_getLocated extends TLObject {
        public static int constructor = 2132356495;
        public InputGeoPoint geo_point;
        public int limit;
        public int radius;

        public Class responseClass() {
            return TL_geochats_located.class;
        }

        public void readParams(SerializedData stream) {
            this.geo_point = (InputGeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.radius = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.geo_point.serializeToStream(stream);
            stream.writeInt32(this.radius);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_geochats_getRecents extends TLObject {
        public static int constructor = -515735953;
        public int limit;
        public int offset;

        public Class responseClass() {
            return geochats_Messages.class;
        }

        public void readParams(SerializedData stream) {
            this.offset = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_geochats_located extends TLObject {
        public static int constructor = 1224651367;
        public ArrayList<Chat> chats = new ArrayList();
        public ArrayList<GeoChatMessage> messages = new ArrayList();
        public ArrayList<TL_chatLocated> results = new ArrayList();
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.results.add((TL_chatLocated) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((GeoChatMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.results.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_chatLocated) this.results.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((GeoChatMessage) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_geochats_search extends TLObject {
        public static int constructor = -808598451;
        public MessagesFilter filter;
        public int limit;
        public int max_date;
        public int max_id;
        public int min_date;
        public int offset;
        public TL_inputGeoChat peer;
        public String f62q;

        public Class responseClass() {
            return geochats_Messages.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.f62q = stream.readString();
            this.filter = (MessagesFilter) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.min_date = stream.readInt32();
            this.max_date = stream.readInt32();
            this.offset = stream.readInt32();
            this.max_id = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeString(this.f62q);
            this.filter.serializeToStream(stream);
            stream.writeInt32(this.min_date);
            stream.writeInt32(this.max_date);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_geochats_sendMedia extends TLObject {
        public static int constructor = -1192173825;
        public InputMedia media;
        public TL_inputGeoChat peer;
        public long random_id;

        public Class responseClass() {
            return TL_geochats_statedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.media = (InputMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.random_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            this.media.serializeToStream(stream);
            stream.writeInt64(this.random_id);
        }
    }

    public static class TL_geochats_sendMessage extends TLObject {
        public static int constructor = 102432836;
        public String message;
        public TL_inputGeoChat peer;
        public long random_id;

        public Class responseClass() {
            return TL_geochats_statedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.message = stream.readString();
            this.random_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeString(this.message);
            stream.writeInt64(this.random_id);
        }
    }

    public static class TL_geochats_setTyping extends TLObject {
        public static int constructor = 146319145;
        public TL_inputGeoChat peer;
        public boolean typing;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.typing = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeBool(this.typing);
        }
    }

    public static class TL_geochats_statedMessage extends TLObject {
        public static int constructor = 397498251;
        public ArrayList<Chat> chats = new ArrayList();
        public GeoChatMessage message;
        public int seq;
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            this.message = (GeoChatMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
            stream.writeInt32(481674261);
            int count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_get_future_salts extends TLObject {
        public static int constructor = -1188971260;
        public int num;

        public int layer() {
            return 0;
        }

        public Class responseClass() {
            return TL_futuresalts.class;
        }

        public void readParams(SerializedData stream) {
            this.num = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.num);
        }
    }

    public static class TL_gzip_packed extends TLObject {
        public static int constructor = 812830625;
        public byte[] packed_data;

        public void readParams(SerializedData stream) {
            this.packed_data = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.packed_data);
        }
    }

    public static class TL_help_getAppUpdate extends TLObject {
        public static int constructor = -938300290;
        public String app_version;
        public String device_model;
        public String lang_code;
        public String system_version;

        public Class responseClass() {
            return help_AppUpdate.class;
        }

        public void readParams(SerializedData stream) {
            this.device_model = stream.readString();
            this.system_version = stream.readString();
            this.app_version = stream.readString();
            this.lang_code = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.device_model);
            stream.writeString(this.system_version);
            stream.writeString(this.app_version);
            stream.writeString(this.lang_code);
        }
    }

    public static class TL_help_getConfig extends TLObject {
        public static int constructor = -990308245;

        public Class responseClass() {
            return TL_config.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_help_getInviteText extends TLObject {
        public static int constructor = -1532407418;
        public String lang_code;

        public Class responseClass() {
            return TL_help_inviteText.class;
        }

        public void readParams(SerializedData stream) {
            this.lang_code = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.lang_code);
        }
    }

    public static class TL_help_getNearestDc extends TLObject {
        public static int constructor = 531836966;

        public Class responseClass() {
            return TL_nearestDc.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_help_inviteText extends TLObject {
        public static int constructor = 415997816;
        public String message;

        public void readParams(SerializedData stream) {
            this.message = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.message);
        }
    }

    public static class TL_help_saveAppLog extends TLObject {
        public static int constructor = 1862465352;
        public ArrayList<TL_inputAppEvent> events = new ArrayList();

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.events.add((TL_inputAppEvent) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.events.size());
            Iterator i$ = this.events.iterator();
            while (i$.hasNext()) {
                ((TL_inputAppEvent) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_http_wait extends TLObject {
        public static int constructor = -1835453025;
        public int max_delay;
        public int max_wait;
        public int wait_after;

        public void readParams(SerializedData stream) {
            this.max_delay = stream.readInt32();
            this.wait_after = stream.readInt32();
            this.max_wait = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.max_delay);
            stream.writeInt32(this.wait_after);
            stream.writeInt32(this.max_wait);
        }
    }

    public static class TL_importedContact extends TLObject {
        public static int constructor = -805141448;
        public long client_id;
        public int user_id;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.client_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt64(this.client_id);
        }
    }

    public static class TL_inputAppEvent extends TLObject {
        public static int constructor = 1996904104;
        public String data;
        public long peer;
        public double time;
        public String type;

        public void readParams(SerializedData stream) {
            this.time = stream.readDouble();
            this.type = stream.readString();
            this.peer = stream.readInt64();
            this.data = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeDouble(this.time);
            stream.writeString(this.type);
            stream.writeInt64(this.peer);
            stream.writeString(this.data);
        }
    }

    public static class TL_inputEncryptedChat extends TLObject {
        public static int constructor = -247351839;
        public long access_hash;
        public int chat_id;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputGeoChat extends TLObject {
        public static int constructor = 1960072954;
        public long access_hash;
        public int chat_id;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputPeerNotifySettings extends TLObject {
        public static int constructor = 1185074840;
        public int events_mask;
        public int mute_until;
        public boolean show_previews;
        public String sound;

        public void readParams(SerializedData stream) {
            this.mute_until = stream.readInt32();
            this.sound = stream.readString();
            this.show_previews = stream.readBool();
            this.events_mask = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.mute_until);
            stream.writeString(this.sound);
            stream.writeBool(this.show_previews);
            stream.writeInt32(this.events_mask);
        }
    }

    public static class TL_inputPhoneContact extends TLObject {
        public static int constructor = -208488460;
        public long client_id;
        public String first_name;
        public String last_name;
        public String phone;

        public void readParams(SerializedData stream) {
            this.client_id = stream.readInt64();
            this.phone = stream.readString();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.client_id);
            stream.writeString(this.phone);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
        }
    }

    public static class TL_invokeAfterMsg extends TLObject {
        public static int constructor = -878758099;
        public long msg_id;
        public TLObject query;

        public void readParams(SerializedData stream) {
            this.msg_id = stream.readInt64();
            this.query = TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.msg_id);
            this.query.serializeToStream(stream);
        }
    }

    public static class TL_messages_acceptEncryption extends TLObject {
        public static int constructor = 1035731989;
        public byte[] g_b;
        public long key_fingerprint;
        public TL_inputEncryptedChat peer;

        public Class responseClass() {
            return EncryptedChat.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputEncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.g_b = stream.readByteArray();
            this.key_fingerprint = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeByteArray(this.g_b);
            stream.writeInt64(this.key_fingerprint);
        }
    }

    public static class TL_messages_addChatUser extends TLObject {
        public static int constructor = 787082910;
        public int chat_id;
        public int fwd_limit;
        public InputUser user_id;

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.user_id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.fwd_limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            this.user_id.serializeToStream(stream);
            stream.writeInt32(this.fwd_limit);
        }
    }

    public static class TL_messages_affectedHistory extends TLObject {
        public static int constructor = -1210173710;
        public int offset;
        public int pts;
        public int seq;

        public void readParams(SerializedData stream) {
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
            this.offset = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
            stream.writeInt32(this.offset);
        }
    }

    public static class TL_messages_chat extends TLObject {
        public static int constructor = 1089011754;
        public Chat chat;
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            this.chat = (Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.chat.serializeToStream(stream);
            stream.writeInt32(481674261);
            stream.writeInt32(this.users.size());
            Iterator i$ = this.users.iterator();
            while (i$.hasNext()) {
                ((User) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_chatFull extends TLObject {
        public static int constructor = -438840932;
        public ArrayList<Chat> chats = new ArrayList();
        public TL_chatFull full_chat;
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            this.full_chat = (TL_chatFull) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            this.full_chat.serializeToStream(stream);
            stream.writeInt32(481674261);
            int count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_chats extends TLObject {
        public static int constructor = -2125411368;
        public ArrayList<Chat> chats = new ArrayList();
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_createChat extends TLObject {
        public static int constructor = 1100847854;
        public String title;
        public ArrayList<InputUser> users = new ArrayList();

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.users.add((InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.title = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.users.size());
            Iterator i$ = this.users.iterator();
            while (i$.hasNext()) {
                ((InputUser) i$.next()).serializeToStream(stream);
            }
            stream.writeString(this.title);
        }
    }

    public static class TL_messages_deleteChatUser extends TLObject {
        public static int constructor = -1010447069;
        public int chat_id;
        public InputUser user_id;

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.user_id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            this.user_id.serializeToStream(stream);
        }
    }

    public static class TL_messages_deleteHistory extends TLObject {
        public static int constructor = -185009311;
        public int offset;
        public InputPeer peer;

        public Class responseClass() {
            return TL_messages_affectedHistory.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.offset = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.offset);
        }
    }

    public static class TL_messages_deleteMessages extends TLObject {
        public static int constructor = 351460618;
        public ArrayList<Integer> id = new ArrayList();

        public Class responseClass() {
            return Vector.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add(Integer.valueOf(stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
        }

        public void parseVector(Vector vector, SerializedData data) {
            int size = data.readInt32();
            for (int a = 0; a < size; a++) {
                vector.objects.add(Integer.valueOf(data.readInt32()));
            }
        }
    }

    public static class TL_messages_discardEncryption extends TLObject {
        public static int constructor = -304536635;
        public int chat_id;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
        }
    }

    public static class TL_messages_editChatPhoto extends TLObject {
        public static int constructor = -662601187;
        public int chat_id;
        public InputChatPhoto photo;

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.photo = (InputChatPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            this.photo.serializeToStream(stream);
        }
    }

    public static class TL_messages_editChatTitle extends TLObject {
        public static int constructor = -1262720843;
        public int chat_id;
        public String title;

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.title = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeString(this.title);
        }
    }

    public static class TL_messages_forwardMessage extends TLObject {
        public static int constructor = 66319602;
        public int id;
        public InputPeer peer;
        public long random_id;

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.id = stream.readInt32();
            this.random_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.id);
            stream.writeInt64(this.random_id);
        }
    }

    public static class TL_messages_forwardMessages extends TLObject {
        public static int constructor = 1363988751;
        public ArrayList<Integer> id = new ArrayList();
        public InputPeer peer;

        public Class responseClass() {
            return messages_StatedMessages.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add(Integer.valueOf(stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
        }
    }

    public static class TL_messages_getChats extends TLObject {
        public static int constructor = 1013621127;
        public ArrayList<Integer> id = new ArrayList();

        public Class responseClass() {
            return TL_messages_chats.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add(Integer.valueOf(stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
        }
    }

    public static class TL_messages_getDhConfig extends TLObject {
        public static int constructor = 651135312;
        public int random_length;
        public int version;

        public Class responseClass() {
            return messages_DhConfig.class;
        }

        public void readParams(SerializedData stream) {
            this.version = stream.readInt32();
            this.random_length = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.version);
            stream.writeInt32(this.random_length);
        }
    }

    public static class TL_messages_getDialogs extends TLObject {
        public static int constructor = -321970698;
        public int limit;
        public int max_id;
        public int offset;

        public Class responseClass() {
            return messages_Dialogs.class;
        }

        public void readParams(SerializedData stream) {
            this.offset = stream.readInt32();
            this.max_id = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_messages_getFullChat extends TLObject {
        public static int constructor = 998448230;
        public int chat_id;

        public Class responseClass() {
            return TL_messages_chatFull.class;
        }

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
        }
    }

    public static class TL_messages_getHistory extends TLObject {
        public static int constructor = -1834885329;
        public int limit;
        public int max_id;
        public int offset;
        public InputPeer peer;

        public Class responseClass() {
            return messages_Messages.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.offset = stream.readInt32();
            this.max_id = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_messages_getMessages extends TLObject {
        public static int constructor = 1109588596;
        public ArrayList<Integer> id = new ArrayList();

        public Class responseClass() {
            return messages_Messages.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add(Integer.valueOf(stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
        }
    }

    public static class TL_messages_readEncryptedHistory extends TLObject {
        public static int constructor = 2135648522;
        public int max_date;
        public TL_inputEncryptedChat peer;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputEncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.max_date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.max_date);
        }
    }

    public static class TL_messages_readHistory extends TLObject {
        public static int constructor = -1336990448;
        public int max_id;
        public int offset;
        public InputPeer peer;

        public Class responseClass() {
            return TL_messages_affectedHistory.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.max_id = stream.readInt32();
            this.offset = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.offset);
        }
    }

    public static class TL_messages_receivedMessages extends TLObject {
        public static int constructor = 682347368;
        public int max_id;

        public Class responseClass() {
            return Vector.class;
        }

        public void readParams(SerializedData stream) {
            this.max_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.max_id);
        }

        public void parseVector(Vector vector, SerializedData data) {
            int size = data.readInt32();
            for (int a = 0; a < size; a++) {
                vector.objects.add(Integer.valueOf(data.readInt32()));
            }
        }
    }

    public static class TL_messages_receivedQueue extends TLObject {
        public static int constructor = 1436924774;
        public int max_qts;

        public Class responseClass() {
            return Vector.class;
        }

        public void parseVector(Vector vector, SerializedData data) {
            int size = data.readInt32();
            for (int a = 0; a < size; a++) {
                vector.objects.add(Long.valueOf(data.readInt64()));
            }
        }

        public void readParams(SerializedData stream) {
            this.max_qts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.max_qts);
        }
    }

    public static class TL_messages_requestEncryption extends TLObject {
        public static int constructor = -162681021;
        public byte[] g_a;
        public int random_id;
        public InputUser user_id;

        public Class responseClass() {
            return EncryptedChat.class;
        }

        public void readParams(SerializedData stream) {
            this.user_id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.random_id = stream.readInt32();
            this.g_a = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.user_id.serializeToStream(stream);
            stream.writeInt32(this.random_id);
            stream.writeByteArray(this.g_a);
        }
    }

    public static class TL_messages_restoreMessages extends TLObject {
        public static int constructor = 962567550;
        public ArrayList<Integer> id = new ArrayList();

        public Class responseClass() {
            return Vector.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add(Integer.valueOf(stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
        }

        public void parseVector(Vector vector, SerializedData data) {
            int size = data.readInt32();
            for (int a = 0; a < size; a++) {
                vector.objects.add(Integer.valueOf(data.readInt32()));
            }
        }
    }

    public static class TL_messages_search extends TLObject {
        public static int constructor = 132772523;
        public MessagesFilter filter;
        public int limit;
        public int max_date;
        public int max_id;
        public int min_date;
        public int offset;
        public InputPeer peer;
        public String f63q;

        public Class responseClass() {
            return messages_Messages.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.f63q = stream.readString();
            this.filter = (MessagesFilter) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.min_date = stream.readInt32();
            this.max_date = stream.readInt32();
            this.offset = stream.readInt32();
            this.max_id = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeString(this.f63q);
            this.filter.serializeToStream(stream);
            stream.writeInt32(this.min_date);
            stream.writeInt32(this.max_date);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_messages_sendBroadcast extends TLObject {
        public static int constructor = 1102776690;
        public ArrayList<InputUser> contacts = new ArrayList();
        public InputMedia media;
        public String message;

        public Class responseClass() {
            return messages_StatedMessages.class;
        }

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.contacts.add((InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.message = stream.readString();
            this.media = (InputMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.contacts.size());
            Iterator i$ = this.contacts.iterator();
            while (i$.hasNext()) {
                ((InputUser) i$.next()).serializeToStream(stream);
            }
            stream.writeString(this.message);
            this.media.serializeToStream(stream);
        }
    }

    public static class TL_messages_sendEncrypted extends TLObject {
        public static int constructor = -1451792525;
        public byte[] data;
        public TL_inputEncryptedChat peer;
        public long random_id;

        public Class responseClass() {
            return messages_SentEncryptedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputEncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.random_id = stream.readInt64();
            this.data = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt64(this.random_id);
            stream.writeByteArray(this.data);
        }
    }

    public static class TL_messages_sendEncryptedFile extends TLObject {
        public static int constructor = -1701831834;
        public byte[] data;
        public InputEncryptedFile file;
        public TL_inputEncryptedChat peer;
        public long random_id;

        public Class responseClass() {
            return messages_SentEncryptedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputEncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.random_id = stream.readInt64();
            this.data = stream.readByteArray();
            this.file = (InputEncryptedFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt64(this.random_id);
            stream.writeByteArray(this.data);
            this.file.serializeToStream(stream);
        }
    }

    public static class TL_messages_sendEncryptedService extends TLObject {
        public static int constructor = 852769188;
        public byte[] data;
        public TL_inputEncryptedChat peer;
        public long random_id;

        public Class responseClass() {
            return messages_SentEncryptedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputEncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.random_id = stream.readInt64();
            this.data = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeInt64(this.random_id);
            stream.writeByteArray(this.data);
        }
    }

    public static class TL_messages_sendMedia extends TLObject {
        public static int constructor = -1547149962;
        public InputMedia media;
        public InputPeer peer;
        public long random_id;

        public Class responseClass() {
            return messages_StatedMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.media = (InputMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.random_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            this.media.serializeToStream(stream);
            stream.writeInt64(this.random_id);
        }
    }

    public static class TL_messages_sendMessage extends TLObject {
        public static int constructor = 1289620139;
        public String message;
        public InputPeer peer;
        public long random_id;

        public Class responseClass() {
            return messages_SentMessage.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.message = stream.readString();
            this.random_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeString(this.message);
            stream.writeInt64(this.random_id);
        }
    }

    public static class TL_messages_setEncryptedTyping extends TLObject {
        public static int constructor = 2031374829;
        public TL_inputEncryptedChat peer;
        public boolean typing;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputEncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.typing = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeBool(this.typing);
        }
    }

    public static class TL_messages_setTyping extends TLObject {
        public static int constructor = 1905801705;
        public InputPeer peer;
        public boolean typing;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.typing = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
            stream.writeBool(this.typing);
        }
    }

    public static class TL_msg_container extends TLObject {
        public static int constructor = 1945237724;
        public ArrayList<TL_protoMessage> messages;

        public void readParams(SerializedData stream) {
            this.messages = new ArrayList();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                TL_protoMessage message = new TL_protoMessage();
                message.msg_id = stream.readInt64();
                message.seqno = stream.readInt32();
                message.bytes = stream.readInt32();
                message.body = TLClassStore.Instance().TLdeserialize(stream, stream.readInt32(), ConnectionsManager.Instance.getRequestWithMessageId(message.msg_id));
                this.messages.add(message);
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.messages.size());
            Iterator i$ = this.messages.iterator();
            while (i$.hasNext()) {
                TL_protoMessage proto = (TL_protoMessage) i$.next();
                stream.writeInt64(proto.msg_id);
                stream.writeInt32(proto.seqno);
                stream.writeInt32(proto.bytes);
                proto.body.serializeToStream(stream);
            }
        }
    }

    public static class TL_msg_copy extends TLObject {
        public static int constructor = -530561358;
        public TL_protoMessage orig_message;

        public void readParams(SerializedData stream) {
            this.orig_message = (TL_protoMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.orig_message.serializeToStream(stream);
        }
    }

    public static class TL_msg_resend_req extends TLObject {
        public static int constructor = 2105940488;
        public ArrayList<Long> msg_ids = new ArrayList();

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.msg_ids.add(Long.valueOf(stream.readInt64()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.msg_ids.size());
            Iterator i$ = this.msg_ids.iterator();
            while (i$.hasNext()) {
                stream.writeInt64(((Long) i$.next()).longValue());
            }
        }
    }

    public static class TL_msgs_ack extends TLObject {
        public static int constructor = 1658238041;
        public ArrayList<Long> msg_ids = new ArrayList();

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.msg_ids.add(Long.valueOf(stream.readInt64()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.msg_ids.size());
            Iterator i$ = this.msg_ids.iterator();
            while (i$.hasNext()) {
                stream.writeInt64(((Long) i$.next()).longValue());
            }
        }
    }

    public static class TL_msgs_all_info extends TLObject {
        public static int constructor = -1933520591;
        public String info;
        public ArrayList<Long> msg_ids = new ArrayList();

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.msg_ids.add(Long.valueOf(stream.readInt64()));
            }
            this.info = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.msg_ids.size());
            Iterator i$ = this.msg_ids.iterator();
            while (i$.hasNext()) {
                stream.writeInt64(((Long) i$.next()).longValue());
            }
            stream.writeString(this.info);
        }
    }

    public static class TL_msgs_state_info extends TLObject {
        public static int constructor = 81704317;
        public String info;
        public long req_msg_id;

        public void readParams(SerializedData stream) {
            this.req_msg_id = stream.readInt64();
            this.info = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.req_msg_id);
            stream.writeString(this.info);
        }
    }

    public static class TL_msgs_state_req extends TLObject {
        public static int constructor = -630588590;
        public ArrayList<Long> msg_ids = new ArrayList();

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.msg_ids.add(Long.valueOf(stream.readInt64()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.msg_ids.size());
            Iterator i$ = this.msg_ids.iterator();
            while (i$.hasNext()) {
                stream.writeInt64(((Long) i$.next()).longValue());
            }
        }
    }

    public static class TL_nearestDc extends TLObject {
        public static int constructor = -1910892683;
        public String country;
        public int nearest_dc;
        public int this_dc;

        public void readParams(SerializedData stream) {
            this.country = stream.readString();
            this.this_dc = stream.readInt32();
            this.nearest_dc = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.country);
            stream.writeInt32(this.this_dc);
            stream.writeInt32(this.nearest_dc);
        }
    }

    public static class TL_new_session_created extends TLObject {
        public static int constructor = -1631450872;
        public long first_msg_id;
        public long server_salt;
        public long unique_id;

        public void readParams(SerializedData stream) {
            this.first_msg_id = stream.readInt64();
            this.unique_id = stream.readInt64();
            this.server_salt = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.first_msg_id);
            stream.writeInt64(this.unique_id);
            stream.writeInt64(this.server_salt);
        }
    }

    public static class TL_null extends TLObject {
        public static int constructor = 1450380236;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_p_q_inner_data extends TLObject {
        public static int constructor = -2083955988;
        public byte[] new_nonce;
        public byte[] nonce;
        public byte[] f64p;
        public byte[] pq;
        public byte[] f65q;
        public byte[] server_nonce;

        public void readParams(SerializedData stream) {
            this.pq = stream.readByteArray();
            this.f64p = stream.readByteArray();
            this.f65q = stream.readByteArray();
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.new_nonce = stream.readData(32);
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.pq);
            stream.writeByteArray(this.f64p);
            stream.writeByteArray(this.f65q);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeRaw(this.new_nonce);
        }
    }

    public static class TL_photos_getUserPhotos extends TLObject {
        public static int constructor = -1209117380;
        public int limit;
        public int max_id;
        public int offset;
        public InputUser user_id;

        public Class responseClass() {
            return photos_Photos.class;
        }

        public void readParams(SerializedData stream) {
            this.user_id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.offset = stream.readInt32();
            this.max_id = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.user_id.serializeToStream(stream);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.max_id);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_photos_photo extends TLObject {
        public static int constructor = 539045032;
        public Photo photo;
        public ArrayList<User> users = new ArrayList();

        public void readParams(SerializedData stream) {
            this.photo = (Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.photo.serializeToStream(stream);
            stream.writeInt32(481674261);
            stream.writeInt32(this.users.size());
            Iterator i$ = this.users.iterator();
            while (i$.hasNext()) {
                ((User) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_photos_updateProfilePhoto extends TLObject {
        public static int constructor = -285902432;
        public InputPhotoCrop crop;
        public InputPhoto id;

        public Class responseClass() {
            return UserProfilePhoto.class;
        }

        public void readParams(SerializedData stream) {
            this.id = (InputPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.crop = (InputPhotoCrop) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
            this.crop.serializeToStream(stream);
        }
    }

    public static class TL_photos_uploadProfilePhoto extends TLObject {
        public static int constructor = -720397176;
        public String caption;
        public InputPhotoCrop crop;
        public InputFile file;
        public InputGeoPoint geo_point;

        public Class responseClass() {
            return TL_photos_photo.class;
        }

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.caption = stream.readString();
            this.geo_point = (InputGeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.crop = (InputPhotoCrop) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            stream.writeString(this.caption);
            this.geo_point.serializeToStream(stream);
            this.crop.serializeToStream(stream);
        }
    }

    public static class TL_ping extends TLObject {
        public static int constructor = 2059302892;
        public long ping_id;

        public Class responseClass() {
            return TL_pong.class;
        }

        public int layer() {
            return 0;
        }

        public void readParams(SerializedData stream) {
            this.ping_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.ping_id);
        }
    }

    public static class TL_pong extends TLObject {
        public static int constructor = 880243653;
        public long msg_id;
        public long ping_id;

        public void readParams(SerializedData stream) {
            this.msg_id = stream.readInt64();
            this.ping_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.msg_id);
            stream.writeInt64(this.ping_id);
        }
    }

    public static class TL_protoMessage extends TLObject {
        public static int constructor = 1538843921;
        public TLObject body;
        public int bytes;
        public long msg_id;
        public int seqno;

        public void readParams(SerializedData stream) {
            this.msg_id = stream.readInt64();
            this.seqno = stream.readInt32();
            this.bytes = stream.readInt32();
            this.body = TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.msg_id);
            stream.writeInt32(this.seqno);
            stream.writeInt32(this.bytes);
            this.body.serializeToStream(stream);
        }
    }

    public static class TL_req_DH_params extends TLObject {
        public static int constructor = -686627650;
        public byte[] encrypted_data;
        public byte[] nonce;
        public byte[] f66p;
        public long public_key_fingerprint;
        public byte[] f67q;
        public byte[] server_nonce;

        public Class responseClass() {
            return Server_DH_Params.class;
        }

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.f66p = stream.readByteArray();
            this.f67q = stream.readByteArray();
            this.public_key_fingerprint = stream.readInt64();
            this.encrypted_data = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeByteArray(this.f66p);
            stream.writeByteArray(this.f67q);
            stream.writeInt64(this.public_key_fingerprint);
            stream.writeByteArray(this.encrypted_data);
        }
    }

    public static class TL_req_pq extends TLObject {
        public static int constructor = 1615239032;
        public byte[] nonce;

        public Class responseClass() {
            return TL_resPQ.class;
        }

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
        }
    }

    public static class TL_resPQ extends TLObject {
        public static int constructor = 85337187;
        public byte[] nonce;
        public byte[] pq;
        public byte[] server_nonce;
        public ArrayList<Long> server_public_key_fingerprints = new ArrayList();

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.pq = stream.readByteArray();
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.server_public_key_fingerprints.add(Long.valueOf(stream.readInt64()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeByteArray(this.pq);
            stream.writeInt32(481674261);
            stream.writeInt32(this.server_public_key_fingerprints.size());
            Iterator i$ = this.server_public_key_fingerprints.iterator();
            while (i$.hasNext()) {
                stream.writeInt64(((Long) i$.next()).longValue());
            }
        }
    }

    public static class TL_rpc_drop_answer extends TLObject {
        public static int constructor = 1491380032;
        public long req_msg_id;

        public int layer() {
            return 0;
        }

        public Class responseClass() {
            return RpcDropAnswer.class;
        }

        public void readParams(SerializedData stream) {
            this.req_msg_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.req_msg_id);
        }
    }

    public static class TL_rpc_result extends TLObject {
        public static int constructor = -212046591;
        public long req_msg_id;
        public TLObject result;

        public void readParams(SerializedData stream) {
            this.req_msg_id = stream.readInt64();
            this.result = TLClassStore.Instance().TLdeserialize(stream, stream.readInt32(), ConnectionsManager.Instance.getRequestWithMessageId(this.req_msg_id));
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.req_msg_id);
            this.result.serializeToStream(stream);
        }
    }

    public static class TL_server_DH_inner_data extends TLObject {
        public static int constructor = -1249309254;
        public byte[] dh_prime;
        public int f68g;
        public byte[] g_a;
        public byte[] nonce;
        public byte[] server_nonce;
        public int server_time;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.f68g = stream.readInt32();
            this.dh_prime = stream.readByteArray();
            this.g_a = stream.readByteArray();
            this.server_time = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeInt32(this.f68g);
            stream.writeByteArray(this.dh_prime);
            stream.writeByteArray(this.g_a);
            stream.writeInt32(this.server_time);
        }
    }

    public static class TL_set_client_DH_params extends TLObject {
        public static int constructor = -184262881;
        public byte[] encrypted_data;
        public byte[] nonce;
        public byte[] server_nonce;

        public Class responseClass() {
            return Set_client_DH_params_answer.class;
        }

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.encrypted_data = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeByteArray(this.encrypted_data);
        }
    }

    public static class TL_updates_getDifference extends TLObject {
        public static int constructor = 168039573;
        public int date;
        public int pts;
        public int qts;

        public Class responseClass() {
            return updates_Difference.class;
        }

        public void readParams(SerializedData stream) {
            this.pts = stream.readInt32();
            this.date = stream.readInt32();
            this.qts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.date);
            stream.writeInt32(this.qts);
        }
    }

    public static class TL_updates_getState extends TLObject {
        public static int constructor = -304838614;

        public Class responseClass() {
            return TL_updates_state.class;
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_updates_state extends TLObject {
        public static int constructor = -1519637954;
        public int date;
        public int pts;
        public int qts;
        public int seq;
        public int unread_count;

        public void readParams(SerializedData stream) {
            this.pts = stream.readInt32();
            this.qts = stream.readInt32();
            this.date = stream.readInt32();
            this.seq = stream.readInt32();
            this.unread_count = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.qts);
            stream.writeInt32(this.date);
            stream.writeInt32(this.seq);
            stream.writeInt32(this.unread_count);
        }
    }

    public static class TL_upload_file extends TLObject {
        public static int constructor = 157948117;
        public byte[] bytes;
        public int mtime;
        public storage_FileType type;

        public void readParams(SerializedData stream) {
            this.type = (storage_FileType) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.mtime = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.type.serializeToStream(stream);
            stream.writeInt32(this.mtime);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_upload_getFile extends TLObject {
        public static int constructor = -475607115;
        public int limit;
        public InputFileLocation location;
        public int offset;

        public Class responseClass() {
            return TL_upload_file.class;
        }

        public void readParams(SerializedData stream) {
            this.location = (InputFileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.offset = stream.readInt32();
            this.limit = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.location.serializeToStream(stream);
            stream.writeInt32(this.offset);
            stream.writeInt32(this.limit);
        }
    }

    public static class TL_upload_saveBigFilePart extends TLObject {
        public static int constructor = -562337987;
        public byte[] bytes;
        public long file_id;
        public int file_part;
        public int file_total_parts;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.file_id = stream.readInt64();
            this.file_part = stream.readInt32();
            this.file_total_parts = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.file_id);
            stream.writeInt32(this.file_part);
            stream.writeInt32(this.file_total_parts);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_upload_saveFilePart extends TLObject {
        public static int constructor = -1291540959;
        public byte[] bytes;
        public long file_id;
        public int file_part;

        public Class responseClass() {
            return Bool.class;
        }

        public void readParams(SerializedData stream) {
            this.file_id = stream.readInt64();
            this.file_part = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.file_id);
            stream.writeInt32(this.file_part);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_userFull extends TLObject {
        public static int constructor = 1997575642;
        public boolean blocked;
        public TL_contacts_link link;
        public PeerNotifySettings notify_settings;
        public Photo profile_photo;
        public String real_first_name;
        public String real_last_name;
        public User user;

        public void readParams(SerializedData stream) {
            this.user = (User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.link = (TL_contacts_link) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.profile_photo = (Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.notify_settings = (PeerNotifySettings) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.blocked = stream.readBool();
            this.real_first_name = stream.readString();
            this.real_last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.user.serializeToStream(stream);
            this.link.serializeToStream(stream);
            this.profile_photo.serializeToStream(stream);
            this.notify_settings.serializeToStream(stream);
            stream.writeBool(this.blocked);
            stream.writeString(this.real_first_name);
            stream.writeString(this.real_last_name);
        }
    }

    public static class TL_users_getFullUser extends TLObject {
        public static int constructor = -902781519;
        public InputUser id;

        public Class responseClass() {
            return TL_userFull.class;
        }

        public void readParams(SerializedData stream) {
            this.id = (InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_users_getUsers extends TLObject {
        public static int constructor = 227648840;
        public ArrayList<InputUser> id = new ArrayList();

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.id.add((InputUser) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.id.size());
            Iterator i$ = this.id.iterator();
            while (i$.hasNext()) {
                ((InputUser) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class Update extends TLObject {
        public long auth_key_id;
        public EncryptedChat chat;
        public int chat_id;
        public int date;
        public ArrayList<TL_dcOption> dc_options = new ArrayList();
        public String device;
        public String first_name;
        public contacts_ForeignLink foreign_link;
        public int id;
        public int inviter_id;
        public String last_name;
        public String location;
        public int max_date;
        public ArrayList<Integer> messages = new ArrayList();
        public contacts_MyLink my_link;
        public ChatParticipants participants;
        public UserProfilePhoto photo;
        public boolean previous;
        public int pts;
        public int qts;
        public long random_id;
        public UserStatus status;
        public int user_id;
        public int version;
    }

    public static class Updates extends TLObject {
        public int chat_id;
        public ArrayList<Chat> chats = new ArrayList();
        public int date;
        public int from_id;
        public int id;
        public String message;
        public int pts;
        public int seq;
        public int seq_start;
        public Update update;
        public ArrayList<Update> updates = new ArrayList();
        public ArrayList<User> users = new ArrayList();
    }

    public static class User extends TLObject {
        public long access_hash;
        public String first_name;
        public int id;
        public boolean inactive;
        public String last_name;
        public String phone;
        public UserProfilePhoto photo;
        public UserStatus status;
    }

    public static class UserProfilePhoto extends TLObject {
        public FileLocation photo_big;
        public long photo_id;
        public FileLocation photo_small;
    }

    public static class UserStatus extends TLObject {
        public int expires;
        public int was_online;
    }

    public static class Vector extends TLObject {
        public static int constructor = 481674261;
        public ArrayList<Object> objects = new ArrayList();
    }

    public static class Video extends TLObject {
        public long access_hash;
        public String caption;
        public int date;
        public int dc_id;
        public int duration;
        public int f69h;
        public long id;
        public byte[] iv;
        public byte[] key;
        public String path;
        public int size;
        public PhotoSize thumb;
        public int user_id;
        public int f70w;
    }

    public static class WallPaper extends TLObject {
        public int bg_color;
        public int color;
        public int id;
        public ArrayList<PhotoSize> sizes = new ArrayList();
        public String title;
    }

    public static class contacts_Blocked extends TLObject {
        public ArrayList<TL_contactBlocked> blocked = new ArrayList();
        public int count;
        public ArrayList<User> users = new ArrayList();
    }

    public static class contacts_Contacts extends TLObject {
        public ArrayList<TL_contact> contacts = new ArrayList();
        public ArrayList<User> users = new ArrayList();
    }

    public static class contacts_ForeignLink extends TLObject {
        public boolean has_phone;
    }

    public static class contacts_MyLink extends TLObject {
        public boolean contact;
    }

    public static class decryptedMessageLayer extends TLObject {
        public static int constructor = -1717290801;
        public int layer;
        public TLObject message;

        public void readParams(SerializedData stream) {
            this.layer = stream.readInt32();
            this.message = TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.layer);
            this.message.serializeToStream(stream);
        }
    }

    public static class geochats_Messages extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public int count;
        public ArrayList<GeoChatMessage> messages = new ArrayList();
        public ArrayList<User> users = new ArrayList();
    }

    public static class help_AppUpdate extends TLObject {
        public boolean critical;
        public int id;
        public String text;
        public String url;
    }

    public static class initConnection extends TLObject {
        public static int constructor = 1769565673;
        public int api_id;
        public String app_version;
        public String device_model;
        public String lang_code;
        public TLObject query;
        public String system_version;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.api_id);
            stream.writeString(this.device_model);
            stream.writeString(this.system_version);
            stream.writeString(this.app_version);
            stream.writeString(this.lang_code);
            this.query.serializeToStream(stream);
        }
    }

    public static class invokeWithLayer11 extends TLObject {
        public static int constructor = -1497853985;
        public TLObject query;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.query.serializeToStream(stream);
        }
    }

    public static class messages_DhConfig extends TLObject {
        public int f71g;
        public byte[] f72p;
        public byte[] random;
        public int version;
    }

    public static class messages_Dialogs extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public int count;
        public ArrayList<TL_dialog> dialogs = new ArrayList();
        public ArrayList<Message> messages = new ArrayList();
        public ArrayList<User> users = new ArrayList();
    }

    public static class messages_Message extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public Message message;
        public ArrayList<User> users = new ArrayList();
    }

    public static class messages_Messages extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public int count;
        public ArrayList<Message> messages = new ArrayList();
        public ArrayList<User> users = new ArrayList();
    }

    public static class messages_SentEncryptedMessage extends TLObject {
        public int date;
        public EncryptedFile file;
    }

    public static class messages_SentMessage extends TLObject {
        public int date;
        public int id;
        public ArrayList<TL_contacts_link> links = new ArrayList();
        public int pts;
        public int seq;
    }

    public static class messages_StatedMessage extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public ArrayList<TL_contacts_link> links = new ArrayList();
        public Message message;
        public int pts;
        public int seq;
        public ArrayList<User> users = new ArrayList();
    }

    public static class messages_StatedMessages extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public ArrayList<TL_contacts_link> links = new ArrayList();
        public ArrayList<Message> messages = new ArrayList();
        public int pts;
        public int seq;
        public ArrayList<User> users = new ArrayList();
    }

    public static class photos_Photos extends TLObject {
        public int count;
        public ArrayList<Photo> photos = new ArrayList();
        public ArrayList<User> users = new ArrayList();
    }

    public static class storage_FileType extends TLObject {
    }

    public static class updates_Difference extends TLObject {
        public ArrayList<Chat> chats = new ArrayList();
        public int date;
        public TL_updates_state intermediate_state;
        public ArrayList<EncryptedMessage> new_encrypted_messages = new ArrayList();
        public ArrayList<Message> new_messages = new ArrayList();
        public ArrayList<Update> other_updates = new ArrayList();
        public int seq;
        public TL_updates_state state;
        public ArrayList<User> users = new ArrayList();
    }

    public static class TL_audio extends Audio {
        public static int constructor = 1114908135;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.duration = stream.readInt32();
            this.size = stream.readInt32();
            this.dc_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.size);
            stream.writeInt32(this.dc_id);
        }
    }

    public static class TL_audioEmpty extends Audio {
        public static int constructor = 1483311320;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
        }
    }

    public static class TL_bad_msg_notification extends BadMsgNotification {
        public static int constructor = -1477445615;

        public void readParams(SerializedData stream) {
            this.bad_msg_id = stream.readInt64();
            this.bad_msg_seqno = stream.readInt32();
            this.error_code = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.bad_msg_id);
            stream.writeInt32(this.bad_msg_seqno);
            stream.writeInt32(this.error_code);
        }
    }

    public static class TL_bad_server_salt extends BadMsgNotification {
        public static int constructor = -307542917;

        public void readParams(SerializedData stream) {
            this.bad_msg_id = stream.readInt64();
            this.bad_msg_seqno = stream.readInt32();
            this.error_code = stream.readInt32();
            this.new_server_salt = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.bad_msg_id);
            stream.writeInt32(this.bad_msg_seqno);
            stream.writeInt32(this.error_code);
            stream.writeInt64(this.new_server_salt);
        }
    }

    public static class TL_boolFalse extends Bool {
        public static int constructor = -1132882121;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_boolTrue extends Bool {
        public static int constructor = -1720552011;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_chat extends Chat {
        public static int constructor = 1855757255;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.title = stream.readString();
            this.photo = (ChatPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.participants_count = stream.readInt32();
            this.date = stream.readInt32();
            this.left = stream.readBool();
            this.version = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.title);
            this.photo.serializeToStream(stream);
            stream.writeInt32(this.participants_count);
            stream.writeInt32(this.date);
            stream.writeBool(this.left);
            stream.writeInt32(this.version);
        }
    }

    public static class TL_chatEmpty extends Chat {
        public static int constructor = -1683826688;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.title = "DELETED";
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
        }
    }

    public static class TL_chatForbidden extends Chat {
        public static int constructor = -83047359;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.title = stream.readString();
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.title);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_chatParticipants extends ChatParticipants {
        public static int constructor = 2017571861;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.admin_id = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.participants.add((TL_chatParticipant) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.version = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.admin_id);
            stream.writeInt32(481674261);
            stream.writeInt32(this.participants.size());
            Iterator i$ = this.participants.iterator();
            while (i$.hasNext()) {
                ((TL_chatParticipant) i$.next()).serializeToStream(stream);
            }
            stream.writeInt32(this.version);
        }
    }

    public static class TL_chatParticipantsForbidden extends ChatParticipants {
        public static int constructor = 265468810;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
        }
    }

    public static class TL_chatPhoto extends ChatPhoto {
        public static int constructor = 1632839530;

        public void readParams(SerializedData stream) {
            this.photo_small = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.photo_big = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.photo_small.serializeToStream(stream);
            this.photo_big.serializeToStream(stream);
        }
    }

    public static class TL_chatPhotoEmpty extends ChatPhoto {
        public static int constructor = 935395612;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_blocked extends contacts_Blocked {
        public static int constructor = 471043349;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.blocked.add((TL_contactBlocked) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.blocked.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contactBlocked) this.blocked.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_blockedSlice extends contacts_Blocked {
        public static int constructor = -1878523231;

        public void readParams(SerializedData stream) {
            int a;
            this.count = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.blocked.add((TL_contactBlocked) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(this.count);
            stream.writeInt32(481674261);
            int count = this.blocked.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contactBlocked) this.blocked.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_contacts extends contacts_Contacts {
        public static int constructor = 1871416498;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.contacts.add((TL_contact) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.contacts.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contact) this.contacts.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_contacts_contactsNotModified extends contacts_Contacts {
        public static int constructor = -1219778094;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_foreignLinkMutual extends contacts_ForeignLink {
        public static int constructor = 468356321;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_foreignLinkRequested extends contacts_ForeignLink {
        public static int constructor = -1484775609;

        public void readParams(SerializedData stream) {
            this.has_phone = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeBool(this.has_phone);
        }
    }

    public static class TL_contacts_foreignLinkUnknown extends contacts_ForeignLink {
        public static int constructor = 322183672;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_myLinkContact extends contacts_MyLink {
        public static int constructor = -1035932711;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_myLinkEmpty extends contacts_MyLink {
        public static int constructor = -768992160;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_contacts_myLinkRequested extends contacts_MyLink {
        public static int constructor = 1818882030;

        public void readParams(SerializedData stream) {
            this.contact = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeBool(this.contact);
        }
    }

    public static class TL_decryptedMessage extends DecryptedMessage {
        public static int constructor = 528568095;

        public void readParams(SerializedData stream) {
            this.random_id = stream.readInt64();
            this.random_bytes = stream.readByteArray();
            this.message = stream.readString();
            this.media = (DecryptedMessageMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.random_id);
            stream.writeByteArray(this.random_bytes);
            stream.writeString(this.message);
            this.media.serializeToStream(stream);
        }
    }

    public static class TL_decryptedMessageMediaAudio extends DecryptedMessageMedia {
        public static int constructor = 1619031439;

        public void readParams(SerializedData stream) {
            this.duration = stream.readInt32();
            this.size = stream.readInt32();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.size);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_decryptedMessageMediaContact extends DecryptedMessageMedia {
        public static int constructor = 1485441687;

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_decryptedMessageMediaDocument extends DecryptedMessageMedia {
        public static int constructor = -1332395189;

        public void readParams(SerializedData stream) {
            this.thumb = stream.readByteArray();
            this.thumb_w = stream.readInt32();
            this.thumb_h = stream.readInt32();
            this.file_name = stream.readString();
            this.mime_type = stream.readString();
            this.size = stream.readInt32();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.thumb);
            stream.writeInt32(this.thumb_w);
            stream.writeInt32(this.thumb_h);
            stream.writeString(this.file_name);
            stream.writeString(this.mime_type);
            stream.writeInt32(this.size);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_decryptedMessageMediaEmpty extends DecryptedMessageMedia {
        public static int constructor = 144661578;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_decryptedMessageMediaGeoPoint extends DecryptedMessageMedia {
        public static int constructor = 893913689;

        public void readParams(SerializedData stream) {
            this.lat = stream.readDouble();
            this._long = stream.readDouble();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeDouble(this.lat);
            stream.writeDouble(this._long);
        }
    }

    public static class TL_decryptedMessageMediaPhoto extends DecryptedMessageMedia {
        public static int constructor = 846826124;

        public void readParams(SerializedData stream) {
            this.thumb = stream.readByteArray();
            this.thumb_w = stream.readInt32();
            this.thumb_h = stream.readInt32();
            this.w = stream.readInt32();
            this.h = stream.readInt32();
            this.size = stream.readInt32();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.thumb);
            stream.writeInt32(this.thumb_w);
            stream.writeInt32(this.thumb_h);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
            stream.writeInt32(this.size);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_decryptedMessageMediaVideo extends DecryptedMessageMedia {
        public static int constructor = 1290694387;

        public void readParams(SerializedData stream) {
            this.thumb = stream.readByteArray();
            this.thumb_w = stream.readInt32();
            this.thumb_h = stream.readInt32();
            this.duration = stream.readInt32();
            this.w = stream.readInt32();
            this.h = stream.readInt32();
            this.size = stream.readInt32();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.thumb);
            stream.writeInt32(this.thumb_w);
            stream.writeInt32(this.thumb_h);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
            stream.writeInt32(this.size);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_decryptedMessageService extends DecryptedMessage {
        public static int constructor = -1438109059;

        public void readParams(SerializedData stream) {
            this.random_id = stream.readInt64();
            this.random_bytes = stream.readByteArray();
            this.action = (TL_decryptedMessageActionSetMessageTTL) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.random_id);
            stream.writeByteArray(this.random_bytes);
            this.action.serializeToStream(stream);
        }
    }

    public static class TL_destroy_session_none extends DestroySessionRes {
        public static int constructor = 1658015945;

        public void readParams(SerializedData stream) {
            this.session_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.session_id);
        }
    }

    public static class TL_destroy_session_ok extends DestroySessionRes {
        public static int constructor = -501201412;

        public void readParams(SerializedData stream) {
            this.session_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.session_id);
        }
    }

    public static class TL_dh_gen_fail extends Set_client_DH_params_answer {
        public static int constructor = -1499615742;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.new_nonce_hash3 = stream.readData(16);
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeRaw(this.new_nonce_hash3);
        }
    }

    public static class TL_dh_gen_ok extends Set_client_DH_params_answer {
        public static int constructor = 1003222836;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.new_nonce_hash1 = stream.readData(16);
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeRaw(this.new_nonce_hash1);
        }
    }

    public static class TL_dh_gen_retry extends Set_client_DH_params_answer {
        public static int constructor = 1188831161;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.new_nonce_hash2 = stream.readData(16);
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeRaw(this.new_nonce_hash2);
        }
    }

    public static class TL_document extends Document {
        public static int constructor = -1627626714;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.file_name = stream.readString();
            this.mime_type = stream.readString();
            this.size = stream.readInt32();
            this.thumb = (PhotoSize) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.dc_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            stream.writeString(this.file_name);
            stream.writeString(this.mime_type);
            stream.writeInt32(this.size);
            this.thumb.serializeToStream(stream);
            stream.writeInt32(this.dc_id);
        }
    }

    public static class TL_documentEmpty extends Document {
        public static int constructor = 922273905;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
        }
    }

    public static class TL_documentEncrypted extends Document {
        public static int constructor = 1431655766;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.file_name = stream.readString();
            this.mime_type = stream.readString();
            this.size = stream.readInt32();
            this.thumb = (PhotoSize) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.dc_id = stream.readInt32();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            stream.writeString(this.file_name);
            stream.writeString(this.mime_type);
            stream.writeInt32(this.size);
            this.thumb.serializeToStream(stream);
            stream.writeInt32(this.dc_id);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_encryptedChat extends EncryptedChat {
        public static int constructor = -94974410;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.access_hash = stream.readInt64();
            this.date = stream.readInt32();
            this.admin_id = stream.readInt32();
            this.participant_id = stream.readInt32();
            this.g_a_or_b = stream.readByteArray();
            this.key_fingerprint = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.date);
            stream.writeInt32(this.admin_id);
            stream.writeInt32(this.participant_id);
            stream.writeByteArray(this.g_a_or_b);
            stream.writeInt64(this.key_fingerprint);
        }
    }

    public static class TL_encryptedChatDiscarded extends EncryptedChat {
        public static int constructor = 332848423;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
        }
    }

    public static class TL_encryptedChatEmpty extends EncryptedChat {
        public static int constructor = -1417756512;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
        }
    }

    public static class TL_encryptedChatRequested extends EncryptedChat {
        public static int constructor = -931638658;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.access_hash = stream.readInt64();
            this.date = stream.readInt32();
            this.admin_id = stream.readInt32();
            this.participant_id = stream.readInt32();
            this.g_a = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.date);
            stream.writeInt32(this.admin_id);
            stream.writeInt32(this.participant_id);
            stream.writeByteArray(this.g_a);
        }
    }

    public static class TL_encryptedChatRequested_old extends EncryptedChat {
        public static int constructor = -39213129;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.access_hash = stream.readInt64();
            this.date = stream.readInt32();
            this.admin_id = stream.readInt32();
            this.participant_id = stream.readInt32();
            this.g_a = stream.readByteArray();
            stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(TL_encryptedChatRequested.constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.date);
            stream.writeInt32(this.admin_id);
            stream.writeInt32(this.participant_id);
            stream.writeByteArray(this.g_a);
        }
    }

    public static class TL_encryptedChatWaiting extends EncryptedChat {
        public static int constructor = 1006044124;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.access_hash = stream.readInt64();
            this.date = stream.readInt32();
            this.admin_id = stream.readInt32();
            this.participant_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.date);
            stream.writeInt32(this.admin_id);
            stream.writeInt32(this.participant_id);
        }
    }

    public static class TL_encryptedFile extends EncryptedFile {
        public static int constructor = 1248893260;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.size = stream.readInt32();
            this.dc_id = stream.readInt32();
            this.key_fingerprint = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.size);
            stream.writeInt32(this.dc_id);
            stream.writeInt32(this.key_fingerprint);
        }
    }

    public static class TL_encryptedFileEmpty extends EncryptedFile {
        public static int constructor = -1038136962;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_encryptedMessage extends EncryptedMessage {
        public static int constructor = -317144808;

        public void readParams(SerializedData stream) {
            this.random_id = stream.readInt64();
            this.chat_id = stream.readInt32();
            this.date = stream.readInt32();
            this.bytes = stream.readByteArray();
            this.file = (EncryptedFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.random_id);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.date);
            stream.writeByteArray(this.bytes);
            this.file.serializeToStream(stream);
        }
    }

    public static class TL_encryptedMessageService extends EncryptedMessage {
        public static int constructor = 594758406;

        public void readParams(SerializedData stream) {
            this.random_id = stream.readInt64();
            this.chat_id = stream.readInt32();
            this.date = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.random_id);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.date);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_fileEncryptedLocation extends FileLocation {
        public static int constructor = 1431655764;

        public void readParams(SerializedData stream) {
            this.dc_id = stream.readInt32();
            this.volume_id = stream.readInt64();
            this.local_id = stream.readInt32();
            this.secret = stream.readInt64();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.dc_id);
            stream.writeInt64(this.volume_id);
            stream.writeInt32(this.local_id);
            stream.writeInt64(this.secret);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_fileLocation extends FileLocation {
        public static int constructor = 1406570614;

        public void readParams(SerializedData stream) {
            this.dc_id = stream.readInt32();
            this.volume_id = stream.readInt64();
            this.local_id = stream.readInt32();
            this.secret = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.dc_id);
            stream.writeInt64(this.volume_id);
            stream.writeInt32(this.local_id);
            stream.writeInt64(this.secret);
        }
    }

    public static class TL_fileLocationUnavailable extends FileLocation {
        public static int constructor = 2086234950;

        public void readParams(SerializedData stream) {
            this.volume_id = stream.readInt64();
            this.local_id = stream.readInt32();
            this.secret = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.volume_id);
            stream.writeInt32(this.local_id);
            stream.writeInt64(this.secret);
        }
    }

    public static class TL_geoChat extends Chat {
        public static int constructor = 1978329690;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.access_hash = stream.readInt64();
            this.title = stream.readString();
            this.address = stream.readString();
            this.venue = stream.readString();
            this.geo = (GeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.photo = (ChatPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.participants_count = stream.readInt32();
            this.date = stream.readInt32();
            this.checked_in = stream.readBool();
            this.version = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeString(this.title);
            stream.writeString(this.address);
            stream.writeString(this.venue);
            this.geo.serializeToStream(stream);
            this.photo.serializeToStream(stream);
            stream.writeInt32(this.participants_count);
            stream.writeInt32(this.date);
            stream.writeBool(this.checked_in);
            stream.writeInt32(this.version);
        }
    }

    public static class TL_geoChatMessage extends GeoChatMessage {
        public static int constructor = 1158019297;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.id = stream.readInt32();
            this.from_id = stream.readInt32();
            this.date = stream.readInt32();
            this.message = stream.readString();
            this.media = (MessageMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.id);
            stream.writeInt32(this.from_id);
            stream.writeInt32(this.date);
            stream.writeString(this.message);
            this.media.serializeToStream(stream);
        }
    }

    public static class TL_geoChatMessageEmpty extends GeoChatMessage {
        public static int constructor = 1613830811;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.id);
        }
    }

    public static class TL_geoChatMessageService extends GeoChatMessage {
        public static int constructor = -749755826;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.id = stream.readInt32();
            this.from_id = stream.readInt32();
            this.date = stream.readInt32();
            this.action = (MessageAction) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.id);
            stream.writeInt32(this.from_id);
            stream.writeInt32(this.date);
            this.action.serializeToStream(stream);
        }
    }

    public static class TL_geoPoint extends GeoPoint {
        public static int constructor = 541710092;

        public void readParams(SerializedData stream) {
            this._long = stream.readDouble();
            this.lat = stream.readDouble();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeDouble(this._long);
            stream.writeDouble(this.lat);
        }
    }

    public static class TL_geoPointEmpty extends GeoPoint {
        public static int constructor = 286776671;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_geochats_messages extends geochats_Messages {
        public static int constructor = -783127119;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((GeoChatMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((GeoChatMessage) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_geochats_messagesSlice extends geochats_Messages {
        public static int constructor = -1135057944;

        public void readParams(SerializedData stream) {
            int a;
            this.count = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((GeoChatMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(this.count);
            stream.writeInt32(481674261);
            int count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((GeoChatMessage) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_help_appUpdate extends help_AppUpdate {
        public static int constructor = -1987579119;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.critical = stream.readBool();
            this.url = stream.readString();
            this.text = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeBool(this.critical);
            stream.writeString(this.url);
            stream.writeString(this.text);
        }
    }

    public static class TL_help_noAppUpdate extends help_AppUpdate {
        public static int constructor = -1000708810;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputAudio extends InputAudio {
        public static int constructor = 2010398975;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputAudioEmpty extends InputAudio {
        public static int constructor = -648356732;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputAudioFileLocation extends InputFileLocation {
        public static int constructor = 1960591437;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputChatPhoto extends InputChatPhoto {
        public static int constructor = -1293828344;

        public void readParams(SerializedData stream) {
            this.id = (InputPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.crop = (InputPhotoCrop) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
            this.crop.serializeToStream(stream);
        }
    }

    public static class TL_inputChatPhotoEmpty extends InputChatPhoto {
        public static int constructor = 480546647;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputChatUploadedPhoto extends InputChatPhoto {
        public static int constructor = -1809496270;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.crop = (InputPhotoCrop) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            this.crop.serializeToStream(stream);
        }
    }

    public static class TL_inputDocument extends InputDocument {
        public static int constructor = 410618194;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputDocumentEmpty extends InputDocument {
        public static int constructor = 1928391342;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputDocumentFileLocation extends InputFileLocation {
        public static int constructor = 1313188841;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputEncryptedFile extends InputEncryptedFile {
        public static int constructor = 1511503333;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputEncryptedFileBigUploaded extends InputEncryptedFile {
        public static int constructor = 767652808;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.parts = stream.readInt32();
            this.key_fingerprint = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt32(this.parts);
            stream.writeInt32(this.key_fingerprint);
        }
    }

    public static class TL_inputEncryptedFileEmpty extends InputEncryptedFile {
        public static int constructor = 406307684;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputEncryptedFileLocation extends InputFileLocation {
        public static int constructor = -182231723;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputEncryptedFileUploaded extends InputEncryptedFile {
        public static int constructor = 1690108678;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.parts = stream.readInt32();
            this.md5_checksum = stream.readString();
            this.key_fingerprint = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt32(this.parts);
            stream.writeString(this.md5_checksum);
            stream.writeInt32(this.key_fingerprint);
        }
    }

    public static class TL_inputFile extends InputFile {
        public static int constructor = -181407105;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.parts = stream.readInt32();
            this.name = stream.readString();
            this.md5_checksum = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt32(this.parts);
            stream.writeString(this.name);
            stream.writeString(this.md5_checksum);
        }
    }

    public static class TL_inputFileBig extends InputFile {
        public static int constructor = -95482955;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.parts = stream.readInt32();
            this.name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt32(this.parts);
            stream.writeString(this.name);
        }
    }

    public static class TL_inputFileLocation extends InputFileLocation {
        public static int constructor = 342061462;

        public void readParams(SerializedData stream) {
            this.volume_id = stream.readInt64();
            this.local_id = stream.readInt32();
            this.secret = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.volume_id);
            stream.writeInt32(this.local_id);
            stream.writeInt64(this.secret);
        }
    }

    public static class TL_inputGeoPoint extends InputGeoPoint {
        public static int constructor = -206066487;

        public void readParams(SerializedData stream) {
            this.lat = stream.readDouble();
            this._long = stream.readDouble();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeDouble(this.lat);
            stream.writeDouble(this._long);
        }
    }

    public static class TL_inputGeoPointEmpty extends InputGeoPoint {
        public static int constructor = -457104426;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputMediaAudio extends InputMedia {
        public static int constructor = -1986820223;
        public InputAudio id;

        public void readParams(SerializedData stream) {
            this.id = (InputAudio) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_inputMediaContact extends InputMedia {
        public static int constructor = -1494984313;

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
        }
    }

    public static class TL_inputMediaDocument extends InputMedia {
        public static int constructor = -779818943;
        public InputDocument id;

        public void readParams(SerializedData stream) {
            this.id = (InputDocument) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_inputMediaEmpty extends InputMedia {
        public static int constructor = -1771768449;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputMediaGeoPoint extends InputMedia {
        public static int constructor = -104578748;

        public void readParams(SerializedData stream) {
            this.geo_point = (InputGeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.geo_point.serializeToStream(stream);
        }
    }

    public static class TL_inputMediaPhoto extends InputMedia {
        public static int constructor = -1893027092;
        public InputPhoto id;

        public void readParams(SerializedData stream) {
            this.id = (InputPhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_inputMediaUploadedAudio extends InputMedia {
        public static int constructor = 1638323254;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.duration = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            stream.writeInt32(this.duration);
        }
    }

    public static class TL_inputMediaUploadedDocument extends InputMedia {
        public static int constructor = 887592125;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.file_name = stream.readString();
            this.mime_type = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            stream.writeString(this.file_name);
            stream.writeString(this.mime_type);
        }
    }

    public static class TL_inputMediaUploadedPhoto extends InputMedia {
        public static int constructor = 767900285;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
        }
    }

    public static class TL_inputMediaUploadedThumbDocument extends InputMedia {
        public static int constructor = 1044831837;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.thumb = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.file_name = stream.readString();
            this.mime_type = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            this.thumb.serializeToStream(stream);
            stream.writeString(this.file_name);
            stream.writeString(this.mime_type);
        }
    }

    public static class TL_inputMediaUploadedThumbVideo extends InputMedia {
        public static int constructor = -433544891;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.thumb = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.duration = stream.readInt32();
            this.w = stream.readInt32();
            this.h = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            this.thumb.serializeToStream(stream);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
        }
    }

    public static class TL_inputMediaUploadedVideo extends InputMedia {
        public static int constructor = 1212668202;

        public void readParams(SerializedData stream) {
            this.file = (InputFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.duration = stream.readInt32();
            this.w = stream.readInt32();
            this.h = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.file.serializeToStream(stream);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
        }
    }

    public static class TL_inputMediaVideo extends InputMedia {
        public static int constructor = 2130852582;
        public InputVideo id;

        public void readParams(SerializedData stream) {
            this.id = (InputVideo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.id.serializeToStream(stream);
        }
    }

    public static class TL_inputMessagesFilterEmpty extends MessagesFilter {
        public static int constructor = 1474492012;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputMessagesFilterPhotoVideo extends MessagesFilter {
        public static int constructor = 1458172132;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputMessagesFilterPhotos extends MessagesFilter {
        public static int constructor = -1777752804;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputMessagesFilterVideo extends MessagesFilter {
        public static int constructor = -1614803355;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputNotifyAll extends InputNotifyPeer {
        public static int constructor = -1540769658;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputNotifyChats extends InputNotifyPeer {
        public static int constructor = 1251338318;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputNotifyGeoChatPeer extends InputNotifyPeer {
        public static int constructor = 1301143240;
        public TL_inputGeoChat peer;

        public void readParams(SerializedData stream) {
            this.peer = (TL_inputGeoChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
        }
    }

    public static class TL_inputNotifyPeer extends InputNotifyPeer {
        public static int constructor = -1195615476;
        public InputPeer peer;

        public void readParams(SerializedData stream) {
            this.peer = (InputPeer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.peer.serializeToStream(stream);
        }
    }

    public static class TL_inputNotifyUsers extends InputNotifyPeer {
        public static int constructor = 423314455;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputPeerChat extends InputPeer {
        public static int constructor = 396093539;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
        }
    }

    public static class TL_inputPeerContact extends InputPeer {
        public static int constructor = 270785512;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_inputPeerEmpty extends InputPeer {
        public static int constructor = 2134579434;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputPeerForeign extends InputPeer {
        public static int constructor = -1690012891;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputPeerNotifyEventsAll extends InputPeerNotifyEvents {
        public static int constructor = -395694988;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputPeerNotifyEventsEmpty extends InputPeerNotifyEvents {
        public static int constructor = -265263912;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputPeerSelf extends InputPeer {
        public static int constructor = 2107670217;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputPhoto extends InputPhoto {
        public static int constructor = -74070332;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputPhotoCrop extends InputPhotoCrop {
        public static int constructor = -644787419;

        public void readParams(SerializedData stream) {
            this.crop_left = stream.readDouble();
            this.crop_top = stream.readDouble();
            this.crop_width = stream.readDouble();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeDouble(this.crop_left);
            stream.writeDouble(this.crop_top);
            stream.writeDouble(this.crop_width);
        }
    }

    public static class TL_inputPhotoCropAuto extends InputPhotoCrop {
        public static int constructor = -1377390588;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputPhotoEmpty extends InputPhoto {
        public static int constructor = 483901197;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputUserContact extends InputUser {
        public static int constructor = -2031530139;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_inputUserEmpty extends InputUser {
        public static int constructor = -1182234929;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputUserForeign extends InputUser {
        public static int constructor = 1700689151;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputUserSelf extends InputUser {
        public static int constructor = -138301121;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputVideo extends InputVideo {
        public static int constructor = -296249774;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_inputVideoEmpty extends InputVideo {
        public static int constructor = 1426648181;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_inputVideoFileLocation extends InputFileLocation {
        public static int constructor = 1023632620;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
        }
    }

    public static class TL_message extends Message {
        public static int constructor = 585853626;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.from_id = stream.readInt32();
            this.to_id = (Peer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.out = stream.readBool();
            this.unread = stream.readBool();
            this.date = stream.readInt32();
            this.message = stream.readString();
            this.media = (MessageMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            if (this.id < 0 || !(this.media == null || (this.media instanceof TL_messageMediaEmpty) || this.message == null || this.message.length() == 0 || !this.message.equals("-1"))) {
                this.attachPath = stream.readString();
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.from_id);
            this.to_id.serializeToStream(stream);
            stream.writeBool(this.out);
            stream.writeBool(this.unread);
            stream.writeInt32(this.date);
            stream.writeString(this.message);
            this.media.serializeToStream(stream);
            stream.writeString(this.attachPath);
        }
    }

    public static class TL_messageActionChatAddUser extends MessageAction {
        public static int constructor = 1581055051;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_messageActionChatCreate extends MessageAction {
        public static int constructor = -1503425638;

        public void readParams(SerializedData stream) {
            this.title = stream.readString();
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.users.add(Integer.valueOf(stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.title);
            stream.writeInt32(481674261);
            stream.writeInt32(this.users.size());
            Iterator i$ = this.users.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
        }
    }

    public static class TL_messageActionChatDeletePhoto extends MessageAction {
        public static int constructor = -1780220945;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_messageActionChatDeleteUser extends MessageAction {
        public static int constructor = -1297179892;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_messageActionChatEditPhoto extends MessageAction {
        public static int constructor = 2144015272;

        public void readParams(SerializedData stream) {
            this.photo = (Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.photo.serializeToStream(stream);
        }
    }

    public static class TL_messageActionChatEditTitle extends MessageAction {
        public static int constructor = -1247687078;

        public void readParams(SerializedData stream) {
            this.title = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.title);
        }
    }

    public static class TL_messageActionEmpty extends MessageAction {
        public static int constructor = -1230047312;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_messageActionGeoChatCheckin extends MessageAction {
        public static int constructor = 209540062;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_messageActionGeoChatCreate extends MessageAction {
        public static int constructor = 1862504124;

        public void readParams(SerializedData stream) {
            this.title = stream.readString();
            this.address = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.title);
            stream.writeString(this.address);
        }
    }

    public static class TL_messageActionLoginUnknownLocation extends MessageAction {
        public static int constructor = 1431655925;

        public void readParams(SerializedData stream) {
            this.title = stream.readString();
            this.address = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.title);
            stream.writeString(this.address);
        }
    }

    public static class TL_messageActionTTLChange extends MessageAction {
        public static int constructor = 1431655762;

        public void readParams(SerializedData stream) {
            this.ttl = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.ttl);
        }
    }

    public static class TL_messageActionUserJoined extends MessageAction {
        public static int constructor = 1431655760;

        public void readParams(SerializedData stream) {
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_messageActionUserUpdatedPhoto extends MessageAction {
        public static int constructor = 1431655761;

        public void readParams(SerializedData stream) {
            this.newUserPhoto = (UserProfilePhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.newUserPhoto.serializeToStream(stream);
        }
    }

    public static class TL_messageEmpty extends Message {
        public static int constructor = -2082087340;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
        }
    }

    public static class TL_messageForwarded extends Message {
        public static int constructor = 99903492;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.fwd_from_id = stream.readInt32();
            this.fwd_date = stream.readInt32();
            this.from_id = stream.readInt32();
            this.to_id = (Peer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.out = stream.readBool();
            this.unread = stream.readBool();
            this.date = stream.readInt32();
            this.message = stream.readString();
            this.media = (MessageMedia) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            if (this.id < 0) {
                this.fwd_msg_id = stream.readInt32();
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.fwd_from_id);
            stream.writeInt32(this.fwd_date);
            stream.writeInt32(this.from_id);
            this.to_id.serializeToStream(stream);
            stream.writeBool(this.out);
            stream.writeBool(this.unread);
            stream.writeInt32(this.date);
            stream.writeString(this.message);
            this.media.serializeToStream(stream);
            if (this.id < 0) {
                stream.writeInt32(this.fwd_msg_id);
            }
        }
    }

    public static class TL_messageMediaAudio extends MessageMedia {
        public static int constructor = -961117440;

        public void readParams(SerializedData stream) {
            this.audio = (Audio) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.audio.serializeToStream(stream);
        }
    }

    public static class TL_messageMediaContact extends MessageMedia {
        public static int constructor = 1585262393;

        public void readParams(SerializedData stream) {
            this.phone_number = stream.readString();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.phone_number);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_messageMediaDocument extends MessageMedia {
        public static int constructor = 802824708;

        public void readParams(SerializedData stream) {
            this.document = (Document) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.document.serializeToStream(stream);
        }
    }

    public static class TL_messageMediaEmpty extends MessageMedia {
        public static int constructor = 1038967584;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_messageMediaGeo extends MessageMedia {
        public static int constructor = 1457575028;

        public void readParams(SerializedData stream) {
            this.geo = (GeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.geo.serializeToStream(stream);
        }
    }

    public static class TL_messageMediaPhoto extends MessageMedia {
        public static int constructor = -926655958;

        public void readParams(SerializedData stream) {
            this.photo = (Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.photo.serializeToStream(stream);
        }
    }

    public static class TL_messageMediaUnsupported extends MessageMedia {
        public static int constructor = 694364726;

        public void readParams(SerializedData stream) {
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_messageMediaVideo extends MessageMedia {
        public static int constructor = -1563278704;

        public void readParams(SerializedData stream) {
            this.video = (Video) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.video.serializeToStream(stream);
        }
    }

    public static class TL_messageService extends Message {
        public static int constructor = -1618124613;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.from_id = stream.readInt32();
            this.to_id = (Peer) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.out = stream.readBool();
            this.unread = stream.readBool();
            this.date = stream.readInt32();
            this.action = (MessageAction) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.from_id);
            this.to_id.serializeToStream(stream);
            stream.writeBool(this.out);
            stream.writeBool(this.unread);
            stream.writeInt32(this.date);
            this.action.serializeToStream(stream);
        }
    }

    public static class TL_messages_dhConfig extends messages_DhConfig {
        public static int constructor = 740433629;

        public void readParams(SerializedData stream) {
            this.g = stream.readInt32();
            this.p = stream.readByteArray();
            this.version = stream.readInt32();
            this.random = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.g);
            stream.writeByteArray(this.p);
            stream.writeInt32(this.version);
            stream.writeByteArray(this.random);
        }
    }

    public static class TL_messages_dhConfigNotModified extends messages_DhConfig {
        public static int constructor = -1058912715;

        public void readParams(SerializedData stream) {
            this.random = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.random);
        }
    }

    public static class TL_messages_dialogs extends messages_Dialogs {
        public static int constructor = 364538944;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.dialogs.add((TL_dialog) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.dialogs.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_dialog) this.dialogs.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_dialogsSlice extends messages_Dialogs {
        public static int constructor = 1910543603;

        public void readParams(SerializedData stream) {
            int a;
            this.count = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.dialogs.add((TL_dialog) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(this.count);
            stream.writeInt32(481674261);
            int count = this.dialogs.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_dialog) this.dialogs.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_message extends messages_Message {
        public static int constructor = -7289833;

        public void readParams(SerializedData stream) {
            int a;
            this.message = (Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
            stream.writeInt32(481674261);
            int count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_messageEmpty extends messages_Message {
        public static int constructor = 1062078024;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_messages_messages extends messages_Messages {
        public static int constructor = -1938715001;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_messagesSlice extends messages_Messages {
        public static int constructor = 189033187;

        public void readParams(SerializedData stream) {
            int a;
            this.count = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(this.count);
            stream.writeInt32(481674261);
            int count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_sentEncryptedFile extends messages_SentEncryptedMessage {
        public static int constructor = -1802240206;

        public void readParams(SerializedData stream) {
            this.date = stream.readInt32();
            this.file = (EncryptedFile) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.date);
            this.file.serializeToStream(stream);
        }
    }

    public static class TL_messages_sentEncryptedMessage extends messages_SentEncryptedMessage {
        public static int constructor = 1443858741;

        public void readParams(SerializedData stream) {
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_messages_sentMessage extends messages_SentMessage {
        public static int constructor = -772484260;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.date = stream.readInt32();
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.date);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_messages_sentMessageLink extends messages_SentMessage {
        public static int constructor = -371504577;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.date = stream.readInt32();
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.links.add((TL_contacts_link) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.date);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
            stream.writeInt32(481674261);
            stream.writeInt32(this.links.size());
            Iterator i$ = this.links.iterator();
            while (i$.hasNext()) {
                ((TL_contacts_link) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_messages_statedMessage extends messages_StatedMessage {
        public static int constructor = -797251802;

        public void readParams(SerializedData stream) {
            int a;
            this.message = (Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
            stream.writeInt32(481674261);
            int count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_messages_statedMessageLink extends messages_StatedMessage {
        public static int constructor = -1448138623;

        public void readParams(SerializedData stream) {
            int a;
            this.message = (Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.links.add((TL_contacts_link) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
            stream.writeInt32(481674261);
            int count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.links.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contacts_link) this.links.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_messages_statedMessages extends messages_StatedMessages {
        public static int constructor = -1768654661;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_messages_statedMessagesLinks extends messages_StatedMessages {
        public static int constructor = 1047852486;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.links.add((TL_contacts_link) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.pts = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.links.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((TL_contacts_link) this.links.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.pts);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_msg_detailed_info extends MsgDetailedInfo {
        public static int constructor = 661470918;

        public void readParams(SerializedData stream) {
            this.msg_id = stream.readInt64();
            this.answer_msg_id = stream.readInt64();
            this.bytes = stream.readInt32();
            this.status = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.msg_id);
            stream.writeInt64(this.answer_msg_id);
            stream.writeInt32(this.bytes);
            stream.writeInt32(this.status);
        }
    }

    public static class TL_msg_new_detailed_info extends MsgDetailedInfo {
        public static int constructor = -2137147681;

        public void readParams(SerializedData stream) {
            this.answer_msg_id = stream.readInt64();
            this.bytes = stream.readInt32();
            this.status = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.answer_msg_id);
            stream.writeInt32(this.bytes);
            stream.writeInt32(this.status);
        }
    }

    public static class TL_peerChat extends Peer {
        public static int constructor = -1160714821;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
        }
    }

    public static class TL_peerNotifyEventsAll extends PeerNotifyEvents {
        public static int constructor = 1830677896;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_peerNotifyEventsEmpty extends PeerNotifyEvents {
        public static int constructor = -1378534221;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_peerNotifySettings extends PeerNotifySettings {
        public static int constructor = -1923214866;

        public void readParams(SerializedData stream) {
            this.mute_until = stream.readInt32();
            this.sound = stream.readString();
            this.show_previews = stream.readBool();
            this.events_mask = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.mute_until);
            stream.writeString(this.sound);
            stream.writeBool(this.show_previews);
            stream.writeInt32(this.events_mask);
        }
    }

    public static class TL_peerNotifySettingsEmpty extends PeerNotifySettings {
        public static int constructor = 1889961234;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_peerUser extends Peer {
        public static int constructor = -1649296275;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_photo extends Photo {
        public static int constructor = 582313809;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.caption = stream.readString();
            this.geo = (GeoPoint) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.sizes.add((PhotoSize) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            stream.writeString(this.caption);
            this.geo.serializeToStream(stream);
            stream.writeInt32(481674261);
            stream.writeInt32(this.sizes.size());
            Iterator i$ = this.sizes.iterator();
            while (i$.hasNext()) {
                ((PhotoSize) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_photoCachedSize extends PhotoSize {
        public static int constructor = -374917894;

        public void readParams(SerializedData stream) {
            this.type = stream.readString();
            this.location = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.w = stream.readInt32();
            this.h = stream.readInt32();
            this.bytes = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.type);
            this.location.serializeToStream(stream);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
            stream.writeByteArray(this.bytes);
        }
    }

    public static class TL_photoEmpty extends Photo {
        public static int constructor = 590459437;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
        }
    }

    public static class TL_photoSize extends PhotoSize {
        public static int constructor = 2009052699;

        public void readParams(SerializedData stream) {
            this.type = stream.readString();
            this.location = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.w = stream.readInt32();
            this.h = stream.readInt32();
            this.size = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.type);
            this.location.serializeToStream(stream);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
            stream.writeInt32(this.size);
        }
    }

    public static class TL_photoSizeEmpty extends PhotoSize {
        public static int constructor = 236446268;

        public void readParams(SerializedData stream) {
            this.type = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeString(this.type);
        }
    }

    public static class TL_photos_photos extends photos_Photos {
        public static int constructor = -1916114267;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.photos.add((Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.photos.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Photo) this.photos.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_photos_photosSlice extends photos_Photos {
        public static int constructor = 352657236;

        public void readParams(SerializedData stream) {
            int a;
            this.count = stream.readInt32();
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.photos.add((Photo) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(this.count);
            stream.writeInt32(481674261);
            int count = this.photos.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Photo) this.photos.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
        }
    }

    public static class TL_rpc_answer_dropped extends RpcDropAnswer {
        public static int constructor = -1539647305;

        public void readParams(SerializedData stream) {
            this.msg_id = stream.readInt64();
            this.seq_no = stream.readInt32();
            this.bytes = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.msg_id);
            stream.writeInt32(this.seq_no);
            stream.writeInt32(this.bytes);
        }
    }

    public static class TL_rpc_answer_dropped_running extends RpcDropAnswer {
        public static int constructor = -847714938;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_rpc_answer_unknown extends RpcDropAnswer {
        public static int constructor = 1579864942;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_rpc_error extends RpcError {
        public static int constructor = 558156313;

        public void readParams(SerializedData stream) {
            this.error_code = stream.readInt32();
            this.error_message = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.error_code);
            stream.writeString(this.error_message);
        }
    }

    public static class TL_rpc_req_error extends RpcError {
        public static int constructor = 2061775605;

        public void readParams(SerializedData stream) {
            this.query_id = stream.readInt64();
            this.error_code = stream.readInt32();
            this.error_message = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.query_id);
            stream.writeInt32(this.error_code);
            stream.writeString(this.error_message);
        }
    }

    public static class TL_server_DH_params_fail extends Server_DH_Params {
        public static int constructor = 2043348061;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.new_nonce_hash = stream.readData(16);
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeRaw(this.new_nonce_hash);
        }
    }

    public static class TL_server_DH_params_ok extends Server_DH_Params {
        public static int constructor = -790100132;

        public void readParams(SerializedData stream) {
            this.nonce = stream.readData(16);
            this.server_nonce = stream.readData(16);
            this.encrypted_answer = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeRaw(this.nonce);
            stream.writeRaw(this.server_nonce);
            stream.writeByteArray(this.encrypted_answer);
        }
    }

    public static class TL_storage_fileGif extends storage_FileType {
        public static int constructor = -891180321;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_fileJpeg extends storage_FileType {
        public static int constructor = 8322574;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_fileMov extends storage_FileType {
        public static int constructor = 1258941372;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_fileMp3 extends storage_FileType {
        public static int constructor = 1384777335;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_fileMp4 extends storage_FileType {
        public static int constructor = -1278304028;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_filePartial extends storage_FileType {
        public static int constructor = 1086091090;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_filePng extends storage_FileType {
        public static int constructor = 172975040;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_fileUnknown extends storage_FileType {
        public static int constructor = -1432995067;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_storage_fileWebp extends storage_FileType {
        public static int constructor = 276907596;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_updateActivation extends Update {
        public static int constructor = 1869154659;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_updateChatParticipantAdd extends Update {
        public static int constructor = 974056226;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.user_id = stream.readInt32();
            this.inviter_id = stream.readInt32();
            this.version = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.inviter_id);
            stream.writeInt32(this.version);
        }
    }

    public static class TL_updateChatParticipantDelete extends Update {
        public static int constructor = 1851755554;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.user_id = stream.readInt32();
            this.version = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.version);
        }
    }

    public static class TL_updateChatParticipants extends Update {
        public static int constructor = 125178264;

        public void readParams(SerializedData stream) {
            this.participants = (ChatParticipants) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.participants.serializeToStream(stream);
        }
    }

    public static class TL_updateChatUserTyping extends Update {
        public static int constructor = 1011273702;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_updateContactLink extends Update {
        public static int constructor = 1369737882;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.my_link = (contacts_MyLink) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.foreign_link = (contacts_ForeignLink) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            this.my_link.serializeToStream(stream);
            this.foreign_link.serializeToStream(stream);
        }
    }

    public static class TL_updateContactRegistered extends Update {
        public static int constructor = 628472761;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_updateDcOptions extends Update {
        public static int constructor = -1906403213;

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.dc_options.add((TL_dcOption) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.dc_options.size());
            Iterator i$ = this.dc_options.iterator();
            while (i$.hasNext()) {
                ((TL_dcOption) i$.next()).serializeToStream(stream);
            }
        }
    }

    public static class TL_updateDeleteMessages extends Update {
        public static int constructor = -1456734682;

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.messages.add(Integer.valueOf(stream.readInt32()));
            }
            this.pts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.messages.size());
            Iterator i$ = this.messages.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
            stream.writeInt32(this.pts);
        }
    }

    public static class TL_updateEncryptedChatTyping extends Update {
        public static int constructor = 386986326;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
        }
    }

    public static class TL_updateEncryptedMessagesRead extends Update {
        public static int constructor = 956179895;

        public void readParams(SerializedData stream) {
            this.chat_id = stream.readInt32();
            this.max_date = stream.readInt32();
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.chat_id);
            stream.writeInt32(this.max_date);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_updateEncryption extends Update {
        public static int constructor = -1264392051;

        public void readParams(SerializedData stream) {
            this.chat = (EncryptedChat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.chat.serializeToStream(stream);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_updateMessageID extends Update {
        public static int constructor = 1318109142;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.random_id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.random_id);
        }
    }

    public static class TL_updateNewAuthorization extends Update {
        public static int constructor = -1895411046;

        public void readParams(SerializedData stream) {
            this.auth_key_id = stream.readInt64();
            this.date = stream.readInt32();
            this.device = stream.readString();
            this.location = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.auth_key_id);
            stream.writeInt32(this.date);
            stream.writeString(this.device);
            stream.writeString(this.location);
        }
    }

    public static class TL_updateNewEncryptedMessage extends Update {
        public static int constructor = 314359194;
        public EncryptedMessage message;

        public void readParams(SerializedData stream) {
            this.message = (EncryptedMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.qts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
            stream.writeInt32(this.qts);
        }
    }

    public static class TL_updateNewGeoChatMessage extends Update {
        public static int constructor = 1516823543;
        public GeoChatMessage message;

        public void readParams(SerializedData stream) {
            this.message = (GeoChatMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
        }
    }

    public static class TL_updateNewMessage extends Update {
        public static int constructor = 20626867;
        public Message message;

        public void readParams(SerializedData stream) {
            this.message = (Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.pts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.message.serializeToStream(stream);
            stream.writeInt32(this.pts);
        }
    }

    public static class TL_updateReadMessages extends Update {
        public static int constructor = -966484431;

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.messages.add(Integer.valueOf(stream.readInt32()));
            }
            this.pts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.messages.size());
            Iterator i$ = this.messages.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
            stream.writeInt32(this.pts);
        }
    }

    public static class TL_updateRestoreMessages extends Update {
        public static int constructor = -782376883;

        public void readParams(SerializedData stream) {
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.messages.add(Integer.valueOf(stream.readInt32()));
            }
            this.pts = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            stream.writeInt32(this.messages.size());
            Iterator i$ = this.messages.iterator();
            while (i$.hasNext()) {
                stream.writeInt32(((Integer) i$.next()).intValue());
            }
            stream.writeInt32(this.pts);
        }
    }

    public static class TL_updateShort extends Updates {
        public static int constructor = 2027216577;

        public void readParams(SerializedData stream) {
            this.update = (Update) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.date = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.update.serializeToStream(stream);
            stream.writeInt32(this.date);
        }
    }

    public static class TL_updateShortChatMessage extends Updates {
        public static int constructor = 724548942;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.from_id = stream.readInt32();
            this.chat_id = stream.readInt32();
            this.message = stream.readString();
            this.pts = stream.readInt32();
            this.date = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.from_id);
            stream.writeInt32(this.chat_id);
            stream.writeString(this.message);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.date);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_updateShortMessage extends Updates {
        public static int constructor = -738961532;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.from_id = stream.readInt32();
            this.message = stream.readString();
            this.pts = stream.readInt32();
            this.date = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeInt32(this.from_id);
            stream.writeString(this.message);
            stream.writeInt32(this.pts);
            stream.writeInt32(this.date);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_updateUserName extends Update {
        public static int constructor = -635250259;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
        }
    }

    public static class TL_updateUserPhoto extends Update {
        public static int constructor = -1791935732;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.photo = (UserProfilePhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.previous = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            this.photo.serializeToStream(stream);
            stream.writeBool(this.previous);
        }
    }

    public static class TL_updateUserStatus extends Update {
        public static int constructor = 469489699;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
            this.status = (UserStatus) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
            this.status.serializeToStream(stream);
        }
    }

    public static class TL_updateUserTyping extends Update {
        public static int constructor = 1806337288;

        public void readParams(SerializedData stream) {
            this.user_id = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.user_id);
        }
    }

    public static class TL_updates extends Updates {
        public static int constructor = 1957577280;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.updates.add((Update) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.date = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.updates.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Update) this.updates.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.date);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_updatesCombined extends Updates {
        public static int constructor = 1918567619;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.updates.add((Update) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.date = stream.readInt32();
            this.seq_start = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.updates.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Update) this.updates.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(this.date);
            stream.writeInt32(this.seq_start);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_updatesTooLong extends Updates {
        public static int constructor = -484987010;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_updates_difference extends updates_Difference {
        public static int constructor = 16030880;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.new_messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.new_encrypted_messages.add((EncryptedMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.other_updates.add((Update) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.state = (TL_updates_state) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.new_messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.new_messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.new_encrypted_messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((EncryptedMessage) this.new_encrypted_messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.other_updates.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Update) this.other_updates.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            this.state.serializeToStream(stream);
        }
    }

    public static class TL_updates_differenceEmpty extends updates_Difference {
        public static int constructor = 1567990072;

        public void readParams(SerializedData stream) {
            this.date = stream.readInt32();
            this.seq = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.date);
            stream.writeInt32(this.seq);
        }
    }

    public static class TL_updates_differenceSlice extends updates_Difference {
        public static int constructor = -1459938943;

        public void readParams(SerializedData stream) {
            int a;
            stream.readInt32();
            int count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.new_messages.add((Message) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.new_encrypted_messages.add((EncryptedMessage) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.other_updates.add((Update) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.chats.add((Chat) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            stream.readInt32();
            count = stream.readInt32();
            for (a = 0; a < count; a++) {
                this.users.add((User) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.intermediate_state = (TL_updates_state) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            int a;
            stream.writeInt32(constructor);
            stream.writeInt32(481674261);
            int count = this.new_messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Message) this.new_messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.new_encrypted_messages.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((EncryptedMessage) this.new_encrypted_messages.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.other_updates.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Update) this.other_updates.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.chats.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((Chat) this.chats.get(a)).serializeToStream(stream);
            }
            stream.writeInt32(481674261);
            count = this.users.size();
            stream.writeInt32(count);
            for (a = 0; a < count; a++) {
                ((User) this.users.get(a)).serializeToStream(stream);
            }
            this.intermediate_state.serializeToStream(stream);
        }
    }

    public static class TL_userContact extends User {
        public static int constructor = -218397927;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
            this.access_hash = stream.readInt64();
            this.phone = stream.readString();
            this.photo = (UserProfilePhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.status = (UserStatus) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
            stream.writeInt64(this.access_hash);
            stream.writeString(this.phone);
            this.photo.serializeToStream(stream);
            this.status.serializeToStream(stream);
        }
    }

    public static class TL_userDeleted extends User {
        public static int constructor = -1298475060;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
        }
    }

    public static class TL_userEmpty extends User {
        public static int constructor = 537022650;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.first_name = "DELETED";
            this.last_name = BuildConfig.FLAVOR;
            this.phone = BuildConfig.FLAVOR;
            this.status = new TL_userStatusEmpty();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
        }
    }

    public static class TL_userForeign extends User {
        public static int constructor = 1377093789;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
            this.access_hash = stream.readInt64();
            this.photo = (UserProfilePhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.status = (UserStatus) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
            stream.writeInt64(this.access_hash);
            this.photo.serializeToStream(stream);
            this.status.serializeToStream(stream);
        }
    }

    public static class TL_userProfilePhoto extends UserProfilePhoto {
        public static int constructor = -715532088;

        public void readParams(SerializedData stream) {
            this.photo_id = stream.readInt64();
            this.photo_small = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.photo_big = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.photo_id);
            this.photo_small.serializeToStream(stream);
            this.photo_big.serializeToStream(stream);
        }
    }

    public static class TL_userProfilePhotoEmpty extends UserProfilePhoto {
        public static int constructor = 1326562017;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_userProfilePhotoOld extends UserProfilePhoto {
        public static int constructor = -1727196013;

        public void readParams(SerializedData stream) {
            this.photo_small = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.photo_big = (FileLocation) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            this.photo_small.serializeToStream(stream);
            this.photo_big.serializeToStream(stream);
        }
    }

    public static class TL_userRequest extends User {
        public static int constructor = 585682608;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
            this.access_hash = stream.readInt64();
            this.phone = stream.readString();
            this.photo = (UserProfilePhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.status = (UserStatus) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
            stream.writeInt64(this.access_hash);
            stream.writeString(this.phone);
            this.photo.serializeToStream(stream);
            this.status.serializeToStream(stream);
        }
    }

    public static class TL_userSelf extends User {
        public static int constructor = 1912944108;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.first_name = stream.readString();
            this.last_name = stream.readString();
            this.phone = stream.readString();
            this.photo = (UserProfilePhoto) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.status = (UserStatus) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.inactive = stream.readBool();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.first_name);
            stream.writeString(this.last_name);
            stream.writeString(this.phone);
            this.photo.serializeToStream(stream);
            this.status.serializeToStream(stream);
            stream.writeBool(this.inactive);
        }
    }

    public static class TL_userStatusEmpty extends UserStatus {
        public static int constructor = 164646985;

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
        }
    }

    public static class TL_userStatusOffline extends UserStatus {
        public static int constructor = 9203775;

        public void readParams(SerializedData stream) {
            this.was_online = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.was_online);
        }
    }

    public static class TL_userStatusOnline extends UserStatus {
        public static int constructor = -306628279;

        public void readParams(SerializedData stream) {
            this.expires = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.expires);
        }
    }

    public static class TL_video extends Video {
        public static int constructor = 1510253727;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.caption = stream.readString();
            this.duration = stream.readInt32();
            this.size = stream.readInt32();
            this.thumb = (PhotoSize) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.dc_id = stream.readInt32();
            this.w = stream.readInt32();
            this.h = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            stream.writeString(this.caption);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.size);
            this.thumb.serializeToStream(stream);
            stream.writeInt32(this.dc_id);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
        }
    }

    public static class TL_videoEmpty extends Video {
        public static int constructor = -1056548696;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
        }
    }

    public static class TL_videoEncrypted extends Video {
        public static int constructor = 1431655763;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt64();
            this.access_hash = stream.readInt64();
            this.user_id = stream.readInt32();
            this.date = stream.readInt32();
            this.caption = stream.readString();
            this.duration = stream.readInt32();
            this.size = stream.readInt32();
            this.thumb = (PhotoSize) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32());
            this.dc_id = stream.readInt32();
            this.w = stream.readInt32();
            this.h = stream.readInt32();
            this.key = stream.readByteArray();
            this.iv = stream.readByteArray();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.user_id);
            stream.writeInt32(this.date);
            stream.writeString(this.caption);
            stream.writeInt32(this.duration);
            stream.writeInt32(this.size);
            this.thumb.serializeToStream(stream);
            stream.writeInt32(this.dc_id);
            stream.writeInt32(this.w);
            stream.writeInt32(this.h);
            stream.writeByteArray(this.key);
            stream.writeByteArray(this.iv);
        }
    }

    public static class TL_wallPaper extends WallPaper {
        public static int constructor = -860866985;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.title = stream.readString();
            stream.readInt32();
            int count = stream.readInt32();
            for (int a = 0; a < count; a++) {
                this.sizes.add((PhotoSize) TLClassStore.Instance().TLdeserialize(stream, stream.readInt32()));
            }
            this.color = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.title);
            stream.writeInt32(481674261);
            stream.writeInt32(this.sizes.size());
            Iterator i$ = this.sizes.iterator();
            while (i$.hasNext()) {
                ((PhotoSize) i$.next()).serializeToStream(stream);
            }
            stream.writeInt32(this.color);
        }
    }

    public static class TL_wallPaperSolid extends WallPaper {
        public static int constructor = 1662091044;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.title = stream.readString();
            this.bg_color = stream.readInt32();
            this.color = stream.readInt32();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.id);
            stream.writeString(this.title);
            stream.writeInt32(this.bg_color);
            stream.writeInt32(this.color);
        }
    }

    public static class TL_encryptedChat_old extends TL_encryptedChat {
        public static int constructor = 1711395151;

        public void readParams(SerializedData stream) {
            this.id = stream.readInt32();
            this.access_hash = stream.readInt64();
            this.date = stream.readInt32();
            this.admin_id = stream.readInt32();
            this.participant_id = stream.readInt32();
            this.g_a_or_b = stream.readByteArray();
            stream.readByteArray();
            this.key_fingerprint = stream.readInt64();
        }

        public void serializeToStream(SerializedData stream) {
            stream.writeInt32(TL_encryptedChat.constructor);
            stream.writeInt32(this.id);
            stream.writeInt64(this.access_hash);
            stream.writeInt32(this.date);
            stream.writeInt32(this.admin_id);
            stream.writeInt32(this.participant_id);
            stream.writeByteArray(this.g_a_or_b);
            stream.writeInt64(this.key_fingerprint);
        }
    }
}
