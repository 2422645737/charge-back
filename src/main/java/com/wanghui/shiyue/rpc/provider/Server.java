package com.wanghui.shiyue.rpc.provider;

public class Server {
    public static void main(String[] args) {
        ProviderServer providerServer = new ProviderServer(8888);
        providerServer.start();
    }
}
