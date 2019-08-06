package chap2.aioserver;

public class AIOClient {
    public static void main(String[] args) throws Exception{
        int port=8080;
        new Thread(new AsyncTimeClientHandler("127.0.0.1",port)).start();
    }
}
