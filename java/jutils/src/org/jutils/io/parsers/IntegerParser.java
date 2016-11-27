package org.jutils.io.parsers;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * A parser that ensures that integers fall within a specified range.
 ******************************************************************************/
public class IntegerParser implements IParser<Integer>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Integer min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Integer max;

    /***************************************************************************
     * Creates a parser that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public IntegerParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a parser that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public IntegerParser( Integer min, Integer max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Integer parse( String text ) throws ValidationException
    {
        try
        {
            int i = Integer.parseInt( text );

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
