package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: maybe lookup method (not really needed (rpc callback might be a use case))
public class DirectoryService implements IDirectoryService, Observable {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final ConcurrentHashMap<UUID, ConcurrentLinkedQueue<InvalidationListener>> serviceListeners;
    private final ConcurrentLinkedQueue<InvalidationListener> directoryListeners;

    private final ConcurrentLinkedQueue<IDirectoryEntry> dib; // directory information base

    public DirectoryService(){
        this.serviceListeners = new ConcurrentHashMap<>();
        this.directoryListeners = new ConcurrentLinkedQueue<>();
        this.dib = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void register(IDirectoryEntry directoryEntry) {
        dib.add(directoryEntry);
        publishServiceUpdate(directoryEntry);
        publishUpdate();
    }

    @Override
    public void unregister(IDirectoryEntry directoryEntry) {
        if (dib.remove(directoryEntry)) {
            directoryEntry.setReachable(false);
            publishServiceUpdate(directoryEntry);
            publishUpdate();
        }
    }

    // TODO: publisher subscriber?
    @Override
    public void addListenerTo(UUID serviceId, InvalidationListener listener) {
        serviceListeners.putIfAbsent(serviceId, new ConcurrentLinkedQueue<>());
        serviceListeners.get(serviceId).add(listener);

        executor.submit(() -> dib.stream()
                .filter(s -> s.getServiceId().equals(serviceId))
                .forEach(listener::invalidated));
    }

    private void publishServiceUpdate(IDirectoryEntry directoryEntry) {
        directoryEntry.setListeners(serviceListeners.getOrDefault(
                directoryEntry.getServiceId(), new ConcurrentLinkedQueue<>()));
        directoryEntry.publishUpdate();
    }

    @Override
    public void removeListenerForm(UUID serviceId, InvalidationListener listener) {
        if (serviceListeners.containsKey(serviceId))
            serviceListeners.get(serviceId).remove(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        directoryListeners.add(listener);
        listener.invalidated(this);
    }

    private void publishUpdate() {
        directoryListeners.forEach(l -> l.invalidated(this));
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        directoryListeners.remove(listener);
    }

    public ConcurrentLinkedQueue<IDirectoryEntry> getDib() {
        return dib;
    }

    @Override
    public String toString() {
        return dib.toString();
    }
}
