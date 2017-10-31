package org.jutils.net;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.jutils.ValidationException;
import org.jutils.io.*;

public class NetMessageSerializer implements IDataSerializer<NetMessage>
{
    private final Charset utf8 = Charset.forName( "UTF-8" );
    private final LocalDateTimeSerializer timeSerializer = new LocalDateTimeSerializer();

    @Override
    public NetMessage read( IDataStream stream )
        throws IOException, ValidationException
    {
        boolean received = stream.readBoolean();

        LocalDateTime time = timeSerializer.read( stream );

        int strLen = stream.readInt();
        byte [] strBytes = new byte[strLen];
        stream.read( strBytes );
        String address = new String( strBytes, utf8 );

        int port = stream.readInt();

        int contentsLen = stream.readInt();
        byte [] contents = new byte[contentsLen];

        stream.read( contents );

        return new NetMessage( received, time, address, port, contents );
    }

    @Override
    public void write( NetMessage msg, IDataStream stream ) throws IOException
    {
        stream.writeBoolean( msg.received );

        timeSerializer.write( msg.time, stream );

        stream.writeInt( msg.address.length() );
        stream.write( msg.address.getBytes( utf8 ) );

        stream.writeInt( msg.port );

        stream.writeInt( msg.contents.length );
        stream.write( msg.contents );
    }
}
