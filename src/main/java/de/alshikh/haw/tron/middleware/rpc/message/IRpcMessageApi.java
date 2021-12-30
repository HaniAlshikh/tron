package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;
import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvalidParamsRpcException;

public interface IRpcMessageApi extends IRpcMarshaller, IRpcUnmarshaller {
    IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;

    Object toInvocationResult(IRpcResponse rpcResponse);
}
