package de.alshikh.haw.tron.middleware.rpc.clientstub;

import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;

import java.net.InetSocketAddress;

public class RpcClientStubFactory {
    private static IRpcMessageApi rpcMessageApi;
    private static IRpcCallbackService rpcCallbackService;

    public static IRpcClientStub getRpcClientStub(InetSocketAddress serverAddress) {
        insureInitialization();
        return new RpcClientStub(new RpcMarshaller(
                rpcMessageApi,
                new RpcSender(serverAddress),
                rpcCallbackService
        ));
    }

    private static void insureInitialization() {
        if (rpcMessageApi == null || rpcCallbackService == null) {
            throw new RuntimeException(new InstantiationException(
                    "the message api and/or the callback service were instantiated"
            ));
        }
    }

    public static void setRpcMessageApi(IRpcMessageApi rpcMessageApi) {
        RpcClientStubFactory.rpcMessageApi = rpcMessageApi;
    }

    public static void setRpcCallbackService(IRpcCallbackService rpcCallbackService) {
        RpcClientStubFactory.rpcCallbackService = rpcCallbackService;
    }
}
