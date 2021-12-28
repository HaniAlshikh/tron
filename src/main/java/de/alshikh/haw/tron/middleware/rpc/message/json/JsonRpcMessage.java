package de.alshikh.haw.tron.middleware.rpc.message.json;

import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessage;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.RpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.IRpcError;
import de.alshikh.haw.tron.middleware.rpc.message.data.exceptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes.JsonRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes.JsonRpcResponse;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.UUID;

public class JsonRpcMessage implements IRpcMessage {

    private final double JSONRPC = 2.0;

    private final JsonRpcSerializer jsonRpcSerializer;

    public JsonRpcMessage() {
        this(new JsonRpcSerializer());
    }

    public JsonRpcMessage(JsonRpcSerializer jsonRpcSerializer) {
        this.jsonRpcSerializer = jsonRpcSerializer;
    }

    @Override
    public RpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException {
        String serviceName = rpcRequest.getMethod().split("\\.")[0];

        String methodName = rpcRequest.getMethod().split("\\.")[1];

        Class<?>[] parameterTypes = new Class[rpcRequest.getParams().keySet().size()];
        Object[] args = new Object[rpcRequest.getParams().keySet().size()];

        for (String type : rpcRequest.getParams().keySet()) {
            int pos = Integer.parseInt(type.split("\\+")[0]);
            String className = type.split("\\+")[1];

            try {
                parameterTypes[pos] = Class.forName(className);
                args[pos] = jsonRpcSerializer.deserialize(rpcRequest.getParams().get(type), parameterTypes[pos]);
            } catch (ClassNotFoundException e) {
                throw new InvalidParamsRpcException("Unknown type: " + className);
            }
        }

        return new RpcCall(serviceName, methodName, parameterTypes, args);
    }

    @Override
    public IRpcRequest newRequest(Class<?> serviceInterface, Method method, Object[] args) {
        JSONObject reqObj = new JSONObject();
        reqObj.put("jsonrpc", JSONRPC);
        reqObj.put("method", serviceInterface.getSimpleName() + "." + method.getName());

        Class<?>[] types =  method.getParameterTypes();
        JSONObject params = new JSONObject();
        for (int i = 0; i < types.length; i++)
            params.put(i + "+" + types[i].getName(), jsonRpcSerializer.serialize((args[i])));

        reqObj.put("params", params);
        reqObj.put("id", UUID.randomUUID().toString());

        return new JsonRpcRequest(reqObj);
    }

    @Override
    public IRpcRequest readRequest(byte[] request) {
        return new JsonRpcRequest(new JSONObject(new String(request)));
    }

    @Override
    public IRpcResponse successResponse(UUID requestId, Object result) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("jsonrpc", JSONRPC);

        responseObj.put("result", jsonRpcSerializer.serialize(result));

        responseObj.put("id", requestId.toString());

        return new JsonRpcResponse(responseObj);
    }

    @Override
    public IRpcResponse errorResponse(UUID requestId, IRpcError rpcError) {
        JSONObject responseObj = new JSONObject();
        responseObj.put("jsonrpc", JSONRPC);


        JSONObject error = new JSONObject();
        error.put("code", rpcError.getCode());
        error.put("message", rpcError.getMessage());
        responseObj.put("error", error);

        responseObj.put("id", requestId.toString());

        return new JsonRpcResponse(responseObj);
    }

    @Override
    public IRpcResponse readResponse(byte[] response) {
        return new JsonRpcResponse(new JSONObject(new String(response)));
    }

    @Override
    public Object toInvocationResult(IRpcResponse rpcResponse) {
        if (!rpcResponse.success())
            return null; // TODO

        Object result = rpcResponse.result();

        if (result instanceof JSONObject) {
            JSONObject resultObj = (JSONObject) result;
            try {
                Class<?> type = Class.forName(resultObj.getString("type"));
                result = jsonRpcSerializer.deserialize(result, type);
            } catch (ClassNotFoundException e) {
                return null; // TODO
            }
        }

        return result;
    }
}
