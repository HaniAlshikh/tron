package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.*;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.InvalidParamsRpcException;

import java.lang.reflect.Method;
import java.util.UUID;

public interface IRpcMessageApi {
    IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;

    IRpcRequest newRequest(Class<?> serviceInterface, Method method, Object[] args);

    IRpcRequest newNotification(Class<?> serviceInterface, Method method, Object[] args);

    IRpcRequest readRequest(byte[] request);

    IRpcResponse newSuccessResponse(UUID requestId, Object result);

    IRpcResponse newErrorResponse(UUID requestId, IRpcError rpcError);

    IRpcResponse readResponse(byte[] response);

    Object toInvocationResult(IRpcResponse rpcResponse);
}
