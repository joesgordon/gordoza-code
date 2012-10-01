package org.jutils.apps.jhex;

import java.io.File;
import java.io.IOException;

import org.jutils.io.IOUtils;
import org.jutils.io.XObject;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexOptions extends XObject
{
    private static final File FILENAME = new File( IOUtils.USERS_DIR,
        ".jHex_Options.xml" );

    private String lastSavedLocation = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public JHexOptions()
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void init()
    {
        ;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getLastSavedLocation()
    {
        return IOUtils.getExistingDir( lastSavedLocation );
    }

    /***************************************************************************
     * @param dir
     **************************************************************************/
    public void setLastSavedLocation( File dir )
    {
        lastSavedLocation = dir.getAbsolutePath();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static JHexOptions lazyRead()
    {
        JHexOptions options = null;

        if( FILENAME.isFile() )
        {
            try
            {
                options = ( JHexOptions )read( FILENAME );
            }
            catch( Exception ex )
            {
                ex.printStackTrace();
                options = new JHexOptions();
            }
            catch( Error er )
            {
                er.printStackTrace();
                options = new JHexOptions();
            }
        }
        else
        {
            if( FILENAME.exists() )
            {
                System.out.println( FILENAME.getAbsolutePath() +
                    " is not a file!" );
            }
            options = new JHexOptions();
        }

        return options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void lazySave()
    {
        try
        {
            this.write( FILENAME );
        }
        catch( IOException ex )
        {
            System.out.println( "WARNING: Cannot save options to" );
            System.out.println( "\t" + FILENAME.getAbsolutePath() );
            System.out.println( "\t" + ex.getMessage() );
        }
    }
}
