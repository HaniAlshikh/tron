package de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DirectoryServiceEntry implements Observable {
    private ConcurrentLinkedQueue<InvalidationListener> listeners;

    private final UUID providerId;
    private final UUID serviceId;
    private final InetSocketAddress serviceAddress;
    private boolean reachable;

    public DirectoryServiceEntry(UUID providerId, UUID serviceId, InetSocketAddress serviceAddress) {
        this(providerId, serviceId, serviceAddress, true);
    }

    public DirectoryServiceEntry(UUID providerId, UUID serviceId, InetSocketAddress serviceAddress, boolean reachable) {
        this.listeners = new ConcurrentLinkedQueue<>();

        this.providerId = providerId;
        this.serviceId = serviceId;
        this.serviceAddress = serviceAddress;
        this.reachable = reachable;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    public void publishUpdate() {
        listeners.forEach(l -> {
            l.invalidated(this);
        });
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryServiceEntry that = (DirectoryServiceEntry) o;
        return providerId.equals(that.providerId) && serviceId.equals(that.serviceId) && serviceAddress.equals(that.serviceAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerId, serviceId, serviceAddress);
    }

    public UUID getProviderId() {
        return providerId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public InetSocketAddress getServiceAddress() {
        return serviceAddress;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setListeners(ConcurrentLinkedQueue<InvalidationListener> listeners) {
        this.listeners = listeners;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    @Override
    public String toString() {
        return "DirectoryServiceEntry{" +
                "id=" + serviceId +
                ", serviceAddress=" + serviceAddress +
                ", reachable=" + reachable +
                '}';
    }
}
