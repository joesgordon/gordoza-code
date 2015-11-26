package org.jutils;

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
