package de.alshikh.haw.tron.middleware.rpc;

import de.alshikh.haw.tron.middleware.helloworld.HelloWorldClient;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldJsonRpcSerializationApi;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldServer;
import de.alshikh.haw.tron.middleware.helloworld.service.HelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackServer;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRPCServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;

class RPC {
    // TODO: iterate over the methods and create a client stub
    // TODO: application stubs: callee/consumer caller/producer and not server client
    // TODO: UDP packte size (propping to see what possible to send)
    // TODO: split application stub into two components callee caller
    // TODO: calculate RTT when implementing UDP ( -><- / 2)
    // TODO: change discover to lookup
    public static void main(String[] args) {
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new HelloWorldJsonRpcSerializationApi());

        IHelloWorld helloWorld = new HelloWorld();
        // TODO: application server stubs are not really necessary?
        IRpcAppServerStub helloWorldServer = new HelloWorldServer(helloWorld);

        IRPCServerStub rpcServer = new RpcServerStub(rpcMessageApi);
        rpcServer.register(helloWorldServer);
        new Thread(() -> rpcServer.getRpcReceiver().start()).start();

        // callback server
        IRpcCallbackService rpcCallbackService = new RpcCallbackService(rpcServer.getRpcReceiver().getServerAddress());
        IRpcCallback rpcCallback = new RpcCallback(rpcCallbackService);
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
                new RpcClientStub(new RpcMarshaller(
                        rpcMessageApi,
                        new RpcSender(rpcServer.getRpcReceiver().getServerAddress()),
                        rpcCallbackService
                        )));

        System.out.println(helloWorldClient.helloWorld());
        System.out.println(helloWorldClient.helloWorldBestEffort());
        System.out.println(helloWorldClient.helloWorld(new HelloWorldMessage("Custom Data Type")));
    }
}
