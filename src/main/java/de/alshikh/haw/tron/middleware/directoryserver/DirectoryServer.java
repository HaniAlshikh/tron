package de.alshikh.haw.tron.middleware.directoryserver;

import de.alshikh.haw.tron.client.stubs.TronJsonRpcSerializer;
import de.alshikh.haw.tron.middleware.directoryserver.service.DirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.data.datatypes.DirectoryServiceEntry;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceClient;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceServer;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.JsonRpcServer;

import java.net.InetSocketAddress;
import java.util.UUID;

public class DirectoryServer {

    public static void main(String[] args) {
        //DirectoryServiceJsonRpcSerializer jsonRpcSerializer = new DirectoryServiceJsonRpcSerializer();
        TronJsonRpcSerializer jsonRpcSerializer = new TronJsonRpcSerializer();

        IDirectoryService directoryService = new DirectoryService();
        IRpcAppServerStub directoryServiceServer = new DirectoryServiceServer(directoryService);

        InetSocketAddress socketAddress = new InetSocketAddress(8090);
        new Thread(() -> {
            IRPCServer rpcServer = new JsonRpcServer(socketAddress, jsonRpcSerializer);
            rpcServer.register(directoryServiceServer);
            rpcServer.start();
        }).start();

        // TODO: this or maybe multicast
        // new ServiceBroadcaster(ip, port).broadcast();

        // delay until the server starts
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IDirectoryService directoryServiceClient = new DirectoryServiceClient(
                new JsonRpcClient(
                        new InetSocketAddress("localhost", 8090),
                        jsonRpcSerializer
                ));

        directoryServiceClient.register(new DirectoryServiceEntry(UUID.randomUUID(), socketAddress));
    }
}
