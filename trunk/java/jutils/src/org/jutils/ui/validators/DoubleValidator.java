package org.jutils.ui.validators;

import org.jutils.ui.validation.ValidationException;


/*******************************************************************************
 * A validator that ensures that the provided text represents a double.
 ******************************************************************************/
public class DoubleValidator implements IDataValidator<Double>
{
    private final Double min;
    private final Double max;

    public DoubleValidator()
    {
        this( null, null );
    }

    public DoubleValidator( Double min, Double max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double validate( String text ) throws ValidationException
    {
        try
        {
            double d = Double.parseDouble( text );

            testBounds( d );

            return d;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }

    private void testBounds( double d ) throws ValidationException
    {
        boolean exceedsMin = min != null && d < min;
        boolean exceedsMax = max != null && d > max;

        if( exceedsMin || exceedsMax )
        {
            String msg = "";

            if( min != null )
            {
                msg += min + " <= ";
            }

            msg += "x ";

            if( max != null )
            {
                msg += "<= " + max;
            }

            throw new ValidationException( "Number (" + d +
                ") out of bounds: " + msg );
        }
    }
}
