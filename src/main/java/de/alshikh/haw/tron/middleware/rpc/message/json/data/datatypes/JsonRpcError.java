package de.alshikh.haw.tron.middleware.rpc.message.json.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcError;

public class JsonRpcError implements IRpcError {

    int code;
    String message;

    public JsonRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "JsonRpcError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
