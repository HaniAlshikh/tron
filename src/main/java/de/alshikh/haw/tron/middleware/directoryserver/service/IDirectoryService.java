package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import javafx.beans.InvalidationListener;

import java.util.UUID;

// TODO: make it publisher
public interface IDirectoryService {
    void register(DirectoryServiceEntry directoryServiceEntry);

    void unregister(DirectoryServiceEntry directoryServiceEntry);

    // TODO: publisher subscriber?
    void addListenerTo(UUID serviceId, InvalidationListener listener);

    void removeListenerForm(UUID serviceId, InvalidationListener listener);
}
