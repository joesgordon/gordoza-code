package org.mc.io;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jutils.ValidationException;
import org.jutils.io.IDataStream;
import org.jutils.io.IStdSerializer;
import org.mc.messages.StringMessage;

public class StringMessageSerializer
    implements IStdSerializer<StringMessage, IDataStream>
{
    private final Charset utf8;

    public StringMessageSerializer()
    {
        this.utf8 = Charset.forName( "UTF-8" );
    }

    @Override
    public StringMessage read( IDataStream stream )
        throws IOException, ValidationException
    {
        int len = stream.readInt();
        byte[] bytes = new byte[len];

        stream.read( bytes );

        String str = new String( bytes, utf8 );

        return new StringMessage( str );
    }

    @Override
    public void write( StringMessage msg, IDataStream stream )
        throws IOException
    {
        stream.writeInt( msg.messageString.length() );
        stream.write( msg.messageString.getBytes( utf8 ) );
    }
}
