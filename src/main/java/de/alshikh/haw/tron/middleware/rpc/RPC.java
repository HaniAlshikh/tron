package de.alshikh.haw.tron.middleware.rpc;

import de.alshikh.haw.tron.middleware.helloworld.HelloWorldCallee;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldCaller;
import de.alshikh.haw.tron.middleware.helloworld.HelloWorldJsonRpcSerializationApi;
import de.alshikh.haw.tron.middleware.helloworld.service.HelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackCallee;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.RpcUnmarshaller;

class RPC {
    public static void main(String[] args) {
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new HelloWorldJsonRpcSerializationApi());

        IHelloWorld helloWorld = new HelloWorld();
        // TODO: application server stubs are not really necessary (service id are the problem)
        IRpcCalleeAppStub helloWorldServer = new HelloWorldCallee(helloWorld);

        IRpcServerStub rpcServerStub = new RpcServerStub(new RpcReceiver(new RpcUnmarshaller(rpcMessageApi)));
        rpcServerStub.register(helloWorldServer);
        new Thread(() -> rpcServerStub.getRpcReceiver().start()).start();

        // callback server
        IRpcCallbackService rpcCallbackService = new RpcCallbackService(rpcServerStub.getRpcReceiver().getServerAddress());
        IRpcCallback rpcCallback = new RpcCallback(rpcCallbackService);
        IRpcCalleeAppStub callbackServer = new RpcCallbackCallee(rpcCallback);
        rpcServerStub.register(callbackServer);

        // delay until the server starts
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IHelloWorld helloWorldClient = new HelloWorldCaller(
                new RpcClientStub(new RpcMarshaller(
                        rpcMessageApi,
                        new RpcSender(rpcServerStub.getRpcReceiver().getServerAddress()),
                        rpcCallbackService
                        )));

        System.out.println(helloWorldClient.helloWorld());
        System.out.println(helloWorldClient.helloWorldBestEffort());
        System.out.println(helloWorldClient.helloWorld(new HelloWorldMessage("Custom Data Type")));
    }
}
