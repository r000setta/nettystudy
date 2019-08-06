package chap2.nioserver;

import chap2.timeserver.TimeServerHandler;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        int port=8080;
        new Thread(new TimeClientHandle("127.0.0.1",port)).start();
    }
}
