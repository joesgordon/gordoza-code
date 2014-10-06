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
    private final DataLineReader lineReader;

    public DataFileReader()
    {
        this.lineReader = new DataLineReader();
    }

    @Override
    public ISeriesData read( File f ) throws IOException,
        RuntimeFormatException
    {
        List<XYPoint> points = new ArrayList<>();
        String line;
        XYPoint point;

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

        return new DefaultSeries( points );
    }
}
