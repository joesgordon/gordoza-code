package chatterbox.io;

import java.io.IOException;

import org.jutils.io.*;

public class StringSerializer implements IDataSerializer<String>
{
    @Override
    public String read( IDataStream stream ) throws IOException
    {
        int length = stream.readInt();
        byte[] bytes = new byte[length];

        stream.readFully( bytes );

        return new String( bytes, IOUtils.US_ASCII );
    }

    @Override
    public void write( String string, IDataStream stream ) throws IOException
    {
        stream.writeInt( string.length() );
        stream.write( string.getBytes( IOUtils.US_ASCII ), 0, string.length() );
    }
}
