package de.alshikh.haw.tron.middleware.directoryserver.discovery;

import java.net.InetSocketAddress;

public interface IDiscoveryClient {
    void listen();

    InetSocketAddress toSocketAddress();
}
