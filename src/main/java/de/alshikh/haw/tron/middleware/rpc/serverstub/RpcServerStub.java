package de.alshikh.haw.tron.middleware.rpc.serverstub;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.IRpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.RpcUnmarshaller;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions.InvocationRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions.MethodNotFoundRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions.ServiceNotFoundRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class RpcServerStub implements IRPCServerStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry;
    private final IRpcReceiver rpcReceiver;

    public RpcServerStub(IRpcMessageApi rpcMessageApi) {
        this.serviceRegistry = new HashMap<>();
        this.rpcReceiver = new RpcReceiver(new RpcUnmarshaller(rpcMessageApi, this::handleRpcCall));
    }

    @Override
    public void register(IRpcAppServerStub serviceServerStub) {
        log.debug("registering service: " + serviceServerStub.getServiceId());
        serviceRegistry.put(serviceServerStub.getServiceId(), serviceServerStub);
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
        IRpcAppServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceId());
        if (serviceServerStub == null)
            return new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceId());

        return serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
    }

    @Override
    public IRpcReceiver getRpcReceiver() {
        return rpcReceiver;
    }
}
