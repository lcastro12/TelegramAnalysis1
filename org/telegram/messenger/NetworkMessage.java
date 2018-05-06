package org.telegram.messenger;

import org.telegram.TL.TLRPC.TL_protoMessage;

public class NetworkMessage {
    public TL_protoMessage protoMessage;
    public Object rawRequest;
    public long requestId;
}
