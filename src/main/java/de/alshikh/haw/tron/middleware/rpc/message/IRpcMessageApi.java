package de.alshikh.haw.tron.middleware.rpc.message;

import de.alshikh.haw.tron.middleware.rpc.message.marshal.IRpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.message.marshal.IRpcUnmarshaller;
import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializer;

public interface IRpcMessageApi extends IRpcMarshaller, IRpcUnmarshaller {
    IRpcSerializer getRpcSerializer();
}
