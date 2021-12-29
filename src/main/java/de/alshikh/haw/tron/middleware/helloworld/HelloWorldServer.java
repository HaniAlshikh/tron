package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceServerStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class HelloWorldServer implements IRpcServiceServerStub {
    public static UUID id = UUID.fromString("08fd9cc9-a1ff-454e-ae22-f3c1329ab93c");

    IHelloWorld helloWorldService;

    public HelloWorldServer(IHelloWorld helloWorld) {
        this.helloWorldService = helloWorld;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = helloWorldService.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(helloWorldService, args);
    }

    @Override
    public UUID getId() {
        return id;
    }
}
