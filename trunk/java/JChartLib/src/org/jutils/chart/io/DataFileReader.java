package org.jutils.chart.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.DefaultSeries;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ISeriesData;
import org.jutils.io.IReader;
import org.jutils.io.RuntimeFormatException;

public class DataFileReader implements IReader<ISeriesData, File>
{
    public DataFileReader()
    {
    }

    @Override
    public ISeriesData read( File f ) throws IOException,
        RuntimeFormatException
    {
        List<XYPoint> points = new ArrayList<>();
        String line;
        double x;
        double y;

        try( BufferedReader reader = new BufferedReader( new FileReader( f ) ) )
        {
            while( ( line = reader.readLine() ) != null )
            {
                if( line.startsWith( "%" ) )
                {
                    continue;
                }

                line = line.trim();

                String [] values = line.split( "\\s+" );

                if( values.length < 2 )
                {
                    continue;
                }

                try
                {
                    x = Double.parseDouble( values[0] );

                    String val = values[values.length - 1];

                    if( val.equals( "999999999.999999999" ) )
                    {
                        y = Double.NaN;
                    }
                    else
                    {
                        y = Double.parseDouble( val );
                    }

                    points.add( new XYPoint( x, y ) );
                }
                catch( NumberFormatException ex )
                {
                    // ignore/don't add point.
                }
            }
        }

        return new DefaultSeries( points );
    }

}
