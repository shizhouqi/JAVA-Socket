package com.heartdreamy.javasocket.bio;

public class BIOServerStart {
    public static void main(String [] args){
        //采用一个请求一个线程的BIO处理方式
        //BIOServer.start();
        //采用一个线程池，对请求分配线程的BIO处理方式
        BIOServer02.start();
    }
}
