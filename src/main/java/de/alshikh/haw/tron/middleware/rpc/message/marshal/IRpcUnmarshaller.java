package de.alshikh.haw.tron.middleware.rpc.message.marshal;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.InvalidParamsRpcException;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcCall;
import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcRequest;

public interface IRpcUnmarshaller {
    IRpcRequest readRequest(byte[] request);

    IRpcCall toRpcCall(IRpcRequest rpcRequest) throws InvalidParamsRpcException;
}
