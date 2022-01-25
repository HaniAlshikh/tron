package de.alshikh.haw.tron.middleware.rpc.clientstub;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.IRpcMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;

public class RpcClientStub implements IRPCClientStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IRpcMarshaller rpcMarshaller;

    public RpcClientStub(IRpcMarshaller rpcMarshaller) {
        this.rpcMarshaller = rpcMarshaller;
    }

    @Override
    public void invoke(UUID serviceId, Method method, Object... args) {
        invoke(serviceId, null, false, method, args);
    }

    @Override
    public void invoke(UUID serviceId, boolean isBestEffort, Method method, Object... args) {
        invoke(serviceId, null, isBestEffort, method, args);
    }

    @Override
    public void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, Method method, Object... args) {
        invoke(serviceId, rpcCallbackHandler, false, method, args);
    }

    @Override
    public void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, boolean isBestEffort, Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        rpcMarshaller.marshal(serviceId, rpcCallbackHandler, isBestEffort, method, args);
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return rpcMarshaller.getServerAddress();
    }
}
