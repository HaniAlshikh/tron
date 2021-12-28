package de.alshikh.haw.tron.middleware.rpc.client;

import java.lang.reflect.Method;

public interface IRPCClient {
    Object invoke(Method method, Object... args);

    Object invokeWithResponse(Method method, Object... args);
}
