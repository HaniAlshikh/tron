package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToConnectNetworkRpcException;
import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToSendNetworkRpcException;

import java.io.Closeable;

public interface IRpcSender extends Closeable {
    void connect() throws FailedToConnectNetworkRpcException;

    void send(byte[] data) throws FailedToSendNetworkRpcException;

    void safeClose();
}
