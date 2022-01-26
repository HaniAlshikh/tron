package de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackCaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.marshal.RpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.RpcSender;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.serverstub.data.datatypes.RpcCall;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions.InvalidParamsRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.function.Function;

public class RpcUnmarshaller implements IRpcUnmarshaller {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private IRpcRequest rpcRequest;
    private Function<IRpcCall, Object> rpcCallHandler;

    private final IRpcMessageApi rpcMsgApi;

    public RpcUnmarshaller(IRpcMessageApi rpcMsgApi) {
        this.rpcMsgApi = rpcMsgApi;
    }

    @Override
    public void unmarshal(byte[] request) {
        try {
            rpcRequest = rpcMsgApi.parseRequest(request);
            log.debug("received request: " + rpcRequest);
            Object result = rpcCallHandler.apply(toRpcCall(rpcRequest));
            callback(result);
        } catch (RpcException e) {
            log.debug("failed to unmarshal request " + new String(request), e);
            if (rpcRequest != null)
                callback(e);
        }
    }

    private IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException {
        UUID serviceId = rpcRequest.getServiceId();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = new Class[rpcRequest.getParams().length()];
        Object[] args = new Object[rpcRequest.getParams().length()];
        rpcMsgApi.parseParamsArray(rpcRequest.getParams(), parameterTypes, args);
        return new RpcCall(serviceId, methodName, parameterTypes, args);
    }

    private void callback(Object result) {
        if (rpcRequest.isNotification())
            return;

        IRpcCallback rpcCallback = newRpcCallback(rpcRequest.getCallbackAddress());
        rpcCallback.retrn(rpcRequest.getId(), result);
    }

    private IRpcCallback newRpcCallback(InetSocketAddress callbackAddress) {
        return new RpcCallbackCaller(new RpcClientStub(new RpcMarshaller(rpcMsgApi, new RpcSender(callbackAddress),
                null))); // callback chaining is not supported
    }

    @Override
    public void setRpcCallHandler(Function<IRpcCall, Object> rpcCallHandler) {
        this.rpcCallHandler = rpcCallHandler;
    }
}
