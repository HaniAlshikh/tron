package de.alshikh.haw.tron.client.stubs;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerUpdateChannelClient implements IUpdateChannel, IRpcAppClientStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1ff-4542-ae32-f3c1329ab93c");

    IRPCClient rpcClient;

    public PlayerUpdateChannelClient(IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void invalidated(Observable observable) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(serviceId, method, observable);
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
    public String getName() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        Object response = rpcClient.invokeWithResponse(serviceId, method);
        if (response instanceof Exception)
            return null;
        return (String) response;
    }

    @Override
    public UUID getId() {
        return serviceId;
        // TODO: this causes a loop because the gameModel is shared and the local room
        //  will be replaced by the remote one in an unending loop
        //Method method = new Object(){}.getClass().getEnclosingMethod();
        //Object response = rpcClient.invokeWithResponse(serviceId, method);
        //if (response instanceof Exception)
        //    return null;
        //return (UUID) response;
    }

    @Override
    public IRPCClient getRpcClient() {
        return rpcClient;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
