package org.jutils.ui.hex;

import java.io.File;
import java.io.IOException;

import org.jutils.io.FileStream;
import org.jutils.io.IStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlockBuffer
{
    /**  */
    public long startOffset;
    /**  */
    public long fileLength;
    /**  */
    private int maxBufferSize;
    /**  */
    public File currentFile;
    /**  */
    public IStream byteStream;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlockBuffer()
    {
        this.startOffset = 0;
        this.fileLength = 0;
        this.maxBufferSize = HexBufferSize.LARGE.size;
        this.currentFile = null;
        this.byteStream = null;
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
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

    /***************************************************************************
     * @param pos
     * @return
     * @throws IOException
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getPreviousPosition()
    {
        long lastOffset = startOffset - ( startOffset % maxBufferSize ) -
            maxBufferSize;
        lastOffset = Math.max( lastOffset, 0 );
        return lastOffset;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getNextPosition()
    {
        return startOffset - ( startOffset % maxBufferSize ) + maxBufferSize;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getLastPosition()
    {
        return getBlockStart( fileLength - 1 );
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

    /***************************************************************************
     * @return
     */
    public boolean isOpen()
    {
        return currentFile != null;
    }

    /***************************************************************************
     * @param position
     * @return
     **************************************************************************/
    public long getBufferStart( Long position )
    {
        return ( position / maxBufferSize ) * maxBufferSize;
    }

    public long setBufferSize( int size )
    {
        this.maxBufferSize = size;

        return getBufferStart( startOffset );
    }

    public long getBufferSize()
    {
        return maxBufferSize;
    }
}
