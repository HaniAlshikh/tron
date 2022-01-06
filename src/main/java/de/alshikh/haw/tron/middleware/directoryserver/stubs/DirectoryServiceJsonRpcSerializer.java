package de.alshikh.haw.tron.middleware.directoryserver.stubs;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.UUID;

public class DirectoryServiceJsonRpcSerializer extends JsonRpcSerializer {
    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (type == DirectoryServiceEntry.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;
            obj = new DirectoryServiceEntry(
                    UUID.fromString(serializedObj.getString("serviceId")),
                    new InetSocketAddress(
                            serializedObj.getString("address"),
                            serializedObj.getInt("port")
                    )
            );
        }

        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof DirectoryServiceEntry) {
            DirectoryServiceEntry e = (DirectoryServiceEntry) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", DirectoryServiceEntry.class.getName());
            serializedObj.put("serviceId", e.getId());
            serializedObj.put("address", e.getServiceAddress().getAddress().getHostAddress());
            serializedObj.put("port", e.getServiceAddress().getPort());
            obj = serializedObj;
        }

        return obj;
    }
}
