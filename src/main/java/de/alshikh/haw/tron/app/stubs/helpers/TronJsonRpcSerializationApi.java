package de.alshikh.haw.tron.app.stubs.helpers;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.app.models.game.data.entities.IPlayerUpdate;
import de.alshikh.haw.tron.app.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelClient;
import de.alshikh.haw.tron.app.stubs.RemoteRoomsFactoryCaller;
import de.alshikh.haw.tron.app.stubs.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
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

    private IRpcCallbackService rpcCallbackService;

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof IRpcCallerAppStub) return serializeIRpcAppClientStub((IRpcCallerAppStub) obj);
        if (obj instanceof IPlayerUpdate) return serializePlayerUpdate((IPlayerUpdate) obj);
        if (obj instanceof IDirectoryEntry) return serializeDirectoryServiceEntry((IDirectoryEntry) obj);
        if (obj instanceof InetAddress) return serializeInetAddress((InetAddress) obj);
        if (obj instanceof UUID) return serializeUUID((UUID) obj);

        return obj;
    }

    private JSONObject newSerializedObj(Class<?> clazz) {
        return new JSONObject()
                .put("type", clazz.getName());
    }

    private Object serializeIRpcAppClientStub(IRpcCallerAppStub rpcAppClientStub) {
        JSONObject serializedObj = null;
        if (rpcAppClientStub instanceof IPlayerUpdateChannel) serializedObj = newSerializedObj(IPlayerUpdateChannel.class);
        if (rpcAppClientStub instanceof IRemoteRoomsFactory) serializedObj = newSerializedObj(IRemoteRoomsFactory.class);

        if (serializedObj == null) return rpcAppClientStub;
        return serializedObj
                .put("ip", rpcAppClientStub.getRpcClientStub().getServerAddress().getAddress().getHostAddress())
                .put("port", rpcAppClientStub.getRpcClientStub().getServerAddress().getPort());
    }

    private JSONObject serializePlayerUpdate(IPlayerUpdate playerUpdate) {
        return newSerializedObj(PlayerUpdate.class)
                .put("movingDirection", playerUpdate.getMovingDirection().name())
                .put("dead", playerUpdate.isDead())
                .put("version", playerUpdate.getVersion());
    }

    private JSONObject serializeDirectoryServiceEntry(IDirectoryEntry directoryEntry) {
        return newSerializedObj(DirectoryEntry.class)
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
        if (type == IPlayerUpdate.class) return deserializePlayerUpdate(serializedObj);
        if (type == IDirectoryEntry.class) return deserializeDirectoryServiceEntry(serializedObj);
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
        if (isType(IPlayerUpdateChannel.class, serializedObj)) return deserializePlayerUpdateChannelClient(serializedObj);

        return serializedObj;
    }

    private Object deserializePlayerUpdateChannelClient(JSONObject serializedObj) {
        return new PlayerUpdateChannelClient(new RpcClientStub(new RpcMarshaller(
                new JsonRpcMessageApi(this),
                new RpcSender(new InetSocketAddress(
                        serializedObj.getString("ip"),
                        serializedObj.getInt("port"))),
                rpcCallbackService)));
    }

    private Object deserializeRemoteRoomsFactoryClient(JSONObject serializedObj) {
        return new RemoteRoomsFactoryCaller(new RpcClientStub(new RpcMarshaller(
                new JsonRpcMessageApi(this),
                new RpcSender(new InetSocketAddress(
                        serializedObj.getString("ip"),
                        serializedObj.getInt("port"))),
                rpcCallbackService)));
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

    public void setRpcCallbackService(IRpcCallbackService rpcCallbackService) {
        this.rpcCallbackService = rpcCallbackService;
    }
}
