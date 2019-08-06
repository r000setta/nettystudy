package chap2.pseudoserver;

import chap2.timeserver.TimeServerHandler;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 伪异步IO，添加线程池
 */
public class PseudoServer {
    public static void main(String[] args) {
        int port=8080;
        try(ServerSocket serverSocket=new ServerSocket(port)) {
            System.out.println("Server start on port "+port);
            Socket socket=null;
            TimeServerHandlerExecutePool executePool=new TimeServerHandlerExecutePool(50,1000);
            while (true){
                socket=serverSocket.accept();
                executePool.execute(new TimeServerHandler(socket));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
