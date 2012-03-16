package org.jutils.ui.explorer;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileConfigurationData
{
    /**  */
    private boolean useCustom = true;

    /**  */
    private List<ExtensionData> extensions;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileConfigurationData()
    {
        extensions = new ArrayList<ExtensionData>();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean useCustom()
    {
        return useCustom;
    }

    /***************************************************************************
     * @param custom
     **************************************************************************/
    public void setUseCustom( boolean custom )
    {
        useCustom = custom;
    }

    /***************************************************************************
     * @param ext
     * @return
     **************************************************************************/
    public boolean addExtension( ExtensionData ext )
    {
        return extensions.add( ext );
    }

    /***************************************************************************
     * @param pgm
     * @return
     **************************************************************************/
    public boolean removeExtension( ExtensionData pgm )
    {
        return extensions.remove( pgm );
    }

    /***************************************************************************
     * @param ext
     * @return
     **************************************************************************/
    public boolean containsExtension( String ext )
    {
        for( ExtensionData ed : extensions )
        {
            if( ed.getExtension().equalsIgnoreCase( ext ) )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<ExtensionData> getExtensions()
    {
        return new ArrayList<ExtensionData>( extensions );
    }
}
