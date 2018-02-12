package com.heartdreamy.javasocket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOClientHandler implements Runnable {

    private AsynchronousSocketChannel asynchronousSocketChannel;
    private String ip;
    private int port;

    public AIOClientHandler(String ip, int port){
        this.ip = ip;
        this.port = port;
        try {
            this.asynchronousSocketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.asynchronousSocketChannel.connect(new InetSocketAddress(this.ip, this.port), this, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                System.out.println("client--connect success");
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("client--connect failed");
            }
        });
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        this.asynchronousSocketChannel.read(byteBuffer, this, new CompletionHandler<Integer, AIOClientHandler>() {
            @Override
            public void completed(Integer result, AIOClientHandler attachment) {
                if(result>0) {
                    System.out.println(result);
                    System.out.println("client read data: " + new String(byteBuffer.array()));
                    byteBuffer.clear();
                    attachment.asynchronousSocketChannel.read(byteBuffer, attachment, this);
                }else {
                    try {
                        asynchronousSocketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failed(Throwable exc, AIOClientHandler attachment) {
                System.out.println("read failed");
            }
        });
    }

    public void sendMessage(String message){
        this.asynchronousSocketChannel.write(ByteBuffer.wrap(message.getBytes()));
    }

    public void stop(){
        try {
            this.asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
