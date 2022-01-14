package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcResponse;

public interface IRpcUnmarshaller {
    IRpcResponse readResponse(byte[] response);

    IRpcRequest readRequest(byte[] request);

    IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;
}
