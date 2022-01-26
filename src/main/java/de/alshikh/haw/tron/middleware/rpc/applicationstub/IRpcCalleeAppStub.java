package de.alshikh.haw.tron.middleware.rpc.applicationstub;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public interface IRpcCalleeAppStub {
    Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
    UUID getServiceId();
}
