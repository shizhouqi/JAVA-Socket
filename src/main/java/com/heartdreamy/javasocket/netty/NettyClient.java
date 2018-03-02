package com.heartdreamy.javasocket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class NettyClient {

    private static  String HOST = "127.0.0.1";
    private static  int PORT = 8080;
    private static ChannelFuture futrue;
    private static EventLoopGroup worker;

    public static void sendMessage(String msg){
        futrue.channel().writeAndFlush(msg);
    }

    public static void start(){
        start(HOST, PORT);
    }

    public static void start(String host, int port){

        //数据读写线程
        worker = new NioEventLoopGroup();
        //新建启动类
        Bootstrap bootstrap = new Bootstrap();
        //设置线程池
        bootstrap.group(worker);
        //设置socket工厂
        bootstrap.channel(NioSocketChannel.class);

        //设置管道
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //获取管道
                ChannelPipeline pipeline = socketChannel.pipeline();
                //字符串解码器
                pipeline.addLast(new StringDecoder());
                //字符串编码器
                pipeline.addLast(new StringEncoder());
                //处理类
                pipeline.addLast(new NettyClientHandler());
            }
        });

        try {
            //发起异步连接操作
            futrue = bootstrap.connect(new InetSocketAddress(host,port)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void stop(){
        //等待客户端链路关闭
        futrue.channel().close();
        //优雅的退出，释放NIO线程组
        worker.shutdownGracefully();
    }
}
