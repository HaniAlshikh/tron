package de.alshikh.haw.tron.middleware.rpc.message.json;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.RpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.*;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes.JsonRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes.JsonRpcResponse;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.UUID;

public class JsonRpcMessageApi implements IRpcMessageApi {

    private final static double JSONRPC = 2.0;

    private final JsonRpcSerializer jsonRpcSerializer;

    // TODO: find a pattern to split into requestApi and ResponseApi

    public JsonRpcMessageApi() {
        this(new JsonRpcSerializer());
    }

    public JsonRpcMessageApi(JsonRpcSerializer jsonRpcSerializer) {
        this.jsonRpcSerializer = jsonRpcSerializer;
    }

    // request API

    @Override
    public IRpcRequest newRequest(UUID serviceId, Method method, Object[] args) {
        JSONObject reqObj = newRequestObj(serviceId, method, args);
        reqObj.put("id", UUID.randomUUID().toString());
        return new JsonRpcRequest(reqObj);
    }

    @Override
    public IRpcRequest newNotification(UUID serviceId, Method method, Object[] args) {
        JSONObject reqObj = newRequestObj(serviceId, method, args);
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

    // response API

    @Override
    public IRpcResponse newSuccessResponse(UUID requestId, Object result) {
        JSONObject resObj = newResponseObj(requestId);
        resObj.put("result", jsonRpcSerializer.serialize(result));
        return new JsonRpcResponse(resObj);
    }

    @Override
    public IRpcResponse newErrorResponse(UUID requestId, IRpcError rpcError) {
        JSONObject resObj = newResponseObj(requestId);
        resObj.put("error", newErrorObj(rpcError));
        return new JsonRpcResponse(resObj);
    }

    @Override
    public IRpcResponse readResponse(byte[] response) {
        return new JsonRpcResponse(new JSONObject(new String(response)));
    }

    @Override
    public Object toInvocationResult(IRpcResponse rpcResponse) {
        if (!rpcResponse.success())
            return null; // TODO map error to exception object

        // TODO: this should not be necessary if we are allowed to outsource serialization
        return parseResultObj(rpcResponse.getResult());
    }

    // logic

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

    private JSONObject newResponseObj(UUID requestId) {
        JSONObject jsonObj = newJsonObject();
        jsonObj.put("id", requestId.toString());
        return jsonObj;
    }

    private JSONObject newErrorObj(IRpcError rpcError) {
        JSONObject error = new JSONObject();
        error.put("code", rpcError.getCode());
        error.put("message", rpcError.getMessage());
        return error;
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

    private Object parseResultObj(Object result) {
        if (result instanceof JSONObject) {
            JSONObject resultObj = (JSONObject) result;
            try {
                Class<?> type = Class.forName(resultObj.getString("type"));
                result = jsonRpcSerializer.deserialize(result, type);
            } catch (ClassNotFoundException e) {
                result = e;
            }
        }
        return result;
    }
}
