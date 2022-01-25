package de.alshikh.haw.tron.middleware.rpc.message.json;

import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.RpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes.JsonRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializationApi;
import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializationApi;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public class JsonRpcMessageApi implements IRpcMessageApi {

    private final static double JSONRPC = 2.0;

    private final JsonRpcSerializationApi jsonRpcSerializer;

    public JsonRpcMessageApi(JsonRpcSerializationApi jsonRpcSerializer) {
        this.jsonRpcSerializer = jsonRpcSerializer;
    }

    @Override
    public IRpcRequest newRequest(UUID serviceId, boolean isNotification, InetSocketAddress rpcCallbackServerAddress, Method method, Object[] args) {
        JSONObject reqObj = newRequestObj(serviceId, method, args);
        if (!isNotification) {
            reqObj.put("id", UUID.randomUUID().toString());
            reqObj.put("callbackIp", rpcCallbackServerAddress.getAddress().getHostAddress());
            reqObj.put("callbackPort", rpcCallbackServerAddress.getPort());
        }
        return new JsonRpcRequest(reqObj);
    }

    @Override
    public IRpcRequest readRequest(byte[] request) {
        return new JsonRpcRequest(new JSONObject(new String(request)));
    }

    @Override
    public IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException {
        UUID serviceId = rpcRequest.getServiceId();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = new Class[rpcRequest.getParams().keySet().size()];
        Object[] args = new Object[rpcRequest.getParams().keySet().size()];
        parseParamsObj(rpcRequest.getParams(), parameterTypes, args);
        return new RpcCall(serviceId, methodName, parameterTypes, args);
    }

    @Override
    public IRpcSerializationApi getRpcSerializer() {
        return jsonRpcSerializer;
    }

    private JSONObject newJsonObject() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("jsonrpc", JSONRPC);
        return jsonObj;
    }

    private JSONObject newRequestObj(UUID serviceId, Method method, Object[] args) {
        JSONObject reqObj = newJsonObject();
        reqObj.put("service", serviceId.toString());
        reqObj.put("method", method.getName());
        reqObj.put("params", newParamsObj(method.getParameterTypes(), args));
        return reqObj;
    }

    private JSONObject newParamsObj(Class<?>[] types, Object[] args) {
        JSONObject params = new JSONObject();
        for (int i = 0; i < types.length; i++)
            params.put(i + "+" + types[i].getName(), jsonRpcSerializer.serialize((args[i])));
        return params;
    }

    private void parseParamsObj(JSONObject params, Class<?>[] parameterTypes, Object[] args) throws InvalidParamsRpcException {
        for (String type : params.keySet()) {
            int pos = Integer.parseInt(type.split("\\+")[0]);
            String className = type.split("\\+")[1];

            try {
                parameterTypes[pos] = Class.forName(className);
                args[pos] = jsonRpcSerializer.deserialize(params.get(type), parameterTypes[pos]);
            } catch (ClassNotFoundException e) {
                throw new InvalidParamsRpcException("Unknown type: " + className);
            }
        }
    }
}
