package de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.message.IRpcResponse;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JsonRpcResponse implements IRpcResponse {

    UUID id;
    Object result;
    JsonRpcError error;

    JSONObject responseObj;

    public JsonRpcResponse(JSONObject responseObj) {
        this.responseObj = responseObj;
        parse();
    }

    private void parse() {
        id = UUID.fromString(responseObj.getString("id"));

        Object result = responseObj.opt("result");
        if (result != null)
            this.result = result;

        JSONObject error = responseObj.optJSONObject("error");
        if (error != null)
            this.error = new JsonRpcError(error.getInt("code"), error.getString("message"));
    }

    @Override
    public byte[] getBytes() {
        return responseObj.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean success() {
        return error == null;
    }

    @Override
    public Object result() {
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
