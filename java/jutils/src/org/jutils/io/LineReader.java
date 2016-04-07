package org.jutils.io;

import java.io.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LineReader implements Closeable
{
    /**  */
    private final RandomAccessFile reader;

    /**  */
    private long lineNumber;

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     **************************************************************************/
    public LineReader( File file ) throws FileNotFoundException
    {
        this.reader = new RandomAccessFile( file, "r" );
        this.lineNumber = -1;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        reader.close();
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public String readLine() throws IOException
    {
        lineNumber++;
        return reader.readLine();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getLastLineNumberRead()
    {
        return lineNumber;
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public long getPosition() throws IOException
    {
        return reader.getFilePointer();
    }
}
