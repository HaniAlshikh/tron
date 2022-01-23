package de.alshikh.haw.tron.middleware.rpc.network.client;

import de.alshikh.haw.tron.middleware.rpc.network.server.UdpTcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class UdpTcpClient {
    public static final int MAX_PACKET_SIZE = UdpTcpServer.MAX_PACKET_SIZE;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    InetSocketAddress serverAddress;

    public UdpTcpClient(InetSocketAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void send(byte[] data, boolean bestEffort) {
        if (bestEffort && data.length < MAX_PACKET_SIZE) // TODO: deal with big messages?
            udpSend(data);
        else
            tcpSend(data);
    }

    private void udpSend(byte[] data) {
        log.info("sending " + new String(data) + " over udp to " + serverAddress);
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
            socket.send(packet);
        } catch (IOException e) {
            log.debug("failed to send" + new String(data), e);
        }
    }

    private void tcpSend(byte[] data) {
        log.info("sending " + new String(data) + " over tcp to " + serverAddress);
        try (Socket connection = new Socket()) {
            connection.connect(serverAddress);
            connection.getOutputStream().write(data);
            connection.shutdownOutput();
        } catch (IOException e) {
            log.debug("failed to send" + new String(data), e);
            // TODO: can we do anything more?
        }
    }
}
