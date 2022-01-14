package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.*;
import de.alshikh.haw.tron.middleware.rpc.message.marshal.IRpcUnmarshaller;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.network.IRpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.network.data.exceptions.FailedToReceiveNetworkRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class ServerStub implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final IRpcCallbackService callbackService = RpcCallbackService.getInstance();

    private IRpcCallback rpcCallback;
    private IRpcRequest rpcRequest;

    private final IRpcReceiver rpcReceiver;
    private final IRpcUnmarshaller rpcUnmarshaller;
    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry;

    public ServerStub(IRpcReceiver rpcReceiver, IRpcUnmarshaller rpcUnmarshaller, HashMap<UUID, IRpcAppServerStub> serviceRegistry) {
        this.rpcReceiver = rpcReceiver;
        this.rpcUnmarshaller = rpcUnmarshaller;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try {
            receive();
            call(unmarshal());
        } catch (RpcException e) {
            if (!rpcRequest.isNotification())
                rpcCallback.retrn(rpcRequest.getId(), e);
        }
    }

    private void receive() throws FailedToReceiveNetworkRpcException {
        rpcRequest = rpcUnmarshaller.readRequest(rpcReceiver.receive());
        log.debug("received request: " + rpcRequest);
        rpcReceiver.safeClose();
        if (!rpcRequest.isNotification())
            rpcCallback = callbackService.newRpcCallback(rpcReceiver.getAddress(), rpcRequest.getRpcServerPort());
    }

    private IRpcCall unmarshal() throws InvalidParamsRpcException {
        return rpcUnmarshaller.toRpcCall(rpcRequest);
    }

    private void call(IRpcCall rpcCall) throws InvocationRpcException, MethodNotFoundRpcException, ServiceNotFoundRpcException {
        try {
            IRpcAppServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceId());
            if (serviceServerStub == null)
                throw new ServiceNotFoundRpcException("Service not found: " + rpcCall.getServiceId());

            Object result = serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
            if (!rpcRequest.isNotification())
                rpcCallback.retrn(rpcRequest.getId(), result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundRpcException();
        }
    }
}
