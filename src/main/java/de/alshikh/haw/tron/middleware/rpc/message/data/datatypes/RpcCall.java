package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

public class RpcCall {

    String serviceName;
    String methodName;
    Class<?>[] parameterTypes;
    Object[] arguments;

    public RpcCall(String serviceName, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
