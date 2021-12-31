package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;

public interface IRpcReceiver {
    byte[] receive() throws FailedToReceiveNetworkRpcException;
}
