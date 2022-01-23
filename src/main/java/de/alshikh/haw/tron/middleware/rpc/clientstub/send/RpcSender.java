package de.alshikh.haw.tron.middleware.rpc.clientstub.send;

import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToSendNetworkRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcSender implements IRpcSender {
    public static final int MAX_PACKET_SIZE = RpcReceiver.MAX_PACKET_SIZE; // TODO: config

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final InetSocketAddress serverAddress;

    public RpcSender(InetSocketAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void send(byte[] data) throws FailedToSendNetworkRpcException {
        send(data, false);
    }

    @Override
    public void send(byte[] data, boolean bestEffort) throws FailedToSendNetworkRpcException {
        if (bestEffort && data.length < MAX_PACKET_SIZE) // TODO: deal with big messages?
            udpSend(data);
        else
            tcpSend(data);
    }

    private void udpSend(byte[] data) throws FailedToSendNetworkRpcException {
        log.info("sending " + new String(data) + " over udp to " + serverAddress);
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
            socket.send(packet);
        } catch (IOException e) {
            log.debug("sending request error:", e);
            throw new FailedToSendNetworkRpcException();
        }
    }

    private void tcpSend(byte[] data) throws FailedToSendNetworkRpcException {
        log.info("sending " + new String(data) + " over tcp to " + serverAddress);
        try (Socket connection = new Socket()) {
            connection.connect(serverAddress);
            connection.getOutputStream().write(data);
            connection.shutdownOutput();
        } catch (IOException e) {
            log.debug("sending request error:", e);
            throw new FailedToSendNetworkRpcException();
            // TODO: can/should we do anything more?
        }
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }
}
