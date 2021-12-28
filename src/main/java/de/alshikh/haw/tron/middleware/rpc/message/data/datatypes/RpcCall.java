package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

public class RpcCall implements IRpcCall {

    private final String serviceName;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Object[] arguments;

    public RpcCall(String serviceName, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    @Override
    public String getServiceName() {
        return serviceName;
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
