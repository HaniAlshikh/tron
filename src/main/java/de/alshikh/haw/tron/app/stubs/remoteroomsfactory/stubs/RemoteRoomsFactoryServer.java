package de.alshikh.haw.tron.app.stubs.remoteroomsfactory.stubs;

import de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service.IRemoteRoomsFactory;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class RemoteRoomsFactoryServer implements IRpcAppServerStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1dd-454e-ae18-f3c1329ab93c");

    IRemoteRoomsFactory remoteRoomsFactory;

    public RemoteRoomsFactoryServer(IRemoteRoomsFactory remoteRoomsFactory) {
        this.remoteRoomsFactory = remoteRoomsFactory;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = remoteRoomsFactory.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(remoteRoomsFactory, args);
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
