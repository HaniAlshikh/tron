//package de.alshikh.haw.tron.middleware.rpc.network;
//
//import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToConnectNetworkRpcException;
//import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.data.exceptions.FailedToReceiveNetworkRpcException;
//import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToSendNetworkRpcException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//
//// TODO: maybe abstract a bit more to TcpConnection <- RpcTcpConnection etc...
//public class RpcConnection implements IRpcConnection {
//    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
//
//    private final SocketAddress address;
//    private Socket connection;
//
//    public RpcConnection(SocketAddress address) {
//        this.address = address;
//        this.connection = new Socket();
//    }
//
//    public RpcConnection(Socket connection) {
//        this.connection = connection;
//        this.address = connection.getRemoteSocketAddress();
//    }
//
//    @Override
//    public void connect() throws FailedToConnectNetworkRpcException {
//        connection = new Socket();
//        try {
//            connection.connect(address);
//        } catch (Exception e) {
//            log.error("Failed to connect:", e);
//            throw new FailedToConnectNetworkRpcException(address);
//        }
//    }
//
//    @Override
//    public void send(byte[] data) throws FailedToSendNetworkRpcException {
//        try {
//            connection.getOutputStream().write(data);
//            connection.shutdownOutput();
//        } catch (IOException e) {
//            log.error("Failed to send data:", e);
//            throw new FailedToSendNetworkRpcException(connection);
//        }
//    }
//
//    @Override
//    public byte[] receive() throws FailedToReceiveNetworkRpcException {
//        try {
//            return connection.getInputStream().readAllBytes();
//        } catch (IOException e) {
//            log.error("Failed to receive data:", e);
//            throw new FailedToReceiveNetworkRpcException(connection);
//        }
//    }
//
//    @Override
//    public void close() throws IOException {
//        connection.close();
//    }
//
//    @Override
//    public void safeClose() {
//        try {
//            connection.close();
//        } catch (IOException ignored) {}
//    }
//
//    @Override
//    public InetAddress getAddress() {
//        return connection.getInetAddress();
//    }
//}
