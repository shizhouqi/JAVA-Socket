package com.heartdreamy.javasocket.mina;


import java.util.Scanner;

public class MinaClientStart {

	public static void main(String [] args){
		MinaClient.start();
		Scanner scanner = new Scanner(System.in);
		String message  = scanner.nextLine();
		while(true){
			MinaClient.sendMessage(message);
			if("close".equals(message)){
				MinaClient.stop();
				break;
			}
			message = scanner.nextLine();
		}
	}
}
