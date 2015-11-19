package org.jutils.io;

import java.io.File;
import java.util.Comparator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LargestToSmallestFileSorter implements Comparator<File>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compare( File thisOne, File thatOne )
    {
        long thisLen = thisOne.length();
        long thatLen = thatOne.length();

        if( thisLen < thatLen )
        {
            return 1;
        }
        else if( thisLen > thatLen )
        {
            return -1;
        }

        return 0;
    }
}
