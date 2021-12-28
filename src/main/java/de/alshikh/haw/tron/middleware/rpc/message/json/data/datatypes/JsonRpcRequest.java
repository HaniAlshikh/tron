package de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.message.IRpcRequest;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class JsonRpcRequest implements IRpcRequest {

    UUID id;
    String method;
    JSONObject params;

    JSONObject reqObj;

    public JsonRpcRequest(JSONObject reqObj) {
        this.reqObj = reqObj;
        parse();
    }

    private void parse() {
        id = UUID.fromString(reqObj.getString("id"));
        method = reqObj.getString("method");
        params = reqObj.getJSONObject("params");
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
    public String getMethod() {
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
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
