package org.jutils.chart.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilteredWriter
{
    /***************************************************************************
     * @param fromFile
     * @param toFile
     * @param data
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public void write( File fromFile, File toFile, ISeriesData<?> data )
        throws FileNotFoundException, IOException
    {
        File temp = fromFile;
        boolean overwrite = fromFile.equals( toFile );

        if( overwrite )
        {
            temp = File.createTempFile( "SeriesData_", ".txt" );
            Files.copy( fromFile.toPath(), temp.toPath(),
                StandardCopyOption.REPLACE_EXISTING );
        }

        try( FileReader fr = new FileReader( temp );
             BufferedReader reader = new BufferedReader( fr ) )
        {
            try( PrintStream stream = new PrintStream( toFile ) )
            {
                DataLineReader lineReader = new DataLineReader();
                String line = null;
                int idx = 0;
                XYPoint point = null;

                while( ( line = reader.readLine() ) != null )
                {
                    point = lineReader.read( line );

                    if( point != null )
                    {
                        if( !data.isHidden( idx ) )
                        {
                            stream.println( line );
                        }

                        idx++;
                    }
                    else
                    {
                        stream.println( line );
                    }
                }
            }
        }

        if( overwrite )
        {
            if( !temp.delete() )
            {
                throw new IOException( "Cannot delete temporary file: " +
                    temp.getAbsolutePath() );
            }
        }
    }

    /***************************************************************************
     * @param toFile
     * @param data
     * @throws FileNotFoundException
     **************************************************************************/
    public void write( File toFile, ISeriesData<?> data )
        throws FileNotFoundException
    {
        try( PrintStream stream = new PrintStream( toFile ) )
        {
            for( int i = 0; i < data.getCount(); i++ )
            {
                if( !data.isHidden( i ) )
                {
                    stream.print( data.getX( i ) );
                    stream.print( '\t' );
                    stream.println( data.getY( i ) );
                }
            }
        }
    }
}
