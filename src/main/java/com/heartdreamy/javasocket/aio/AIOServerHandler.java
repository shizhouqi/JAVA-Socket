package com.heartdreamy.javasocket.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * 该服务端代码，存在不能连续处理消息的问题，
 * 对于同一个连接，只能处理客户端的一个请求，无法处理客户端的其他请求
 */
public class AIOServerHandler implements Runnable {

    private int port;
    private int threadSize;
    private AsynchronousChannelGroup asynchronousChannelGroup;
    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AIOServerHandler(int port, int threadSize){
        this.port = port;
        this.threadSize = threadSize;
    }
    @Override
    public void run() {
        try {
            asynchronousChannelGroup = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(),this.threadSize);
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(this.asynchronousChannelGroup);
            asynchronousServerSocketChannel.bind( new InetSocketAddress(this.port));
            System.out.println("server is start, port:" + this.port);
            asynchronousServerSocketChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, AIOServerHandler>() {
                final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                @Override
                public void completed(AsynchronousSocketChannel result, AIOServerHandler attachment) {
                    byteBuffer.clear();
                    try {
                        int readBytes = result.read(byteBuffer).get();
                        if(readBytes>0){
                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            String message = new String(bytes,"UTF-8");
                            System.out.println("server--received message:" + message);
                            String returnMessage = "server received massage:" + message;
                            result.write(ByteBuffer.wrap(returnMessage.getBytes())).get();
                            if("exit".equals(message)){
                                try {
                                    result.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            try {
                                result.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } finally {
                        asynchronousServerSocketChannel.accept(attachment,this);
                    }
                }

                @Override
                public void failed(Throwable exc, AIOServerHandler attachment) {
                    System.out.println("server--received failed");
                    exc.printStackTrace();
                    attachment.asynchronousServerSocketChannel.accept(attachment, this);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        try {
            this.asynchronousServerSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
