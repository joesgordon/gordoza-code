package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.VersionedFile;
import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

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
    public VersionedFile read( IDataStream stream )
        throws IOException, ValidationException
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
    public void write( VersionedFile t, IDataStream stream ) throws IOException
    {
        strSerializer.write( t.getRepositoryPath(), stream );
        strSerializer.write( t.getVersion(), stream );
    }
}
