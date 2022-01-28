package de.alshikh.haw.tron.middleware.rpc.clientstub.send;

import de.alshikh.haw.tron.manager.Config;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.data.exceptions.FailedToSendNetworkRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcSender implements IRpcSender {
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
    public void send(byte[] data, boolean isBestEffort) throws FailedToSendNetworkRpcException {
        if (isBestEffort && data.length < Config.MAX_PACKET_SIZE)
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
        } catch (IOException e) {
            log.debug("sending request error:", e);
            throw new FailedToSendNetworkRpcException();
        }
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }
}
