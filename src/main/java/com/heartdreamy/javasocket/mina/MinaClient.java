package com.heartdreamy.javasocket.mina;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClient {

    private static  String HOST = "127.0.0.1";
    private static  int PORT = 8080;
    private static final String CHARSET = "UTF-8";
    private static SocketConnector connector;
    private static IoSession session;

    public static void start(){
        start(HOST, PORT);
    }

    public static void start(String ip, int port){
        HOST = ip;
        PORT = port;
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName(MinaClient.CHARSET))));
        //使用拦截器实现断线重连
        connector.getFilterChain().addFirst("reconnection", new IoFilterAdapter(){
            public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception{
                reconnect();
            }
        });
        //使用监听器实现断线重连
        //connector.addListener(new SessionReconnectListener());

        connector.getSessionConfig().setReceiveBufferSize(10240);   // 设置接收缓冲区的大小
        connector.getSessionConfig().setSendBufferSize(10240);// 设置输出缓冲区的大小
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30000);  //读写都空闲时间:30秒
        connector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 40000);//读(接收通道)空闲时间:40秒
        connector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, 50000);//写(发送通道)空闲时间:50秒

        connector.setConnectTimeoutMillis(20 * 1000);
        connector.setHandler(new MinaClientHandler());
        connect();
    }

    public static void stop(){
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }

    public static void sendMessage(String message){
        System.out.println("client--send message:" + message);
        session.write(message);
    }

    private static void connect(){
        ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));
        future.awaitUninterruptibly();
        session = future.getSession();
        System.out.println("客户端正在连接服务器，"+HOST+":"+PORT);
    }

    public static void reconnect(){
        while (true){
            try {
                Thread.sleep(3000);
                connect();
                if (session != null && session.isConnected()) {
                    System.out.println("断线重连成功");
                    break;
                }
            }catch (Exception e){
                System.out.println("断线重连失败" + e);
            }
        }
    }
}
