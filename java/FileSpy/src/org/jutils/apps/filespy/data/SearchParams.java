package org.jutils.apps.filespy.data;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.jutils.Utils;
import org.jutils.io.IOUtils;
import org.jutils.io.XObject;

/*******************************************************************************
 * This class defines the parameters of the files to find.
 ******************************************************************************/
public class SearchParams extends XObject
{
    /** The filename pattern. */
    public String filename = null;

    /** The contents pattern. */
    public String contents = null;

    /** The paths to be searched. */
    private String searchIn = "/";

    /** Option to search sub-folders. */
    public boolean searchSubfolders = true;

    /** Kilobytes that the file must be more than. */
    public Long moreThan = null;

    /** Kilobytes that the file must be less than. */
    public Long lessThan = null;

    /** Date after which the last modified date of the file must occur. */
    public Calendar after = null;

    /** Date before which the last modified date of the file must occur. */
    public Calendar before = null;

    /** Option to interpret the filename pattern as a regular expression. */
    public boolean filenameRegex = false;

    /** Option to match the case in the filename pattern. */
    public boolean filenameMatch = false;

    /** Option to return all files not matched. */
    public boolean filenameNot = false;

    /** Option to interpret the contents pattern as a regular expression. */
    public boolean contentsRegex = false;

    /** Option to match the case in the filename pattern. */
    public boolean contentsMatch = false;

    /** The name of these parameters. */
    public transient String name = null;

    /** The pattern used for matching the filename. */
    private transient Pattern filenamePattern = null;

    /** The pattern used for matching the contents. */
    private transient Pattern contentsPattern = null;

    /***************************************************************************
     * Creates new search parameters.
     **************************************************************************/
    public SearchParams()
    {
        init();
    }

    /***************************************************************************
     * Sets the paths to be search with the provided files.
     * @param paths The paths to be searched.
     **************************************************************************/
    public void setSearchFolders( File [] paths )
    {
        searchIn = IOUtils.getStringFromFiles( paths );
    }

    /***************************************************************************
     * Returns the files to be searched.
     * @return the files to be searched.
     **************************************************************************/
    public File [] getSearchFolders()
    {
        return IOUtils.getFilesFromString( searchIn );
    }

    /***************************************************************************
     * Returns the pattern needed to match the filename based on the options
     * available.
     * @return the pattern needed to match the filename.
     **************************************************************************/
    public Pattern getFilenamePattern()
    {
        if( filenamePattern == null && filename != null &&
            filename.length() > 0 )
        {
            int flags = 0;
            String pattern = filename;
            if( !filenameRegex )
            {
                pattern = Utils.escapeRegexMetaChar( pattern );
            }
            flags |= filenameMatch ? flags : Pattern.CASE_INSENSITIVE;
            filenamePattern = Pattern.compile( pattern, flags );
        }
        return filenamePattern;
    }

    /***************************************************************************
     * Returns the pattern needed to match the contents based on the options
     * available.
     * @return the pattern needed to match the contents.
     **************************************************************************/
    public Pattern getContentsPattern()
    {
        if( contentsPattern == null && contents != null &&
            contents.length() > 0 )
        {
            int flags = Pattern.MULTILINE;
            String pattern = contents;
            if( !contentsRegex )
            {
                pattern = Utils.escapeRegexMetaChar( pattern );
            }
            flags |= contentsMatch ? flags : Pattern.CASE_INSENSITIVE;
            contentsPattern = Pattern.compile( pattern, flags );
        }
        return contentsPattern;
    }

    /***************************************************************************
     * Initializes this data (called after de-serialization).
     **************************************************************************/
    public void init()
    {
        if( searchIn == null )
        {
            searchIn = "\\";
        }
        if( filename == null )
        {
            filename = "";
        }
    }
}
