package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.LockInfo;
import org.jutils.io.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LockInfoSerializer implements IDataSerializer<LockInfo>
{
    /**  */
    private StringSerializer ss;

    /***************************************************************************
     * 
     **************************************************************************/
    public LockInfoSerializer()
    {
        ss = new StringSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public LockInfo read( IDataStream stream ) throws IOException,
        RuntimeFormatException
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( LockInfo t, IDataStream stream ) throws IOException
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
