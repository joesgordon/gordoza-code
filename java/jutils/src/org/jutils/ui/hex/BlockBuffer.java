package org.jutils.ui.hex;

import java.io.File;
import java.io.IOException;

import org.jutils.io.FileStream;
import org.jutils.io.IStream;

public class BlockBuffer
{
    /**  */
    public long startOffset;
    /**  */
    public long fileLength;
    /**  */
    public int maxBufferSize;
    /**  */
    public File currentFile;
    /**  */
    public IStream byteStream;

    public BlockBuffer()
    {
        this.startOffset = 0;
        this.fileLength = 0;
        this.maxBufferSize = HexBufferSize.LARGE.size;
        this.currentFile = null;
        this.byteStream = null;
    }

    public void openFile( File file ) throws IOException
    {
        currentFile = file;
        if( byteStream != null )
        {
            closeFile();
        }
        byteStream = new FileStream( file, true );
        fileLength = byteStream.getLength();
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void closeFile() throws IOException
    {
        if( byteStream != null )
        {
            byteStream.close();
            byteStream = null;
            startOffset = 0;
            fileLength = 0;
        }
    }

    public DataBlock loadBufferAt( long pos ) throws IOException
    {
        this.startOffset = pos;

        int bufLen = ( int )Math.min( maxBufferSize, fileLength - startOffset );
        byte [] buffer = new byte[bufLen];

        // LogUtils.printDebug( "Loading buffer @ " + startOffset + " , " +
        // percent + "%" );

        byteStream.seek( pos );
        byteStream.readFully( buffer );

        return new DataBlock( pos, buffer );
    }

    public static final class DataBlock
    {
        public final long position;
        public final byte [] buffer;

        public DataBlock( long position, byte [] buffer )
        {
            this.position = position;
            this.buffer = buffer;
        }
    }

    public long getPreviousPosition()
    {
        return startOffset - ( startOffset % maxBufferSize ) - maxBufferSize;
    }

    public long getNextPosition()
    {
        return startOffset - ( startOffset % maxBufferSize ) + maxBufferSize;
    }

    /***************************************************************************
     * @param offset
     * @return
     **************************************************************************/
    public long getBlockStart( long offset )
    {
        long blockCount = offset / maxBufferSize;

        return blockCount * maxBufferSize;
    }

    public boolean isOpen()
    {
        return currentFile != null;
    }

    public long getBufferStart( Long position )
    {
        return ( position / maxBufferSize ) * maxBufferSize;
    }
}
