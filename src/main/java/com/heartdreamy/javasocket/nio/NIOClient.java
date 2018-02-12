package com.heartdreamy.javasocket.nio;

public class NIOClient {
    private static int DEFAULT_SERVER_PORT = 22222;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";
    private static NIOClientHandler nioClientHander;
    private static volatile boolean isStart = false;

    public static void start(){
        start(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT);
    }

    public static synchronized void start(String ip, int port){
        if(isStart)
            return;
        else {
            isStart = true;
            nioClientHander = new NIOClientHandler(ip, port);
            new Thread(nioClientHander).start();
        }
    }

    public static synchronized void stop(){
        isStart = false;
        nioClientHander.stop();
    }

    public static void sendMessage(String message){
        System.out.println("client--send message:" + message);
        nioClientHander.sendMessage(message);
        if ("exit".equals(message)) {
            System.out.println("client--disconnect server socket");
            stop();
        }
    }
}
