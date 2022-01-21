package de.alshikh.haw.tron.middleware.discoveryservice;

import java.net.InetSocketAddress;

public interface IDiscoveryClient {
    void listen();

    InetSocketAddress toSocketAddress();
}
