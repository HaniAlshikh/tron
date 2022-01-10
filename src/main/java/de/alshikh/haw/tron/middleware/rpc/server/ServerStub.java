package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.*;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.network.IRpcConnection;
import de.alshikh.haw.tron.middleware.rpc.network.RpcConnection;
import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;
import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToSendNetworkRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class ServerStub implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private UUID reqId;

    private final IRpcConnection client;
    private final IRpcMessageApi rpcMsgApi;
    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry;

    public ServerStub(Socket client, IRpcMessageApi rpcMsgApi, HashMap<UUID, IRpcAppServerStub> serviceRegistry) {
        this.client = new RpcConnection(client);
        this.rpcMsgApi = rpcMsgApi;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        IRpcResponse response = null;
        try {
            IRpcRequest request = receive();
            IRpcCall rpcCall = unmarshal(request);
            response = call(rpcCall);
        } catch (RpcException e) {
            if (!isNotification())
                response = rpcMsgApi.newErrorResponse(reqId, e);
        } finally {
            if (!isNotification()) {
                send(response);
                client.safeClose();
            }
        }
    }

    private IRpcRequest receive() throws FailedToReceiveNetworkRpcException {
        IRpcRequest req = rpcMsgApi.readRequest(client.receive());
        log.debug("received request: " + req);
        this.reqId = req.getId();
        if (req.isNotification())
            client.safeClose();
        return req;
    }

    private IRpcCall unmarshal(IRpcRequest request) throws InvalidParamsRpcException {
        return rpcMsgApi.toRpcCall(request);
    }

    private IRpcResponse call(IRpcCall rpcCall) throws InvocationRpcException, MethodNotFoundRpcException, ServiceNotFoundRpcException {
        try {
            IRpcAppServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceId());
            if (serviceServerStub == null)
                throw new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceId());

            Object result = serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
            if (isNotification())
                return null;

            return rpcMsgApi.newSuccessResponse(reqId, result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundRpcException();
        }
    }

    private void send(IRpcResponse response) {
        if (response == null) return;
        try {
            client.send(response.getBytes());
        } catch (FailedToSendNetworkRpcException ignored) {}
    }

    private boolean isNotification() {
        return reqId == null;
    }
}
