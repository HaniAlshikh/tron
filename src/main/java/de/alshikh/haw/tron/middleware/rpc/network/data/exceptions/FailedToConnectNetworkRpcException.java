package de.alshikh.haw.tron.middleware.rpc.network.data.exceptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

import java.net.SocketAddress;

public class FailedToConnectNetworkRpcException extends RpcException {
    public static String MESSAGE = "Failed to connect";

    public FailedToConnectNetworkRpcException() {
        this(MESSAGE);
    }

    public FailedToConnectNetworkRpcException(SocketAddress address) {
        this(MESSAGE + " to" + address);
    }

    public FailedToConnectNetworkRpcException(String message) {
        super(message);
        this.code = 32282;
    }
}
