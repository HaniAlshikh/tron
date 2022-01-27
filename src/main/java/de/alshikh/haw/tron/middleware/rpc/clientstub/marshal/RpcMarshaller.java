package de.alshikh.haw.tron.middleware.rpc.clientstub.marshal;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.clientstub.send.IRpcSender;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public class RpcMarshaller implements IRpcMarshaller {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IRpcMessageApi rpcMessageApi;
    private final IRpcSender rpcSender;
    private final IRpcCallbackService callbackService;

    public RpcMarshaller(IRpcMessageApi rpcMessageApi, IRpcSender rpcSender, IRpcCallbackService callbackService) {
        this.rpcMessageApi = rpcMessageApi;
        this.rpcSender = rpcSender;
        this.callbackService = callbackService;
    }

    @Override
    public void marshal(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, boolean isBestEffort, Method method, Object[] args) {
        boolean isNotification = rpcCallbackHandler == null;
        InetSocketAddress rpcCallbackServerAddress = isNotification ? null : callbackService.getCallbackServerAddress();

        IRpcRequest rpcRequest = rpcMessageApi.newRequest(serviceId, isNotification, rpcCallbackServerAddress, method, args);
        send(rpcRequest, isBestEffort);

        if (!isNotification)
            callbackService.register(rpcRequest.getId(), rpcCallbackHandler);
    }

    private void send(IRpcRequest request, boolean isBestEffort) {
        try {
            log.debug("sending request: " + request);
            rpcSender.send(request.getBytes(), isBestEffort);
        } catch (RpcException e) {
            log.info("failed to send request: " + request);
            log.debug("sending request error: ", e);
            if (request.isNotification())
                callbackService.setResult(request.getId(), e);
        }
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return rpcSender.getServerAddress();
    }
}
