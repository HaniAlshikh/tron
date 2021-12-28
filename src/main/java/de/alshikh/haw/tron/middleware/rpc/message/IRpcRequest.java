package de.alshikh.haw.tron.middleware.rpc.message;

import org.json.JSONObject;

import java.util.UUID;

public interface IRpcRequest {
    byte[] getBytes();

    UUID getId();

    String getMethod();

    JSONObject getParams();
}
