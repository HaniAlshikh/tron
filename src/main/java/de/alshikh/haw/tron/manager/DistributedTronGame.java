package de.alshikh.haw.tron.manager;

import de.alshikh.haw.tron.app.model.lobby.LobbyModel;
import de.alshikh.haw.tron.app.stub.PlayerUpdateChannelCallee;
import de.alshikh.haw.tron.app.stub.RemoteRoomsFactoryCallee;
import de.alshikh.haw.tron.app.stub.RemoteRoomsFactoryCaller;
import de.alshikh.haw.tron.app.stub.helpers.TronJsonRpcSerializationApi;
import de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.RemoteRoomsFactory;
import de.alshikh.haw.tron.manager.overlays.LoadingMenu;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DirectoryDiscoveryClient;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stub.DirectoryServiceCaller;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackCallee;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStubFactory;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.RpcUnmarshaller;
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
        RpcClientStubFactory.setRpcMessageApi(rpcMessageApi);

        // rpcServerStub
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "Starting RPC Server Stub..."));
        IRpcServerStub rpcServerStub = new RpcServerStub(new RpcReceiver(new RpcUnmarshaller(rpcMessageApi)));
        new Thread(() -> rpcServerStub.getRpcReceiver().start()).start();

        // callback service
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "Setting up callback service..."));
        IRpcCallbackService rpcCallbackService = new RpcCallbackService(rpcServerStub.getRpcReceiver().getServerAddress());
        IRpcCallback rpcCallback = new RpcCallback(rpcCallbackService);
        IRpcCalleeAppStub callbackServer = new RpcCallbackCallee(rpcCallback);
        RpcClientStubFactory.setRpcCallbackService(rpcCallbackService);
        rpcServerStub.register(callbackServer);

        // directoryService
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "looking up directory server..."));
        InetSocketAddress directoryServiceAddress = DirectoryDiscoveryClient.discover();
        IDirectoryService dsc = new DirectoryServiceCaller(RpcClientStubFactory.getRpcClientStub(directoryServiceAddress));

        // remoteRoomsFactory
        Platform.runLater(() -> loadingMenu.getProgressTxt().textProperty().setValue(
                "binding lobby to directory server..."));
        IRemoteRoomsFactory remoteRoomsFactory = new RemoteRoomsFactory(tronGame.getId(), tronGame.getLobbyModel(), rpcServerStub, dsc);
        IRpcCalleeAppStub remoteRoomsFactoryServer = new RemoteRoomsFactoryCallee(remoteRoomsFactory);
        rpcServerStub.register(remoteRoomsFactoryServer);
        IRpcCallerAppStub remoteRoomsFactoryClient = new RemoteRoomsFactoryCaller(RpcClientStubFactory.getRpcClientStub(rpcServerStub.getRpcReceiver().getServerAddress()));
        dsc.addListenerTo(PlayerUpdateChannelCallee.SERVICE_ID, (InvalidationListener) remoteRoomsFactoryClient);
    }
}
