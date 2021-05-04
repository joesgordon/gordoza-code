package testbed;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class TouchMain
{
    public static void main( String[] args )
    {
        File file = new File( "test.bin" );
        try( RandomAccessFile raf = new RandomAccessFile( file, "rw" ) )
        {
            raf.setLength( 164160L );
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main2( String[] args )
    {
        long now = Calendar.getInstance().getTimeInMillis();

        for( String arg : args )
        {
            File file = new File( arg );

            if( file.exists() )
            {
                touch( file, now );
            }
        }
    }

    private static void touch( File file, long now )
    {
        file.setLastModified( now );

        if( file.isDirectory() )
        {
            File[] files = file.listFiles();

            if( files != null )
            {
                for( File f : files )
                {
                    touch( f, now );
                }
            }
        }
    }
}
