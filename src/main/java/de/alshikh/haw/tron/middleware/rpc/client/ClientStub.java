package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvocationRpcException;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;
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

    public Object invoke(UUID serviceId, Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        IRpcResponse response = send(marshal(serviceId, method, args));
        if (response == null) return null;
        log.debug("Received response: " + response);
        return rpcMsgApi.toInvocationResult(response);
    }

    private IRpcRequest marshal(UUID serviceId, Method method, Object[] args) {
        if (waitForResponse)
            return rpcMsgApi.newRequest(serviceId, method, args);
        return rpcMsgApi.newNotification(serviceId, method, args);
    }

    private IRpcResponse send(IRpcRequest request) {
        try (server) {
            server.connect();
            log.debug("sending request: " + request);
            server.send(request.getBytes());

            if (request.isNotification())
                return null;

            return rpcMsgApi.readResponse(server.receive());
        } catch (Exception e) {
            log.debug("Failed to send request: ", e);
            return rpcMsgApi.newErrorResponse(request.getId(),
                    e instanceof RpcException ? (RpcException) e : new InvocationRpcException());
        }
    }
}
