package chap2.aioserver;

public class AIOServer {
    public static void main(String[] args) {
        int port=8080;
        AsyncTimeServerHandler timeServer=new AsyncTimeServerHandler(port);
        new Thread(timeServer).start();
    }
}
