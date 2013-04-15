package org.jutils.ui.validation;

public class ValidationException extends Exception
{
    public ValidationException( String string )
    {
        super( string );
    }

    public ValidationException( String string, Throwable cause )
    {
        super( string, cause );
    }
}
