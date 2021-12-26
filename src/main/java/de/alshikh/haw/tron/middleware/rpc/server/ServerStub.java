package de.alshikh.haw.tron.middleware.rpc.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class ServerStub implements Runnable {
    private final HashMap<String, Object> serviceRegistry;
    private final Socket client;

    public ServerStub(HashMap<String, Object> serviceRegistry, Socket client) {
        this.serviceRegistry = serviceRegistry;
        this.client = client;
    }

    @Override
    public void run() {
        try (client;
             ObjectInputStream input = new ObjectInputStream(client.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream())) {

            // unmarshal
            String serviceName = input.readUTF();
            String methodName = input.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
            Object[] arguments = (Object[]) input.readObject();
            Object service = serviceRegistry.get(serviceName);
            if (service == null) {
                throw new ClassNotFoundException(serviceName + " not found");
            }

            // call
            Method method = service.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(service, arguments);

            // return
            output.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
