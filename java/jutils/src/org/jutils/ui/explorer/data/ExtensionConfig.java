package org.jutils.ui.explorer.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExtensionConfig implements Comparable<Object>
{
    /**  */
    public String ext;
    /** A short description of the file type */
    public String description;
    /**  */
    public final List<String> programs;

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensionConfig()
    {
        this.ext = "";
        this.description = "";
        this.programs = new ArrayList<>();
    }

    public ExtensionConfig( String ext, String description )
    {
        this();

        this.ext = ext;
        this.description = description;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return ext;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compareTo( Object obj )
    {
        return ext.compareToIgnoreCase( obj.toString() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return ext.hashCode();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj != null )
        {
            return ext.equalsIgnoreCase( obj.toString() );
        }

        return false;
    }
}
