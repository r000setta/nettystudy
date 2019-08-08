package example.code;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

public class MarshallingEncoder {

    private static final byte[] LENGTH_PLACEHOLDER=new byte[4];

    private Marshaller marshaller;

    public MarshallingEncoder() throws IOException{
        this.marshaller=MarshallingCodeFactory.buildMarshalling();
    }

    public void encode(Object body, ByteBuf out) throws IOException{
        try {
            int lengthPos=out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output=new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(body);
            int endPos=out.writerIndex();
            out.setInt(lengthPos,endPos-lengthPos-4);
        }finally {
            marshaller.close();
        }
    }
}
