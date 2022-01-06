package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;

public interface IRpcMessageApi extends IRpcMarshaller, IRpcUnmarshaller {
    IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;

    Object toInvocationResult(IRpcResponse rpcResponse);

    IRpcSerializer getRpcSerializer();
}
