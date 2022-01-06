package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.controllers.game.GameController;
import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.client.controllers.lobby.LobbyController;
import de.alshikh.haw.tron.client.models.game.GameModel;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.LobbyModel;
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
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceClient;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.JsonRpcServer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

public class DistributedTronGame implements Runnable {

    public final static String VIEW_CONFIG_FILE = "view.properties";

    private final ExecutorService es;

    public DistributedTronGame(ExecutorService es) {
        this.es = es;
    }

    @Override
    public void run() {
        try {
            ITronView baseView = new TronView(VIEW_CONFIG_FILE);

            ILobbyModel lobbyModel = LobbyModel.getInstance();
            ILobbyView lobbyView = new LobbyView(baseView);
            ILobbyController lobbyController = new LobbyController(lobbyView);

            IGameModel gameModel = new GameModel();
            IGameView gameView = new GameView(baseView);

            IGameController gameController = new GameController(gameModel, gameView, lobbyController, es);
            gameController.showStartMenu("Ready?");


            // RPC

            // rpcServer
            //InetSocketAddress socketAddress = new InetSocketAddress(getRandomFreePort());
            //InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), getRandomFreePort());
            JsonRpcSerializer jsonRpcSerializer = new TronJsonRpcSerializer();
            IRPCServer rpcServer = new JsonRpcServer(jsonRpcSerializer);
            // TODO: maybe replace with ExecutorService (what if we have one core only)
            new Thread(rpcServer::start).start();

            // directoryService
            //InetSocketAddress directoryServiceAddress = new DirectoryServiceListener().getFirst();
            IDirectoryService dsc = new DirectoryServiceClient(new JsonRpcClient(
                    new InetSocketAddress("192.168.2.106", 58698), jsonRpcSerializer
            ));

            // remoteRoomsFactory
            IRemoteRoomsFactory remoteRoomsFactory = new RemoteRoomsFactory(gameModel.getPlayer().getId(), lobbyModel, rpcServer, dsc, jsonRpcSerializer);
            IRpcAppServerStub remoteRoomsFactoryServer = new RemoteRoomsFactoryServer(remoteRoomsFactory);
            rpcServer.register(remoteRoomsFactoryServer);
            System.out.println(rpcServer.getSocketAddress());
            dsc.addListener(new RemoteRoomsFactoryClient(new JsonRpcClient(rpcServer.getSocketAddress(), jsonRpcSerializer)));

            // configure and show stage
            Stage stage = new Stage();
            stage.setTitle("TRON - Light Cycles");
            stage.setScene(gameView.getScene());
            stage.show();
            stage.setOnCloseRequest(e -> gameController.closeGame());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
