package com.heartdreamy.javasocket.aio;

import java.util.Scanner;

public class AIOClientStart {
    public static void main(String [] args){
        AIOClient.start();
        Scanner scanner = new Scanner(System.in);
        String message  = scanner.nextLine();
        while(true){
            AIOClient.sendMessage(message);
            if("exit".equals(message)){
                break;
            }
            message = scanner.nextLine();
        }
        AIOClient.stop();
    }
}
