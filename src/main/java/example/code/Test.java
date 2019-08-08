package example.code;

import example.struct.Header;
import example.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test {
    MarshallingDecoder marshallingDecoder;
    MarshallingEncoder marshallingEncoder;

    public Test() throws IOException{
        marshallingDecoder=new MarshallingDecoder();
        marshallingEncoder=new MarshallingEncoder();
    }

    public Message getMessage(){
        Message message=new Message();
        Header header=new Header();
        header.setLength(123);
        header.setSessionID(9999);
        header.setType((byte)1);
        header.setPriority((byte)7);
        Map<String,Object> attachment=new HashMap<>();
        for (int i=0;i<10;i++){
            attachment.put("name",i);
        }
        header.setAttachment(attachment);
        message.setHeader(header);
        message.setBody("Hello World");
        return message;
    }

    public ByteBuf encode(Message message) throws Exception{
        ByteBuf sendBuf= Unpooled.buffer();
        sendBuf.writeInt(message.getHeader().getCrcCode());
        sendBuf.writeInt(message.getHeader().getLength());
        sendBuf.writeLong(message.getHeader().getSessionID());
        sendBuf.writeByte(message.getHeader().getType());
        sendBuf.writeByte(message.getHeader().getPriority());
        sendBuf.writeInt(message.getHeader().getAttachment().size());
        String key=null;
        byte[] keyArray=null;
        Object value=null;
        for (Map.Entry<String,Object> param:message.getHeader().getAttachment().entrySet()){
            key=param.getKey();
            keyArray=key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value=param.getValue();
            marshallingEncoder.encode(value,sendBuf);
        }
        key=null;
        keyArray=null;
        value=null;
        if (message.getBody()!=null)
            marshallingEncoder.encode(message.getBody(),sendBuf);
        else
            sendBuf.writeInt(0);
        sendBuf.setInt(4,sendBuf.readableBytes());
        return sendBuf;
    }

    public Message decode(ByteBuf in) throws Exception {
        Message message = new Message();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        int size = in.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = in.readInt();
                keyArray = new byte[keySize];
                in.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attch.put(key, marshallingDecoder.decode(in));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attch);
        }
        if (in.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(in));
        }
        message.setHeader(header);
        return message;
    }

    public static void main(String[] args) throws Exception{
        Test test=new Test();
        Message message=test.getMessage();
        ByteBuf buf=test.encode(message);
        System.out.println(buf);
    }
}
