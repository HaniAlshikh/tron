package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.InvocationRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;

public class ClientStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    // TODO: rpc callback with future<IRpcResponse>
    private boolean waitForResponse = false;

    private final InetSocketAddress serverAddress;
    private final IRpcMessageApi rpcMsgApi;

    public ClientStub(InetSocketAddress serverAddress, IRpcMessageApi rpcMsgApi) {
        this.serverAddress = serverAddress;
        this.rpcMsgApi = rpcMsgApi;
    }

    public Object invoke(UUID serviceId, Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        IRpcResponse response = send(marshal(serviceId, method, args));
        log.debug("Received response: " + response);
        return rpcMsgApi.toInvocationResult(response);
    }

    private IRpcRequest marshal(UUID serviceId, Method method, Object[] args) {
        return rpcMsgApi.newRequest(serviceId, method, args);
    }

    private IRpcResponse send(IRpcRequest request) {
        try (Socket server = new Socket()) {
            server.connect(serverAddress);

            log.debug("sending request: " + request);
            server.getOutputStream().write(request.getBytes());
            server.shutdownOutput();

            if (!waitForResponse)
                return rpcMsgApi.newSuccessResponse(request.getId(), null);

            // TODO: receive method
            byte[] response = server.getInputStream().readAllBytes();
            return rpcMsgApi.readResponse(response);
        } catch (IOException e) {
            log.debug("Failed to send request due to network error", e);
            return rpcMsgApi.newErrorResponse(request.getId(), new InvocationRpcException());
        }
    }

    public void setWaitForResponse(boolean waitForResponse) {
        this.waitForResponse = waitForResponse;
    }
}
