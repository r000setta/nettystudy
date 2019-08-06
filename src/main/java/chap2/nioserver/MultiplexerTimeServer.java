package chap2.nioserver;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel channel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port){
        try {
            selector=Selector.open();
            channel=ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port),1024);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop=true;
    }

    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys=selector.selectedKeys();
                Iterator<SelectionKey> it=selectionKeys.iterator();
                SelectionKey key=null;
                while (it.hasNext()){
                    key=it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if (key!=null){
                            key.cancel();
                            if (key.channel()!=null)
                                key.channel().close();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel serverSocketChannel=(ServerSocketChannel)key.channel();
                SocketChannel socketChannel=serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            if (key.isReadable()){
                SocketChannel socketChannel=(SocketChannel)key.channel();
                ByteBuffer readBuffer=ByteBuffer.allocate(1024);
                int readBytes=socketChannel.read(readBuffer);
                if (readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("The time server receive order: "+body);
                    String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date((System.currentTimeMillis())).toString():"BAD ORDER";
                    doWrite(socketChannel,currentTime);
                }else if (readBytes<0){
                    key.cancel();
                    socketChannel.close();
                }else {

                }
            }
        }
    }

    private void doWrite(SocketChannel channel,String response) throws IOException{
        if (response!=null && response.trim().length()>0){
            byte[] bytes=response.getBytes();
            ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
