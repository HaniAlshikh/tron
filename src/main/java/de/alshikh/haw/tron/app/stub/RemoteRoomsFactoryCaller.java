package de.alshikh.haw.tron.app.stub;

import de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;
import javafx.beans.Observable;

import java.lang.reflect.Method;
import java.util.UUID;

public class RemoteRoomsFactoryCaller implements IRemoteRoomsFactory, IRpcCallerAppStub {
    public static UUID SERVICE_ID = RemoteRoomsFactoryCallee.SERVICE_ID;

    IRpcClientStub rpcClientStub;

    public RemoteRoomsFactoryCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public void invalidated(Observable observable) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, method, observable);
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
