package chap3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 基于Netty构建的服务器端
 */
public class NettyTimeServer {
    public void bind(int port) throws Exception{
        //配置服务端的NIO线程组,一个用于接收客户端连接，一个用于进行SocketChannel网络读写
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            //启动NIO服务端的辅助启动类
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //绑定IO事件的处理类，用于处理网络IO事件，如记录日志，消息编解码
                    .childHandler(new ChildChannelHandler());
            //绑定端口，同步等待成功，返回ChannelFuture，用于异步操作的通知回调
            ChannelFuture f=b.bind(port).sync();

            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new NettyServerHandler());
        }
    }

    public static void main(String[] args) throws Exception{
        int port=8080;
        new NettyTimeServer().bind(port);
    }

}


