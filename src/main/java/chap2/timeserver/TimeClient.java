package chap2.timeserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 同步阻塞式IO客户端
 */
public class TimeClient {
    public static void main(String[] args) {
        int port=8080;
        try(Socket socket=new Socket("127.0.0.1",port);
                BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out=new PrintWriter(socket.getOutputStream(),true)) {
            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed");
            String resp=in.readLine();
            System.out.println("Now is:"+resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
