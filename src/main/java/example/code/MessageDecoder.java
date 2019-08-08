package example.code;

import example.struct.Header;
import example.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {
    private MarshallingDecoder marshallingDecoder;

    public MessageDecoder(int maxFrame,int lengthFieldOffset,int lengthFieldLength) throws IOException{
        super(maxFrame,lengthFieldOffset,lengthFieldLength);
        this.marshallingDecoder=new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame=(ByteBuf)super.decode(ctx,in);
        if (frame==null){
            return null;
        }

        Message message=new Message();
        Header header=new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());
        int size=frame.readInt();
        if (size>0){
            Map<String,Object> attachment=new HashMap<>(size);
            int keySize=0;
            byte[] keyArray=null;
            String key=null;
            for (int i=0;i<size;i++){
                keySize=frame.readInt();
                keyArray=new byte[keySize];
                frame.readBytes(keyArray);
                key=new String(keyArray,"UTF-8");
                attachment.put(key,marshallingDecoder.decode(frame));
            }
            keyArray=null;
            key=null;
        }
        message.setHeader(header);
        if (frame.readableBytes()>4){
            message.setBody(marshallingDecoder.decode(frame));
        }
        return message;
    }
}
