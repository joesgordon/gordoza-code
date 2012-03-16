package org.cc.edit.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

public class FileSerializer implements IDataSerializer<File>
{
    private StringSerializer ss;

    public FileSerializer()
    {
        ss = new StringSerializer();
    }

    @Override
    public File read( DataInput stream ) throws IOException
    {
        return new File( ss.read( stream ) );
    }

    @Override
    public void write( File t, DataOutput stream ) throws IOException
    {
        ss.write( t.getAbsolutePath(), stream );
    }
}
