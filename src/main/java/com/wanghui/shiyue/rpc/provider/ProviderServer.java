package com.wanghui.shiyue.rpc.provider;

import com.wanghui.shiyue.rpc.codec.ResponseEncoder;
import com.wanghui.shiyue.rpc.codec.SYDecoder;
import com.wanghui.shiyue.rpc.message.Request;
import com.wanghui.shiyue.rpc.message.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                            nioSocketChannel.pipeline()
                                    .addLast(new SYDecoder())
                                    .addLast(new ResponseEncoder())
                                    .addLast(new SimpleChannelInboundHandler<Request>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                                                    Request request) throws Exception {
                                            System.out.println(request);
                                            Response response = new Response();
                                            response.setData(1);
                                            channelHandlerContext.writeAndFlush(response);
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