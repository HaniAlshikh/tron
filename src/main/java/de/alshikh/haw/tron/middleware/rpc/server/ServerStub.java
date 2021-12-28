package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class ServerStub implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private UUID reqId;

    private final Socket client;
    private final IRpcMessageApi rpcMsgApi;
    private final HashMap<String, IRpcServiceServerStub> serviceRegistry;

    public ServerStub(Socket client, IRpcMessageApi rpcMsgApi, HashMap<String, IRpcServiceServerStub> serviceRegistry) {
        this.client = client;
        this.rpcMsgApi = rpcMsgApi;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        IRpcResponse response = null;
        try {
            byte[] request = receive();
            IRpcCall rpcCall = unmarshal(request);
            response = call(rpcCall);
        } catch (RpcException e) {
            response = rpcMsgApi.newErrorResponse(reqId, e);
        } catch (IOException e) {
            log.error("Failed to handle Request due to network error:", e);
        } finally {
            if (response != null)
                send(response);
            try {
                client.close();
            } catch (IOException e) {
                log.error("Failed to close client connection: " + reqId, e);
            }
        }
    }

    private byte[] receive() throws IOException {
        return client.getInputStream().readAllBytes();
    }

    private IRpcCall unmarshal(byte[] request) throws InvalidParamsRpcException {
        IRpcRequest requestObj = rpcMsgApi.readRequest(request);
        log.debug("received request: " + requestObj);
        this.reqId = requestObj.getId();
        return rpcMsgApi.toRpcCall(requestObj);
    }

    private IRpcResponse call(IRpcCall rpcCall) throws InvocationRpcException, MethodNotFoundRpcException, ServiceNotFoundRpcException {
        try {
            IRpcServiceServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceName());
            if (serviceServerStub == null)
                throw new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceName());

            Object result = serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());

            return rpcMsgApi.newSuccessResponse(reqId, result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundRpcException();
        }
    }

    private void send(IRpcResponse response) {
        try {
            client.getOutputStream().write(response.getBytes());
            client.shutdownOutput();
        } catch (IOException e) {
            log.error("Failed to send Response:", e);
        }
    }
}
