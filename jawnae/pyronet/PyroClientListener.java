package jawnae.pyronet;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface PyroClientListener {
    void connectedClient(PyroClient pyroClient);

    void disconnectedClient(PyroClient pyroClient);

    void droppedClient(PyroClient pyroClient, IOException iOException);

    void receivedData(PyroClient pyroClient, ByteBuffer byteBuffer);

    void sentData(PyroClient pyroClient, int i);

    void unconnectableClient(PyroClient pyroClient, Exception exception);
}
