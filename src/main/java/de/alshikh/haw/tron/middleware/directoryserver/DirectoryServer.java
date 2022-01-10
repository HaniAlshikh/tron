package de.alshikh.haw.tron.middleware.directoryserver;

import de.alshikh.haw.tron.client.stubs.TronJsonRpcSerializer;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DirectoryDiscoveryClient;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DirectoryDiscoveryServer;
import de.alshikh.haw.tron.middleware.directoryserver.service.DirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceClient;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceServer;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.JsonRpcServer;
import javafx.beans.Observable;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class DirectoryServer {

    public static void main(String[] args) {
        //DirectoryServiceJsonRpcSerializer jsonRpcSerializer = new DirectoryServiceJsonRpcSerializer();
        TronJsonRpcSerializer jsonRpcSerializer = new TronJsonRpcSerializer();

        DirectoryService directoryService = new DirectoryService();
        IRpcAppServerStub directoryServiceServer = new DirectoryServiceServer(directoryService);

        IRPCServer rpcServer = new JsonRpcServer(jsonRpcSerializer);
        rpcServer.register(directoryServiceServer);
        new Thread(rpcServer::start).start();

        InetSocketAddress serverAddress = rpcServer.getSocketAddress();
        DirectoryDiscoveryServer.multicast(serverAddress, 2, TimeUnit.SECONDS);

        // TODO: for testing should be removed
        InetSocketAddress discoveredAddress = DirectoryDiscoveryClient.discover();
        IDirectoryService directoryServiceClient = new DirectoryServiceClient(
                new JsonRpcClient(
                        discoveredAddress,
                        jsonRpcSerializer
                ));

        //directoryServiceClient.register(new DirectoryServiceEntry(UUID.randomUUID(), UUID.randomUUID(), rpcServer.getSocketAddress()));
        directoryService.addListener(DirectoryServer::tabilise);
    }

    private static void tabilise(Observable observable) {
        System.out.println();
        DirectoryService directoryService = (DirectoryService) observable;
        System.out.format("%-40s%-40s%-32s%-6s\n", "Provider ID", "Service ID", "Service Address", "Is Reachable");
        directoryService.getServiceRegistry().forEach(s -> {
            System.out.format("%-40s%-40s%-32s%-6s\n",
                    s.getProviderId(), s.getServiceId(), s.getServiceAddress(), s.isReachable());
        });
        System.out.println();
    }
}
