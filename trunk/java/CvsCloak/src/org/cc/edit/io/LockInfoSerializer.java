package org.cc.edit.io;

import java.io.*;

import org.cc.data.LockInfo;
import org.jutils.io.IDataSerializer;

/**
 * 
 */
public class LockInfoSerializer implements IDataSerializer<LockInfo>
{
    /**  */
    private StringSerializer ss;

    /**
     * 
     */
    public LockInfoSerializer()
    {
        ss = new StringSerializer();
    }

    /**
     * 
     */
    @Override
    public LockInfo read( DataInput stream ) throws IOException
    {
        LockInfo info = null;
        boolean locked = stream.readBoolean();

        if( locked )
        {
            info = new LockInfo();

            info.setUser( ss.read( stream ) );
            info.setReason( ss.read( stream ) );
            info.setTime( stream.readLong() );
        }

        return info;
    }

    /**
     * 
     */
    @Override
    public void write( LockInfo t, DataOutput stream ) throws IOException
    {
        stream.writeBoolean( t != null );
        if( t != null )
        {
            ss.write( t.getUser(), stream );
            ss.write( t.getReason(), stream );
            stream.writeLong( t.getTime() );
        }
    }
}
