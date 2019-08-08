package example.code;

import org.jboss.marshalling.*;

import java.io.IOException;

public class MarshallingCodeFactory {

    /**
     * build JBoss Marshaller encode object
     * @return
     * @throws Exception
     */
    public static Marshaller buildMarshalling() throws IOException {
        final MarshallerFactory marshallerFactory= Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller=marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }

    /**
     * build JBoss Unmarshalling decode object
     * @return
     * @throws Exception
     */
    public static Unmarshaller buildUnMarshalling() throws IOException{
        final MarshallerFactory marshallerFactory=Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        Unmarshaller unmarshaller=marshallerFactory.createUnmarshaller(configuration);
        return unmarshaller;
    }
}
