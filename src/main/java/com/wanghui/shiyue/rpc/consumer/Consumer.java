package com.wanghui.shiyue.rpc.consumer;

import com.wanghui.shiyue.rpc.codec.RequestEncoder;
import com.wanghui.shiyue.rpc.codec.ResponseEncoder;
import com.wanghui.shiyue.rpc.codec.SYDecoder;
import com.wanghui.shiyue.rpc.message.Request;
import com.wanghui.shiyue.rpc.message.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Consumer {
    public int add(int a, int b) throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> addResultFuture = new CompletableFuture<>();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(new NioEventLoopGroup(4))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                .addLast(new SYDecoder())
                                .addLast(new RequestEncoder())
                                .addLast(new SimpleChannelInboundHandler<Response>() {

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
                                        System.out.println(response);
                                        addResultFuture.complete(Integer.valueOf(response.getData().toString()));
                                    }
                                });
                    }
                });
        ChannelFuture localhost = bootstrap.connect("localhost", 8888).sync();
        Request request = new Request();
        request.setMethodName("add");
        request.setParams(new Object[]{1,2});
        request.setParameterTypes(new String[]{"int","int"});
        request.setServiceName("bbb");
        localhost.channel().writeAndFlush(request);
        return addResultFuture.get();
    }
}