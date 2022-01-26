package de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal;

import de.alshikh.haw.tron.middleware.rpc.serverstub.data.datatypes.IRpcCall;

import java.util.function.Function;

public interface IRpcUnmarshaller {
    void unmarshal(byte[] request);

    void setRpcCallHandler(Function<IRpcCall, Object> rpcCallHandler);
}
