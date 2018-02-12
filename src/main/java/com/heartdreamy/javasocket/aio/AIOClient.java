package com.heartdreamy.javasocket.aio;

public class AIOClient {
    private static int DEFAULT_SERVER_PORT = 22222;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";
    private static boolean isStart = false;
    private static AIOClientHandler aioClientHandler;

    public static void start(){
        start(DEFAULT_SERVER_IP,DEFAULT_SERVER_PORT);
    }

    public static synchronized void start(String ip ,int port){
        aioClientHandler = new AIOClientHandler(ip, port);
        new Thread(aioClientHandler).start();
    }

    public static synchronized void stop(){
        aioClientHandler.stop();
        isStart = false;
    }

    public static void sendMessage(String message){
        aioClientHandler.sendMessage(message);
    }
}
