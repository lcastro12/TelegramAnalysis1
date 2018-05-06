package org.telegram.TL;

import org.telegram.TL.TLRPC.Vector;
import org.telegram.messenger.SerializedData;

public class TLObject {
    public void readParams(SerializedData stream) {
    }

    public byte[] serialize() {
        return null;
    }

    public void serializeToStream(SerializedData stream) {
    }

    public Class<? extends TLObject> responseClass() {
        return getClass();
    }

    public int layer() {
        return 8;
    }

    public void parseVector(Vector vector, SerializedData data) {
    }
}
