package com.heartdreamy.javasocket.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaServer {
    private static final int PORT = 8080;
    private static final String CHARSET = "UTF-8";
    /** 30秒后超时 */
    private static final int IDELTIMEOUT = 30;
    /** 15秒发送一次心跳包 */
    private static final int HEARTBEATRATE = 15;
    private static IoAcceptor acceptor;

    public static void start(){
        start(PORT);
    }

    public static void start(int port){
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName(MinaServer.CHARSET))));
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new MyKeepAliveMessageFactory(), IdleStatus.BOTH_IDLE);
        keepAliveFilter.setRequestInterval(HEARTBEATRATE);
        keepAliveFilter.setRequestTimeout(IDELTIMEOUT);
        keepAliveFilter.setForwardEvent(false); //idle事件回发, false表示当session进入idle状态的时候,不在调用handler中的sessionIdle方法
        acceptor.getFilterChain().addLast("heartbeat", keepAliveFilter);
        acceptor.getSessionConfig().setReadBufferSize(2048);
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        acceptor.setHandler(new MinaServerHandler());
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop(){
        acceptor.dispose();
    }
}
