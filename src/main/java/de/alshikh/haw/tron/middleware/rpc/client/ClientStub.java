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

public class ClientStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    // TODO: rpc callback with future<IRpcResponse>
    private boolean waitForResponse = false;

    private final Class<?> serviceInterface;
    private final InetSocketAddress serverAddress;
    private final IRpcMessageApi rpcMsgApi;

    public ClientStub(Class<?> serviceInterface, InetSocketAddress serverAddress, IRpcMessageApi rpcMsgApi) {
        this.serviceInterface = serviceInterface;
        this.serverAddress = serverAddress;
        this.rpcMsgApi = rpcMsgApi;
    }

    public Object invoke(Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        IRpcResponse response = send(marshal(method, args));
        log.debug("Received response: " + response);
        return rpcMsgApi.toInvocationResult(response);
    }

    private IRpcRequest marshal(Method method, Object[] args) {
        return rpcMsgApi.newRequest(serviceInterface, method, args);
    }

    private IRpcResponse send(IRpcRequest request) {
        try (Socket server = new Socket()) {
            server.connect(serverAddress);

            log.debug("sending request: " + request);
            server.getOutputStream().write(request.getBytes());
            server.shutdownOutput();

            if (!waitForResponse)
                return rpcMsgApi.newSuccessResponse(request.getId(), null);

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
