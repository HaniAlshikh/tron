package de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JsonRpcResponse implements IRpcResponse {

    UUID id;
    Object result;
    JsonRpcError error;

    JSONObject resObj;

    public JsonRpcResponse(JSONObject resObj) {
        this.resObj = resObj;
        parse();
    }

    private void parse() {
        id = UUID.fromString(resObj.getString("id"));
        result = resObj.opt("result");
        JSONObject err = resObj.optJSONObject("error");
        if (err == null) return;
        error = new JsonRpcError(err.getInt("code"), err.getString("message"));
    }

    @Override
    public byte[] getBytes() {
        return resObj.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean success() {
        return error == null;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "JsonRpcResponse{" +
                "id=" + id +
                ", result=" + result +
                ", error=" + error +
                '}';
    }
}
