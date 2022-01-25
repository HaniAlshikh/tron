package de.alshikh.haw.tron.middleware.rpc.message.json;

import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes.JsonRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializationApi;
import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializationApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.InvalidParamsRpcException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public class JsonRpcMessageApi implements IRpcMessageApi {

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

    private JSONObject newRequestObj(UUID serviceId, Method method, Object[] args) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("service", serviceId.toString());
        reqObj.put("method", method.getName());
        reqObj.put("params", newParamsArray(method.getParameterTypes(), args));
        return reqObj;
    }

    private JSONArray newParamsArray(Class<?>[] types, Object[] args) {
        JSONArray paramsArray = new JSONArray();
        for (int i = 0; i < types.length; i++)
            paramsArray.put(newParamObj(types[i], args[i]));
        return paramsArray;
    }

    private JSONObject newParamObj(Class<?> type, Object arg) {
        return new JSONObject()
                .put("type", type.getName())
                .put("argument", jsonRpcSerializer.serialize((arg)));
    }

    @Override
    public IRpcRequest parseRequest(byte[] request) {
        return new JsonRpcRequest(new JSONObject(new String(request)));
    }

    @Override
    public void parseParamsArray(JSONArray params, Class<?>[] parameterTypes, Object[] args) throws InvalidParamsRpcException {
        for (int i = 0; i < params.length(); i++) {
            JSONObject param = params.getJSONObject(i);
            try {
                parameterTypes[i] = Class.forName(param.getString("type"));
                args[i] = jsonRpcSerializer.deserialize(param.get("argument"), parameterTypes[i]);
            } catch (ClassNotFoundException e) {
                throw new InvalidParamsRpcException("Unknown type: " + param.getString("type"));
            }
        }
    }

    @Override
    public IRpcSerializationApi getRpcSerializer() {
        return jsonRpcSerializer;
    }
}
