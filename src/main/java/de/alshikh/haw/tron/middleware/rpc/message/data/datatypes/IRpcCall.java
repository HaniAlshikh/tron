package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

public interface IRpcCall {
    String getServiceName();

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();
}
