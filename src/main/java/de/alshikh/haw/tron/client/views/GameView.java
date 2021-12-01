package de.alshikh.haw.tron.client.views;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.views.overlayes.StartMenu;
import de.alshikh.haw.tron.client.views.overlayes.WatingMenu;
import de.alshikh.haw.tron.client.views.overlayes.WinnerMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.io.IOException;

public class GameView implements IGameView {

    public final static String VIEW_CONFIG_FILE = "view.properties";

    private final ITronView view;

    public GameView() throws IOException {
        this.view = new TronView(VIEW_CONFIG_FILE);
    }

    @Override
    public void showGame(Player... players) {
        for (Player p : players) {
            try {
                view.draw(p.getBike().getTrail(), p.getBike().getColor().getFXColor());
            } catch (IllegalArgumentException e) { // went out of boundary
                p.die();
            }
        }
    }

    @Override
    public void showStartMenu(EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler) {
        StartMenu startMenu = new StartMenu("menu.css");
        startMenu.getBtnStart().setOnAction(startBtnHandler);
        startMenu.getBtnJoin().setOnAction(joinBtnHandler);
        view.registerOverlay("start", startMenu);
        view.init();
        view.showOverlay("start");
    }

    @Override
    public void showWaitingMenu() {
        WatingMenu watingMenu = new WatingMenu("menu.css");
        view.registerOverlay("wait", watingMenu);
        view.showOverlay("wait");
    }

    @Override
    public void showWinnerMenu(Player winner, EventHandler<ActionEvent> startBtnHandler) {
        WinnerMenu winnerMenu = new WinnerMenu("menu.css", winner);
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
