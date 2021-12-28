package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessage;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.RpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class ServerStub implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private UUID id;

    private final Socket client;
    private final IRpcMessage rpcMessage;
    private final HashMap<String, IRpcServiceServerStub> serviceRegistry;

    public ServerStub(Socket client, IRpcMessage rpcMessage, HashMap<String, IRpcServiceServerStub> serviceRegistry) {
        this.client = client;
        this.rpcMessage = rpcMessage;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        IRpcResponse response = null;
        try {
            byte[] request = receive();
            RpcCall rpcCall = unmarshal(request);
            response = call(rpcCall);
        } catch (RpcException e) {
            response = rpcMessage.errorResponse(id, e);
        } catch (IOException e) {
            log.error("Can't read Request:", e);
        } finally {
            if (response != null)
                send(response);
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] receive() throws IOException {
        // TODO: receive bytes
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());
        return input.readUTF().getBytes(StandardCharsets.UTF_8);
    }

    private RpcCall unmarshal(byte[] request) throws InvalidParamsRpcException {
        IRpcRequest requestObj = rpcMessage.readRequest(request);
        log.debug("received request: " + requestObj);
        this.id = requestObj.getId();
        return rpcMessage.toRpcCall(requestObj);
    }

    private IRpcResponse call(RpcCall rpcCall) throws InvocationRpcException, MethodNotFoundRpcException, ServiceNotFoundRpcException {
        IRpcServiceServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceName());
        if (serviceServerStub == null)
            throw new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceName());

        Object result;
        try {
            result = serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundRpcException();
        }

        return rpcMessage.successResponse(id, result);

    }

    private void send(IRpcResponse response) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeUTF(new String(response.getBytes()));
            output.flush();
            //client.getOutputStream().write(response.getBytes());
        } catch (IOException e) {
            log.error("Can't send Response:", e);
        }
    }
}
