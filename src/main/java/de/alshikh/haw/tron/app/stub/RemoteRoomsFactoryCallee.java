package de.alshikh.haw.tron.app.stub;

import de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.IRemoteRoomsFactory;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class RemoteRoomsFactoryCallee implements IRpcCalleeAppStub {
    public static UUID SERVICE_ID = UUID.fromString("08fd9cc9-a1dd-454e-ae18-f3c1329ab93c");

    IRemoteRoomsFactory remoteRoomsFactory;

    public RemoteRoomsFactoryCallee(IRemoteRoomsFactory remoteRoomsFactory) {
        this.remoteRoomsFactory = remoteRoomsFactory;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = remoteRoomsFactory.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(remoteRoomsFactory, args);
    }

    @Override
    public UUID getServiceId() {
        return SERVICE_ID;
    }
}
