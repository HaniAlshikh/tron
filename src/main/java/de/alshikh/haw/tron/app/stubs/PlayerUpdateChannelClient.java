package de.alshikh.haw.tron.app.stubs;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRPCClientStub;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerUpdateChannelClient implements IPlayerUpdateChannel, IRpcAppClientStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1ff-4542-ae32-f3c1329ab93c");

    IRPCClientStub rpcClient;

    public PlayerUpdateChannelClient(IRPCClientStub rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void invalidated(Observable observable) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(serviceId, true, method, observable); // TODO: enable udp
    }

    @Override
    public void addListener(InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(serviceId, method, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(serviceId, method, listener);
    }

    @Override
    public String getPlayerName() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClient.invoke(serviceId, rpcCallbackHandler, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (String) response;
    }

    @Override
    public UUID getPlayerId() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClient.invoke(serviceId, rpcCallbackHandler, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (UUID) response;
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
