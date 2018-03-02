package com.heartdreamy.javasocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
    private static  int PORT = 8080;
    private static ChannelFuture future;
    private static EventLoopGroup listener;
    private static EventLoopGroup worker;

    public static void start(){
        start(PORT);
    }

    public static void start(int port){
        //监听端口线程
        listener = new NioEventLoopGroup();
        //数据读写线程
        worker = new NioEventLoopGroup();
        //新建启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //新建线程池
        bootstrap.group(listener, worker);
        //设置socket工厂
        bootstrap.channel(NioServerSocketChannel.class);

        //设置管道工厂
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //获取管道
                ChannelPipeline pipeline = socketChannel.pipeline();
                //字符串解码器
                pipeline.addLast(new StringDecoder());
                //字符串编码器
                pipeline.addLast(new StringEncoder());
                //处理类
                pipeline.addLast(new NettyServerHandler());
            }
        });

        //设置TCP参数
        //1.链接缓冲池的大小（ServerSocketChannel的设置）
        bootstrap.option(ChannelOption.SO_BACKLOG,1024);
        //维持链接的活跃，清除死链接(SocketChannel的设置)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
        //关闭延迟发送
        bootstrap.childOption(ChannelOption.TCP_NODELAY,true);
        try {
            //绑定端口
            future = bootstrap.bind(8080).sync();
            System.out.println("server start ...... ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stop(){
        future.channel().close();
        listener.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
