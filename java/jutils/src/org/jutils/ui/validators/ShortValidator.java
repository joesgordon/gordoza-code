package org.jutils.ui.validators;

import org.jutils.ValidationException;

/*******************************************************************************
 * A validator that ensures that shorts fall within a specified range.
 ******************************************************************************/
public class ShortValidator implements IDataValidator<Short>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Short min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Short max;

    /***************************************************************************
     * Creates a validator that ensures the text is an short and performs no
     * bounds checking.
     **************************************************************************/
    public ShortValidator()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a validator that ensures the text is an short and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public ShortValidator( Short min, Short max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Short validate( String text ) throws ValidationException
    {
        try
        {
            short i = Short.parseShort( text );

            if( min != null && i < min )
            {
                throw new ValidationException(
                    "Value less than minimum: " + i + " < " + min );
            }

            if( max != null && i > max )
            {
                throw new ValidationException(
                    "Value greater than maximum: " + i + " > " + max );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
