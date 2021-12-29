package de.alshikh.haw.tron.middleware.rpc;

import de.alshikh.haw.tron.middleware.helloworld.HelloWorldClient;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldJsonRpcSerializer;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldServer;
import de.alshikh.haw.tron.middleware.helloworld.service.HelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceServerStub;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.JsonRpcServer;

import java.io.IOException;
import java.net.InetSocketAddress;

class RPC {
    // TODO: iterate over the methods and create a client stub
    // TODO: callback server and locking queue
    // TODO: application stubs are preferred to be in the application layer (rename client to application and create stubs package)
    // TODO: see if return type can be inferred or add it in the marshaling protocol
    // TODO: rpc middleware per call
    // TODO: maybe rename message package to marshal and create network to send/receive
    // TODO: naming server component (send dose lookup and receive dose register)
    public static void main(String[] args) throws IOException {
        JsonRpcSerializer jsonRpcSerializer = new HelloWorldJsonRpcSerializer();

        IHelloWorld helloWorld = new HelloWorld();
        // TODO: application server stubs are not really necessary?
        IRpcServiceServerStub helloWorldServer = new HelloWorldServer(helloWorld);

        new Thread(() -> {
            IRPCServer rpcServer = new JsonRpcServer(8088, jsonRpcSerializer);
            rpcServer.register(helloWorldServer);
            rpcServer.start();
        }).start();

        // delay until the server starts
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // TODO: are we allowed to use java ProxyInstance to generate application stubs?
        IHelloWorld helloWorldClient = new HelloWorldClient(
                new JsonRpcClient(
                        new InetSocketAddress("localhost", 8088),
                        jsonRpcSerializer
                        ));

        System.out.println(helloWorldClient.helloWorld());
        System.out.println(helloWorldClient.helloWorld(new HelloWorldMessage("Custom Data Type")));
    }
}
