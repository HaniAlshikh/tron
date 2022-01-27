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
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStubFactory;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.RpcUnmarshaller;

// POC on a hello world service
class RPC {
    public static void main(String[] args) {
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new HelloWorldJsonRpcSerializationApi());
        RpcClientStubFactory.setRpcMessageApi(rpcMessageApi);

        IHelloWorld helloWorld = new HelloWorld();
        IRpcCalleeAppStub helloWorldSCallee = new HelloWorldCallee(helloWorld);

        // rpc server stub
        IRpcServerStub rpcServerStub = new RpcServerStub(new RpcReceiver(new RpcUnmarshaller(rpcMessageApi)));
        rpcServerStub.register(helloWorldSCallee);
        new Thread(() -> rpcServerStub.getRpcReceiver().start()).start();

        // callback
        IRpcCallbackService rpcCallbackService = new RpcCallbackService(rpcServerStub.getRpcReceiver().getServerAddress());
        RpcClientStubFactory.setRpcCallbackService(rpcCallbackService);
        IRpcCallback rpcCallback = new RpcCallback(rpcCallbackService);
        IRpcCalleeAppStub callbackServer = new RpcCallbackCallee(rpcCallback);
        rpcServerStub.register(callbackServer);

        IHelloWorld helloWorldClient = new HelloWorldCaller(RpcClientStubFactory.getRpcClientStub(rpcServerStub.getRpcReceiver().getServerAddress()));

        System.out.println(helloWorldClient.helloWorld());
        System.out.println(helloWorldClient.helloWorldBestEffort());
        System.out.println(helloWorldClient.helloWorld(new HelloWorldMessage("Custom Data Type")));
    }
}
