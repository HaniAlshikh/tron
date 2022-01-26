package de.alshikh.haw.tron.middleware.directoryserver;

import de.alshikh.haw.tron.app.stubs.helpers.TronJsonRpcSerializationApi;
import de.alshikh.haw.tron.middleware.directoryserver.service.DirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stubs.DirectoryServiceCallee;
import de.alshikh.haw.tron.middleware.discoveryservice.DirectoryDiscoveryServer;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.IRpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.RpcServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.RpcUnmarshaller;
import javafx.beans.Observable;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class DirectoryServer {

    public static void main(String[] args) {
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new TronJsonRpcSerializationApi());

        DirectoryService directoryService = new DirectoryService();
        IRpcCalleeAppStub directoryServiceServer = new DirectoryServiceCallee(directoryService);

        IRpcServerStub rpcServerStub = new RpcServerStub(new RpcReceiver(new RpcUnmarshaller(rpcMessageApi)));
        rpcServerStub.register(directoryServiceServer);
        new Thread(() -> rpcServerStub.getRpcReceiver().start()).start();

        InetSocketAddress serverAddress = rpcServerStub.getRpcReceiver().getServerAddress();
        DirectoryDiscoveryServer.multicast(serverAddress, 2, TimeUnit.SECONDS);

        // TODO: for testing should be removed
        //InetSocketAddress discoveredAddress = DirectoryDiscoveryClient.discover();
        //IDirectoryService directoryServiceClient = new DirectoryServiceClient(
        //        new RpcClientStub(
        //                discoveredAddress,
        //                rpcMessageApi
        //        ));

        //directoryServiceClient.register(new DirectoryServiceEntry(UUID.randomUUID(), UUID.randomUUID(), rpcServerStub.getSocketAddress()));
        directoryService.addListener(DirectoryServer::tableize);
    }

    private static void tableize(Observable observable) {
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
