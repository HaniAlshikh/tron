package de.alshikh.haw.tron.middleware.rpc.clientstub;

import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class RpcClientStubFactory {
    private static final Logger log = LoggerFactory.getLogger(RpcClientStubFactory.class.getSimpleName());

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
        if (rpcMessageApi == null) {
            throw new RuntimeException(new InstantiationException(
                    "no the message api was provided"
            ));
        }
        if (rpcCallbackService == null) {
            log.warn("no rpc callback service was provided"); // some services accept no callbacks
        }
    }

    public static void setRpcMessageApi(IRpcMessageApi rpcMessageApi) {
        RpcClientStubFactory.rpcMessageApi = rpcMessageApi;
    }

    public static void setRpcCallbackService(IRpcCallbackService rpcCallbackService) {
        RpcClientStubFactory.rpcCallbackService = rpcCallbackService;
    }
}
