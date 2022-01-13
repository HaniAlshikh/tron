package de.alshikh.haw.tron.middleware.rpc.callback;

// TODO: this is a workaround to get the local port the rpc server is listing on
//  to pass into an rpc request if a callback is needed
//  (the port is needed so that the callee knows how to send a callback with the result to the caller
//  without needing a directory service to ask for the callback service port
//  this avoids the dependency on the directory service when it comes to callbacks
//  if the dependency is expect and should be implemented the stubs should be created from IRpcCallbackService)
public class LocalRpcServerPortFinder {
    public static int PORT = 0;
}
