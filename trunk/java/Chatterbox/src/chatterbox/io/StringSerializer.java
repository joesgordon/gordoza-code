package chatterbox.io;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class StringSerializer implements IDataSerializer<String>
{
    public StringSerializer()
    {
        ;
    }

    @Override
    public String read( IDataStream stream ) throws IOException
    {
        int length = stream.readInt();
        byte[] bytes = new byte[length];
        stream.readFully( bytes );

        return new String( bytes );
    }

    @Override
    public void write( String string, IDataStream stream ) throws IOException
    {
        stream.writeInt( string.length() );
        stream.write( string.getBytes( Charset.forName( "US-ASCII" ) ) );
    }

}
