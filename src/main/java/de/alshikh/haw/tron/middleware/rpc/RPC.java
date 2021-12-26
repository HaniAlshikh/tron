package de.alshikh.haw.tron.middleware.rpc;

import de.alshikh.haw.tron.middleware.helloworld.HelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.IHelloWorld;
import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.RPCServer;

import java.io.IOException;
import java.net.InetSocketAddress;

class RPC {
    public static void main(String[] args) throws IOException {

        IHelloWorld helloWorldServer = new HelloWorld();

        new Thread(() -> {
            try {
                IRPCServer rpcServer = new RPCServer(8088);
                rpcServer.register(IHelloWorld.class, helloWorldServer);
                rpcServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        IHelloWorld helloWorldClient = IRPCClient.getServiceStub(
                IHelloWorld.class,
                new InetSocketAddress("localhost", 8088)
        );

        System.out.println(helloWorldClient.sayHello());
    }
}
