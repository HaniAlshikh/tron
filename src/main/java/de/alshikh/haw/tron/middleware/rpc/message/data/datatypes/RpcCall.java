package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

import java.util.UUID;

public class RpcCall implements IRpcCall {

    private final UUID serviceId;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Object[] arguments;

    public RpcCall(UUID serviceId, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.serviceId = serviceId;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }
}
