package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

import org.json.JSONObject;

import java.util.UUID;

public interface IRpcRequest {
    boolean isNotification();

    byte[] getBytes();

    UUID getId();

    String getMethod();

    JSONObject getParams();
}
