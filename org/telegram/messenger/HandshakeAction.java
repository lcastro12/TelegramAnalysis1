package org.telegram.messenger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.TL.TLClassStore;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.Server_DH_Params;
import org.telegram.TL.TLRPC.Set_client_DH_params_answer;
import org.telegram.TL.TLRPC.TL_client_DH_inner_data;
import org.telegram.TL.TLRPC.TL_dh_gen_fail;
import org.telegram.TL.TLRPC.TL_dh_gen_ok;
import org.telegram.TL.TLRPC.TL_dh_gen_retry;
import org.telegram.TL.TLRPC.TL_msgs_ack;
import org.telegram.TL.TLRPC.TL_p_q_inner_data;
import org.telegram.TL.TLRPC.TL_req_DH_params;
import org.telegram.TL.TLRPC.TL_req_pq;
import org.telegram.TL.TLRPC.TL_resPQ;
import org.telegram.TL.TLRPC.TL_server_DH_inner_data;
import org.telegram.TL.TLRPC.TL_server_DH_params_ok;
import org.telegram.TL.TLRPC.TL_set_client_DH_params;
import org.telegram.messenger.TcpConnection.TcpConnectionDelegate;
import org.telegram.messenger.Utilities.TPFactorizedValue;

public class HandshakeAction extends Action implements TcpConnectionDelegate {
    static ArrayList<HashMap<String, Object>> serverPublicKeys = null;
    private byte[] authKey;
    private byte[] authKeyId;
    private byte[] authNewNonce;
    private byte[] authNonce;
    private byte[] authServerNonce;
    public Datacenter datacenter;
    private long lastOutgoingMessageId;
    final Integer lock = Integer.valueOf(1);
    private ArrayList<Long> processedMessageIds;
    private boolean processedPQRes;
    private byte[] reqDHMsgData;
    private byte[] reqPQMsgData;
    ServerSalt serverSalt;
    private byte[] setClientDHParamsMsgData;
    int timeDifference;
    private boolean wasDisconnect = false;

    public HandshakeAction(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

    public void execute(HashMap params) {
        FileLog.m798d("tmessages", String.format(Locale.US, "Begin handshake with DC%d", new Object[]{Integer.valueOf(this.datacenter.datacenterId)}));
        beginHandshake(true);
    }

    void beginHandshake(boolean dropConnection) {
        if (this.datacenter.connection == null) {
            this.datacenter.connection = new TcpConnection(this.datacenter.datacenterId);
            this.datacenter.connection.delegate = this;
            this.datacenter.connection.transportRequestClass = RPCRequest.RPCRequestClassGeneric;
        }
        this.processedMessageIds = new ArrayList();
        this.authNonce = null;
        this.authServerNonce = null;
        this.authNewNonce = null;
        this.authKey = null;
        this.authKeyId = null;
        this.processedPQRes = false;
        this.reqPQMsgData = null;
        this.reqDHMsgData = null;
        this.setClientDHParamsMsgData = null;
        if (dropConnection) {
            this.datacenter.connection.suspendConnection(true);
            this.datacenter.connection.connect();
        }
        TL_req_pq reqPq = new TL_req_pq();
        byte[] nonceBytes = new byte[16];
        MessagesController.random.nextBytes(nonceBytes);
        reqPq.nonce = nonceBytes;
        this.authNonce = nonceBytes;
        this.reqPQMsgData = sendMessageData(reqPq, generateMessageId());
    }

    HashMap<String, Object> selectPublicKey(ArrayList<Long> fingerprints) {
        synchronized (this.lock) {
            if (serverPublicKeys == null) {
                serverPublicKeys = new ArrayList();
                HashMap<String, Object> map = new HashMap();
                map.put("key", new BigInteger[]{new BigInteger("c150023e2f70db7985ded064759cfecf0af328e69a41daf4d6f01b538135a6f91f8f8b2a0ec9ba9720ce352efcf6c5680ffc424bd634864902de0b4bd6d49f4e580230e3ae97d95c8b19442b3c0a10d8f5633fecedd6926a7f6dab0ddb7d457f9ea81b8465fcd6fffeed114011df91c059caedaf97625f6c96ecc74725556934ef781d866b34f011fce4d835a090196e9a5f0e4449af7eb697ddb9076494ca5f81104a305b6dd27665722c46b60e5df680fb16b210607ef217652e60236c255f6a28315f4083a96791d7214bf64c1df4fd0db1944fb26a2a57031b32eee64ad15a8ba68885cde74a5bfc920f6abf59ba5c75506373e7130f9042da922179251f", 16), new BigInteger("010001", 16)});
                map.put("fingerprint", Long.valueOf(-4344800451088585951L));
                serverPublicKeys.add(map);
                map = new HashMap();
                map.put("key", new BigInteger[]{new BigInteger("c6aeda78b02a251db4b6441031f467fa871faed32526c436524b1fb3b5dca28efb8c089dd1b46d92c895993d87108254951c5f001a0f055f3063dcd14d431a300eb9e29517e359a1c9537e5e87ab1b116faecf5d17546ebc21db234d9d336a693efcb2b6fbcca1e7d1a0be414dca408a11609b9c4269a920b09fed1f9a1597be02761430f09e4bc48fcafbe289054c99dba51b6b5eb7d9c3a2ab4e490545b4676bd620e93804bcac93bf94f73f92c729ca899477ff17625ef14a934d51dc11d5f8650a3364586b3a52fcff2fedec8a8406cac4e751705a472e55707e3c8cd5594342b119c6c3293532d85dbe9271ed54a2fd18b4dc79c04a30951107d5639397", 16), new BigInteger("010001", 16)});
                map.put("fingerprint", Long.valueOf(-7306692244673891685L));
                serverPublicKeys.add(map);
                map = new HashMap();
                map.put("key", new BigInteger[]{new BigInteger("b1066749655935f0a5936f517034c943bea7f3365a8931ae52c8bcb14856f004b83d26cf2839be0f22607470d67481771c1ce5ec31de16b20bbaa4ecd2f7d2ecf6b6356f27501c226984263edc046b89fb6d3981546b01d7bd34fedcfcc1058e2d494bda732ff813e50e1c6ae249890b225f82b22b1e55fcb063dc3c0e18e91c28d0c4aa627dec8353eee6038a95a4fd1ca984eb09f94aeb7a2220635a8ceb450ea7e61d915cdb4eecedaa083aa3801daf071855ec1fb38516cb6c2996d2d60c0ecbcfa57e4cf1fb0ed39b2f37e94ab4202ecf595e167b3ca62669a6da520859fb6d6c6203dfdfc79c75ec3ee97da8774b2da903e3435f2cd294670a75a526c1", 16), new BigInteger("010001", 16)});
                map.put("fingerprint", Long.valueOf(-5738946642031285640L));
                serverPublicKeys.add(map);
                map = new HashMap();
                map.put("key", new BigInteger[]{new BigInteger("c2a8c55b4a62e2b78a19b91cf692bcdc4ba7c23fe4d06f194e2a0c30f6d9996f7d1a2bcc89bc1ac4333d44359a6c433252d1a8402d9970378b5912b75bc8cc3fa76710a025bcb9032df0b87d7607cc53b928712a174ea2a80a8176623588119d42ffce40205c6d72160860d8d80b22a8b8651907cf388effbef29cd7cf2b4eb8a872052da1351cfe7fec214ce48304ea472bd66329d60115b3420d08f6894b0410b6ab9450249967617670c932f7cbdb5d6fbcce1e492c595f483109999b2661fcdeec31b196429b7834c7211a93c6789d9ee601c18c39e521fda9d7264e61e518add6f0712d2d5228204b851e13c4f322e5c5431c3b7f31089668486aadc59f", 16), new BigInteger("010001", 16)});
                map.put("fingerprint", Long.valueOf(8205599988028290019L));
                serverPublicKeys.add(map);
            }
        }
        Iterator it = serverPublicKeys.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> keyDesc = (HashMap) it.next();
            long keyFingerprint = ((Long) keyDesc.get("fingerprint")).longValue();
            Iterator i$ = fingerprints.iterator();
            while (i$.hasNext()) {
                if (((Long) i$.next()).longValue() == keyFingerprint) {
                    return keyDesc;
                }
            }
        }
        return null;
    }

    long generateMessageId() {
        long messageId = (long) ((((double) System.currentTimeMillis()) * 4.294967296E9d) / 1000.0d);
        if (messageId <= this.lastOutgoingMessageId) {
            messageId = this.lastOutgoingMessageId + 1;
        }
        while (messageId % 4 != 0) {
            messageId++;
        }
        this.lastOutgoingMessageId = messageId;
        return messageId;
    }

    byte[] sendMessageData(TLObject message, long messageId) {
        SerializedData innerOs = new SerializedData();
        message.serializeToStream(innerOs);
        byte[] messageData = innerOs.toByteArray();
        SerializedData messageOs = new SerializedData();
        messageOs.writeInt64(0);
        messageOs.writeInt64(messageId);
        messageOs.writeInt32(messageData.length);
        messageOs.writeRaw(messageData);
        byte[] transportData = messageOs.toByteArray();
        this.datacenter.connection.sendData(transportData, false, false);
        return transportData;
    }

    void processMessage(TLObject message, long messageId) {
        TLObject msgsAck;
        if (message instanceof TL_resPQ) {
            if (this.processedPQRes) {
                msgsAck = new TL_msgs_ack();
                msgsAck.msg_ids = new ArrayList();
                msgsAck.msg_ids.add(Long.valueOf(messageId));
                sendMessageData(msgsAck, generateMessageId());
                return;
            }
            this.processedPQRes = true;
            final TL_resPQ resPq = (TL_resPQ) message;
            if (Arrays.equals(this.authNonce, resPq.nonce)) {
                final HashMap<String, Object> publicKey = selectPublicKey(resPq.server_public_key_fingerprints);
                if (publicKey == null) {
                    FileLog.m800e("tmessages", "***** Couldn't find valid server public key");
                    beginHandshake(false);
                    return;
                }
                this.authServerNonce = resPq.server_nonce;
                final long pqf = ByteBuffer.wrap(resPq.pq).getLong();
                final long messageIdf = messageId;
                Utilities.globalQueue.postRunnable(new Runnable() {
                    public void run() {
                        final TPFactorizedValue factorizedPq = Utilities.getFactorizedValue(pqf);
                        Utilities.stageQueue.postRunnable(new Runnable() {
                            public void run() {
                                ByteBuffer pBytes = ByteBuffer.allocate(4);
                                pBytes.putInt((int) factorizedPq.f49p);
                                byte[] pData = pBytes.array();
                                ByteBuffer qBytes = ByteBuffer.allocate(4);
                                qBytes.putInt((int) factorizedPq.f50q);
                                byte[] qData = qBytes.array();
                                TLObject reqDH = new TL_req_DH_params();
                                reqDH.nonce = HandshakeAction.this.authNonce;
                                reqDH.server_nonce = HandshakeAction.this.authServerNonce;
                                reqDH.f66p = pData;
                                reqDH.f67q = qData;
                                reqDH.public_key_fingerprint = ((Long) publicKey.get("fingerprint")).longValue();
                                SerializedData os = new SerializedData();
                                TL_p_q_inner_data innerData = new TL_p_q_inner_data();
                                innerData.nonce = HandshakeAction.this.authNonce;
                                innerData.server_nonce = HandshakeAction.this.authServerNonce;
                                innerData.pq = resPq.pq;
                                innerData.f64p = reqDH.f66p;
                                innerData.f65q = reqDH.f67q;
                                byte[] nonceBytes = new byte[32];
                                MessagesController.random.nextBytes(nonceBytes);
                                innerData.new_nonce = HandshakeAction.this.authNewNonce = nonceBytes;
                                innerData.serializeToStream(os);
                                byte[] innerDataBytes = os.toByteArray();
                                SerializedData dataWithHash = new SerializedData();
                                dataWithHash.writeRaw(Utilities.computeSHA1(innerDataBytes));
                                dataWithHash.writeRaw(innerDataBytes);
                                byte[] b = new byte[1];
                                while (dataWithHash.length() < 255) {
                                    MessagesController.random.nextBytes(b);
                                    dataWithHash.writeByte(b[0]);
                                }
                                byte[] encryptedBytes = Utilities.encryptWithRSA((BigInteger[]) publicKey.get("key"), dataWithHash.toByteArray());
                                SerializedData encryptedData = new SerializedData();
                                encryptedData.writeRaw(encryptedBytes);
                                if (encryptedData.length() < 256) {
                                    SerializedData newEncryptedData = new SerializedData();
                                    for (int i = 0; i < 256 - encryptedData.length(); i++) {
                                        newEncryptedData.writeByte(0);
                                    }
                                    newEncryptedData.writeRaw(encryptedData.toByteArray());
                                    encryptedData = newEncryptedData;
                                }
                                reqDH.encrypted_data = encryptedData.toByteArray();
                                TL_msgs_ack msgsAck = new TL_msgs_ack();
                                msgsAck.msg_ids = new ArrayList();
                                msgsAck.msg_ids.add(Long.valueOf(messageIdf));
                                HandshakeAction.this.sendMessageData(msgsAck, HandshakeAction.this.generateMessageId());
                                HandshakeAction.this.reqPQMsgData = null;
                                HandshakeAction.this.reqDHMsgData = HandshakeAction.this.sendMessageData(reqDH, HandshakeAction.this.generateMessageId());
                            }
                        });
                    }
                });
                return;
            }
            FileLog.m800e("tmessages", "***** Error: invalid handshake nonce");
            beginHandshake(false);
        } else if (message instanceof Server_DH_Params) {
            if (message instanceof TL_server_DH_params_ok) {
                int i;
                TL_server_DH_params_ok serverDhParams = (TL_server_DH_params_ok) message;
                SerializedData tmpAesKey = new SerializedData();
                SerializedData newNonceAndServerNonce = new SerializedData();
                newNonceAndServerNonce.writeRaw(this.authNewNonce);
                newNonceAndServerNonce.writeRaw(this.authServerNonce);
                SerializedData serverNonceAndNewNonce = new SerializedData();
                serverNonceAndNewNonce.writeRaw(this.authServerNonce);
                serverNonceAndNewNonce.writeRaw(this.authNewNonce);
                tmpAesKey.writeRaw(Utilities.computeSHA1(newNonceAndServerNonce.toByteArray()));
                Object serverNonceAndNewNonceHash = Utilities.computeSHA1(serverNonceAndNewNonce.toByteArray());
                Object serverNonceAndNewNonceHash0_12 = new byte[12];
                System.arraycopy(serverNonceAndNewNonceHash, 0, serverNonceAndNewNonceHash0_12, 0, 12);
                tmpAesKey.writeRaw(serverNonceAndNewNonceHash0_12);
                SerializedData tmpAesIv = new SerializedData();
                Object serverNonceAndNewNonceHash12_8 = new byte[8];
                System.arraycopy(serverNonceAndNewNonceHash, 12, serverNonceAndNewNonceHash12_8, 0, 8);
                tmpAesIv.writeRaw(serverNonceAndNewNonceHash12_8);
                SerializedData newNonceAndNewNonce = new SerializedData();
                newNonceAndNewNonce.writeRaw(this.authNewNonce);
                newNonceAndNewNonce.writeRaw(this.authNewNonce);
                tmpAesIv.writeRaw(Utilities.computeSHA1(newNonceAndNewNonce.toByteArray()));
                Object newNonce0_4 = new byte[4];
                System.arraycopy(this.authNewNonce, 0, newNonce0_4, 0, 4);
                tmpAesIv.writeRaw(newNonce0_4);
                Object answerWithHash = Utilities.aesIgeEncryption(serverDhParams.encrypted_answer, tmpAesKey.toByteArray(), tmpAesIv.toByteArray(), false, false);
                byte[] answerHash = new byte[20];
                System.arraycopy(answerWithHash, 0, answerHash, 0, 20);
                byte[] answerData = new byte[(answerWithHash.length - 20)];
                System.arraycopy(answerWithHash, 20, answerData, 0, answerWithHash.length - 20);
                boolean hashVerified = false;
                for (i = 0; i < 16; i++) {
                    if (Arrays.equals(Utilities.computeSHA1(answerData), answerHash)) {
                        hashVerified = true;
                        break;
                    }
                    byte[] answerData2 = new byte[(answerData.length - 1)];
                    System.arraycopy(answerData, 0, answerData2, 0, answerData.length - 1);
                    answerData = answerData2;
                }
                if (hashVerified) {
                    SerializedData serializedData = new SerializedData(answerData);
                    TL_server_DH_inner_data dhInnerData = (TL_server_DH_inner_data) TLClassStore.Instance().TLdeserialize(serializedData, serializedData.readInt32());
                    if (!(dhInnerData instanceof TL_server_DH_inner_data)) {
                        FileLog.m800e("tmessages", "***** Couldn't parse decoded DH params");
                        beginHandshake(false);
                        return;
                    } else if (!Utilities.isGoodPrime(dhInnerData.dh_prime, dhInnerData.f68g)) {
                        throw new RuntimeException("bad prime");
                    } else if (!Arrays.equals(this.authNonce, dhInnerData.nonce)) {
                        FileLog.m800e("tmessages", "***** Invalid DH nonce");
                        beginHandshake(false);
                        return;
                    } else if (Arrays.equals(this.authServerNonce, dhInnerData.server_nonce)) {
                        byte[] b = new byte[256];
                        MessagesController.random.nextBytes(b);
                        BigInteger bigInteger = new BigInteger(1, dhInnerData.dh_prime);
                        bigInteger = new BigInteger(1, dhInnerData.g_a);
                        if (Utilities.isGoodGaAndGb(bigInteger, bigInteger)) {
                            BigInteger g_b = BigInteger.valueOf((long) dhInnerData.f68g).modPow(new BigInteger(1, b), bigInteger);
                            this.authKey = bigInteger.modPow(new BigInteger(1, b), bigInteger).toByteArray();
                            Object correctedAuth;
                            if (this.authKey.length > 256) {
                                correctedAuth = new byte[256];
                                System.arraycopy(this.authKey, 1, correctedAuth, 0, 256);
                                this.authKey = correctedAuth;
                            } else if (this.authKey.length < 256) {
                                correctedAuth = new byte[256];
                                System.arraycopy(this.authKey, 0, correctedAuth, 256 - this.authKey.length, this.authKey.length);
                                for (int a = 0; a < 256 - this.authKey.length; a++) {
                                    this.authKey[a] = (byte) 0;
                                }
                                this.authKey = correctedAuth;
                            }
                            Object authKeyHash = Utilities.computeSHA1(this.authKey);
                            this.authKeyId = new byte[8];
                            System.arraycopy(authKeyHash, authKeyHash.length - 8, this.authKeyId, 0, 8);
                            SerializedData serverSaltData = new SerializedData();
                            for (i = 7; i >= 0; i--) {
                                serverSaltData.writeByte((byte) (this.authNewNonce[i] ^ this.authServerNonce[i]));
                            }
                            ByteBuffer saltBuffer = ByteBuffer.wrap(serverSaltData.toByteArray());
                            this.timeDifference = dhInnerData.server_time - ((int) (System.currentTimeMillis() / 1000));
                            this.serverSalt = new ServerSalt();
                            this.serverSalt.validSince = ((int) (System.currentTimeMillis() / 1000)) + this.timeDifference;
                            this.serverSalt.validUntil = (((int) (System.currentTimeMillis() / 1000)) + this.timeDifference) + 1800;
                            this.serverSalt.value = saltBuffer.getLong();
                            FileLog.m798d("tmessages", String.format(Locale.US, "===== Time difference: %d", new Object[]{Integer.valueOf(this.timeDifference)}));
                            TL_client_DH_inner_data clientInnerData = new TL_client_DH_inner_data();
                            clientInnerData.nonce = this.authNonce;
                            clientInnerData.server_nonce = this.authServerNonce;
                            clientInnerData.g_b = g_b.toByteArray();
                            clientInnerData.retry_id = 0;
                            SerializedData os = new SerializedData();
                            clientInnerData.serializeToStream(os);
                            byte[] clientInnerDataBytes = os.toByteArray();
                            SerializedData clientDataWithHash = new SerializedData();
                            clientDataWithHash.writeRaw(Utilities.computeSHA1(clientInnerDataBytes));
                            clientDataWithHash.writeRaw(clientInnerDataBytes);
                            byte[] bb = new byte[1];
                            while (clientDataWithHash.length() % 16 != 0) {
                                MessagesController.random.nextBytes(bb);
                                clientDataWithHash.writeByte(bb[0]);
                            }
                            TLObject setClientDhParams = new TL_set_client_DH_params();
                            setClientDhParams.nonce = this.authNonce;
                            setClientDhParams.server_nonce = this.authServerNonce;
                            setClientDhParams.encrypted_data = Utilities.aesIgeEncryption(clientDataWithHash.toByteArray(), tmpAesKey.toByteArray(), tmpAesIv.toByteArray(), true, false);
                            msgsAck = new TL_msgs_ack();
                            msgsAck.msg_ids = new ArrayList();
                            msgsAck.msg_ids.add(Long.valueOf(messageId));
                            sendMessageData(msgsAck, generateMessageId());
                            this.reqDHMsgData = null;
                            this.setClientDHParamsMsgData = sendMessageData(setClientDhParams, generateMessageId());
                            return;
                        }
                        throw new RuntimeException("bad prime");
                    } else {
                        FileLog.m800e("tmessages", "***** Invalid DH server nonce");
                        beginHandshake(false);
                        return;
                    }
                }
                FileLog.m800e("tmessages", "***** Couldn't decode DH params");
                beginHandshake(false);
                return;
            }
            FileLog.m800e("tmessages", "***** Couldn't set DH params");
            beginHandshake(false);
        } else if (message instanceof Set_client_DH_params_answer) {
            Set_client_DH_params_answer dhAnswer = (Set_client_DH_params_answer) message;
            if (!Arrays.equals(this.authNonce, dhAnswer.nonce)) {
                FileLog.m800e("tmessages", "***** Invalid DH answer nonce");
                beginHandshake(false);
            } else if (Arrays.equals(this.authServerNonce, dhAnswer.server_nonce)) {
                this.reqDHMsgData = null;
                msgsAck = new TL_msgs_ack();
                msgsAck.msg_ids = new ArrayList();
                msgsAck.msg_ids.add(Long.valueOf(messageId));
                sendMessageData(msgsAck, generateMessageId());
                Object authKeyAuxHash = new byte[8];
                System.arraycopy(Utilities.computeSHA1(this.authKey), 0, authKeyAuxHash, 0, 8);
                SerializedData newNonce1 = new SerializedData();
                newNonce1.writeRaw(this.authNewNonce);
                newNonce1.writeByte(1);
                newNonce1.writeRaw(authKeyAuxHash);
                Object newNonceHash1Full = Utilities.computeSHA1(newNonce1.toByteArray());
                Object newNonceHash1 = new byte[16];
                System.arraycopy(newNonceHash1Full, newNonceHash1Full.length - 16, newNonceHash1, 0, 16);
                SerializedData newNonce2 = new SerializedData();
                newNonce2.writeRaw(this.authNewNonce);
                newNonce2.writeByte(2);
                newNonce2.writeRaw(authKeyAuxHash);
                Object newNonceHash2Full = Utilities.computeSHA1(newNonce2.toByteArray());
                Object newNonceHash2 = new byte[16];
                System.arraycopy(newNonceHash2Full, newNonceHash2Full.length - 16, newNonceHash2, 0, 16);
                SerializedData newNonce3 = new SerializedData();
                newNonce3.writeRaw(this.authNewNonce);
                newNonce3.writeByte(3);
                newNonce3.writeRaw(authKeyAuxHash);
                Object newNonceHash3Full = Utilities.computeSHA1(newNonce3.toByteArray());
                Object newNonceHash3 = new byte[16];
                System.arraycopy(newNonceHash3Full, newNonceHash3Full.length - 16, newNonceHash3, 0, 16);
                if (message instanceof TL_dh_gen_ok) {
                    if (Arrays.equals(newNonceHash1, ((TL_dh_gen_ok) message).new_nonce_hash1)) {
                        FileLog.m798d("tmessages", String.format("Handshake with DC%d completed", new Object[]{Integer.valueOf(this.datacenter.datacenterId)}));
                        this.datacenter.connection.delegate = null;
                        final Action action = this;
                        Utilities.stageQueue.postRunnable(new Runnable() {
                            public void run() {
                                HandshakeAction.this.datacenter.authKey = HandshakeAction.this.authKey;
                                HandshakeAction.this.datacenter.authKeyId = HandshakeAction.this.authKeyId;
                                HandshakeAction.this.datacenter.addServerSalt(HandshakeAction.this.serverSalt);
                                HashMap<String, Object> resultDict = new HashMap();
                                resultDict.put("timeDifference", Integer.valueOf(HandshakeAction.this.timeDifference));
                                if (HandshakeAction.this.delegate != null) {
                                    HandshakeAction.this.delegate.ActionDidFinishExecution(action, resultDict);
                                }
                            }
                        });
                        return;
                    }
                    FileLog.m800e("tmessages", "***** Invalid DH answer nonce hash 1");
                    beginHandshake(false);
                } else if (message instanceof TL_dh_gen_retry) {
                    if (Arrays.equals(newNonceHash2, ((TL_dh_gen_retry) message).new_nonce_hash2)) {
                        FileLog.m798d("tmessages", "***** Retry DH");
                        beginHandshake(false);
                        return;
                    }
                    FileLog.m800e("tmessages", "***** Invalid DH answer nonce hash 2");
                    beginHandshake(false);
                } else if (message instanceof TL_dh_gen_fail) {
                    if (Arrays.equals(newNonceHash3, ((TL_dh_gen_fail) message).new_nonce_hash3)) {
                        FileLog.m798d("tmessages", "***** Server declined DH params");
                        beginHandshake(false);
                        return;
                    }
                    FileLog.m800e("tmessages", "***** Invalid DH answer nonce hash 3");
                    beginHandshake(false);
                } else {
                    FileLog.m800e("tmessages", "***** Unknown DH params response");
                    beginHandshake(false);
                }
            } else {
                FileLog.m800e("tmessages", "***** Invalid DH answer server nonce");
                beginHandshake(false);
            }
        } else {
            msgsAck = new TL_msgs_ack();
            msgsAck.msg_ids = new ArrayList();
            msgsAck.msg_ids.add(Long.valueOf(messageId));
            sendMessageData(msgsAck, generateMessageId());
        }
    }

    public void tcpConnectionProgressChanged(TcpConnection connection, long messageId, int currentSize, int length) {
    }

    public void tcpConnectionClosed(TcpConnection connection) {
        this.wasDisconnect = true;
    }

    public void tcpConnectionConnected(TcpConnection connection) {
        if (!this.wasDisconnect) {
            return;
        }
        if (this.reqPQMsgData != null) {
            this.datacenter.connection.sendData(this.reqPQMsgData, false, false);
        } else if (this.reqDHMsgData != null) {
            this.datacenter.connection.sendData(this.reqDHMsgData, false, false);
        } else if (this.setClientDHParamsMsgData != null) {
            this.datacenter.connection.sendData(this.setClientDHParamsMsgData, false, false);
        }
    }

    public void tcpConnectionQuiackAckReceived(TcpConnection connection, int ack) {
    }

    public void tcpConnectionReceivedData(TcpConnection connection, byte[] data) {
        SerializedData is = new SerializedData(data);
        if (is.readInt64() == 0) {
            long messageId = is.readInt64();
            if (this.processedMessageIds.contains(Long.valueOf(messageId))) {
                FileLog.m798d("tmessages", String.format("===== Duplicate message id %d received, ignoring", new Object[]{Long.valueOf(messageId)}));
                return;
            }
            int messageLength = is.readInt32();
            TLObject object = TLClassStore.Instance().TLdeserialize(is, is.readInt32());
            if (object != null) {
                this.processedMessageIds.add(Long.valueOf(messageId));
            }
            processMessage(object, messageId);
            return;
        }
        FileLog.m798d("tmessages", "***** Received encrypted message while in handshake, restarting");
        beginHandshake(true);
    }
}
