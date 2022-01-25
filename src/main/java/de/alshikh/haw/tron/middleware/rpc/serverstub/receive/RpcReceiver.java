package de.alshikh.haw.tron.middleware.rpc.serverstub.receive;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.IRpcUnmarshaller;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.RpcUnmarshaller;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.alshikh.haw.tron.middleware.rpc.serverstub.receive.util.util.getLocalIp;

public class RpcReceiver implements IRpcReceiver {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    // TODO: managed executor service
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static final int MAX_PACKET_SIZE = 1024; // based on request message size TODO: dynamic?

    private DatagramChannel udpServer;
    private ServerSocketChannel tcpServer;
    private Selector selector;
    private boolean running;

    private final IRpcUnmarshaller rpcUnmarshaller;
    private final CompletableFuture<InetSocketAddress> serverAddress;

    public RpcReceiver(IRpcMessageApi rpcMsgApi, HashMap<UUID, IRpcAppServerStub> serviceRegistry) {
        this.rpcUnmarshaller = new RpcUnmarshaller(rpcMsgApi, serviceRegistry);
        this.serverAddress = new CompletableFuture<>();
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
                throw e;
            }
        } catch (Exception e) {
            log.info("Fatal error: " + e.getMessage());
            log.error("Exception: ", e);
        }
    }

    private void receive() throws IOException {
        while (running) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey channelKey = iter.next();
                iter.remove();
                executor.submit(() -> handleRequest(channelKey));
            }
        }
    }

    private void handleRequest(SelectionKey channelKey) {
        if (channelKey.isReadable() && channelKey.channel() == udpServer)
            udpReceive();
        else if (channelKey.isAcceptable() && channelKey.channel() == tcpServer)
            tcpReceive();
    }

    private void udpReceive() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            udpServer.receive(buffer);
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            if (data.length > 0) {
                log.debug("data received over udp: " + new String(data));
                unmarshal(data);
            }
        } catch (IOException e) { // TODO
            log.error("Failed to receive data:", e);
        }
    }

    private void tcpReceive() {
        try (Socket connection = tcpServer.accept().socket();) {
            byte[] data = connection.getInputStream().readAllBytes();
            log.debug("data received over tcp: " + new String(data));
            unmarshal(data);
        }  catch (IOException e) { // TODO
            log.error("Failed to receive data:", e);
        }
    }

    private void unmarshal(byte[] request) {
        rpcUnmarshaller.unmarshal(request);
    }

    private void init() throws IOException {
        tcpServer = ServerSocketChannel.open();
        tcpServer.socket().bind(new InetSocketAddress(getLocalIp(), 0));

        // in case of random port
        InetSocketAddress boundedAddress = (InetSocketAddress) tcpServer.socket().getLocalSocketAddress();

        udpServer = DatagramChannel.open();
        udpServer.socket().bind(boundedAddress);

        tcpServer.configureBlocking(false);
        udpServer.configureBlocking(false);

        selector = Selector.open(); // to block while waiting
        tcpServer.register(selector, SelectionKey.OP_ACCEPT);
        udpServer.register(selector, SelectionKey.OP_READ);

        log.info("Server Started on port: " + boundedAddress.getPort() + "!");
        running = true;
        serverAddress.complete(boundedAddress);
    }

    @Override
    public void stop() {
        // TODO
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
        } catch (Exception e) { // TODO:
            return null;
        }
    }
}