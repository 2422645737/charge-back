package com.wanghui.shiyue.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ProviderServer {

    private int port;

    private EventLoopGroup bossEventLoopGroup;

    private EventLoopGroup workerEventLoopGroup;

    public ProviderServer(int port){
        this.port = port;;
    }

    public void start(){
        bossEventLoopGroup = new NioEventLoopGroup();
        workerEventLoopGroup = new NioEventLoopGroup(4);
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try{
            serverBootstrap.group(bossEventLoopGroup,workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new SimpleChannelInboundHandler<String>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                                                    String s) throws Exception {
                                            String[] split = s.split(",");
                                            String method = split[0];
                                            int a = Integer.parseInt(split[1]);
                                            int b = Integer.parseInt(split[2]);
                                            if(method.equals("add"))                                    {
                                                channelHandlerContext.writeAndFlush(add(a, b) + "\n");
                                            }
                                        }
                                    });
                        }
                    });

            serverBootstrap.bind(port).sync();
        }catch (Exception e){
            throw new RuntimeException("服务器启动异常",e);
        }
    }

    public void stop(){
        if(bossEventLoopGroup != null){
            bossEventLoopGroup.shutdownGracefully();
        }
        if(workerEventLoopGroup != null){
            workerEventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
    }

    private static int add(int a, int b) {
        return a + b;
    }
}