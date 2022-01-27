package de.alshikh.haw.tron.app.stub;

import de.alshikh.haw.tron.app.model.lobby.data.datatypes.IPlayerUpdateChannel;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerUpdateChannelCallee implements IRpcCalleeAppStub {
    public static UUID SERVICE_ID = UUID.fromString("08fd9cc9-a1ff-4542-ae32-f3c1329ab93c");

    IPlayerUpdateChannel playerUpdateChannel;

    public PlayerUpdateChannelCallee(IPlayerUpdateChannel playerUpdateChannel) {
        this.playerUpdateChannel = playerUpdateChannel;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = playerUpdateChannel.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(playerUpdateChannel, args);
    }

    @Override
    public UUID getServiceId() {
        return SERVICE_ID;
    }
}
