package de.alshikh.haw.tron.middleware.rpc.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class ServerStub implements Runnable {
    private final HashMap<String, Class<?>> serviceRegistry;
    private final Socket client;

    public ServerStub(HashMap<String, Class<?>> serviceRegistry, Socket client) {
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
            Class<?> serviceClass = serviceRegistry.get(serviceName);
            if (serviceClass == null) {
                throw new ClassNotFoundException(serviceName + " not found");
            }

            // invoke
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serviceClass.getDeclaredConstructor().newInstance(), arguments);

            // return
            output.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
