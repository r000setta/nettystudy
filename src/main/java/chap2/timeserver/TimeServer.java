package chap2.timeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞IO服务器端
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port=8080;
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(port);
            System.out.println("Server start on port:"+port);
            Socket socket=null;
            //监听客户端连接
            while (true){
                socket=serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        }finally {
            if (serverSocket!=null){
                System.out.println("Server closed");
                serverSocket.close();
                serverSocket=null;
            }
        }
    }

}
