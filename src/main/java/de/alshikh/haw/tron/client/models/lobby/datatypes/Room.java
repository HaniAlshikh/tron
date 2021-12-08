package de.alshikh.haw.tron.client.models.lobby.datatypes;

import de.alshikh.haw.tron.client.controllers.game.IGameController;

public class Room {

    private final String name;
    private final IGameController hostController;

    public Room(String name, IGameController hostController) {
        this.name = name;
        this.hostController = hostController;
    }

    public String getName() {
        return name;
    }

    public IGameController getHostController() {
        return hostController;
    }

    @Override
    public String toString() {
        return name;
    }
}
