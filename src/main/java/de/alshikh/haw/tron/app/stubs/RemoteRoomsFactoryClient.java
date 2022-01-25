package de.alshikh.haw.tron.app.stubs;

import de.alshikh.haw.tron.app.stubs.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRPCClientStub;
import javafx.beans.Observable;

import java.lang.reflect.Method;
import java.util.UUID;

public class RemoteRoomsFactoryClient implements IRemoteRoomsFactory, IRpcAppClientStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1dd-454e-ae18-f3c1329ab93c");

    IRPCClientStub rpcClient;

    public RemoteRoomsFactoryClient(IRPCClientStub rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void invalidated(Observable observable) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(serviceId, method, observable);
    }

    @Override
    public IRPCClientStub getRpcClient() {
        return rpcClient;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
