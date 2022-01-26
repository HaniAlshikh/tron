package de.alshikh.haw.tron.middleware.directoryserver.stub;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializationApi;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.UUID;

public class DirectoryServiceJsonRpcSerializationApi extends JsonRpcSerializationApi {
    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (type == DirectoryEntry.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;
            return new DirectoryEntry(
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
            IDirectoryEntry e = (IDirectoryEntry) obj;
            return new JSONObject()
                    .put("type", DirectoryEntry.class.getName())
                    .put("serviceId", e.getServiceId())
                    .put("address", e.getServiceAddress().getAddress().getHostAddress())
                    .put("port", e.getServiceAddress().getPort());
        }

        return obj;
    }
}
