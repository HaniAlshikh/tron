package de.alshikh.haw.tron.middleware.rpc.message.marshal;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;

import java.lang.reflect.Method;
import java.util.UUID;

public interface IRpcMarshaller {
    IRpcRequest newRequest(UUID serviceId, Method method, Object[] args, boolean isNotification);
}
