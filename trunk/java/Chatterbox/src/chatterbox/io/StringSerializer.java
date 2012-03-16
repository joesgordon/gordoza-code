package chatterbox.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

public class StringSerializer implements IDataSerializer<String>
{
    public StringSerializer()
    {
        ;
    }

    @Override
    public String read( DataInput stream ) throws IOException
    {
        int length = stream.readInt();
        byte[] bytes = new byte[length];
        stream.readFully( bytes );

        return new String( bytes );
    }

    @Override
    public void write( String string, DataOutput stream ) throws IOException
    {
        stream.writeInt( string.length() );
        stream.writeBytes( string );
    }

}
