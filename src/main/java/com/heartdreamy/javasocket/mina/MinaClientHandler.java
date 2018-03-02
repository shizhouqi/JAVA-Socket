package com.heartdreamy.javasocket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

public class MinaClientHandler extends IoHandlerAdapter {

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("程序出现异常"+cause);
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = message.toString();
        System.out.println("客户端收到消息："+str);
        if(str.equals("close")){
            session.closeOnFlush();
        }
        if(str.equals("+"))
            session.write("-");
    }

    public void messageSent(IoSession session, Object message) throws Exception {

    }

    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        System.out.println("客户端关闭session连接!");
    }

    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("客户端创建session连接!");
        IoSessionConfig config = session.getConfig();
        if (config instanceof SocketSessionConfig) {
            SocketSessionConfig sessionConfig = (SocketSessionConfig) config;
            sessionConfig.setKeepAlive(true);// 长连接
        }
    }

    public void sessionIdle(IoSession session, IdleStatus arg1) throws Exception {
        System.out.println("客户端session连接空闲！");
        if(session != null){
            session.closeNow();
        }
    }

    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("客户端开始session连接!");
    }

}
