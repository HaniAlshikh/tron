package de.alshikh.haw.tron.app.stub;

import de.alshikh.haw.tron.app.model.lobby.data.datatypes.IPlayerUpdateChannel;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerUpdateChannelCaller implements IPlayerUpdateChannel, IRpcCallerAppStub {
    public static UUID SERVICE_ID = PlayerUpdateChannelCallee.SERVICE_ID;

    IRpcClientStub rpcClientStub;

    public PlayerUpdateChannelCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public void invalidated(Observable observable) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, true, method, observable);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, method, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, method, listener);
    }

    @Override
    public String getPlayerName() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClientStub.invoke(SERVICE_ID, rpcCallbackHandler, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (String) response;
    }

    @Override
    public UUID getPlayerId() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClientStub.invoke(SERVICE_ID, rpcCallbackHandler, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (UUID) response;
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
