package com.heartdreamy.javasocket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BIOServerHandler implements Runnable {
    private Socket socket;
    public BIOServerHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
            String readStr;
            while(true){
                readStr = reader.readLine();
                if(null != readStr){
                    System.out.println("server--received message:" + readStr);
                    writer.println("server received massage:" + readStr);
                }
                if("exit".equals(readStr)){
                    System.out.println("server--disconnect client socket");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    }
}
