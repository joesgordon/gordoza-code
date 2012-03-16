package org.jutils.ui.explorer;

import java.util.ArrayList;
import java.util.List;

public class ExtensionData implements Comparable<Object>
{

    private String ext;

    private String description;

    private byte[] iconData;

    private ArrayList<ProgramData> programs = new ArrayList<ProgramData>();

    public ExtensionData( String ext, String desc, byte[] iconData )
    {
        setExtension( ext );
        setDescription( desc );
        setIconData( iconData );
    }

    public byte[] getIconData()
    {
        return iconData;
    }

    public void setIconData( byte[] iconData )
    {
        this.iconData = iconData;
    }

    public String getExtension()
    {
        return ext;
    }

    public void setExtension( String extension )
    {
        ext = extension.toString();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String desc )
    {
        description = desc.toString();
    }

    /***********************************************************************
     * @param pgm
     * @return
     **********************************************************************/
    public boolean addProgram( ProgramData pgm )
    {
        pgm.hashCode();
        return programs.add( pgm );
    }

    /***********************************************************************
     * @param pgm
     * @return
     **********************************************************************/
    public boolean removeProgram( ProgramData pgm )
    {
        pgm.hashCode();
        return programs.remove( pgm );
    }

    /***********************************************************************
     * @return
     **********************************************************************/
    public List<ProgramData> getPrograms()
    {
        return new ArrayList<ProgramData>( programs );
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
        assert false : "hashCode not designed";
        return 42; // any arbitrary constant will do
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        return compareTo( obj ) == 0;
    }
}
