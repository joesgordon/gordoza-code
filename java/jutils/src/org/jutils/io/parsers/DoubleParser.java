package org.jutils.io.parsers;

import org.jutils.io.IParser;
import org.jutils.ui.validation.ValidationException;

public class DoubleParser implements IParser<Double>
{

    @Override
    public Double parse( String str ) throws ValidationException
    {
        Double d;

        try
        {
            d = Double.parseDouble( str );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( "Cannot parse \"" + str +
                "\" as a double" );
        }

        return d;
    }

}
