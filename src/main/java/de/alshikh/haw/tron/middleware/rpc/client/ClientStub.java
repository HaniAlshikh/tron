package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.message.marshal.IRpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.network.IRpcSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

public class ClientStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    // TODO: singleton days are gone -> find a better way
    private final IRpcCallbackService callbackService = RpcCallbackService.getInstance();

    private final IRpcMarshaller rpcMarshaller;
    private final IRpcSender rpcSender;

    public ClientStub(IRpcMarshaller rpcMarshaller, IRpcSender rpcSender) {
        this.rpcMarshaller = rpcMarshaller;
        this.rpcSender = rpcSender;
    }

    public void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, Method method, Object... args) {
        log.debug("Invoking: " + method.getName() + " with args: " + Arrays.toString(args));
        boolean isNotification = rpcCallbackHandler == null;
        IRpcRequest rpcRequest = marshal(serviceId, method, args, isNotification);
        send(rpcRequest);

        if (!isNotification)
            callbackService.register(rpcRequest.getId(), rpcCallbackHandler);
    }

    private IRpcRequest marshal(UUID serviceId, Method method, Object[] args, boolean isNotification) {
        return rpcMarshaller.newRequest(serviceId, method, args, isNotification);
    }

    private void send(IRpcRequest request) {
        try (rpcSender) {
            rpcSender.connect();
            log.debug("sending request: " + request);
            rpcSender.send(request.getBytes());
        } catch (Exception e) {
            log.info("failed to send request: " + request);
            log.debug("sending request error:", e);
            if (request.isNotification())
                callbackService.setResponse(request.getId(), e);
        }
    }
}
