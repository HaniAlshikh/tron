package de.alshikh.haw.tron.middleware.rpc;

import de.alshikh.haw.tron.client.controllers.GameControllerStub;
import de.alshikh.haw.tron.client.controllers.IGameController;
import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.RPCServer;

import java.io.IOException;
import java.net.InetSocketAddress;

class RPC {
    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            try {
                IRPCServer rpcServer = new RPCServer(8088);
                rpcServer.register(IGameController.class, GameControllerStub.class);
                rpcServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        IGameController gameController = IRPCClient.getRemoteProxyObj(
                IGameController.class,
                new InetSocketAddress("localhost", 8088)
        );

        System.out.println(gameController.getTestMessage());
    }

}