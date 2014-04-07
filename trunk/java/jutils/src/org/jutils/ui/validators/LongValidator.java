package org.jutils.ui.validators;

import org.jutils.ui.validation.ValidationException;

/*******************************************************************************
 * A validator that ensures that integers fall within a specified range.
 ******************************************************************************/
public class LongValidator implements IDataValidator<Long>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Long min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Long max;

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public LongValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a validator that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public LongValidator( Long min, Long max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long validate( String text ) throws ValidationException
    {
        try
        {
            long i = Long.parseLong( text );

            if( min != null && i < min )
            {
                throw new ValidationException( "Value less than minimum: " + i +
                    " < " + min );
            }

            if( max != null && i > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    i + " > " + max );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
