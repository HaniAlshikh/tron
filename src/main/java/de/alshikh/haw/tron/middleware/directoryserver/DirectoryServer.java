package de.alshikh.haw.tron.middleware.directoryserver;

import de.alshikh.haw.tron.client.stubs.TronJsonRpcSerializer;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DiscoveryClient;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DiscoveryServer;
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

        IRPCServer rpcServer = new JsonRpcServer(jsonRpcSerializer);
        rpcServer.register(directoryServiceServer);
        new Thread(rpcServer::start).start();

        // TODO: this or maybe multicast
        InetSocketAddress serverAddress = rpcServer.getSocketAddress();
        DiscoveryServer.multicast(serverAddress.toString(), 2);

        InetSocketAddress discoveredAddress = DiscoveryClient.getServerAddress();

        IDirectoryService directoryServiceClient = new DirectoryServiceClient(
                new JsonRpcClient(
                        discoveredAddress,
                        jsonRpcSerializer
                ));

        directoryServiceClient.register(new DirectoryServiceEntry(UUID.randomUUID(), rpcServer.getSocketAddress()));
    }
}
