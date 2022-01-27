package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializationApi;
import org.json.JSONObject;

import java.util.UUID;

public class HelloWorldJsonRpcSerializationApi extends JsonRpcSerializationApi {
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

        if (type == HelloWorldMessage.class)
            return new HelloWorldMessage(serializedObj.getString("message"));

        if (type == UUID.class)
            return UUID.fromString(serializedObj.getString("uuid"));

        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof HelloWorldMessage) {
            HelloWorldMessage message = (HelloWorldMessage) obj;
            return new JSONObject()
                    .put("type", HelloWorldMessage.class.getName())
                    .put("message", message.getMessage());
        }

        if (obj instanceof UUID) {
            UUID uuid = (UUID) obj;
            return new JSONObject()
                    .put("type", UUID.class.getName())
                    .put("uuid", uuid.toString());
        }

        return obj;
    }
}
