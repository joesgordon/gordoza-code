package org.jutils.io;

import java.io.*;

public class LineReader implements Closeable
{
    private final RandomAccessFile reader;

    private long lineNumber;

    public LineReader( File file ) throws FileNotFoundException
    {
        this.reader = new RandomAccessFile( file, "r" );
        this.lineNumber = -1;
    }

    @Override
    public void close() throws IOException
    {
        reader.close();
    }

    public String readLine() throws IOException
    {
        lineNumber++;
        return reader.readLine();
    }

    public long getLastLineNumberRead()
    {
        return lineNumber;
    }

    public long getPosition() throws IOException
    {
        return reader.getFilePointer();
    }
}
