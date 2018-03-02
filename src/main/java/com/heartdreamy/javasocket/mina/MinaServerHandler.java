package com.heartdreamy.javasocket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MinaServerHandler extends IoHandlerAdapter {

    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("服务器创建session连接!");
    }

    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("服务器打开session连接!");
    }

    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("服务器关闭session连接!");
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("服务器session空闲!");
    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("服务器出现异常"+cause);
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = message.toString();
        System.out.println("服务器收到消息："+str);
        session.write(str);
        if(str.trim().equals("close")){
            session.closeOnFlush();
        }
    }

    public void messageSent(IoSession session, Object message) throws Exception {

    }
}
