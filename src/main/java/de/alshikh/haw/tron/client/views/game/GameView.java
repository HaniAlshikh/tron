package de.alshikh.haw.tron.client.views.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Player;
import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.views.game.overlayes.StartMenu;
import de.alshikh.haw.tron.client.views.game.overlayes.WatingMenu;
import de.alshikh.haw.tron.client.views.game.overlayes.WinnerMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Arrays;

public class GameView implements IGameView {


    private final ITronView view;

    public GameView(ITronView view) throws IOException {
        this.view = view;
    }

    @Override
    public void showGame(Game game) {
        for (Player p : Arrays.asList(game.getPlayer(), game.getOpponent())) {
            try {
                view.draw(p.getBike().getCoordinates(), p.getBike().getColor());
            } catch (IllegalArgumentException e) { // went out of bound
                p.die();
            }
        }
    }

    @Override
    public void showStartMenu(EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler,
                              StringProperty playerName) {
        StartMenu startMenu = new StartMenu("menu.css");
        startMenu.getBtnStart().setOnAction(startBtnHandler);
        startMenu.getBtnJoin().setOnAction(joinBtnHandler);
        startMenu.getTxtPlayerName().textProperty().bindBidirectional(playerName);
        view.registerOverlay("start", startMenu);
        view.init();
        view.showOverlay("start");
    }

    @Override
    public void showWaitingMenu(EventHandler<ActionEvent> cancelBtnHandler) {
        WatingMenu watingMenu = new WatingMenu("menu.css");
        watingMenu.getBtnCancel().setOnAction(cancelBtnHandler);
        view.registerOverlay("wait", watingMenu);
        view.showOverlay("wait");
    }

    @Override
    public void showWinnerMenu(String message, EventHandler<ActionEvent> startBtnHandler) {
        WinnerMenu winnerMenu = new WinnerMenu("menu.css", message);
        winnerMenu.getBtnStart().setOnAction(startBtnHandler);
        view.registerOverlay("winner", winnerMenu);
        view.showOverlay("winner");
    }

    @Override
    public void reset() {
        view.init();
    }

    @Override
    public Scene getScene() {
        return view.getScene();
    }
}
