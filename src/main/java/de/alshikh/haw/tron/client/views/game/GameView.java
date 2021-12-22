package de.alshikh.haw.tron.client.views.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.models.game.data.entities.Player;
import de.alshikh.haw.tron.client.views.game.overlayes.StartMenu;
import de.alshikh.haw.tron.client.views.game.overlayes.WatingMenu;
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
    public void showStartMenu(StringProperty playerName, String message,
              EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler) {
        StartMenu startMenu = new StartMenu("menu.css");
        startMenu.getTxtPlayerName().textProperty().bindBidirectional(playerName);
        startMenu.getLblMessage().textProperty().set(message);
        startMenu.getBtnStart().setOnAction(startBtnHandler);
        startMenu.getBtnJoin().setOnAction(joinBtnHandler);
        view.registerOverlay("start", startMenu);
        view.showOverlay("start");
    }

    @Override
    public void showWaitingMenu(EventHandler<ActionEvent> cancelBtnHandler) {
        WatingMenu watingMenu = new WatingMenu("menu.css");
        watingMenu.getBtnCancel().setOnAction(cancelBtnHandler);
        view.registerOverlay("wait", watingMenu);
        view.init();
        view.showOverlay("wait");
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
