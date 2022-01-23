package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
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

    private final ConcurrentLinkedQueue<DirectoryEntry> dib; // directory information base

    public DirectoryService(){
        this.serviceListeners = new ConcurrentHashMap<>();
        this.directoryListeners = new ConcurrentLinkedQueue<>();
        this.dib = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void register(DirectoryEntry directoryEntry) {
        dib.add(directoryEntry);
        publishServiceUpdate(directoryEntry);
        publishUpdate();
    }

    @Override
    public void unregister(DirectoryEntry directoryEntry) {
        if (dib.remove(directoryEntry)) { // known service
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

        // TODO: make serviceRegistry a map as well?
        executor.submit(() -> dib.stream()
                .filter(s -> s.getServiceId().equals(serviceId))
                .forEach(listener::invalidated));
    }

    private void publishServiceUpdate(DirectoryEntry directoryEntry) {
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

    public void publishUpdate() {
        directoryListeners.forEach(l -> l.invalidated(this));
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        directoryListeners.remove(listener);
    }

    // lookup a port based on service id and provider ip (for example rpc callback server port)
    //public int lookup(UUID serviceId, InetAddress serviceProviderAddress) {
    //    Optional<DirectoryEntry> entry = dib.stream()
    //            .filter(s -> s.getServiceId().equals(serviceId) &&
    //                    s.getServiceAddress().getAddress().equals(serviceProviderAddress))
    //            .findFirst();
    //
    //    return entry.map(s -> s.getServiceAddress().getPort()).orElse(-1);
    //}

    @Override
    public String toString() {
        return dib.toString();
    }

    public ConcurrentLinkedQueue<DirectoryEntry> getDib() {
        return dib;
    }
}
