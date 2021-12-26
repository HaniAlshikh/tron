package de.alshikh.haw.tron.middleware.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientStub implements InvocationHandler {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final Class<?> serviceInterface;
    private final InetSocketAddress serverAddress;

    public ClientStub(Class<?> serviceInterface, InetSocketAddress serverAddress) {
        this.serviceInterface = serviceInterface;
        this.serverAddress = serverAddress;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            socket = new Socket();
            socket.connect(serverAddress);

            // marshal and send
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeUTF(serviceInterface.getName());
            output.writeUTF(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);

            // receive (block and wait for server)
            // TODO: bad idea? (async all methods return void -> doesn't matter?)
            input = new ObjectInputStream(socket.getInputStream());
            return input.readObject();
        } finally {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        }
    }
}
