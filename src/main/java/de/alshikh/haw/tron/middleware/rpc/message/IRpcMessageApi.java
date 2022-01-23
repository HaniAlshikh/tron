package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializationApi;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRpcMessageApi {
    IRpcRequest newRequest(UUID serviceId, boolean isNotification, InetSocketAddress rpcCallbackServerAddress, Method method, Object[] args);

    IRpcRequest readRequest(byte[] request);

    IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;

    IRpcSerializationApi getRpcSerializer();
}
