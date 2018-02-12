package com.heartdreamy.javasocket.bio;

import java.util.Scanner;

public class BIOClientStart {
    public static void main(String [] args){
        BIOClient.start();
        Scanner scanner = new Scanner(System.in);
        String message  = scanner.nextLine();
        while(true){
            BIOClient.sendMessage(message);
            if("exit".equals(message)){
                break;
            }
            message = scanner.nextLine();
        }
    }
}
