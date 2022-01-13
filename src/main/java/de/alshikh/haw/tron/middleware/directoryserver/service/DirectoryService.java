package de.alshikh.haw.tron.middleware.directoryserver.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DirectoryService implements IDirectoryService, Observable {

    private final ConcurrentHashMap<UUID, ConcurrentLinkedQueue<InvalidationListener>> listenersRegistry;
    private final ConcurrentLinkedQueue<InvalidationListener> listeners;

    private final ConcurrentLinkedQueue<DirectoryServiceEntry> serviceRegistry;

    public DirectoryService(){
        this.listenersRegistry = new ConcurrentHashMap<>();
        this.listeners = new ConcurrentLinkedQueue<>();
        this.serviceRegistry = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void register(DirectoryServiceEntry directoryServiceEntry) {
        serviceRegistry.add(directoryServiceEntry);
        publishServiceUpdate(directoryServiceEntry);
        publishUpdate();
    }

    @Override
    public void unregister(DirectoryServiceEntry directoryServiceEntry) {
        if (serviceRegistry.remove(directoryServiceEntry)) { // known service
            directoryServiceEntry.setReachable(false);
            publishServiceUpdate(directoryServiceEntry);
            publishUpdate();
        }
    }

    // TODO: publisher subscriber?
    @Override
    public void addListenerTo(UUID serviceId, InvalidationListener listener) {
        listenersRegistry.putIfAbsent(serviceId, new ConcurrentLinkedQueue<>());
        listenersRegistry.get(serviceId).add(listener);
        // TODO: make serviceRegistry a map as well?
        serviceRegistry.stream().filter(s -> s.getServiceId().equals(serviceId)).forEach(listener::invalidated);

    }

    private void publishServiceUpdate(DirectoryServiceEntry directoryServiceEntry) {
        directoryServiceEntry.setListeners(listenersRegistry.getOrDefault(
                directoryServiceEntry.getServiceId(), new ConcurrentLinkedQueue<>()));
        directoryServiceEntry.publishUpdate();
    }

    @Override
    public void removeListenerForm(UUID serviceId, InvalidationListener listener) {
        if (listenersRegistry.containsKey(serviceId))
            listenersRegistry.get(serviceId).remove(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
        listener.invalidated(this);
    }

    public void publishUpdate() {
        listeners.forEach(l -> l.invalidated(this));
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    //public int getPort(UUID serviceId, InetAddress serviceProviderAddress) {
    //    Optional<DirectoryServiceEntry> entry = serviceRegistry.stream()
    //            .filter(s -> s.getServiceId().equals(serviceId) &&
    //                    s.getServiceAddress().getAddress().equals(serviceProviderAddress))
    //            .findFirst();
    //
    //    return entry.map(s -> s.getServiceAddress().getPort()).orElse(-1);
    //}

    @Override
    public String toString() {
        return serviceRegistry.toString();
    }

    public ConcurrentLinkedQueue<DirectoryServiceEntry> getServiceRegistry() {
        return serviceRegistry;
    }
}
