package de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory;

import de.alshikh.haw.tron.app.controller.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.model.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.model.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.app.stub.PlayerUpdateChannelCallee;
import de.alshikh.haw.tron.app.stub.PlayerUpdateChannelCaller;
import de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.data.datatypes.RemoteRoom;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStubFactory;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRpcServerStub;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;

import java.util.UUID;

public class RemoteRoomsFactory implements IRemoteRoomsFactory, ListChangeListener<IRoom> {

    private final UUID playerId;
    private final ILobbyModel lobbyModel;
    private final IRpcServerStub rpcServerStub;
    private final IDirectoryService directoryService;

    public RemoteRoomsFactory(UUID playerId, ILobbyModel lobbyModel, IRpcServerStub rpcServerStub, IDirectoryService directoryService) {
        this.playerId = playerId;
        this.lobbyModel = lobbyModel;
        this.rpcServerStub = rpcServerStub;
        this.directoryService = directoryService;

        lobbyModel.getRoomsList().addListener((ListChangeListener<IRoom>) this);
    }

    @Override
    public void invalidated(Observable observable) {
        if (!(observable instanceof IDirectoryEntry))
            return;
        IDirectoryEntry e = (IDirectoryEntry) observable;

        if (!isPlayerUpdateChannel(e) || isOwnUpdateChannel(e))
            return;

        Platform.runLater(() -> newPlayerUpdateChannelObserved(e));
    }

    private void newPlayerUpdateChannelObserved(IDirectoryEntry e) {
        if (e.isReachable())
            lobbyModel.addRoom(new RemoteRoom(new PlayerUpdateChannelCaller(
                    RpcClientStubFactory.getRpcClientStub(e.getServiceAddress())),
                    this::playerUpdateChannelAppStubFactory));
        else
            lobbyModel.removeRoom(e.getProviderId());
    }

    @Override
    public void onChanged(Change<? extends IRoom> change) {
        while (change.next()) {
            change.getAddedSubList().forEach(r -> {
                if (r.getId().equals(playerId)) { // owen room
                    rpcServerStub.register(new PlayerUpdateChannelCallee(r.getHostUpdateChannel()));
                    directoryService.register(new DirectoryEntry(playerId, PlayerUpdateChannelCaller.SERVICE_ID, rpcServerStub.getRpcReceiver().getServerAddress()));
                }
            });

            change.getRemoved().forEach(r -> {
                if (r.getId().equals(playerId)) { // owen room
                    directoryService.unregister(new DirectoryEntry(playerId, PlayerUpdateChannelCaller.SERVICE_ID, rpcServerStub.getRpcReceiver().getServerAddress()));
                }
            });
        }
    }

    private PlayerUpdateChannelCaller playerUpdateChannelAppStubFactory(IPlayerUpdateChannel playerUpdateChannel) {
        rpcServerStub.register(new PlayerUpdateChannelCallee(playerUpdateChannel));
        return new PlayerUpdateChannelCaller(
                RpcClientStubFactory.getRpcClientStub(rpcServerStub.getRpcReceiver().getServerAddress()));
    }

    private boolean isPlayerUpdateChannel(IDirectoryEntry e) {
        return e.getServiceId().equals(PlayerUpdateChannelCaller.SERVICE_ID);
    }

    private boolean isOwnUpdateChannel(IDirectoryEntry entry) {
        return entry.getServiceAddress().getPort() == rpcServerStub.getRpcReceiver().getServerAddress().getPort();
    }
}
