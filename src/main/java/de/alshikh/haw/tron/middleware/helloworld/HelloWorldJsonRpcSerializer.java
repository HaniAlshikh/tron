package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import org.json.JSONObject;

import java.util.UUID;

public class HelloWorldJsonRpcSerializer extends JsonRpcSerializer {
    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (!(obj instanceof JSONObject))
            return obj;
        JSONObject serializedObj = (JSONObject) obj;

        if (type == Object.class) {
            try {
                return deserialize(obj, Class.forName(serializedObj.getString("type")));
            } catch (ClassNotFoundException e) {
                return obj;
            }
        }

        if (type == HelloWorldMessage.class) {
            obj = new HelloWorldMessage(serializedObj.getString("message"));
        }

        if (type == UUID.class) {
            return UUID.fromString(serializedObj.getString("uuid"));
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
            return serializedObj;
        }

        if (obj instanceof UUID) {
            UUID uuid = (UUID) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", UUID.class.getName());
            serializedObj.put("uuid", uuid.toString());
            return serializedObj;
        }

        return obj;
    }
}
