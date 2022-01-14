package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;

public interface IRpcMessageApi extends IRpcMarshaller, IRpcUnmarshaller {
    Object toInvocationResult(IRpcResponse rpcResponse);

    IRpcSerializer getRpcSerializer();
}
