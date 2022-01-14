package de.alshikh.haw.tron.client.stubs;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.IRemoteRoomsFactory;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryClient;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.client.RpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.serialize.JsonRpcSerializer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

// TODO: refactor
public class TronJsonRpcSerializer extends JsonRpcSerializer {
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

        if (type == InvalidationListener.class) {
            if (serializedObj.getString("type").equals(IRemoteRoomsFactory.class.getName())) {
                return new RemoteRoomsFactoryClient(new RpcClient(
                        new InetSocketAddress(serializedObj.getString("ip"), serializedObj.getInt("port")),
                        new JsonRpcMessageApi(this)));
            }

            if (serializedObj.getString("type").equals(IUpdateChannel.class.getName())) {
                return new PlayerUpdateChannelClient(new RpcClient(
                        new InetSocketAddress(serializedObj.getString("ip"), serializedObj.getInt("port")),
                        new JsonRpcMessageApi(this)));
            }
        }

        if (type == Observable.class) {
            if (serializedObj.getString("type").equals(DirectoryServiceEntry.class.getName())) {
                return new DirectoryServiceEntry(
                        UUID.fromString(serializedObj.getString("providerId")),
                        UUID.fromString(serializedObj.getString("serviceId")),
                        new InetSocketAddress(
                                serializedObj.getString("address"),
                                serializedObj.getInt("port")
                        ),
                        serializedObj.getBoolean("reachable")
                );
            }

            if (serializedObj.getString("type").equals(PlayerUpdate.class.getName())) {
                return new PlayerUpdate(
                        Direction.valueOf(serializedObj.getString("movingDirection")),
                        serializedObj.getBoolean("pauseGame"),
                        serializedObj.getBoolean("dead"),
                        serializedObj.getInt("version")
                );
            }
        }

        if (type == PlayerUpdate.class) {
            return new PlayerUpdate(
                    Direction.valueOf(serializedObj.getString("movingDirection")),
                    serializedObj.getBoolean("pauseGame"),
                    serializedObj.getBoolean("dead"),
                    serializedObj.getInt("version")
            );
        }

        if (type == DirectoryServiceEntry.class) {
            return new DirectoryServiceEntry(
                    UUID.fromString(serializedObj.getString("providerId")),
                    UUID.fromString(serializedObj.getString("serviceId")),
                    new InetSocketAddress(
                            serializedObj.getString("address"),
                            serializedObj.getInt("port")
                    ),
                    serializedObj.getBoolean("reachable")
            );
        }

        if (type == UUID.class) {
            return UUID.fromString(serializedObj.getString("uuid"));
        }

        if (type == InetAddress.class) {
            try {
                return InetAddress.getByName(serializedObj.getString("address"));
            } catch (UnknownHostException e) {
                return null; // TODO;
            }
        }

        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof IRpcAppClientStub) {
            if (obj instanceof IUpdateChannel) {
                IRpcAppClientStub clientStub = (IRpcAppClientStub) obj;
                JSONObject serializedObj = new JSONObject();
                serializedObj.put("type", IUpdateChannel.class.getName());
                serializedObj.put("ip", clientStub.getRpcClient().getServerAddress().getAddress().getHostAddress());
                serializedObj.put("port", clientStub.getRpcClient().getServerAddress().getPort());
                return serializedObj;
            }

            if (obj instanceof IRemoteRoomsFactory) {
                IRpcAppClientStub clientStub = (IRpcAppClientStub) obj;
                JSONObject serializedObj = new JSONObject();
                serializedObj.put("type", IRemoteRoomsFactory.class.getName());
                serializedObj.put("ip", clientStub.getRpcClient().getServerAddress().getAddress().getHostAddress());
                serializedObj.put("port", clientStub.getRpcClient().getServerAddress().getPort());
                return serializedObj;
            }
        }

        if (obj instanceof PlayerUpdate) {
            PlayerUpdate playerUpdate = (PlayerUpdate) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", PlayerUpdate.class.getName());
            serializedObj.put("movingDirection", playerUpdate.getMovingDirection().name());
            serializedObj.put("pauseGame", playerUpdate.pauseGame());
            serializedObj.put("dead", playerUpdate.isDead());
            serializedObj.put("version", playerUpdate.getVersion());
            return serializedObj;
        }

        if (obj instanceof DirectoryServiceEntry) {
            DirectoryServiceEntry e = (DirectoryServiceEntry) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", DirectoryServiceEntry.class.getName());
            serializedObj.put("providerId", e.getProviderId());
            serializedObj.put("serviceId", e.getServiceId());
            serializedObj.put("address", e.getServiceAddress().getAddress().getHostAddress());
            serializedObj.put("port", e.getServiceAddress().getPort());
            serializedObj.put("reachable", e.isReachable());
            return serializedObj;
        }

        if (obj instanceof UUID) {
            UUID uuid = (UUID) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", UUID.class.getName());
            serializedObj.put("uuid", uuid.toString());
            return serializedObj;
        }

        if (obj instanceof InetAddress) {
            InetAddress address = (InetAddress) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", InetAddress.class.getName());
            serializedObj.put("address", address.getHostAddress());
            return serializedObj;
        }

        return obj;
    }
}
