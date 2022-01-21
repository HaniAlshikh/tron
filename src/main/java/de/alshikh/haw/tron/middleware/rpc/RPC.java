package de.alshikh.haw.tron.middleware.rpc;

import de.alshikh.haw.tron.middleware.helloworld.HelloWorldClient;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldJsonRpcSerializer;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldServer;
import de.alshikh.haw.tron.middleware.helloworld.service.HelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.LocalRpcServerPortFinder;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackServer;
import de.alshikh.haw.tron.middleware.rpc.client.RpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.RpcServer;

import java.io.IOException;

class RPC {
    // TODO: iterate over the methods and create a client stub
    // TODO: callback server and locking queue (the queue is the executor service?)
    //  doesn't really make sense in the tron usecase
    // TODO: application stubs are preferred to be in the application layer (rename client to application and create stubs package)
    // TODO: see if return type can be inferred or add it in the marshaling protocol
    // TODO: directory vs name service
    // TODO: naming server component (send dose lookup and receive dose register)
    // TODO: directory service could broadcast itself (or simply make the address as user input)
    // TODO: directory service uni/multi/brodcast to clients (multi ip4 alone)
    // TODO: JsonRpcServer is actually the DCE Daemon and it's responsible of registering itself by the directory server
    // TODO: per app/service/game the name ip and port should be sent to the directory server (07.12.2021 -1:30)
    // TODO: if really needed use both TCP and UDP with reliable/unreliable (realtime game -> TCP)
    // TODO: we don't want to save anything if the information (update) is not there we forget about it (transient and not persistent (no message queues)) so TCP
    // TODO: Business Context: application is the customer
    // TODO: as long as the data type contains only primitive types it's ok for serialization
    // TODO: split the network and message components (marshal send receive umarshal) (marshaling != serializing/parsing)
    // TODO: application stubs: callee/consumer caller/producer and not server client
    // TODO: use UDP with some mechanism to resend missing packets (lamport?)
    // TODO: TCP dosn't work over VPN (use UDP)
    // TODO: lamport is implemented in a indirect way (PlayerUpdate version)
    // TODO: UDP packte size (propping to see what possible to send)
    // TODO: marshaling component is the ClientStub
    // TODO: split application stub into zwei components callee caller
    // TODO: calculate RTT when implementing UDP ( -><- / 2)
    // TODO: fix loopback test
    // TODO: change discover to lookup
    // TODO: ValueObject?
    public static void main(String[] args) throws IOException {
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new HelloWorldJsonRpcSerializer());

        IHelloWorld helloWorld = new HelloWorld();
        // TODO: application server stubs are not really necessary?
        IRpcAppServerStub helloWorldServer = new HelloWorldServer(helloWorld);

        IRPCServer rpcServer = new RpcServer(rpcMessageApi);
        rpcServer.register(helloWorldServer);
        new Thread(rpcServer::start).start();

        // callback server
        LocalRpcServerPortFinder.PORT = rpcServer.getSocketAddress().getPort();
        RpcCallbackService.getInstance().setRpcMarshaller(rpcMessageApi);
        IRpcCallback rpcCallback = new RpcCallback();
        IRpcAppServerStub callbackServer = new RpcCallbackServer(rpcCallback);
        rpcServer.register(callbackServer);


        // delay until the server starts
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // TODO: are we allowed to use java ProxyInstance to generate application stubs?
        IHelloWorld helloWorldClient = new HelloWorldClient(
                new RpcClient(
                        rpcServer.getSocketAddress(),
                        rpcMessageApi
                        ));

        System.out.println(helloWorldClient.helloWorld());
        System.out.println(helloWorldClient.helloWorld(new HelloWorldMessage("Custom Data Type")));
    }
}
