package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToSendNetworkRpcException;

public interface IRpcSender {
    void send(byte[] data) throws FailedToSendNetworkRpcException;
}
