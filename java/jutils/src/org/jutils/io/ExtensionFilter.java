package org.jutils.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFilter extends FileFilter implements java.io.FileFilter
{
    private final String extension;

    public ExtensionFilter( String ext )
    {
        extension = ext;
    }

    @Override
    public boolean accept( File f )
    {
        return f.isDirectory() ||
            ( f.isFile() && f.getName().endsWith( extension ) );
    }

    @Override
    public String getDescription()
    {
        return ( "*." + extension ).toUpperCase();
    }
}
