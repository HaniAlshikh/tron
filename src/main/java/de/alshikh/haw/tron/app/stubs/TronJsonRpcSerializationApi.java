package de.alshikh.haw.tron.app.stubs;

import de.alshikh.haw.tron.app.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.app.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service.IRemoteRoomsFactory;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryClient;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializationApi;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class TronJsonRpcSerializationApi extends JsonRpcSerializationApi {

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof IRpcAppClientStub) return serializeIRpcAppClientStub((IRpcAppClientStub) obj);
        if (obj instanceof PlayerUpdate) return serializePlayerUpdate((PlayerUpdate) obj);
        if (obj instanceof DirectoryEntry) return serializeDirectoryServiceEntry((DirectoryEntry) obj);
        if (obj instanceof InetAddress) return serializeInetAddress((InetAddress) obj);
        if (obj instanceof UUID) return serializeUUID((UUID) obj);

        return obj;
    }

    private JSONObject newSerializedObj(Class<?> clazz) {
        return new JSONObject()
                .put("type", clazz.getName());
    }

    private Object serializeIRpcAppClientStub(IRpcAppClientStub rpcAppClientStub) {
        JSONObject serializedObj = null;
        if (rpcAppClientStub instanceof IUpdateChannel) serializedObj = newSerializedObj(IUpdateChannel.class);
        if (rpcAppClientStub instanceof IRemoteRoomsFactory) serializedObj = newSerializedObj(IRemoteRoomsFactory.class);

        if (serializedObj == null) return rpcAppClientStub;
        return serializedObj
                .put("ip", rpcAppClientStub.getRpcClient().getServerAddress().getAddress().getHostAddress())
                .put("port", rpcAppClientStub.getRpcClient().getServerAddress().getPort());
    }

    private JSONObject serializePlayerUpdate(PlayerUpdate playerUpdate) {
        return newSerializedObj(PlayerUpdate.class)
                .put("movingDirection", playerUpdate.getMovingDirection().name())
                .put("pauseGame", playerUpdate.pauseGame())
                .put("dead", playerUpdate.isDead())
                .put("version", playerUpdate.getVersion());
    }

    private JSONObject serializeDirectoryServiceEntry(DirectoryEntry directoryEntry) {
        return newSerializedObj(DirectoryEntry.class)
                .put("type", DirectoryEntry.class.getName())
                .put("providerId", directoryEntry.getProviderId())
                .put("serviceId", directoryEntry.getServiceId())
                .put("address", directoryEntry.getServiceAddress().getAddress().getHostAddress())
                .put("port", directoryEntry.getServiceAddress().getPort())
                .put("reachable", directoryEntry.isReachable());
    }

    private JSONObject serializeInetAddress(InetAddress address) {
        return newSerializedObj(InetAddress.class)
                .put("address", address.getHostAddress());
    }

    private JSONObject serializeUUID(UUID uuid) {
        return newSerializedObj(UUID.class)
                .put("uuid", uuid.toString());
    }

    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (!(obj instanceof JSONObject)) return obj;
        
        JSONObject serializedObj = (JSONObject) obj;

        if (type == Observable.class) return deserializeObservable(serializedObj);
        if (type == InvalidationListener.class) return deserializeInvalidationListener(serializedObj);
        if (type == Object.class) return deserializeObject(obj, serializedObj);
        if (type == PlayerUpdate.class) return deserializePlayerUpdate(serializedObj);
        if (type == DirectoryEntry.class) return deserializeDirectoryServiceEntry(serializedObj);
        if (type == InetAddress.class) return deserializeInetAddress(serializedObj);
        if (type == UUID.class) return deserializeUuid(serializedObj);

        return obj;
    }

    private Object deserializeObservable(JSONObject serializedObj) {
        if (isType(DirectoryEntry.class, serializedObj)) return deserializeDirectoryServiceEntry(serializedObj);
        if (isType(PlayerUpdate.class, serializedObj)) return deserializePlayerUpdate(serializedObj);

        return serializedObj;
    }

    private Object deserializeInvalidationListener(JSONObject serializedObj) {
        if (isType(IRemoteRoomsFactory.class, serializedObj)) return deserializeRemoteRoomsFactoryClient(serializedObj);
        if (isType(IUpdateChannel.class, serializedObj)) return deserializePlayerUpdateChannelClient(serializedObj);

        return serializedObj;
    }

    private Object deserializePlayerUpdateChannelClient(JSONObject serializedObj) {
        return new PlayerUpdateChannelClient(new RpcClientStub(new RpcMarshaller(
                new JsonRpcMessageApi(this),
                new RpcSender(new InetSocketAddress(
                        serializedObj.getString("ip"),
                        serializedObj.getInt("port"))),
                rcs))); // TODO
    }

    private Object deserializeRemoteRoomsFactoryClient(JSONObject serializedObj) {
        return new RemoteRoomsFactoryClient(new RpcClientStub(new RpcMarshaller(
                new JsonRpcMessageApi(this),
                new RpcSender(new InetSocketAddress(
                        serializedObj.getString("ip"),
                        serializedObj.getInt("port"))),
                rcs))); // TODO
    }

    private Object deserializeObject(Object obj, JSONObject serializedObj) {
        try {
            return deserialize(obj, Class.forName(serializedObj.getString("type")));
        } catch (ClassNotFoundException e) {
            return obj;
        }
    }
    
    private Object deserializePlayerUpdate(JSONObject serializedObj) {
        return new PlayerUpdate(
                Direction.valueOf(serializedObj.getString("movingDirection")),
                serializedObj.getBoolean("pauseGame"),
                serializedObj.getBoolean("dead"),
                serializedObj.getInt("version")
        );
    }

    private Object deserializeDirectoryServiceEntry(JSONObject serializedObj) {
        return new DirectoryEntry(
                UUID.fromString(serializedObj.getString("providerId")),
                UUID.fromString(serializedObj.getString("serviceId")),
                new InetSocketAddress(
                        serializedObj.getString("address"),
                        serializedObj.getInt("port")),
                serializedObj.getBoolean("reachable")
        );
    }

    private Object deserializeInetAddress(JSONObject serializedObj) {
        try {
            return InetAddress.getByName(serializedObj.getString("address"));
        } catch (UnknownHostException e) {
            return serializedObj;
        }
    }

    private UUID deserializeUuid(JSONObject serializedObj) {
        return UUID.fromString(serializedObj.getString("uuid"));
    }

    private boolean isType(Class<?> clazz, JSONObject serializedObj) {
        return serializedObj.getString("type").equals(clazz.getName());
    }
}
