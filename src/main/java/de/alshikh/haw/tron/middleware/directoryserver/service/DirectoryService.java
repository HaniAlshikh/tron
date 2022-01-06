package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import javafx.beans.InvalidationListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DirectoryService implements IDirectoryService {

    private final List<InvalidationListener> listeners = new ArrayList<>();

    private final ConcurrentHashMap<Integer, DirectoryServiceEntry> serviceRegistry;

    public DirectoryService(){
        serviceRegistry = new ConcurrentHashMap<>();
    }

    @Override
    public void register(DirectoryServiceEntry directoryServiceEntry) {
        serviceRegistry.put(directoryServiceEntry.hashCode(), directoryServiceEntry);
        directoryServiceEntry.setListeners(listeners);
        directoryServiceEntry.publishUpdate();

        // TODO: make it observable
        System.out.println(this);
    }

    @Override
    public void unregister(DirectoryServiceEntry directoryServiceEntry) {
        directoryServiceEntry = serviceRegistry.remove(directoryServiceEntry.hashCode());
        directoryServiceEntry.setReachable(false);
        directoryServiceEntry.publishUpdate();

        // TODO: make it observable
        System.out.println(this);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
        serviceRegistry.values().forEach(DirectoryServiceEntry::publishUpdate);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public String toString() {
        return serviceRegistry.toString();
    }
}
