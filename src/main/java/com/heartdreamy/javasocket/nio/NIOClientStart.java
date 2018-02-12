package com.heartdreamy.javasocket.nio;

import java.util.Scanner;

public class NIOClientStart {
    public static void main(String [] args){
        NIOClient.start();
        Scanner scanner = new Scanner(System.in);
        String message  = scanner.nextLine();
        while(true){
            NIOClient.sendMessage(message);
            if("exit".equals(message)){
                break;
            }
            message = scanner.nextLine();
        }
        NIOClient.stop();
    }
}
