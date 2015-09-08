package testbed;

import java.io.*;

import org.jutils.io.SplitFileInputStream;
import org.jutils.io.SplitFileOutputStream;

/*******************************************************************************
 *
 ******************************************************************************/
public class SplitStreamMain
{
    public static final int RW_LIMIT = 0x100000;

    public static final int CT_LIMIT = 0x100000;

    /***************************************************************************
     *
     **************************************************************************/
    private SplitStreamMain()
    {
        ;
    }

    /***************************************************************************
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        File dir = new File( "testDir" );
        File file = new File( dir, "test.bin" );

        if( !dir.mkdir() )
        {
            System.out.println(
                "Cannot create directory " + dir.getAbsolutePath() );
            System.exit( 1 );
        }

        try
        {
            writeFiles( file, RW_LIMIT );
            readFiles( file );
            System.out.println( "Test succeeded!" );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }

        if( !dir.delete() )
        {
            System.out.println( "Can't delete dir!" );
            dir.deleteOnExit();
        }
        else
        {
            System.out.println( "Deleted dir!" );
        }

    }

    /***************************************************************************
     * @param baseFile File
     * @param size int
     * @throws IOException
     **************************************************************************/
    private static void writeFiles( File baseFile, int size ) throws IOException
    {
        try( SplitFileOutputStream sfos = new SplitFileOutputStream( baseFile,
            size ); DataOutputStream stream = new DataOutputStream( sfos ) )
        {
            for( int i = 0; i < CT_LIMIT; i++ )
            {
                stream.writeInt( i );
            }
        }
    }

    /***************************************************************************
     * @param baseFile File
     * @throws IOException
     **************************************************************************/
    private static void readFiles( File baseFile ) throws IOException
    {
        try( SplitFileInputStream sfis = new SplitFileInputStream( baseFile );
             DataInputStream stream = new DataInputStream( sfis ) )
        {
            int intRead = -1;

            for( int i = 0; i < CT_LIMIT; i++ )
            {
                intRead = stream.readInt();
                if( i != intRead )
                {
                    throw new IOException( i + " != " + intRead );
                }
            }
        }
    }
}
