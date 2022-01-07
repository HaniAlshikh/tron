package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.controllers.game.GameController;
import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.client.controllers.lobby.LobbyController;
import de.alshikh.haw.tron.client.models.game.GameModel;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.LobbyModel;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelServer;
import de.alshikh.haw.tron.client.stubs.TronJsonRpcSerializer;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.IRemoteRoomsFactory;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.RemoteRoomsFactory;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryClient;
import de.alshikh.haw.tron.client.stubs.remoteroomsfactory.stubs.RemoteRoomsFactoryServer;
import de.alshikh.haw.tron.client.views.game.GameView;
import de.alshikh.haw.tron.client.views.game.IGameView;
import de.alshikh.haw.tron.client.views.lobby.ILobbyView;
import de.alshikh.haw.tron.client.views.lobby.LobbyView;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DirectoryDiscoveryClient;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceClient;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.JsonRpcServer;
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
            stage.setOnCloseRequest(e -> gameController.closeGame());

            if (distributed)
                es.execute(() -> bootUpMiddleware(gameModel.getPlayer().getId(), lobbyModel));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bootUpMiddleware(UUID playerId, ILobbyModel lobbyModel) {
        JsonRpcSerializer jsonRpcSerializer = new TronJsonRpcSerializer();

        // rpcServer
        IRPCServer rpcServer = new JsonRpcServer(jsonRpcSerializer);
        // TODO: maybe replace with ExecutorService (what if we have one core only)
        // TODO: dose it make sense to have a server per service (now it's sever per instance)
        new Thread(rpcServer::start).start();

        // directoryService
        InetSocketAddress directoryServiceAddress = DirectoryDiscoveryClient.discover();
        IDirectoryService dsc = new DirectoryServiceClient(new JsonRpcClient(directoryServiceAddress, jsonRpcSerializer));

        // remoteRoomsFactory
        IRemoteRoomsFactory remoteRoomsFactory = new RemoteRoomsFactory(playerId, lobbyModel, rpcServer, dsc, jsonRpcSerializer);
        IRpcAppServerStub remoteRoomsFactoryServer = new RemoteRoomsFactoryServer(remoteRoomsFactory);
        IRpcAppClientStub remoteRoomsFactoryClient = new RemoteRoomsFactoryClient(new JsonRpcClient(rpcServer.getSocketAddress(), jsonRpcSerializer));

        rpcServer.register(remoteRoomsFactoryServer);
        dsc.addListenerTo(PlayerUpdateChannelServer.serviceId, (InvalidationListener) remoteRoomsFactoryClient);
    }
}
