package de.alshikh.haw.tron.app;

import de.alshikh.haw.tron.app.controllers.game.GameController;
import de.alshikh.haw.tron.app.controllers.game.IGameController;
import de.alshikh.haw.tron.app.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.app.controllers.lobby.LobbyController;
import de.alshikh.haw.tron.app.models.game.GameModel;
import de.alshikh.haw.tron.app.models.game.IGameModel;
import de.alshikh.haw.tron.app.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.models.lobby.LobbyModel;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelServer;
import de.alshikh.haw.tron.app.stubs.TronJsonRpcSerializationApi;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service.IRemoteRoomsFactory;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service.RemoteRoomsFactory;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryClient;
import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryServer;
import de.alshikh.haw.tron.app.views.game.GameView;
import de.alshikh.haw.tron.app.views.game.IGameView;
import de.alshikh.haw.tron.app.views.lobby.ILobbyView;
import de.alshikh.haw.tron.app.views.lobby.LobbyView;
import de.alshikh.haw.tron.app.views.view_library.ITronView;
import de.alshikh.haw.tron.app.views.view_library.TronView;
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
import javafx.beans.InvalidationListener;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class TronGame implements Runnable {

    public final static String VIEW_CONFIG_FILE = "view.properties";

    private final ExecutorService es;
    private final boolean distributed;

    public TronGame(ExecutorService es, boolean distributed) {
        this.es = es;
        this.distributed = distributed;
    }

    @Override
    public void run() {
        try {
            ITronView baseView = new TronView(VIEW_CONFIG_FILE);

            // TODO: find a pattern to have multiple model instances
            //  as this is causing a problem when adding a remote room
            //  where all instances will try to add the same room over and over again
            ILobbyModel lobbyModel = LobbyModel.getInstance();
            ILobbyView lobbyView = new LobbyView(baseView);
            ILobbyController lobbyController = new LobbyController(lobbyModel, lobbyView);

            IGameModel gameModel = new GameModel();
            IGameView gameView = new GameView(baseView);
            IGameController gameController = new GameController(gameModel, gameView, lobbyController, es);

            gameController.showStartMenu("Ready?");

            Stage stage = new Stage();
            stage.setTitle("TRON - Light Cycles");
            stage.setScene(gameView.getScene());
            stage.show();

            if (distributed)
                es.execute(() -> bootUpMiddleware(gameModel.getPlayer().getId(), lobbyModel));

            stage.setOnCloseRequest(e -> gameController.closeGame());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: how to handle user starting a game while the middleware is booting up
    public void bootUpMiddleware(UUID playerId, ILobbyModel lobbyModel) {
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new TronJsonRpcSerializationApi());

        // rpcServer
        IRPCServerStub rpcServer = new RpcServerStub(rpcMessageApi);
        // TODO: maybe replace with ExecutorService (what if we have one core only)
        // TODO: dose it make sense to have a server per service (now it's sever per instance)
        new Thread(() -> rpcServer.getRpcReceiver().start()).start();

        // callback service
        IRpcCallbackService rpcCallbackService = new RpcCallbackService(rpcServer.getRpcReceiver().getServerAddress());
        IRpcCallback rpcCallback = new RpcCallback(rpcCallbackService);
        IRpcAppServerStub callbackServer = new RpcCallbackServer(rpcCallback);
        rpcServer.register(callbackServer);

        // TODO: fix this
        rpcMessageApi.getRpcSerializer().setR(rpcCallbackService);

        // directoryService
        InetSocketAddress directoryServiceAddress = DirectoryDiscoveryClient.discover();
        IDirectoryService dsc = new DirectoryServiceClient(
                new RpcClientStub(new RpcMarshaller(
                        rpcMessageApi, new RpcSender(directoryServiceAddress), rpcCallbackService)));

        // remoteRoomsFactory
        IRemoteRoomsFactory remoteRoomsFactory = new RemoteRoomsFactory(playerId, lobbyModel, rpcServer, dsc, rpcMessageApi, rpcCallbackService);
        IRpcAppServerStub remoteRoomsFactoryServer = new RemoteRoomsFactoryServer(remoteRoomsFactory);
        rpcServer.register(remoteRoomsFactoryServer);
        IRpcAppClientStub remoteRoomsFactoryClient = new RemoteRoomsFactoryClient(
                new RpcClientStub(
                        new RpcMarshaller(rpcMessageApi,
                        new RpcSender(rpcServer.getRpcReceiver().getServerAddress()),
                                rpcCallbackService)));
        dsc.addListenerTo(PlayerUpdateChannelServer.serviceId, (InvalidationListener) remoteRoomsFactoryClient);
    }
}
