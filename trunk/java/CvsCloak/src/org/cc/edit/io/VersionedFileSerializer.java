package org.cc.edit.io;

import java.io.*;

import org.cc.data.VersionedFile;
import org.jutils.io.IDataSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class VersionedFileSerializer implements IDataSerializer<VersionedFile>
{
    private StringSerializer strSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public VersionedFileSerializer()
    {
        strSerializer = new StringSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public VersionedFile read( DataInput stream ) throws IOException
    {
        VersionedFile t = new VersionedFile();

        t.setRepositoryPath( strSerializer.read( stream ) );
        t.setVersion( strSerializer.read( stream ) );

        return t;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( VersionedFile t, DataOutput stream ) throws IOException
    {
        strSerializer.write( t.getRepositoryPath(), stream );
        strSerializer.write( t.getVersion(), stream );
    }
}
