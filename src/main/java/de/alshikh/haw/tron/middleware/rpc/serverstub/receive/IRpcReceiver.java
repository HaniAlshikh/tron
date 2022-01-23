package de.alshikh.haw.tron.middleware.rpc.serverstub.receive;

import java.net.InetSocketAddress;

public interface IRpcReceiver {
    void start();

    void stop();

    boolean isRunning();

    InetSocketAddress getServerAddress();
}
