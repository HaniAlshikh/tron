package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class RpcConnection implements IRpcConnection {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final SocketAddress address;
    private Socket connection;

    public RpcConnection(SocketAddress address) {
        this.address = address;
        this.connection = new Socket();
    }

    public RpcConnection(Socket connection) {
        this.connection = connection;
        this.address = connection.getRemoteSocketAddress();
    }

    @Override
    public void connect() throws IOException {
        connection = new Socket();
        connection.connect(address);
    }

    @Override
    public void send(byte[] data) throws FailedToReceiveNetworkRpcException {
        try {
            connection.getOutputStream().write(data);
            connection.shutdownOutput();
        } catch (IOException e) {
            log.error("Failed to send data:", e);
            throw new FailedToReceiveNetworkRpcException(connection);
        }
    }

    @Override
    public byte[] receive() throws FailedToReceiveNetworkRpcException {
        try {
            return connection.getInputStream().readAllBytes();
        } catch (IOException e) {
            log.error("Failed to receive data:", e);
            throw new FailedToReceiveNetworkRpcException(connection);
        }
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

    @Override
    public void safeClose() {
        try {
            connection.close();
        } catch (IOException ignored) {}
    }
}
