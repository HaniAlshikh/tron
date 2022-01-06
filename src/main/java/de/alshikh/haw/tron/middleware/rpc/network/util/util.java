package de.alshikh.haw.tron.middleware.rpc.network.util;

import java.io.IOException;
import java.net.ServerSocket;

public class util {
    public static int getRandomFreePort() {
        int port = 9000;
        while (true) {
            try (ServerSocket s = new ServerSocket(port++)) {
                return s.getLocalPort();
            } catch (IOException ignored) {}
        }
    }
}
