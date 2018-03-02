package com.heartdreamy.javasocket.netty;

import java.util.Scanner;

public class NettyClientStart {
    public static void main(String [] args){
        NettyClient.start();
        Scanner scanner = new Scanner(System.in);
        String message  = scanner.nextLine();
        while(true){
            NettyClient.sendMessage(message);
            if("close".equals(message)){
                NettyClient.stop();
                break;
            }
            message = scanner.nextLine();
        }
    }
}
