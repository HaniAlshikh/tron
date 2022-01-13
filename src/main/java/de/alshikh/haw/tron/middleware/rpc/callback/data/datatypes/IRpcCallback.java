package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

import java.util.UUID;

public interface IRpcCallback {
    void retrn(UUID requestId, Object result);
}
