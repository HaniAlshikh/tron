package de.alshikh.haw.tron.app.controllers.game.helpers;

import de.alshikh.haw.tron.app.models.game.data.entities.Player;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerUpdateChannel implements IUpdateChannel {

    Player player;
    IUpdater updater;
    Consumer<String> onAddedListener;

    public PlayerUpdateChannel(Player player, IUpdater updater, Consumer<String> onAddedListener) {
        this.player = player;
        this.updater = updater;
        this.onAddedListener = onAddedListener;
    }

    @Override
    public void addListener(InvalidationListener opponentUpdateChannel) {
        player.getUpdate().addListener(opponentUpdateChannel);
        // TODO: find a better way (this make no sense and only work for two players)
        onAddedListener.accept(((IUpdateChannel) opponentUpdateChannel).getName());
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        player.getUpdate().addListener(listener);
    }

    @Override
    public void invalidated(Observable observable) {
        updater.invalidated(observable);
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getId() {
        return player.getId();
    }
}
