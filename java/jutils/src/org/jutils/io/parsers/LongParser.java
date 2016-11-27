package org.jutils.io.parsers;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * A parser that ensures that integers fall within a specified range.
 ******************************************************************************/
public class LongParser implements IParser<Long>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Long min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Long max;

    /***************************************************************************
     * Creates a parser that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public LongParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a parser that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public LongParser( Long min, Long max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long parse( String text ) throws ValidationException
    {
        try
        {
            long i = Long.parseLong( text );

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
