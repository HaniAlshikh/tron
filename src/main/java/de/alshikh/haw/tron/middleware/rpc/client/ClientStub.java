package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessage;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.InvocationRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class ClientStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    // TODO: rpc callback with future<IRpcResponse>
    private boolean waitForResponse = false;

    private final Class<?> serviceInterface;
    private final InetSocketAddress serverAddress;
    private final IRpcMessage rpcMessage;
    private final UUID id;

    public ClientStub(Class<?> serviceInterface, InetSocketAddress serverAddress, IRpcMessage rpcMessage) {
        this.serviceInterface = serviceInterface;
        this.serverAddress = serverAddress;
        this.rpcMessage = rpcMessage;
        this.id = UUID.randomUUID();
    }

    public Object invoke(Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        IRpcResponse rpcResponse = send(marshal(method, args));
        log.debug("Received response: " + rpcResponse);
        return rpcMessage.toInvocationResult(rpcResponse);
    }

    private IRpcRequest marshal(Method method, Object[] args) {
        return rpcMessage.newRequest(serviceInterface, method, args);
    }

    private IRpcResponse send(IRpcRequest request) {
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;

        try {
            socket = new Socket();
            socket.connect(serverAddress);

            log.debug("sending request: " + request);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeUTF(new String(request.getBytes()));
            output.flush();
            //socket.getOutputStream().write(request.getBytes());

            if (!waitForResponse)
                return rpcMessage.successResponse(id, null);

            input = new ObjectInputStream(socket.getInputStream());
            byte[] response = input.readUTF().getBytes(StandardCharsets.UTF_8);
            //byte[] response = socket.getInputStream().readAllBytes();
            return rpcMessage.readResponse(response);
        } catch (IOException e) {
            return rpcMessage.errorResponse(id, new InvocationRpcException());
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setWaitForResponse(boolean waitForResponse) {
        this.waitForResponse = waitForResponse;
    }
}
