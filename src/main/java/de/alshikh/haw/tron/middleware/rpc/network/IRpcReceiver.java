package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;

import java.net.InetAddress;

public interface IRpcReceiver {
    byte[] receive() throws FailedToReceiveNetworkRpcException;

    void safeClose();

    InetAddress getAddress();
}
