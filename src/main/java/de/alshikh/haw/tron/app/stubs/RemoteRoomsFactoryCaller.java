package de.alshikh.haw.tron.app.stubs;

import de.alshikh.haw.tron.app.stubs.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;
import javafx.beans.Observable;

import java.lang.reflect.Method;
import java.util.UUID;

public class RemoteRoomsFactoryCaller implements IRemoteRoomsFactory, IRpcCallerAppStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1dd-454e-ae18-f3c1329ab93c");

    IRpcClientStub rpcClientStub;

    public RemoteRoomsFactoryCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public void invalidated(Observable observable) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(serviceId, method, observable);
    }

    @Override
    public IRpcClientStub getRpcClientStub() {
        return rpcClientStub;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
