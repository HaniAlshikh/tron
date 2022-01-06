package de.alshikh.haw.tron.client.stubs;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.IRemoteRoomsFactory;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryClient;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.UUID;

public class TronJsonRpcSerializer extends JsonRpcSerializer {

    @Override
    public Object deserialize(Object obj, Class<?> type) {
        if (type == InvalidationListener.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;

            if (serializedObj.getString("type").equals(IRemoteRoomsFactory.class.getName())) {
                obj = new RemoteRoomsFactoryClient(new JsonRpcClient(
                        new InetSocketAddress(serializedObj.getString("ip"), serializedObj.getInt("port")),
                        this));
            }

            if (serializedObj.getString("type").equals(IUpdateChannel.class.getName())) {
                obj = new PlayerUpdateChannelClient(new JsonRpcClient(
                        new InetSocketAddress(serializedObj.getString("ip"), serializedObj.getInt("port")),
                        this));
            }
        }

        if (type == Observable.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;

            if (serializedObj.getString("type").equals(DirectoryServiceEntry.class.getName())) {
                obj = new DirectoryServiceEntry(
                        UUID.fromString(serializedObj.getString("serviceId")),
                        new InetSocketAddress(
                                serializedObj.getString("address"),
                                serializedObj.getInt("port")
                        )
                );
            }

            if (serializedObj.getString("type").equals(PlayerUpdate.class.getName())) {
                obj = new PlayerUpdate(
                        Direction.valueOf(serializedObj.getString("movingDirection")),
                        serializedObj.getBoolean("pauseGame"),
                        serializedObj.getBoolean("dead"),
                        serializedObj.getInt("version")
                );
            }
        }

        if (type == PlayerUpdate.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;
            obj = new PlayerUpdate(
                    Direction.valueOf(serializedObj.getString("movingDirection")),
                    serializedObj.getBoolean("pauseGame"),
                    serializedObj.getBoolean("dead"),
                    serializedObj.getInt("version")
            );
        }

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

        if (type == UUID.class && obj instanceof JSONObject) {
            JSONObject serializedObj = (JSONObject) obj;
            obj = UUID.fromString(serializedObj.getString("uuid"));
        }

        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        if (obj instanceof IUpdateChannel && obj instanceof IRpcAppClientStub) {
            IRpcAppClientStub clientStub = (IRpcAppClientStub) obj;
            JSONObject serializedObj = new JSONObject();
            // TODO: type can be inferred from the method
            serializedObj.put("type", IUpdateChannel.class.getName());
            serializedObj.put("ip", clientStub.getRpcClient().getServerAddress().getAddress().getHostAddress());
            serializedObj.put("port", clientStub.getRpcClient().getServerAddress().getPort());
            obj = serializedObj;
        }

        if (obj instanceof IRemoteRoomsFactory && obj instanceof IRpcAppClientStub) {
            IRpcAppClientStub clientStub = (IRpcAppClientStub) obj;
            JSONObject serializedObj = new JSONObject();
            // TODO: type can be inferred from the method
            serializedObj.put("type", IRemoteRoomsFactory.class.getName());
            serializedObj.put("ip", clientStub.getRpcClient().getServerAddress().getAddress().getHostAddress());
            serializedObj.put("port", clientStub.getRpcClient().getServerAddress().getPort());
            obj = serializedObj;
        }

        if (obj instanceof PlayerUpdate) {
            PlayerUpdate playerUpdate = (PlayerUpdate) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", PlayerUpdate.class.getName());
            serializedObj.put("movingDirection", playerUpdate.getMovingDirection().name());
            serializedObj.put("pauseGame", playerUpdate.pauseGame());
            serializedObj.put("dead", playerUpdate.isDead());
            serializedObj.put("version", playerUpdate.getVersion());
            obj = serializedObj;
        }

        if (obj instanceof DirectoryServiceEntry) {
            DirectoryServiceEntry e = (DirectoryServiceEntry) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", DirectoryServiceEntry.class.getName());
            serializedObj.put("serviceId", e.getId());
            serializedObj.put("address", e.getServiceAddress().getAddress().getHostAddress());
            serializedObj.put("port", e.getServiceAddress().getPort());
            obj = serializedObj;
        }

        if (obj instanceof UUID) {
            UUID uuid = (UUID) obj;
            JSONObject serializedObj = new JSONObject();
            serializedObj.put("type", UUID.class.getName());
            serializedObj.put("uuid", uuid.toString());
            obj = serializedObj;
        }

        return obj;
    }
}
