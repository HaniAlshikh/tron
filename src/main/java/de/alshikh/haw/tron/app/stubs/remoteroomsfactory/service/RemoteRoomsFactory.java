package de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
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
            if (isPlayerUpdateChannel(e) && !isOwnUpdateChannel(e)) {
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
                if (r.getId().equals(playerId)) { // owen room
                    rpcServer.register(new PlayerUpdateChannelServer(r.getHostUpdateChannel()));
                    directoryService.register(new DirectoryEntry(playerId, PlayerUpdateChannelClient.serviceId, rpcServer.getRpcReceiver().getServerAddress()));
                }
            });

            change.getRemoved().forEach(r -> {
                if (r.getId().equals(playerId)) { // owen room
                    directoryService.unregister(new DirectoryEntry(playerId, PlayerUpdateChannelClient.serviceId, rpcServer.getRpcReceiver().getServerAddress()));
                }
            });
        }
    }

    private PlayerUpdateChannelClient createGuestUpdateChannelRpcClient(IPlayerUpdateChannel guestUpdateChannel) {
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
