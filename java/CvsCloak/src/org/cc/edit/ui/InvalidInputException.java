package org.cc.edit.ui;

public class InvalidInputException extends Exception
{
    public InvalidInputException()
    {
        this( "" );
    }

    public InvalidInputException( String str )
    {
        super( str );
    }
}
