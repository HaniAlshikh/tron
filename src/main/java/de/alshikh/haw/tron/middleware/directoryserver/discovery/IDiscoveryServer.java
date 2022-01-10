package de.alshikh.haw.tron.middleware.directoryserver.discovery;

import java.io.IOException;

public interface IDiscoveryServer extends Runnable {
    void announce() throws IOException;
}
