package com.heartdreamy.javasocket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BIOClient {
    private static int DEFAULT_SERVER_PORT = 22222;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";
    private static Socket socket = null;
    private static BufferedReader reader = null;
    private static PrintWriter writer = null;

    public static void start(){
        start(DEFAULT_SERVER_IP,DEFAULT_SERVER_PORT);
    }

    public static void start(String ip, int port){
        try {
            socket = new Socket(ip,port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop(){
        try {
            if(reader != null){
                reader.close();
                reader = null;
            }
            if(writer != null){
                writer.close();
                writer = null;
            }
            if(socket != null){
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message){
        try {
            System.out.println("client--send message:" + message);
            writer.println(message);
            System.out.println("client--receive return message: " + reader.readLine());
            if ("exit".equals(message)) {
                System.out.println("client--disconnect server socket");
                stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
