package de.alshikh.haw.tron.middleware.directoryserver.stubs;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializationApi;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.UUID;

public class DirectoryServiceJsonRpcSerializationApi extends JsonRpcSerializationApi {
    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (type == DirectoryEntry.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;
            obj = new DirectoryEntry(
                    UUID.fromString(serializedObj.getString("providerId")),
                    UUID.fromString(serializedObj.getString("serviceId")),
                    new InetSocketAddress(
                            serializedObj.getString("address"),
                            serializedObj.getInt("port")
                    ),
                    serializedObj.getBoolean("reachable")
            );
        }

        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof DirectoryEntry) {
            DirectoryEntry e = (DirectoryEntry) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", DirectoryEntry.class.getName());
            serializedObj.put("serviceId", e.getServiceId());
            serializedObj.put("address", e.getServiceAddress().getAddress().getHostAddress());
            serializedObj.put("port", e.getServiceAddress().getPort());
            obj = serializedObj;
        }

        return obj;
    }
}
