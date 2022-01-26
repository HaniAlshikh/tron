package de.alshikh.haw.tron.middleware.rpc.callback.stubs;

import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class RpcCallbackCallee implements IRpcCalleeAppStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1fd-454e-ae21-f3c1329ab93c");

    IRpcCallback rpcCallback;

    public RpcCallbackCallee(IRpcCallback rpcCallback) {
        this.rpcCallback = rpcCallback;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = rpcCallback.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(rpcCallback, args);
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
