package org.jutils.ui.validators;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ParserValidator<T> implements IDataValidator<T>
{
    /**  */
    private final IParser<T> parser;

    /***************************************************************************
     * 
     **************************************************************************/
    public ParserValidator( IParser<T> parser )
    {
        this.parser = parser;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T validate( String text ) throws ValidationException
    {
        return parser.parse( text );
    }
}
