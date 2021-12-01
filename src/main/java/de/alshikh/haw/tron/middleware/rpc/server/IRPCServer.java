package de.alshikh.haw.tron.middleware.rpc.server;

import java.io.IOException;
import java.util.HashMap;

public interface IRPCServer {
    public void stop();

    public void start() throws IOException;

    public void register(Class<?> serviceInterface, Class<?> impl);

    public boolean isRunning();

    public int getPort();

    HashMap<String, Class<?>> getServiceRegistry();
}
