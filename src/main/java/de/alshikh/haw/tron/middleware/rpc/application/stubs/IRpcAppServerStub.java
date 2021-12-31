package de.alshikh.haw.tron.middleware.rpc.application.stubs;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public interface IRpcAppServerStub {
    Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
    UUID getId();
}
