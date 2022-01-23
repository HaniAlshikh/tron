package de.alshikh.haw.tron.middleware.directoryserver.stubs;

import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRPCClientStub;
import javafx.beans.InvalidationListener;

import java.lang.reflect.Method;
import java.util.UUID;

public class DirectoryServiceClient implements IDirectoryService, IRpcAppClientStub {
    public static UUID id = UUID.fromString("08fd9cc9-a1dd-454e-ae22-f3c1329ab93c");

    IRPCClientStub rpcClient;

    public DirectoryServiceClient(IRPCClientStub rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void register(DirectoryEntry directoryEntry) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(id, method, directoryEntry);
    }

    @Override
    public void unregister(DirectoryEntry directoryEntry) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(id, method, directoryEntry);
    }

    @Override
    public void addListenerTo(UUID serviceId, InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(id, method, serviceId, listener);
    }

    @Override
    public void removeListenerForm(UUID serviceId, InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(id, method, serviceId, listener);
    }

    @Override
    public IRPCClientStub getRpcClient() {
        return rpcClient;
    }

    @Override
    public UUID getServiceId() {
        return id;
    }
}
