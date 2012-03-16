package org.duak.utils;

import org.duak.model.LongComparer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileSize implements Comparable<FileSize>
{
    /**  */
    private long size;
    /**  */
    private String str;

    /***************************************************************************
     * @param size
     **************************************************************************/
    public FileSize( long size )
    {
        int count = 0;
        double frac = size;
        String units;

        for( ; frac > 1024; count++ )
        {
            frac /= 1024.0;
        }

        switch( count )
        {
            case 0:
                units = " bytes";
                break;
            case 1:
                units = " KB";
                break;
            case 2:
                units = " MB";
                break;
            case 3:
                units = " GB";
                break;
            default:
                units = " Really?(" + count + ")";
                break;
        }

        // str = frac + units;
        str = String.format( "%.2f%s", frac, units );
        this.size = size;
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
