package org.jutils.task;

public class TaskError
{
    public final String name;
    public final String message;
    public final String description;
    public final Throwable exception;

    public TaskError( String name, String message )
    {
        this( name, message, null, null );
    }

    public TaskError( String name, String message, String description )
    {
        this( name, message, description, null );
    }

    public TaskError( String name, Throwable exception )
    {
        this( name, null, null, exception );
    }

    public TaskError( String name, String message, Throwable exception )
    {
        this( name, message, null, exception );
    }

    public TaskError( String name, String message, String description,
        Throwable exception )
    {
        this.name = name;
        this.message = message;
        this.description = description;
        this.exception = exception;
    }
}
