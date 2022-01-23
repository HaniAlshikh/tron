package de.alshikh.haw.tron.middleware.directoryserver;

import de.alshikh.haw.tron.app.stubs.TronJsonRpcSerializationApi;
import de.alshikh.haw.tron.middleware.directoryserver.service.DirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceServer;
import de.alshikh.haw.tron.middleware.discoveryservice.DirectoryDiscoveryServer;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRPCServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;
import javafx.beans.Observable;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class DirectoryServer {

    public static void main(String[] args) {
        //DirectoryServiceJsonRpcSerializer jsonRpcSerializer = new DirectoryServiceJsonRpcSerializer();
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new TronJsonRpcSerializationApi());

        DirectoryService directoryService = new DirectoryService();
        IRpcAppServerStub directoryServiceServer = new DirectoryServiceServer(directoryService);

        IRPCServerStub rpcServer = new RpcServerStub(rpcMessageApi);
        rpcServer.register(directoryServiceServer);
        new Thread(() -> rpcServer.getRpcReceiver().start()).start();

        InetSocketAddress serverAddress = rpcServer.getRpcReceiver().getServerAddress();
        DirectoryDiscoveryServer.multicast(serverAddress, 2, TimeUnit.SECONDS);

        // TODO: for testing should be removed
        //InetSocketAddress discoveredAddress = DirectoryDiscoveryClient.discover();
        //IDirectoryService directoryServiceClient = new DirectoryServiceClient(
        //        new RpcClientStub(
        //                discoveredAddress,
        //                rpcMessageApi
        //        ));

        //directoryServiceClient.register(new DirectoryServiceEntry(UUID.randomUUID(), UUID.randomUUID(), rpcServer.getSocketAddress()));
        directoryService.addListener(DirectoryServer::tabilise);
    }

    private static void tabilise(Observable observable) {
        System.out.println();
        DirectoryService directoryService = (DirectoryService) observable;
        System.out.format("%-40s%-40s%-32s%-6s\n", "Provider ID", "Service ID", "Service Address", "Is Reachable");
        directoryService.getDib().forEach(s -> {
            System.out.format("%-40s%-40s%-32s%-6s\n",
                    s.getProviderId(), s.getServiceId(), s.getServiceAddress(), s.isReachable());
        });
        System.out.println();
    }
}
