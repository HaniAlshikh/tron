package de.alshikh.haw.tron.middleware.rpc.network;

import java.io.Closeable;
import java.io.IOException;

public interface IRpcConnection extends IRpcSender, IRpcReceiver, Closeable {
    void connect() throws IOException;

    void safeClose();
}
