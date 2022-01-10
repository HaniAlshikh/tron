package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToConnectNetworkRpcException;

import java.io.Closeable;

public interface IRpcConnection extends IRpcSender, IRpcReceiver, Closeable {
    void connect() throws FailedToConnectNetworkRpcException;

    void safeClose();
}
