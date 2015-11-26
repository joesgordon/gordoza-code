package org.cc.edit.io;

import java.io.IOException;

import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class StringSerializer implements IDataSerializer<String>
{
    @Override
    public String read( IDataStream stream )
        throws IOException, ValidationException
    {
        String str = null;
        int numChars = stream.readInt();

        if( numChars > 2048 )
        {
            throw new ValidationException( "Cannot read string of size 0x" +
                Integer.toHexString( numChars ).toUpperCase() );
        }

        if( numChars > 0 )
        {
            byte[] bytes = new byte[numChars];

            stream.readFully( bytes );
            str = new String( bytes );
        }
        return str;
    }

    @Override
    public void write( String t, IDataStream stream ) throws IOException
    {
        if( t == null )
        {
            stream.writeInt( 0 );
        }
        else
        {
            stream.writeInt( t.length() );
            stream.write( t.getBytes() );
        }
    }
}
