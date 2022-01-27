package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import javafx.beans.InvalidationListener;

import java.util.UUID;

public interface IDirectoryService {
    void register(IDirectoryEntry directoryEntry);

    void unregister(IDirectoryEntry directoryEntry);

    void addListenerTo(UUID serviceId, InvalidationListener listener);
}
