package de.alshikh.haw.tron.middleware.rpc.network.data.exceptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

import java.net.Socket;

public class FailedToSendNetworkRpcException extends RpcException {
    public static String MESSAGE = "Failed to send data due to network error";

    public FailedToSendNetworkRpcException() {
        this(MESSAGE);
    }

    public FailedToSendNetworkRpcException(Socket connection) {
        this(MESSAGE + " from" + connection.getInetAddress() + ":" + connection.getPort());
    }

    public FailedToSendNetworkRpcException(String message) {
        super(message);
        this.code = 32280;
    }
}
