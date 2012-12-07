package testbed;

import java.io.File;
import java.util.Calendar;

public class TouchMain
{
    public static void main( String[] args )
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
