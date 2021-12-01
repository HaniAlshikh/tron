package de.alshikh.haw.tron.middleware.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RPCInvoker implements InvocationHandler {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final InetSocketAddress calleeAddress;
    private final Class<?> serviceInterface;

    public RPCInvoker(InetSocketAddress calleeAddress, Class<?> serviceInterface) {
        this.calleeAddress = calleeAddress;
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            socket = new Socket();
            socket.connect(calleeAddress);

            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeUTF(serviceInterface.getName());
            output.writeUTF(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);

            // block and wait for server
            input = new ObjectInputStream(socket.getInputStream());
            return input.readObject();
        } finally {
            if (socket != null) socket.close();
            if (output != null) output.close();
            if (input != null) input.close();
        }
    }
}
