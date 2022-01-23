package de.alshikh.haw.tron.middleware.rpc.network.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.alshikh.haw.tron.middleware.rpc.serverstub.receive.util.util.getLocalIp;

public class UdpTcpServer {
    public static final int MAX_PACKET_SIZE = 4096; // TODO: based on request message

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private DatagramChannel udpserver;
    private ServerSocketChannel tcpserver;
    private Selector selector;

    private final int port;
    private final CompletableFuture<InetSocketAddress> address;

    public UdpTcpServer(int port) {
        this.port = port;
        this.address = new CompletableFuture<>();
    }

    public void start() {
        try {
            init();
            try {
                accept();
            } catch (Exception e) {
                selector.close();
                tcpserver.close();
                udpserver.close();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
            System.exit(1);
        }
    }

    private void accept() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                handleChannelRequest(iter.next());
                iter.remove();
            }
        }
    }

    private void init() throws IOException {
        tcpserver = ServerSocketChannel.open();
        tcpserver.socket().bind(new InetSocketAddress(getLocalIp(), port));

        // in case of random port
        InetSocketAddress boundedAddress = (InetSocketAddress) tcpserver.socket().getLocalSocketAddress();

        udpserver = DatagramChannel.open();
        udpserver.socket().bind(boundedAddress);

        tcpserver.configureBlocking(false);
        udpserver.configureBlocking(false);

        selector = Selector.open(); // to block while waiting
        tcpserver.register(selector, SelectionKey.OP_ACCEPT);
        udpserver.register(selector, SelectionKey.OP_READ);

        log.info("Server Started on port: " + boundedAddress.getPort() + "!");
        address.complete(boundedAddress);
    }

    private void handleChannelRequest(SelectionKey channelKey) throws IOException {
        Channel c = channelKey.channel();
        if (channelKey.isReadable() && c == udpserver) {
            log.debug("handling udp datagram");
            ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            udpserver.receive(buffer);
            executor.submit(() -> printUDP(buffer));
            //new UDPThread(udpserver.socket()).start();
        }
        else if (channelKey.isAcceptable() && c == tcpserver) {
            Socket connection = tcpserver.accept().socket();
            log.debug("handling tcp connection");
            executor.submit(() -> printTCP(connection));
        }
    }

    public void printTCP(Socket socket) {
        //try {
        //    log.info(new String(new RpcConnection(socket).receive()));
        //} catch (FailedToReceiveNetworkRpcException e) {
        //    e.printStackTrace();
        //}
    }

    public void printUDP(ByteBuffer buffer) {
        //ByteBuffer buffer = ByteBuffer.allocate(1);
        //buffer.clear();
        //SocketAddress remoteAdd = null;
        //try {
        //    remoteAdd = server.receive(buffer);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        String message = extractMessage(buffer);
        log.info("udp Client sent: " + message);
    }

    private static String extractMessage(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String msg = new String(bytes);
        //String msg = new String(buffer.array());

        return msg;
    }

    public CompletableFuture<InetSocketAddress> getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
