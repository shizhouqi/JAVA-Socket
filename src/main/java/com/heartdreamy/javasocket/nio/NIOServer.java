package com.heartdreamy.javasocket.nio;

public class NIOServer {
    private static int DEFAULT_PORT = 22222;
    private static boolean isStart = false;
    private static NIOServerHandler nioServerHandler;

    public static void start(){
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port){
        if(isStart)
            return;
        else{
            isStart = true;
            nioServerHandler = new NIOServerHandler(port);
            new Thread(nioServerHandler).start();
        }
    }

    public static synchronized void stop(){
        isStart = false;
        nioServerHandler.stop();
    }
}
