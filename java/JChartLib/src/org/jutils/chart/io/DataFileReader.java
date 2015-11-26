package org.jutils.chart.io;

import java.io.*;
import java.util.ArrayList;

import org.jutils.ValidationException;
import org.jutils.chart.data.DefaultSeries;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ISeriesData;
import org.jutils.io.IReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataFileReader implements IReader<ISeriesData<?>, File>
{
    /**  */
    private final DataLineReader lineReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataFileReader()
    {
        this.lineReader = new DataLineReader();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public DefaultSeries read( File f ) throws IOException, ValidationException
    {
        ArrayList<XYPoint> points = new ArrayList<>();
        String line;
        XYPoint point;

        // LogUtils.printDebug( "Reading " + f.getName() );

        try( BufferedReader reader = new BufferedReader( new FileReader( f ) ) )
        {
            while( ( line = reader.readLine() ) != null )
            {
                point = lineReader.read( line );

                if( point != null )
                {
                    points.add( point );
                }
            }
        }

        // LogUtils.printDebug( "Done Reading " + f.getName() );

        // XYPoint [] array = points.toArray( new XYPoint[points.size()] );
        //
        // return new ArraySeries( array );

        return new DefaultSeries( points );
    }
}
