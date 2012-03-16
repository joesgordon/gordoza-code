package org.cc.edit.io;

import java.io.*;

import org.cc.data.ClosedTask;
import org.cc.data.VersionedFile;
import org.jutils.io.IDataSerializer;

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
    public ClosedTask read( DataInput stream ) throws IOException
    {
        ClosedTask t = new ClosedTask();

        t.setName( strSerializer.read( stream ) );
        t.setFiles( vfsSerializer.read( stream ) );

        return t;
    }

    @Override
    public void write( ClosedTask t, DataOutput stream ) throws IOException
    {
        strSerializer.write( t.getName(), stream );
        vfsSerializer.write( t.getFiles(), stream );
    }
}
