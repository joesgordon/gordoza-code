package org.duak.model;

import java.io.File;
import java.util.List;

import org.duak.data.FileInfo;
import org.jutils.concurrent.IStopper;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Analyzer
{
    /***************************************************************************
     * @param dir
     * @return
     **************************************************************************/
    public FileInfo analyze( File dir, IProgressReporter reporter,
        IStopper stopper )
    {
        FileInfo results = new FileInfo( dir );

        reporter.setProgressIndeterminate();
        reporter.setStatus( "Building file list..." );

        results.refresh();

        getSize( results, reporter, stopper, 0, results.getNumFiles() );

        return results;
    }

    /***************************************************************************
     * @param fileInfo
     * @param reporter
     * @param i
     * @param len
     **************************************************************************/
    private int getSize( FileInfo fileInfo, IProgressReporter reporter,
        IStopper stopper, int i, int len )
    {
        if( !stopper.continueProcessing() )
        {
            return i;
        }

        // long size = ( long )( Math.random() * ( Long.MAX_VALUE ) );
        long size = 0;
        List<FileInfo> childResults = fileInfo.getChildren();

        for( FileInfo fileResult : childResults )
        {
            if( !fileResult.isDir() )
            {
                reporter.setprogress( i++ / ( double )len );

                fileResult.setSize( fileResult.getFile().length() );
                size += fileResult.getSize();
            }
            else
            {
                reporter.setStatus( fileResult.getFile().getAbsolutePath() );
                i = getSize( fileResult, reporter, stopper, i, len );
                size += fileResult.getSize();
            }

            if( !stopper.continueProcessing() )
            {
                break;
            }
        }

        fileInfo.setSize( size );

        return i;
    }
}
