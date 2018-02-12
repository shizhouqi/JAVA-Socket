package com.heartdreamy.javasocket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClientHandler implements Runnable {

    private String ip;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean isStart = false;

    public NIOClientHandler(String ip, int port){
        try {
            this.ip = ip;
            this.port = port;
            this.selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            isStart = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socketChannel.connect(new InetSocketAddress(ip, port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isStart) {
            try {
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null){
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key){
        SocketChannel sc = (SocketChannel)key.channel();
        if(key.isValid()){
            try {
                if(key.isConnectable()){
                    sc.finishConnect();
                }else if(key.isReadable()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(byteBuffer);
                    if(readBytes>0){
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        String result = new String(bytes,"UTF-8");
                        System.out.println("client--receive return message: " + result);
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

    public void stop(){
        isStart = false;
    }

    public void sendMessage(String message){
        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        doWrite(socketChannel, message);
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
