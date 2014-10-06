package org.jutils.chart.io;

import java.util.regex.Pattern;

import org.jutils.chart.data.XYPoint;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataLineReader
{
    /**  */
    private final Pattern splitter;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataLineReader()
    {
        this.splitter = Pattern.compile( "\\s+" );
    }

    /***************************************************************************
     * @param line
     * @return
     **************************************************************************/
    public XYPoint read( String line )
    {
        double x;
        double y;

        if( line.charAt( 0 ) == '%' )
        {
            return null;
        }

        line = line.trim();

        String [] values = splitter.split( line );

        if( values.length < 2 )
        {
            return null;
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
        }
        catch( NumberFormatException ex )
        {
            return null;
        }

        return new XYPoint( x, y );
    }
}
