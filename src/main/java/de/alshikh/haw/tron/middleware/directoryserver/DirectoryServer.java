package de.alshikh.haw.tron.middleware.directoryserver;

import de.alshikh.haw.tron.app.stub.helpers.TronJsonRpcSerializationApi;
import de.alshikh.haw.tron.manager.Config;
import de.alshikh.haw.tron.middleware.directoryserver.discovery.DirectoryDiscoveryServer;
import de.alshikh.haw.tron.middleware.directoryserver.service.DirectoryService;
import de.alshikh.haw.tron.middleware.directoryserver.stub.DirectoryServiceCallee;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.clientstub.RpcClientStubFactory;
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
        // Ideally Directory Service Json Rpc Serialization Api is used
        //IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new DirectoryServiceJsonRpcSerializationApi());
        // but this for now cover more cases and avoids code duplication
        IRpcMessageApi rpcMessageApi = new JsonRpcMessageApi(new TronJsonRpcSerializationApi());
        RpcClientStubFactory.setRpcMessageApi(rpcMessageApi);

        DirectoryService directoryService = new DirectoryService();
        IRpcCalleeAppStub directoryServiceCallee = new DirectoryServiceCallee(directoryService);

        IRpcServerStub rpcServerStub = new RpcServerStub(new RpcReceiver(new RpcUnmarshaller(rpcMessageApi)));
        rpcServerStub.register(directoryServiceCallee);
        new Thread(() -> rpcServerStub.getRpcReceiver().start()).start();

        InetSocketAddress serverAddress = rpcServerStub.getRpcReceiver().getServerAddress();
        DirectoryDiscoveryServer.multicast(serverAddress, Config.DISCOVERY_PERIOD, TimeUnit.SECONDS);

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
