package org.cc.edit.io;

import java.io.*;

import org.cc.data.*;
import org.jutils.io.IDataSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class OpenTaskSerializer implements IDataSerializer<OpenTask>
{
    /**  */
    private StringSerializer strSerializer;
    /**  */
    private ListSerializer<VersionedFile> vfsSerializer;
    /**  */
    private ListSerializer<Sandbox> sandboxesSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public OpenTaskSerializer()
    {
        strSerializer = new StringSerializer();
        vfsSerializer = new ListSerializer<VersionedFile>(
            new VersionedFileSerializer() );
        sandboxesSerializer = new ListSerializer<Sandbox>(
            new SandboxSerializer() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public OpenTask read( DataInput stream ) throws IOException
    {
        OpenTask t = new OpenTask();

        t.setName( strSerializer.read( stream ) );
        t.setApproved( stream.readBoolean() );
        t.setSandboxes( sandboxesSerializer.read( stream ) );
        t.setFiles( vfsSerializer.read( stream ) );

        return t;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( OpenTask t, DataOutput stream ) throws IOException
    {
        strSerializer.write( t.getName(), stream );
        stream.writeBoolean( t.isApproved() );
        sandboxesSerializer.write( t.getSandboxes(), stream );
        vfsSerializer.write( t.getFiles(), stream );
    }
}
