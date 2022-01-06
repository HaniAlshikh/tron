package de.alshikh.haw.tron.middleware.directoryserver.stubs;

import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class DirectoryServiceServer implements IRpcAppServerStub {
    public static UUID id = UUID.fromString("08fd9cc9-a1dd-454e-ae22-f3c1329ab93c");

    IDirectoryService directoryService;

    public DirectoryServiceServer(IDirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @Override
    public Object call(String methodName, Class<?>[] parameterTypes, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = directoryService.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(directoryService, args);
    }

    @Override
    public UUID getServiceId() {
        return id;
    }
}
