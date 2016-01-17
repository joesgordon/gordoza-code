package org.jutils.datadist;

import java.util.*;
import java.util.Map.Entry;

import org.jutils.io.ByteUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DistributionBuilder
{
    /**  */
    private final Map<Integer, Long> findings;

    /***************************************************************************
     * 
     **************************************************************************/
    public DistributionBuilder()
    {
        this.findings = new HashMap<>();
    }

    /***************************************************************************
     * @param buffer
     **************************************************************************/
    public void addData( byte [] buffer )
    {
        addData( buffer, 0, buffer.length );
    }

    /***************************************************************************
     * @param buffer
     * @param index
     * @param length
     **************************************************************************/
    public void addData( byte [] buffer, int index, int length )
    {
        int cnt = length - 3;

        if( !findings.isEmpty() )
        {
            DataDistribution dd = buildDistribution();

            findings.clear();

            for( DataRecord dr : dd.records )
            {
                findings.put( dr.data, dr.count );
            }
        }

        for( int i = index; i < cnt; i++ )
        {
            int value = ByteUtils.getInteger( buffer, i );

            addValue( value );
        }
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    public void addValue( int value )
    {
        Long cnt = findings.get( value );
        cnt = cnt == null ? 1 : cnt + 1;
        findings.put( value, cnt );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DataDistribution buildDistribution()
    {
        Set<Entry<Integer, Long>> set = findings.entrySet();
        DataDistribution dd = new DataDistribution( set.size() );

        for( Entry<Integer, Long> e : set )
        {
            dd.records.add( new DataRecord( e.getKey(), e.getValue() ) );
        }

        // LogUtils.printInfo( "\t\tClearing: %d", dd.records.size() );
        findings.clear();

        // LogUtils.printInfo( "\t\tSorting" );
        Collections.sort( dd.records, new CountComparator() );

        if( dd.records.size() > 100 )
        {
            dd.records.subList( 100, dd.records.size() ).clear();
        }

        return dd;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CountComparator implements Comparator<DataRecord>
    {
        @Override
        public int compare( DataRecord this1, DataRecord that1 )
        {
            int cmp = Long.compare( that1.count, this1.count );
            return cmp != 0 ? cmp : Integer.compare( this1.data, that1.data );
        }
    }
}
