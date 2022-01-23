package de.alshikh.haw.tron.middleware.rpc.clientstub.send;

import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToSendNetworkRpcException;

import java.net.InetSocketAddress;

public interface IRpcSender {
    void send(byte[] data) throws FailedToSendNetworkRpcException;

    void send(byte[] data, boolean bestEffort) throws FailedToSendNetworkRpcException;

    InetSocketAddress getServerAddress();
}
