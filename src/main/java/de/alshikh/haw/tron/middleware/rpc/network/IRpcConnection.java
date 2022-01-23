//package de.alshikh.haw.tron.middleware.rpc.network;
//
//import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToConnectNetworkRpcException;
//import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.data.exceptions.FailedToReceiveNetworkRpcException;
//import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToSendNetworkRpcException;
//
//import java.io.Closeable;
//import java.net.InetAddress;
//
//public interface IRpcConnection extends Closeable {
//    void connect() throws FailedToConnectNetworkRpcException;
//
//    void send(byte[] data) throws FailedToSendNetworkRpcException;
//
//    void safeClose();
//
//    byte[] receive() throws FailedToReceiveNetworkRpcException;
//
//    InetAddress getAddress();
//}
