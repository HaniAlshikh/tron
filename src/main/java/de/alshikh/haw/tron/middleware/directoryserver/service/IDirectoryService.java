package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import javafx.beans.InvalidationListener;

import java.util.UUID;

// TODO: make it publisher
public interface IDirectoryService {
    void register(IDirectoryEntry directoryEntry);

    void unregister(IDirectoryEntry directoryEntry);

    // TODO: publisher subscriber
    void addListenerTo(UUID serviceId, InvalidationListener listener);

    void removeListenerForm(UUID serviceId, InvalidationListener listener);
}
