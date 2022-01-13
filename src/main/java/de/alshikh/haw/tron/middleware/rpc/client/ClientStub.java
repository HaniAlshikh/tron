package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.network.IRpcConnection;
import de.alshikh.haw.tron.middleware.rpc.network.RpcConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.UUID;

public class ClientStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    // TODO: who know about callbacks AppStub || JsonRpcClient || ClientStub?
    // TODO: how to tell the server about the callback service server?
    //  - add the port to the messaging protocol?
    //  - use the directory server?
    // TODO: this doesn't feel right
    private final IRpcCallbackService callbackService = RpcCallbackService.getInstance();

    private final IRpcConnection server;
    private final IRpcMessageApi rpcMsgApi;
    private final boolean waitForResponse;

    public ClientStub(SocketAddress serverAddress, IRpcMessageApi rpcMsgApi) {
        this(serverAddress, rpcMsgApi, false);
    }

    public ClientStub(SocketAddress serverAddress, IRpcMessageApi rpcMsgApi, boolean waitForResponse) {
        this.server = new RpcConnection(serverAddress);
        this.rpcMsgApi = rpcMsgApi;
        this.waitForResponse = waitForResponse;
    }

    public void invoke(UUID serviceId, IRpcCallbackHandler callback, Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        IRpcRequest rpcRequest = marshal(serviceId, method, args);
        send(rpcRequest);

        if (callback != null)
            callbackService.register(rpcRequest.getId(), callback);
    }

    private IRpcRequest marshal(UUID serviceId, Method method, Object[] args) {
        if (waitForResponse)
            return rpcMsgApi.newRequest(serviceId, method, args);
        return rpcMsgApi.newNotification(serviceId, method, args);
    }

    private void send(IRpcRequest request) {
        try (server) {
            server.connect();
            log.debug("sending request: " + request);
            server.send(request.getBytes());
        } catch (Exception e) {
            log.info("failed to send request: " + request);
            log.debug("sending request error:", e);
            if (request.isNotification())
                callbackService.setResponse(request.getId(), e);
        }
    }
}
