package org.jutils.ui.validators;

import java.io.File;

import org.jutils.ui.validation.ValidationException;

public class FileValidator implements IDataValidator<File>
{
    private final ExistenceType type;

    public FileValidator()
    {
        this( ExistenceType.FILE_ONLY );
    }

    public FileValidator( ExistenceType type )
    {
        this.type = type;
    }

    @Override
    public File validate( String text ) throws ValidationException
    {
        if( type != ExistenceType.DO_NOT_CHECK && text.isEmpty() )
        {
            throw new ValidationException( "Empty path string" );
        }

        if( text.isEmpty() && type == ExistenceType.DO_NOT_CHECK )
        {
            return null;
        }

        File f = new File( text );

        // LogUtils.printDebug( "Testing path " + text );

        if( type != ExistenceType.DO_NOT_CHECK && !f.exists() )
        {
            throw new ValidationException( "Path does not exist" );
        }

        boolean isFile = f.isFile();
        boolean isDir = f.isDirectory();

        if( type != ExistenceType.DO_NOT_CHECK )
        {
            if( type == ExistenceType.DIRECTORY_ONLY && !isDir )
            {
                throw new ValidationException( "Path is not a directory" );
            }
            else if( type == ExistenceType.FILE_ONLY && !isFile )
            {
                throw new ValidationException( "Path is not a file" );
            }
            else if( type == ExistenceType.FILE_OR_DIRECTORY && !isFile &&
                !isDir )
            {
                throw new ValidationException(
                    "Path is not a file or directory" );
            }
        }

        return f.getAbsoluteFile();
    }

    public static enum ExistenceType
    {
        DO_NOT_CHECK,
        FILE_ONLY,
        DIRECTORY_ONLY,
        FILE_OR_DIRECTORY;
    }
}
