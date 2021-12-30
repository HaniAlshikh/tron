package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcError;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;

import java.lang.reflect.Method;
import java.util.UUID;

public interface IRpcMarshaller {
    IRpcRequest newRequest(UUID serviceId, Method method, Object[] args);

    IRpcRequest newNotification(UUID serviceId, Method method, Object[] args);

    IRpcResponse newSuccessResponse(UUID requestId, Object result);

    IRpcResponse newErrorResponse(UUID requestId, IRpcError rpcError);
}
