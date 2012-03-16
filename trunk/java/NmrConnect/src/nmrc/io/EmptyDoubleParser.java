package nmrc.io;

import org.jutils.io.IParser;

public class EmptyDoubleParser implements IParser<Double>
{
    @Override
    public Double parseItem( String str ) throws IllegalArgumentException
    {
        Double d = null;

        if( str != null && str.length() > 0 )
        {
            try
            {
                d = Double.parseDouble( str );
            }
            catch( NumberFormatException ex )
            {
                throw new IllegalArgumentException( ex );
            }
        }

        return d;
    }
}
