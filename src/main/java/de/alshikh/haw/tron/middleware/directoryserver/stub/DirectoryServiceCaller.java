package de.alshikh.haw.tron.middleware.directoryserver.stub;

import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.IDirectoryEntry;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;
import javafx.beans.InvalidationListener;

import java.lang.reflect.Method;
import java.util.UUID;

public class DirectoryServiceCaller implements IDirectoryService, IRpcCallerAppStub {
    public static UUID SERVICE_ID = DirectoryServiceCallee.SERVICE_ID;

    IRpcClientStub rpcClientStub;

    public DirectoryServiceCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public void register(IDirectoryEntry directoryEntry) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, method, directoryEntry);
    }

    @Override
    public void unregister(IDirectoryEntry directoryEntry) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, method, directoryEntry);
    }

    @Override
    public void addListenerTo(UUID serviceId, InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(DirectoryServiceCaller.SERVICE_ID, method, serviceId, listener);
    }

    @Override
    public IRpcClientStub getRpcClientStub() {
        return rpcClientStub;
    }

    @Override
    public UUID getServiceId() {
        return SERVICE_ID;
    }
}
