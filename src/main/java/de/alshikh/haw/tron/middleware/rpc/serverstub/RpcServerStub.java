package de.alshikh.haw.tron.middleware.rpc.serverstub;

import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions.InvocationRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions.MethodNotFoundRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions.ServiceNotFoundRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.IRpcReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class RpcServerStub implements IRpcServerStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final HashMap<UUID, IRpcCalleeAppStub> serviceRegistry = new HashMap<>();
    private final IRpcReceiver rpcReceiver;

    public RpcServerStub(IRpcReceiver rpcReceiver) {
        this.rpcReceiver = rpcReceiver;
        this.rpcReceiver.getRpcUnmarshaller().setRpcCallHandler(this::handleRpcCall);
    }

    @Override
    public void register(IRpcCalleeAppStub rpcCalleeAppStub) {
        log.debug("registering service: " + rpcCalleeAppStub.getServiceId());
        serviceRegistry.put(rpcCalleeAppStub.getServiceId(), rpcCalleeAppStub);
    }

    @Override
    public void unregister(UUID serviceId) {
        log.debug("unregistering service: " + serviceId);
        serviceRegistry.remove(serviceId);
    }

    private Object handleRpcCall(IRpcCall rpcCall) {
        try {
            return call(rpcCall);
        } catch (InvocationTargetException | IllegalAccessException e) {
            return new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            return new MethodNotFoundRpcException();
        }
    }

    private Object call(IRpcCall rpcCall) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        IRpcCalleeAppStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceId());
        if (serviceServerStub == null)
            return new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceId());

        return serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
    }

    @Override
    public IRpcReceiver getRpcReceiver() {
        return rpcReceiver;
    }
}
