package org.jutils.ui.validators;

import java.io.File;

import org.jutils.ui.validation.ValidationException;

public class FileValidator implements IDataValidator<File>
{
    private final ExistanceType type;

    public FileValidator()
    {
        this( ExistanceType.FILE_ONLY );
    }

    public FileValidator( ExistanceType type )
    {
        this.type = type;
    }

    @Override
    public File validate( String text ) throws ValidationException
    {
        if( text.length() < 1 )
        {
            throw new ValidationException( "Empty path string" );
        }

        File f = new File( text );

        // LogUtils.printDebug( "Testing path " + text );

        if( !f.exists() )
        {
            throw new ValidationException( "Path does not exist" );
        }

        boolean isFile = f.isFile();
        boolean isDir = f.isDirectory();

        if( type == ExistanceType.DIRECTORY_ONLY && !isDir )
        {
            throw new ValidationException( "Path is not a directory" );
        }
        else if( type == ExistanceType.FILE_ONLY && !isFile )
        {
            throw new ValidationException( "Path is not a file" );
        }
        else if( type == ExistanceType.FILE_OR_DIRECTORY && !isFile && !isDir )
        {
            throw new ValidationException( "Path is not a file or directory" );
        }

        return f.getAbsoluteFile();
    }

    public static enum ExistanceType
    {
        FILE_ONLY,
        DIRECTORY_ONLY,
        FILE_OR_DIRECTORY;
    }
}
