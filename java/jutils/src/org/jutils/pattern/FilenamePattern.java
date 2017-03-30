package org.jutils.pattern;

import java.io.File;

import org.jutils.INamedItem;
import org.jutils.ValidationException;
import org.jutils.pattern.StringPattern.IMatcher;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilenamePattern
{
    /***************************************************************************
     * 
     **************************************************************************/
    private FilenamePattern()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static IFilenameMatcher createMatcher( StringPattern pattern )
        throws ValidationException
    {
        IMatcher strMatcher = pattern.createMatcher();

        return new FilenameMatcher( strMatcher );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IFilenameMatcher extends INamedItem
    {
        public boolean match( File file );

        @Override
        public String getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FilenameMatcher implements IFilenameMatcher
    {
        private final IMatcher strMatcher;

        public FilenameMatcher( IMatcher strMatcher )
        {
            this.strMatcher = strMatcher;
        }

        @Override
        public boolean match( File file )
        {
            return strMatcher.matches( file.getName() );
        }

        @Override
        public String getName()
        {
            return strMatcher.getName();
        }
    }
}
