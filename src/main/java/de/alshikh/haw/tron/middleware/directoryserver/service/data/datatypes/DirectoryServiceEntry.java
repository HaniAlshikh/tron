package de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DirectoryServiceEntry implements Observable {
    private List<InvalidationListener> listeners = new ArrayList<>();

    private final UUID id;
    private final InetSocketAddress serviceAddress;
    private boolean reachable;

    public DirectoryServiceEntry(UUID id, InetSocketAddress serviceAddress) {
        this.id = id;
        this.serviceAddress = serviceAddress;
        this.reachable = true;
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
        listeners.forEach(l -> l.invalidated(this));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryServiceEntry that = (DirectoryServiceEntry) o;
        return id.equals(that.id) && serviceAddress.equals(that.serviceAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceAddress);
    }

    public UUID getId() {
        return id;
    }

    public InetSocketAddress getServiceAddress() {
        return serviceAddress;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setListeners(List<InvalidationListener> listeners) {
        this.listeners = listeners;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    @Override
    public String toString() {
        return "DirectoryServiceEntry{" +
                "id=" + id +
                ", serviceAddress=" + serviceAddress +
                ", reachable=" + reachable +
                '}';
    }
}
