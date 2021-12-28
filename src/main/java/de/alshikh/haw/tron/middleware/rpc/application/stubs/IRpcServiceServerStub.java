package de.alshikh.haw.tron.middleware.rpc.application.stubs;

import java.lang.reflect.InvocationTargetException;

public interface IRpcServiceServerStub {
    Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
