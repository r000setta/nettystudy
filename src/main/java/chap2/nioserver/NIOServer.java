package chap2.nioserver;

/**
 * NIO Servers
 */
public class NIOServer {
    public static void main(String[] args) {
        int port=8080;
        MultiplexerTimeServer timeServer=new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-001").start();
        System.out.println("Main over");
    }
}
