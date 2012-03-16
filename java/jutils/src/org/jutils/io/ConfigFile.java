package org.jutils.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConfigFile<K, V> extends HashMap<K, V>
{
    /** File where the data is saved. */
    private transient File configFile;

    /***************************************************************************
     * @param path
     **************************************************************************/
    public ConfigFile( String path )
    {
        this( new File( path ) );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public ConfigFile( File file )
    {
        configFile = file;
        readConfigData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void save()
    {
        try
        {
            XStreamUtils.writeObjectXStream( this, configFile );
        }
        catch( IOException e )
        {
            // Do nothing. User settings will not be available past this session
            // but will work otherwise.
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createDefaultFile()
    {
        save();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void readConfigData()
    {
        try
        {
            Object obj = XStreamUtils.readObjectXStream( configFile );
            @SuppressWarnings( "unchecked")
            HashMap<K, V> map = ( HashMap<K, V> )obj;
            this.putAll( map );
        }
        catch( Exception e )
        {
            createDefaultFile();
        }
    }
}
