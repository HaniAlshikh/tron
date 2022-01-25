package de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackClient;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.*;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.ServiceNotFoundRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.InvocationRpcException;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.MethodNotFoundRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

public class RpcUnmarshaller implements IRpcUnmarshaller {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private IRpcCallback rpcCallback;
    private IRpcRequest rpcRequest;

    private final IRpcMessageApi rpcMsgApi;
    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry;

    public RpcUnmarshaller(IRpcMessageApi rpcMsgApi, HashMap<UUID, IRpcAppServerStub> serviceRegistry) {
        this.rpcMsgApi = rpcMsgApi;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void unmarshal(byte[] request) {
        try {
            call(toRpcCall(request));
        } catch (RpcException e) {
            log.debug("failed to unmarshal request " + new String(request), e);
            if (rpcRequest != null && !rpcRequest.isNotification())
                rpcCallback.retrn(rpcRequest.getId(), e);
        }
    }

    private IRpcCall toRpcCall(byte[] request) throws ServiceNotFoundRpcException, InvalidParamsRpcException {
        rpcRequest = rpcMsgApi.readRequest(request);
        log.debug("received request: " + rpcRequest);

        if (!rpcRequest.isNotification())
            rpcCallback = newRpcCallback(rpcRequest.getCallbackAddress());

        if (!serviceRegistry.containsKey(rpcRequest.getServiceId()))
            throw new ServiceNotFoundRpcException("Service not found: " + rpcRequest.getServiceId());

        return rpcMsgApi.toRpcCall(rpcRequest);
    }

    // TODO: move call to ServerStub to remove serviceRegistry dependency
    private void call(IRpcCall rpcCall) throws InvocationRpcException, MethodNotFoundRpcException, ServiceNotFoundRpcException {
        try {
            IRpcAppServerStub serviceServerStub = serviceRegistry.get(rpcCall.getServiceId());
            Object result = serviceServerStub.call(rpcCall.getMethodName(), rpcCall.getParameterTypes(), rpcCall.getArguments());
            if (!rpcRequest.isNotification())
                rpcCallback.retrn(rpcRequest.getId(), result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationRpcException();
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundRpcException();
        }
    }


    private IRpcCallback newRpcCallback(InetSocketAddress callbackAddress) {
        return new RpcCallbackClient(new RpcClientStub(new RpcMarshaller(rpcMsgApi, new RpcSender(callbackAddress),
                null))); // callback chaining is not supported
    }

}
