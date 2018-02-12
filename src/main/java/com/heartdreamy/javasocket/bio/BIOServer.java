package com.heartdreamy.javasocket.bio;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class BIOServer {
	private static int DEFAULT_PORT = 22222;
	private static ServerSocket server;
	private static volatile boolean isStart = false;
	public static void start(){
		start(DEFAULT_PORT);
	}
	public synchronized static void start(int port){
		if( isStart)
			return;
		try {
			isStart = true;
			server = new ServerSocket(port);
			System.out.println("server is start, port:" + port);
			while (isStart){
                Socket socket = server.accept();
                new Thread(new BIOServerHandler(socket)).start();
            }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != server){
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("server is closed.");
				server = null;
			}
		}

	}

	public static void stop(){
		isStart = false;
	}
}
