package com.heartdreamy.javasocket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServerHandler implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean isStart = false;

    public NIOServerHandler(int port){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            //设置通道为非阻塞状态
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server is start, port:" + port);
            isStart = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        isStart = false;
    }

    @Override
    public void run() {
        while (isStart){
            try {
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(null != selector){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key){
        if(key.isValid()){
            try {
                if(key.isAcceptable()){
                    ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel sc = (SocketChannel)key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                    int readBytes = sc.read(byteBuffer);
                    if(readBytes>0){
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        String message = new String(bytes,"UTF-8");
                        System.out.println("server--received message:" + message);
                        doWrite(sc,"server received massage:" + message);
                        if("exit".equals(message)){
                            System.out.println("server--disconnect client socket");
                            sc.close();
                        }
                    }else{
                        key.cancel();
                        sc.close();
                    }
                }
            } catch (IOException e) {
                try {
                    key.cancel();
                    key.channel().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc , String message){
        byte[] bytes = message.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        try {
            sc.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
