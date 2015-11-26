package org.cc.edit.io;

import java.io.File;
import java.io.IOException;

import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class FileSerializer implements IDataSerializer<File>
{
    private StringSerializer ss;

    public FileSerializer()
    {
        ss = new StringSerializer();
    }

    @Override
    public File read( IDataStream stream )
        throws IOException, ValidationException
    {
        return new File( ss.read( stream ) );
    }

    @Override
    public void write( File t, IDataStream stream ) throws IOException
    {
        ss.write( t.getAbsolutePath(), stream );
    }
}
