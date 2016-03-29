package org.jutils.datadist;

import java.util.ArrayList;
import java.util.List;

import org.jutils.io.StringPrintStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataDistribution
{
    /**  */
    public final List<DataRecord> records;

    /***************************************************************************
     * @param size
     **************************************************************************/
    public DataDistribution( int size )
    {
        this.records = new ArrayList<>( size );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getDescription()
    {
        String desc = null;

        try( StringPrintStream stream = new StringPrintStream() )
        {
            for( int i = 0; i < records.size(); i++ )
            {
                DataRecord dr = records.get( i );
                stream.println( "#%03d %08X: %d", i + 1, dr.data, dr.count );
            }

            desc = stream.toString();
        }

        return desc;
    }
}
