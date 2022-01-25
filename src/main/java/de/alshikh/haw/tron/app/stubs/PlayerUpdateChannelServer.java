package de.alshikh.haw.tron.app.stubs;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerUpdateChannelServer implements IRpcAppServerStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1ff-4542-ae32-f3c1329ab93c");

    IPlayerUpdateChannel playerUpdateChannel;

    public PlayerUpdateChannelServer(IPlayerUpdateChannel playerUpdateChannel) {
        this.playerUpdateChannel = playerUpdateChannel;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = playerUpdateChannel.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(playerUpdateChannel, args);
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
