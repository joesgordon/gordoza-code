package org.duak.utils;

import org.duak.model.LongComparer;
import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileSize implements Comparable<FileSize>
{
    /**  */
    private final long size;
    /**  */
    private final String str;

    /***************************************************************************
     * @param size
     **************************************************************************/
    public FileSize( long size )
    {
        this.size = size;
        this.str = IOUtils.byteCount( size );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String toString()
    {
        return str;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compareTo( FileSize thatSize )
    {
        return LongComparer.compareLong( size, thatSize.size );
    }
}
