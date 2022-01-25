package de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes;

import javafx.beans.InvalidationListener;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DirectoryEntry implements IDirectoryEntry {
    private ConcurrentLinkedQueue<InvalidationListener> listeners = new ConcurrentLinkedQueue<>();

    private final UUID providerId;
    private final UUID serviceId;
    private final InetSocketAddress serviceAddress;
    private boolean reachable;

    public DirectoryEntry(UUID providerId, UUID serviceId, InetSocketAddress serviceAddress) {
        this(providerId, serviceId, serviceAddress, true);
    }

    public DirectoryEntry(UUID providerId, UUID serviceId, InetSocketAddress serviceAddress, boolean reachable) {
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

    @Override
    public void publishUpdate() {
        listeners.parallelStream().forEach(l -> l.invalidated(this));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryEntry that = (DirectoryEntry) o;
        return providerId.equals(that.providerId) && serviceId.equals(that.serviceId) && serviceAddress.equals(that.serviceAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerId, serviceId, serviceAddress);
    }

    @Override
    public void setListeners(ConcurrentLinkedQueue<InvalidationListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public UUID getProviderId() {
        return providerId;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }

    @Override
    public InetSocketAddress getServiceAddress() {
        return serviceAddress;
    }

    @Override
    public boolean isReachable() {
        return reachable;
    }

    @Override
    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    @Override
    public String toString() {
        return "DirectoryEntry{" +
                "providerId=" + providerId +
                ", serviceId=" + serviceId +
                ", serviceAddress=" + serviceAddress +
                ", reachable=" + reachable +
                '}';
    }
}
