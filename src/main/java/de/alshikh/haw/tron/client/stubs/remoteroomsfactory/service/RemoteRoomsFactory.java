package de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelClient;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelServer;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.data.datatypes.RemoteRoom;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;

import java.util.UUID;

public class RemoteRoomsFactory implements IRemoteRoomsFactory, ListChangeListener<IRoom> {

    UUID playerId;
    ILobbyModel lobbyModel;
    IRPCServer rpcServer;
    IDirectoryService directoryService;
    JsonRpcSerializer jsonRpcSerializer;

    public RemoteRoomsFactory(UUID playerId, ILobbyModel lobbyModel, IRPCServer rpcServer, IDirectoryService directoryService, JsonRpcSerializer jsonRpcSerializer) {
        this.playerId = playerId;
        this.lobbyModel = lobbyModel;
        this.rpcServer = rpcServer;
        this.directoryService = directoryService;
        this.jsonRpcSerializer = jsonRpcSerializer;

        lobbyModel.getRoomsList().addListener((ListChangeListener<IRoom>) this);
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof DirectoryServiceEntry) {
            DirectoryServiceEntry e = (DirectoryServiceEntry) observable;
            if (!isLocalRoom(e) &&
                    isPlayerUpdateChannel(e) && !isOwnUpdateChannel(e)) {
                Platform.runLater(() -> {
                    if (e.isReachable())
                        lobbyModel.addRoom(new RemoteRoom(new PlayerUpdateChannelClient(
                                new JsonRpcClient(e.getServiceAddress(), jsonRpcSerializer)
                        ), this::createGuestUpdateChannelRpcClient));
                    else
                        lobbyModel.removeRoom(e.getId());
                });
            }
        }
    }

    @Override
    public void onChanged(Change<? extends IRoom> change) {
        while (change.next()) {
            change.getAddedSubList().forEach(r -> {
                if (r.getUuid().equals(playerId)) { // owen room
                    rpcServer.register(new PlayerUpdateChannelServer(r.getHostUpdateChannel()));
                    directoryService.register(new DirectoryServiceEntry(PlayerUpdateChannelClient.serviceId, rpcServer.getSocketAddress()));
                }
            });

            change.getRemoved().forEach(r -> {
                if (r.getUuid().equals(playerId)) { // owen room
                    rpcServer.unregister(PlayerUpdateChannelServer.serviceId);
                    directoryService.unregister(new DirectoryServiceEntry(PlayerUpdateChannelClient.serviceId, rpcServer.getSocketAddress()));
                }
            });
        }
    }

    private PlayerUpdateChannelClient createGuestUpdateChannelRpcClient(IUpdateChannel guestUpdateChannel) {
        rpcServer.register(new PlayerUpdateChannelServer(guestUpdateChannel));
        return new PlayerUpdateChannelClient(new JsonRpcClient(rpcServer.getSocketAddress(), jsonRpcSerializer));
    };

    private boolean isLocalRoom(DirectoryServiceEntry e) {
        return e.getServiceAddress().getAddress().equals(rpcServer.getSocketAddress().getAddress());
    }

    private boolean isPlayerUpdateChannel(DirectoryServiceEntry e) {
        return e.getId().equals(PlayerUpdateChannelClient.serviceId);
    }


    private boolean isOwnUpdateChannel(DirectoryServiceEntry entry) {
        return entry.getServiceAddress().getPort() == rpcServer.getSocketAddress().getPort();
    }
}
