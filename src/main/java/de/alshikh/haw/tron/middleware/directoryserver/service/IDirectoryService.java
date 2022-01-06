package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import javafx.beans.Observable;

public interface IDirectoryService extends Observable {
    void register(DirectoryServiceEntry directoryServiceEntry);

    void unregister(DirectoryServiceEntry directoryServiceEntry);
}
