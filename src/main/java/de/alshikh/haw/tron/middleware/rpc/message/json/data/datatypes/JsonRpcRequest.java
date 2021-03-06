package de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JsonRpcRequest implements IRpcRequest {

    private UUID id;
    private InetSocketAddress callbackAddress;
    private UUID serviceId;
    private String method;
    private JSONArray params;

    private final JSONObject reqObj;

    public JsonRpcRequest(JSONObject reqObj) {
        this.reqObj = reqObj;
        parse();
    }

    private void parse() {
        String uuid = reqObj.optString("id");
        if (!uuid.isEmpty()) {
            id = UUID.fromString(uuid);
            callbackAddress = new InetSocketAddress(
                    reqObj.getString("callbackIp"),
                    reqObj.getInt("callbackPort"));
        }
        serviceId = UUID.fromString(reqObj.getString("service"));
        method = reqObj.getString("method");
        params = reqObj.getJSONArray("params");
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
    public InetSocketAddress getCallbackAddress() {
        return callbackAddress;
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
    public JSONArray getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "JsonRpcRequest{" +
                (isNotification() ? "" : "id=" + id) +
                (isNotification() ? "" : ", callbackAddress=" + callbackAddress + ", ") +
                "serviceId=" + serviceId +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
