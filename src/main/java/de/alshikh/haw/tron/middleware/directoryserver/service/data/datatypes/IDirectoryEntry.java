package de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IDirectoryEntry extends Observable {
    void publishUpdate();

    void setListeners(ConcurrentLinkedQueue<InvalidationListener> listeners);

    UUID getProviderId();

    UUID getServiceId();

    InetSocketAddress getServiceAddress();

    boolean isReachable();

    void setReachable(boolean reachable);
}
