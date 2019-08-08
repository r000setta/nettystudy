package example.code;

import example.struct.Header;
import example.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    private MarshallingEncoder marshallingEncoder;

    public MessageEncoder() throws IOException{
        this.marshallingEncoder=new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        if (message==null||message.getHeader()==null){
            throw new Exception("Failed! No data info!");
        }
        Header header=message.getHeader();
        byteBuf.writeInt(header.getCrcCode());
        byteBuf.writeInt(header.getLength());
        byteBuf.writeLong(header.getSessionID());
        byteBuf.writeByte(header.getType());
        byteBuf.writeByte(header.getPriority());

        byteBuf.writeInt(header.getAttachment().size());
        String key=null;
        byte[] keyArray=null;
        Object value=null;
        for (Map.Entry<String,Object> param:header.getAttachment().entrySet()){
            key=param.getKey();
            keyArray=key.getBytes("UTF-8");
            byteBuf.writeInt(keyArray.length);
            byteBuf.writeBytes(keyArray);
            value=param.getValue();
            marshallingEncoder.encode(value,byteBuf);
        }
        key=null;
        keyArray=null;
        value=null;
        Object body=message.getBody();
        if (body!=null){
            this.marshallingEncoder.encode(body,byteBuf);
        }else {
            byteBuf.writeInt(0);
        }

        byteBuf.setInt(4,byteBuf.readableBytes()-8);
    }
}
