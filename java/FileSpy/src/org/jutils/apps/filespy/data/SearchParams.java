package org.jutils.apps.filespy.data;

import java.io.File;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jutils.Utils;
import org.jutils.ValidationException;
import org.jutils.utils.Usable;

/*******************************************************************************
 * This class defines the parameters of the files to find.
 ******************************************************************************/
public class SearchParams
{
    /** The filename pattern. */
    public String filename;
    /** The contents pattern. */
    public final Usable<String> contents;
    /** The paths to be searched. */
    public File path;
    /** Option to search sub-folders. */
    public boolean searchSubfolders;
    /** Kilobytes that the file must be more than. */
    public final Usable<Long> moreThan;
    /** Kilobytes that the file must be less than. */
    public final Usable<Long> lessThan;
    /** Date after which the last modified date of the file must occur. */
    public final Usable<LocalDate> after;
    /** Date before which the last modified date of the file must occur. */
    public final Usable<LocalDate> before;
    /** Option to interpret the filename pattern as a regular expression. */
    public boolean filenameRegex;
    /** Option to match the case in the filename pattern. */
    public boolean filenameMatch;
    /** Option to return all files not matched. */
    public boolean filenameNot;
    /** Option to interpret the contents pattern as a regular expression. */
    public boolean contentsRegex;
    /** Option to match the case in the filename pattern. */
    public boolean contentsMatch;

    /***************************************************************************
     * Creates new search parameters.
     **************************************************************************/
    public SearchParams()
    {
        this.filename = "";
        this.contents = new Usable<>( false, "" );
        this.path = new File( "/" );
        this.searchSubfolders = true;
        this.moreThan = new Usable<>( false );
        this.lessThan = new Usable<>( false );
        this.after = new Usable<>( false );
        this.before = new Usable<>( false );
        this.filenameRegex = false;
        this.filenameMatch = false;
        this.filenameNot = false;
        this.contentsRegex = false;
        this.contentsMatch = false;
    }

    /***************************************************************************
     * @param sp
     **************************************************************************/
    public SearchParams( SearchParams sp )
    {
        this.filename = sp.filename;
        this.contents = new Usable<>( sp.contents );
        this.path = sp.path;
        this.searchSubfolders = sp.searchSubfolders;
        this.moreThan = new Usable<>( sp.moreThan );
        this.lessThan = new Usable<>( sp.lessThan );
        this.after = new Usable<>( sp.after );
        this.before = new Usable<>( sp.before );
        this.filenameRegex = sp.filenameRegex;
        this.filenameMatch = sp.filenameMatch;
        this.filenameNot = sp.filenameNot;
        this.contentsRegex = sp.contentsRegex;
        this.contentsMatch = sp.contentsMatch;
    }

    /***************************************************************************
     * Returns the pattern needed to match the filename based on the options
     * available.
     * @return the pattern needed to match the filename.
     **************************************************************************/
    public Pattern getFilenamePattern()
    {
        return createPattern( filename, filenameRegex, filenameMatch, false );
    }

    /***************************************************************************
     * Returns the pattern needed to match the contents based on the options
     * available.
     * @return the pattern needed to match the contents.
     **************************************************************************/
    public Pattern getContentsPattern()
    {
        if( contents.isUsed )
        {
            return createPattern( contents.data, contentsRegex, contentsMatch,
                true );
        }
        return null;
    }

    /***************************************************************************
     * @param pattern
     * @param isRegex
     * @param caseSensitive
     * @param multiline
     * @return
     **************************************************************************/
    private static Pattern createPattern( String pattern, boolean isRegex,
        boolean caseSensitive, boolean multiline )
    {
        if( pattern != null && pattern.length() > 0 )
        {
            int flags = 0;

            flags |= ( multiline ? Pattern.MULTILINE : flags );
            flags |= ( caseSensitive ? flags : Pattern.CASE_INSENSITIVE );

            if( !isRegex )
            {
                pattern = Utils.escapeRegexMetaChar( pattern );
            }

            return Pattern.compile( pattern, flags );
        }

        return null;
    }

    public void validate() throws ValidationException
    {
        // ---------------------------------------------------------------------
        // Check the files
        // ---------------------------------------------------------------------
        if( !path.isDirectory() )
        {
            throw new ValidationException(
                "Non-existant directory: " + path.getAbsolutePath() );
        }

        // ---------------------------------------------------------------------
        // Check regex.
        // ---------------------------------------------------------------------
        try
        {
            getFilenamePattern();
        }
        catch( PatternSyntaxException ex )
        {
            throw new ValidationException(
                "Invalid filename pattern: " + ex.getMessage() );
        }
        try
        {
            getContentsPattern();
        }
        catch( PatternSyntaxException ex )
        {
            throw new ValidationException(
                "Invalid contents pattern: " + ex.getMessage() );
        }
    }
}
