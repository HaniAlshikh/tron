package de.alshikh.haw.tron.middleware.rpc.serverstub.receive;

import de.alshikh.haw.tron.manager.Config;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.data.exceptions.FailedToReceiveNetworkRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.IRpcUnmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.alshikh.haw.tron.middleware.rpc.serverstub.receive.util.ServerStubUtil.getLocalIp;

public class RpcReceiver implements IRpcReceiver {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final CompletableFuture<InetSocketAddress> serverAddress = new CompletableFuture<>();

    private DatagramChannel udpServer;
    private ServerSocketChannel tcpServer;
    private Selector selector;
    private boolean running;

    private final IRpcUnmarshaller rpcUnmarshaller;

    public RpcReceiver(IRpcUnmarshaller rpcUnmarshaller) {
        this.rpcUnmarshaller = rpcUnmarshaller;
    }

    @Override
    public void start() {
        try {
            init();
            try {
                receive();
            } catch (Exception e) {
                selector.close();
                tcpServer.close();
                udpServer.close();
                log.debug("Network error: " + e);
                throw new FailedToReceiveNetworkRpcException();
            }
        } catch (Exception e) {
            log.info("Fatal error: " + e.getMessage());
            log.error("Exception: ", e);
        }
    }

    private void receive() throws IOException {
        while (running) {
            selector.select();
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey channelKey = selectedKeys.next();
                selectedKeys.remove();
                handleRequest(channelKey);
            }
        }
    }

    private void handleRequest(SelectionKey channelKey) {
        try {
            if (channelKey.isReadable() && channelKey.channel() == udpServer)
                udpReceive();
            else if (channelKey.isAcceptable() && channelKey.channel() == tcpServer)
                tcpReceive();
        } catch (Exception e) {
            log.error("Failed to handle request: ", e);
        }
    }

    private void udpReceive() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(Config.MAX_PACKET_SIZE);
            udpServer.receive(buffer);
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            log.debug("data received over udp: " + new String(data));
            unmarshal(data);
        } catch (IOException e) {
            log.error("Failed to receive data:", e);
        }
    }

    private void tcpReceive() {
        try (Socket connection = tcpServer.accept().socket()) {
            byte[] data = connection.getInputStream().readAllBytes();
            log.debug("data received over tcp: " + new String(data));
            unmarshal(data);
        } catch (IOException e) {
            log.error("Failed to receive data:", e);
        }
    }

    private void unmarshal(byte[] request) {
        executor.submit(() -> rpcUnmarshaller.unmarshal(request));
    }

    private void init() throws IOException {
        tcpServer = ServerSocketChannel.open();
        tcpServer.socket().bind(new InetSocketAddress(getLocalIp(), 0));

        InetSocketAddress boundedAddress = (InetSocketAddress) tcpServer.socket().getLocalSocketAddress();

        udpServer = DatagramChannel.open();
        udpServer.socket().bind(boundedAddress);

        tcpServer.configureBlocking(false);
        udpServer.configureBlocking(false);

        selector = Selector.open();
        tcpServer.register(selector, SelectionKey.OP_ACCEPT);
        udpServer.register(selector, SelectionKey.OP_READ);

        log.info("listening on: " + boundedAddress);
        running = true;
        serverAddress.complete(boundedAddress);
    }

    @Override
    public void stop() {
        running = false;
        executor.shutdown();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public InetSocketAddress getServerAddress() {
        try {
            return serverAddress.get();
        } catch (Exception e) {
            log.info("Rpc Receiver could not be initialized probably");
            throw new RuntimeException(e);
        }
    }

    @Override
    public IRpcUnmarshaller getRpcUnmarshaller() {
        return rpcUnmarshaller;
    }
}
