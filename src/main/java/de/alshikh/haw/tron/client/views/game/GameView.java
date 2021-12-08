package de.alshikh.haw.tron.client.views.game;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.views.game.overlayes.StartMenu;
import de.alshikh.haw.tron.client.views.game.overlayes.WatingMenu;
import de.alshikh.haw.tron.client.views.game.overlayes.WinnerMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.io.IOException;

public class GameView implements IGameView {


    private final ITronView view;

    public GameView(ITronView view) throws IOException {
        this.view = view;
    }

    @Override
    public void showGame(Player... players) {
        for (Player p : players) {
            try {
                view.draw(p.getBike().getTrail(), p.getBike().getColor());
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
