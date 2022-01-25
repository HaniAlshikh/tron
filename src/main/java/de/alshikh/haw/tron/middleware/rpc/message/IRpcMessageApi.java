package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializationApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.InvalidParamsRpcException;
import org.json.JSONArray;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRpcMessageApi {
    IRpcRequest newRequest(UUID serviceId, boolean isNotification, InetSocketAddress rpcCallbackServerAddress, Method method, Object[] args);

    IRpcRequest parseRequest(byte[] request);

    void parseParamsArray(JSONArray params, Class<?>[] parameterTypes, Object[] args) throws InvalidParamsRpcException;

    IRpcSerializationApi getRpcSerializer();
}
