package de.alshikh.haw.tron.client.views;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.views.overlayes.StartMenu;
import de.alshikh.haw.tron.client.views.overlayes.WinnerMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Set;

public class GameView implements IGameView {

    public final static String VIEW_CONFIG_FILE = "view.properties";

    private final ITronView view;

    public GameView() throws IOException {
        this.view = new TronView(VIEW_CONFIG_FILE);
    }

    @Override
    public void showGame(Set<Player> players) {
        for (Player p : players) {
            try {
                view.draw(p.getBike().getTrail(), p.getBike().getColor());
            } catch (IllegalArgumentException e) { // went out of boundary
                p.die();
            }
        }
    }

    @Override
    public void showStartMenu(EventHandler<ActionEvent> startBtnHandler) {
        StartMenu startMenu = new StartMenu("menu.css");
        startMenu.getBtnStart().setOnAction(startBtnHandler);
        view.registerOverlay("start", startMenu);
        view.init();
        view.showOverlay("start");
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
