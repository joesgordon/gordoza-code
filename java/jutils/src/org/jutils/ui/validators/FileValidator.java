package org.jutils.ui.validators;

import java.io.File;

import org.jutils.ui.validation.ValidationException;

public class FileValidator implements IDataValidator<File>
{
    @Override
    public File validate( String text ) throws ValidationException
    {
        if( text.length() < 1 )
        {
            throw new ValidationException( "Empty path string" );
        }

        File f = new File( text );

        // System.out.println( "Testing path " + text );

        if( !f.exists() )
        {
            throw new ValidationException( "Path does not exist" );
        }

        if( !f.isFile() )
        {
            throw new ValidationException( "Path is not a file" );
        }

        return f.getAbsoluteFile();
    }
}
