package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

import org.json.JSONArray;

import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRpcRequest {
    boolean isNotification();

    byte[] getBytes();

    UUID getId();

    UUID getServiceId();

    String getMethodName();

    JSONArray getParams();

    InetSocketAddress getCallbackAddress();
}
