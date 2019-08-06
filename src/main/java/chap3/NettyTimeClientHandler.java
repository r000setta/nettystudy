package chap3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyTimeClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger= LoggerFactory.getLogger(NettyTimeClient.class);

    private ByteBuf firstMessage;

    public NettyTimeClientHandler(){
        byte[] req="QUERY TIME ORDER".getBytes();
        firstMessage= Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    //客户端和服务端TCP链路建立成功时调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将消息发送给服务端
        ctx.writeAndFlush(firstMessage);
    }

    //服务端返回应答消息时调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req);
        logger.info("Now is {}",body);
        //释放资源
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downstream:{}",cause.getMessage());
        ctx.close();
    }
}
