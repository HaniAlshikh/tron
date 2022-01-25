package de.alshikh.haw.tron;

import de.alshikh.haw.tron.app.TronGame;
import de.alshikh.haw.tron.app.models.lobby.LobbyModel;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelServer;
import de.alshikh.haw.tron.app.stubs.RemoteRoomsFactoryClient;
import de.alshikh.haw.tron.app.stubs.RemoteRoomsFactoryServer;
import de.alshikh.haw.tron.app.stubs.helpers.TronJsonRpcSerializationApi;
import de.alshikh.haw.tron.app.stubs.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.app.stubs.helpers.remoteroomsfactory.RemoteRoomsFactory;
import de.alshikh.haw.tron.app.views.manager.overlays.LoadingMenu;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceClient;
import de.alshikh.haw.tron.middleware.discoveryservice.DirectoryDiscoveryClient;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackServer;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRPCServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.concurrent.Task;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class DistributedTronGame implements Runnable {

    ITronView gameView;

    public DistributedTronGame(ITronView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run() {
        TronGame tronGame = new TronGame(gameView, new LobbyModel());

        LoadingMenu loadingMenu = new LoadingMenu(Config.MENU_CSS);
        gameView.registerOverlay("loadingMenu", loadingMenu);
        gameView.init();
        gameView.showOverlay("loadingMenu");

        Task<Void> bootUpMiddlewareTask = new Task<>() {
            @Override public Void call() throws Exception {
                bootUpMiddleware(tronGame, loadingMenu);
                return null;
            }
        };
        new Thread(bootUpMiddlewareTask).start();
        tronGame.run();

        bootUpMiddlewareTask.setOnSucceeded(e -> {gameView.init(); gameView.showOverlay("start");});
        bootUpMiddlewareTask.setOnRunning(e -> {gameView.init(); gameView.showOverlay("loadingMenu");});
    }

    public void bootUpMiddleware(TronGame tronGame, LoadingMenu loadingMenu) throws ExecutionException, InterruptedException {
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "Initializing..."));
        TronJsonRpcSerializationApi tronJsonRpcSerializationApi = new TronJsonRpcSerializationApi();
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(tronJsonRpcSerializationApi);

        // rpcServer
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "Starting RPC Server..."));
        IRPCServerStub rpcServer = new RpcServerStub(rpcMessageApi);
        new Thread(() -> rpcServer.getRpcReceiver().start()).start();

        // callback service
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "Setting up callback service..."));
        IRpcCallbackService rpcCallbackService = new RpcCallbackService(rpcServer.getRpcReceiver().getServerAddress());
        IRpcCallback rpcCallback = new RpcCallback(rpcCallbackService);
        IRpcAppServerStub callbackServer = new RpcCallbackServer(rpcCallback);
        tronJsonRpcSerializationApi.setRpcCallbackService(rpcCallbackService);
        rpcServer.register(callbackServer);

        // directoryService
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "looking up directory server..."));
        InetSocketAddress directoryServiceAddress = DirectoryDiscoveryClient.discover();
        IDirectoryService dsc = new DirectoryServiceClient(new RpcClientStub(
                new RpcMarshaller(rpcMessageApi,
                new RpcSender(directoryServiceAddress), rpcCallbackService)));

        // remoteRoomsFactory
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "binding lobby to directory server..."));
        IRemoteRoomsFactory remoteRoomsFactory = new RemoteRoomsFactory(tronGame.getId(), tronGame.getLobbyModel(), rpcServer, dsc, rpcMessageApi, rpcCallbackService);
        IRpcAppServerStub remoteRoomsFactoryServer = new RemoteRoomsFactoryServer(remoteRoomsFactory);
        rpcServer.register(remoteRoomsFactoryServer);
        IRpcAppClientStub remoteRoomsFactoryClient = new RemoteRoomsFactoryClient(new RpcClientStub(
                        new RpcMarshaller(rpcMessageApi,
                        new RpcSender(rpcServer.getRpcReceiver().getServerAddress()), rpcCallbackService)));
        dsc.addListenerTo(PlayerUpdateChannelServer.serviceId, (InvalidationListener) remoteRoomsFactoryClient);
    }
}
