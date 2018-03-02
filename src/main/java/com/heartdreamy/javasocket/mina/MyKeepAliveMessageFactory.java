package com.heartdreamy.javasocket.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {

    String req = "+";
    String resp = "-";

    public MyKeepAliveMessageFactory(){
    }

    public boolean isRequest(IoSession ioSession, Object o) {
        boolean isReq = (o!=null && req.equals(o.toString()));

        System.out.println(o.toString() + " : isRequest : " + isReq);

        return isReq;
    }

    public boolean isResponse(IoSession ioSession, Object o) {
        boolean isResp = (o!=null && resp.equals(o.toString()));

        System.out.println(o.toString() + " : isResponse : " + isResp);

        return isResp;
    }

    public Object getRequest(IoSession ioSession) {
        System.out.println("getRequest");
        return req;
    }

    public Object getResponse(IoSession ioSession, Object o) {
        System.out.println("getResponse");
        return resp;
    }
}