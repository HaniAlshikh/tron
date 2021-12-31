package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import org.json.JSONObject;

public class HelloWorldJsonRpcSerializer extends JsonRpcSerializer {
    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (type == HelloWorldMessage.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;
            obj = new HelloWorldMessage(serializedObj.getString("message"));
        }

        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof HelloWorldMessage) {
            HelloWorldMessage message = (HelloWorldMessage) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", HelloWorldMessage.class.getName());
            serializedObj.put("message", message.getMessage());
            obj = serializedObj;
        }

        return obj;
    }
}
