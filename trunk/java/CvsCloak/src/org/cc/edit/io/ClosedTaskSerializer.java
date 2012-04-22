package org.cc.edit.io;

import java.io.IOException;

import org.cc.data.ClosedTask;
import org.cc.data.VersionedFile;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class ClosedTaskSerializer implements IDataSerializer<ClosedTask>
{
    private StringSerializer strSerializer;
    private ListSerializer<VersionedFile> vfsSerializer;

    public ClosedTaskSerializer()
    {
        strSerializer = new StringSerializer();
        vfsSerializer = new ListSerializer<VersionedFile>(
            new VersionedFileSerializer() );
    }

    @Override
    public ClosedTask read( IDataStream stream ) throws IOException
    {
        ClosedTask t = new ClosedTask();

        t.setName( strSerializer.read( stream ) );
        t.setFiles( vfsSerializer.read( stream ) );

        return t;
    }

    @Override
    public void write( ClosedTask t, IDataStream stream ) throws IOException
    {
        strSerializer.write( t.getName(), stream );
        vfsSerializer.write( t.getFiles(), stream );
    }
}
