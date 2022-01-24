package de.alshikh.haw.tron.app.controllers.game.helpers;

import de.alshikh.haw.tron.app.models.game.data.entities.Player;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerUpdateChannel implements IPlayerUpdateChannel {

    private final Player player;
    private final IGameUpdater gameUpdater;
    private final Consumer<String> gameStarter;

    public PlayerUpdateChannel(Player player, IGameUpdater gameUpdater, Consumer<String> gameStarter) {
        this.player = player;
        this.gameUpdater = gameUpdater;
        this.gameStarter = gameStarter;
    }

    @Override
    public void addListener(InvalidationListener opponentUpdateChannel) {
        player.getUpdate().addListener(opponentUpdateChannel);
        gameStarter.accept(((IPlayerUpdateChannel) opponentUpdateChannel).getPlayerName());
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        player.getUpdate().removeListener(listener);
    }

    @Override
    public void invalidated(Observable observable) {
        gameUpdater.invalidated(observable);
    }

    @Override
    public String getPlayerName() {
        return player.getName();
    }

    @Override
    public UUID getPlayerId() {
        return player.getId();
    }
}
