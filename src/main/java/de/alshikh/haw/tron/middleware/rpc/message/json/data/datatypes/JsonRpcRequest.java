package de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JsonRpcRequest implements IRpcRequest {

    UUID id;
    int rpcServerPort;
    UUID serviceId;
    String method;
    JSONObject params;

    JSONObject reqObj;

    public JsonRpcRequest(JSONObject reqObj) {
        this.reqObj = reqObj;
        parse();
    }

    private void parse() {
        String uuid = reqObj.optString("id");
        if (!uuid.isEmpty()) {
            id = UUID.fromString(uuid);
            rpcServerPort = reqObj.getInt("rpcServerPort");
        }
        serviceId = UUID.fromString(reqObj.getString("service"));
        method = reqObj.getString("method");
        params = reqObj.getJSONObject("params");
    }

    @Override
    public boolean isNotification() {
        return id == null;
    }

    @Override
    public byte[] getBytes() {
        return reqObj.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public int getRpcServerPort() {
        return rpcServerPort;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }

    @Override
    public String getMethodName() {
        return method;
    }

    @Override
    public JSONObject getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "JsonRpcRequest{" +
                "id=" + id +
                ", rpcServerPort=" + rpcServerPort +
                ", serviceId=" + serviceId +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
