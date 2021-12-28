package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.RpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.IRpcError;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.InvalidParamsRpcException;

import java.lang.reflect.Method;
import java.util.UUID;

public interface IRpcMessage {
    RpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;

    IRpcRequest newRequest(Class<?> serviceInterface, Method method, Object[] args);

    IRpcRequest readRequest(byte[] request);

    IRpcResponse successResponse(UUID requestId, Object result);

    IRpcResponse errorResponse(UUID requestId, IRpcError rpcError);

    IRpcResponse readResponse(byte[] response);

    Object toInvocationResult(IRpcResponse rpcResponse);
}
