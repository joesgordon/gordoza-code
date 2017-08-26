package org.jutils.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jutils.IconConstants;

public class BuildInfo
{
    private static final String FILE_NAME = "info.properties";
    public final String version;
    public final String buildDate;

    public BuildInfo( String version, String date )
    {
        this.version = version;
        this.buildDate = date;
    }

    public static BuildInfo load()
    {
        URL url = IconConstants.loader.loader.getUrl( FILE_NAME );

        try( InputStream stream = url.openStream() )
        {
            Properties props = new Properties();

            props.load( stream );

            String version = props.getProperty( "version" );
            String date = props.getProperty( "buildtime" );
            String sub = props.getProperty( "subversion" );

            if( sub != null )
            {
                version += "." + sub;
            }

            return new BuildInfo( version, date );
        }
        catch( IOException ex )
        {
            throw new IllegalStateException(
                "Unable to open/parse build information.", ex );
        }
    }
}
