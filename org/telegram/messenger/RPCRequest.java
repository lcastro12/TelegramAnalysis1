package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_error;

public class RPCRequest {
    public static int RPCRequestClassDownloadMedia = 2;
    public static int RPCRequestClassEnableUnauthorized = 8;
    public static int RPCRequestClassFailOnServerErrors = 16;
    public static int RPCRequestClassGeneric = 1;
    static int RPCRequestClassTransportMask = ((RPCRequestClassGeneric | RPCRequestClassDownloadMedia) | RPCRequestClassUploadMedia);
    public static int RPCRequestClassUploadMedia = 4;
    boolean cancelled;
    RPCRequestDelegate completionBlock;
    boolean confirmed;
    int flags;
    boolean initRequest = false;
    RPCProgressDelegate progressBlock;
    RPCQuickAckDelegate quickAckBlock;
    TLObject rawRequest;
    boolean requiresCompletion;
    ArrayList<Long> respondsToMessageIds = new ArrayList();
    public int retryCount = 0;
    TLObject rpcRequest;
    int runningDatacenterId;
    long runningMessageId;
    int runningMessageSeqNo;
    int runningMinStartTime;
    int runningStartTime;
    int serializedLength;
    int serverFailureCount;
    long token;
    int transportChannelToken;

    public interface RPCProgressDelegate {
        void progress(int i, int i2);
    }

    public interface RPCQuickAckDelegate {
        void quickAck();
    }

    public interface RPCRequestDelegate {
        void run(TLObject tLObject, TL_error tL_error);
    }

    public void addRespondMessageId(long messageId) {
        this.respondsToMessageIds.add(Long.valueOf(messageId));
    }

    boolean respondsToMessageId(long messageId) {
        return this.runningMessageId == messageId || this.respondsToMessageIds.contains(Long.valueOf(messageId));
    }
}
