package org.jutils.ui.validators;

import org.jutils.ValidationException;

/*******************************************************************************
 * A validator that ensures that the provided text represents a double.
 ******************************************************************************/
public class DoubleValidator implements IDataValidator<Double>
{
    /** The minimum allowed value for this validator (inclusive). */
    private final Double min;
    /** The maximum allowed value for this validator (inclusive). */
    private final Double max;
    /** Flag that indicates that empty values are valid when {@code true}. */
    private final boolean allowEmpty;
    /** Value used when the text to be validated is empty. */
    private final Double emptyValue;

    /***************************************************************************
     * Creates a double validator that has no minimum or maximum and does not
     * allow empty strings.
     **************************************************************************/
    public DoubleValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a double validator with the provided minimum and maximum that
     * does not allow empty strings.
     * @param min the minimum value (inclusive).
     * @param max the maximum value (inclusive).
     **************************************************************************/
    public DoubleValidator( Double min, Double max )
    {
        this( min, max, false, null );
    }

    /***************************************************************************
     * Creates a double validator with the provided minimum and maximum that
     * allow empty strings according to the user's specifications.
     * @param min the minimum value (inclusive).
     * @param max the maximum value (inclusive).
     * @param allowEmpty allows empty text to be the provided value if
     * {@code true}.
     * @param emptyValue value used when the text to be validated is empty.
     **************************************************************************/
    public DoubleValidator( Double min, Double max, boolean allowEmpty,
        Double emptyValue )
    {
        this.min = min;
        this.max = max;

        this.allowEmpty = allowEmpty;
        this.emptyValue = emptyValue;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double validate( String text ) throws ValidationException
    {
        if( text.isEmpty() && allowEmpty )
        {
            return emptyValue;
        }

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

            throw new ValidationException(
                "Number (" + d + ") out of bounds: " + msg );
        }
    }
}
