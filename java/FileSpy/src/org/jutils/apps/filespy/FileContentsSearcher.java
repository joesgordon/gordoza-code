package org.jutils.apps.filespy;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jutils.apps.filespy.data.LineMatch;
import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.concurrent.IConsumer;
import org.jutils.concurrent.IStopper;

public class FileContentsSearcher implements IConsumer<SearchRecord>
{
    private final Pattern contentsPattern;
    private final SearchHandler searchHandler;

    public FileContentsSearcher( Pattern contentsPattern,
        SearchHandler searchHandler )
    {
        this.contentsPattern = contentsPattern;
        this.searchHandler = searchHandler;
    }

    private boolean searchString( String str, SearchRecord record,
        int lineNumber )
    {
        boolean matched = false;
        byte[] chars = str.getBytes();
        ByteCharSequence sequence = new ByteCharSequence( chars );
        Matcher matcher = contentsPattern.matcher( sequence );

        if( matcher.find() )
        {
            matched = true;

            // Utils.printDebug( "bufLen: " + chars.length +
            // ", start: " + matcher.start() + ", end: " +
            // matcher.end() );

            record.addLine( createLineMatch( chars, matcher, lineNumber ) );
        }

        str = null;
        record = null;

        return matched;
    }

    /***********************************************************************
     * @param chars
     * @param matcher
     * @param lineNum
     * @return
     **********************************************************************/
    private LineMatch createLineMatch( byte[] chars, Matcher matcher,
        int lineNum )
    {
        int lineStart = 0;
        int lineEnd = chars.length;
        int start = matcher.start();
        int end = matcher.end();
        int length = end - start;

        if( length > 1024 )
        {
            lineStart = start;
            end = start + 1024;
            length = 1024;
            lineEnd = end;
        }

        String pre = new String( chars, lineStart, start );
        String mat = new String( chars, start, length );
        String pst = end == lineEnd ? "" : new String( chars, end, lineEnd -
            end );

        return new LineMatch( lineNum, pre, mat, pst );
    }

    /***********************************************************************
     * @param file
     * @throws IOException
     **********************************************************************/
    private void searchFile( SearchRecord record, IStopper stopper )
        throws IOException
    {
        File file = record.getFile();
        String line;
        FileReader reader = new FileReader( file );
        LineNumberReader lineReader = new LineNumberReader( reader );

        boolean matched = false;
        try
        {
            while( ( line = lineReader.readLine() ) != null &&
                stopper.continueProcessing() )
            {
                if( searchString( line, record, lineReader.getLineNumber() ) )
                {
                    matched = true;
                }
            }
        }
        catch( IOException ex )
        {
            throw ex;
        }
        finally
        {
            lineReader.close();

            if( matched )
            {
                searchHandler.addFile( record );
            }
        }
    }

    /***********************************************************************
     * 
     **********************************************************************/
    @Override
    public void consume( SearchRecord data, IStopper stopper )
    {
        try
        {
            searchFile( data, stopper );
        }
        catch( IOException ex )
        {
            searchHandler.addErrorMessage( ex.getMessage() );
        }
    }
}
