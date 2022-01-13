package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.*;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.network.IRpcConnection;
import de.alshikh.haw.tron.middleware.rpc.network.RpcConnection;
import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class ServerStub implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private IRpcCallback rpcCallback;
    private IRpcRequest rpcRequest;

    private final IRpcConnection client;
    private final Function<Integer, IRpcCallback> newRpcCallback;
    private final IRpcMessageApi rpcMsgApi;
    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry;

    public ServerStub(Socket client, Function<Integer, IRpcCallback> newRpcCallback, IRpcMessageApi rpcMsgApi, HashMap<UUID, IRpcAppServerStub> serviceRegistry) {
        this.client = new RpcConnection(client);
        this.newRpcCallback = newRpcCallback;
        this.rpcMsgApi = rpcMsgApi;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try {
            receive();
            call(unmarshal());
        } catch (RpcException e) {
            if (!isNotification())
                rpcCallback.retrn(rpcRequest.getId(), e);
        }
    }

    private void receive() throws FailedToReceiveNetworkRpcException {
        rpcRequest = rpcMsgApi.readRequest(client.receive());
        log.debug("received request: " + rpcRequest);
        client.safeClose();
        if (!rpcRequest.isNotification())
            rpcCallback = newRpcCallback.apply(rpcRequest.getRpcServerPort());
    }

    private IRpcCall unmarshal() throws InvalidParamsRpcException {
        return rpcMsgApi.toRpcCall(rpcRequest);
    }

    private void call(IRpcCall rpcCall) throws InvocationRpcException, MethodNotFoundRpcException, ServiceNotFoundRpcException {
        try {
            IRpcAppServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceId());
            if (serviceServerStub == null)
                throw new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceId());

            Object result = serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
            if (!isNotification())
                rpcCallback.retrn(rpcRequest.getId(), result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundRpcException();
        }
    }

    private boolean isNotification() {
        return rpcCallback == null;
    }
}
