package de.alshikh.haw.tron.middleware.discoveryservice;

import java.io.IOException;

public interface IDiscoveryServer extends Runnable {
    void announce() throws IOException;
}
