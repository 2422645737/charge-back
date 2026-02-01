package com.wanghui.shiyue.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                        int result = Integer.parseInt(s);
                                        addResultFuture.complete(result);
                                        channelHandlerContext.close();
                                    }
                                });
                    }
                });
        ChannelFuture localhost = bootstrap.connect("localhost", 8888).sync();
        localhost.channel().writeAndFlush("add," + a + "," + b + "\n");
        return addResultFuture.get();
    }
}