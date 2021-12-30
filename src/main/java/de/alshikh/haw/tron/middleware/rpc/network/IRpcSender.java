package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;

public interface IRpcSender {
    void send(byte[] data) throws FailedToReceiveNetworkRpcException;
}
