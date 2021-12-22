package de.alshikh.haw.tron.client.models.lobby.datatypes;

import de.alshikh.haw.tron.client.controllers.game.IGameController;

import java.util.UUID;

public class Room {

    private final UUID uuid;
    private final String name;
    private final IGameController hostController;

    public Room(UUID uuid, String name, IGameController hostController) {
        this.uuid = uuid;
        this.name = name;
        this.hostController = hostController;
    }

    public UUID getUuid() {
        return uuid;
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
