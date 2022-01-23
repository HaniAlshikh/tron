package de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service;

import de.alshikh.haw.tron.app.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.app.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.models.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelClient;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelServer;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service.data.datatypes.RemoteRoom;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRPCServerStub;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;

import java.util.UUID;

public class RemoteRoomsFactory implements IRemoteRoomsFactory, ListChangeListener<IRoom> {

    UUID playerId;
    ILobbyModel lobbyModel;
    IRPCServerStub rpcServer;
    IDirectoryService directoryService;
    IRpcMessageApi rpcMessageApi;
    IRpcCallbackService rpcCallbackService;

    public RemoteRoomsFactory(UUID playerId, ILobbyModel lobbyModel, IRPCServerStub rpcServer, IDirectoryService directoryService, IRpcMessageApi rpcMessageApi, IRpcCallbackService rpcCallbackService) {
        this.playerId = playerId;
        this.lobbyModel = lobbyModel;
        this.rpcServer = rpcServer;
        this.directoryService = directoryService;
        this.rpcMessageApi = rpcMessageApi;
        this.rpcCallbackService = rpcCallbackService;

        lobbyModel.getRoomsList().addListener((ListChangeListener<IRoom>) this);
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof DirectoryEntry) {
            DirectoryEntry e = (DirectoryEntry) observable;
            if (isPlayerUpdateChannel(e) && !isLocalRoom(e) && !isOwnUpdateChannel(e)) {
                Platform.runLater(() -> {
                    if (e.isReachable())
                        lobbyModel.addRoom(new RemoteRoom(new PlayerUpdateChannelClient(
                                new RpcClientStub(new RpcMarshaller(
                                        rpcMessageApi, new RpcSender(e.getServiceAddress()), rpcCallbackService))),
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
                    directoryService.register(new DirectoryEntry(playerId, PlayerUpdateChannelClient.serviceId, rpcServer.getRpcReceiver().getServerAddress()));
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
                    directoryService.unregister(new DirectoryEntry(playerId, PlayerUpdateChannelClient.serviceId, rpcServer.getRpcReceiver().getServerAddress()));
                }
            });
        }
    }

    private PlayerUpdateChannelClient createGuestUpdateChannelRpcClient(IUpdateChannel guestUpdateChannel) {
        rpcServer.register(new PlayerUpdateChannelServer(guestUpdateChannel));
        return new PlayerUpdateChannelClient(new RpcClientStub(new RpcMarshaller(
                rpcMessageApi,
                new RpcSender(rpcServer.getRpcReceiver().getServerAddress()),
                rpcCallbackService
        )));
    };

    private boolean isLocalRoom(DirectoryEntry e) {
        return e.getServiceAddress().getAddress().equals(rpcServer.getRpcReceiver().getServerAddress().getAddress());
    }

    private boolean isPlayerUpdateChannel(DirectoryEntry e) {
        return e.getServiceId().equals(PlayerUpdateChannelClient.serviceId);
    }


    private boolean isOwnUpdateChannel(DirectoryEntry entry) {
        return entry.getServiceAddress().getPort() == rpcServer.getRpcReceiver().getServerAddress().getPort();
    }
}
