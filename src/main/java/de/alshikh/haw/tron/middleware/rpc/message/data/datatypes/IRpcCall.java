package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

import java.util.UUID;

public interface IRpcCall {
    UUID getServiceId();

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();
}
