package example.code;


import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

public class MarshallingDecoder {
    private Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        this.unmarshaller=MarshallingCodeFactory.buildUnMarshalling();
    }

    public Object decode(ByteBuf in) throws Exception{
        try {
            int bodySize=in.readInt();
            int readIndex=in.readerIndex();
            ByteBuf buf=in.slice(readIndex,bodySize);
            ChannelBufferByteInput input=new ChannelBufferByteInput(buf);
            this.unmarshaller.start(input);
            Object ret=this.unmarshaller.readObject();
            this.unmarshaller.finish();
            in.readerIndex(in.readerIndex()+bodySize);
            return ret;
        }finally {
            this.unmarshaller.close();
        }
    }
}
