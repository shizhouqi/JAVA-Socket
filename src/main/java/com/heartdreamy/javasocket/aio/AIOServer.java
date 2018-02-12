package com.heartdreamy.javasocket.aio;

import java.io.IOException;

public class AIOServer {
    private static int DEFAULT_PORT = 22222;
    private static AIOServerHandler aioServerHandler;
    private static boolean isStart = false;
    private static int DEFAULT_THREAD_SIZE = 10;

    public static void start(){
        start(DEFAULT_PORT, DEFAULT_THREAD_SIZE);
    }

    public static synchronized void start(int port, int threadSize){
        if(isStart)
            return;
        aioServerHandler = new AIOServerHandler(port, threadSize);
        new Thread(aioServerHandler).start();
        isStart = true;
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void stop(){
        isStart = false;
        aioServerHandler.stop();
    }
}
