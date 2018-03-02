package com.heartdreamy.javasocket.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    //接受服务端发来的消息
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("client get message： "+msg);
    }

    //与服务器建立连接
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        //给服务器发消息
        ctx.channel().writeAndFlush("i am client !");
    }

    //与服务器断开连接
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    //异常
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭管道
        ctx.channel().close();
        //打印异常信息
        cause.printStackTrace();
    }
}
