package de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelClient;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelServer;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.data.datatypes.RemoteRoom;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import de.alshikh.haw.tron.middleware.rpc.client.RpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
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
    IRpcMessageApi rpcMessageApi;

    public RemoteRoomsFactory(UUID playerId, ILobbyModel lobbyModel, IRPCServer rpcServer, IDirectoryService directoryService, IRpcMessageApi rpcMessageApi) {
        this.playerId = playerId;
        this.lobbyModel = lobbyModel;
        this.rpcServer = rpcServer;
        this.directoryService = directoryService;
        this.rpcMessageApi = rpcMessageApi;

        lobbyModel.getRoomsList().addListener((ListChangeListener<IRoom>) this);
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof DirectoryServiceEntry) {
            DirectoryServiceEntry e = (DirectoryServiceEntry) observable;
            if (isPlayerUpdateChannel(e) && !isLocalRoom(e) && !isOwnUpdateChannel(e)) {
                Platform.runLater(() -> {
                    if (e.isReachable())
                        lobbyModel.addRoom(new RemoteRoom(new PlayerUpdateChannelClient(
                                new RpcClient(e.getServiceAddress(), rpcMessageApi)),
                                this::createGuestUpdateChannelRpcClient));
                    else
                        lobbyModel.removeRoom(e.getProviderId());
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
                    directoryService.register(new DirectoryServiceEntry(playerId, PlayerUpdateChannelClient.serviceId, rpcServer.getSocketAddress()));
                }
            });

            change.getRemoved().forEach(r -> {
                if (r.getUuid().equals(playerId)) { // owen room
                    // TODO: we remove the "room" when the opponent joins or the player cancels the game
                    //  by unregistering the update channel service we "close" the channel
                    //  which means player and opponent can't communicate anymore
                    //  yet we have to close the channel
                    //  a better solution would be to bind the service to the opponent and only accept calls from him
                    //  but for now we comment this and leave the channel open (security is not a prio)
                    //rpcServer.unregister(PlayerUpdateChannelServer.serviceId);
                    directoryService.unregister(new DirectoryServiceEntry(playerId, PlayerUpdateChannelClient.serviceId, rpcServer.getSocketAddress()));
                }
            });
        }
    }

    private PlayerUpdateChannelClient createGuestUpdateChannelRpcClient(IUpdateChannel guestUpdateChannel) {
        rpcServer.register(new PlayerUpdateChannelServer(guestUpdateChannel));
        return new PlayerUpdateChannelClient(new RpcClient(rpcServer.getSocketAddress(), rpcMessageApi));
    };

    private boolean isLocalRoom(DirectoryServiceEntry e) {
        return e.getServiceAddress().getAddress().equals(rpcServer.getSocketAddress().getAddress());
    }

    private boolean isPlayerUpdateChannel(DirectoryServiceEntry e) {
        return e.getServiceId().equals(PlayerUpdateChannelClient.serviceId);
    }


    private boolean isOwnUpdateChannel(DirectoryServiceEntry entry) {
        return entry.getServiceAddress().getPort() == rpcServer.getSocketAddress().getPort();
    }
}
