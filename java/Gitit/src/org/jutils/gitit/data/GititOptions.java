package org.jutils.gitit.data;

import java.io.File;
import java.util.List;

import org.jutils.utils.MaxQueue;

/**
 *
 */
public class GititOptions
{
    /**  */
    public final MaxQueue<File> recentFiles;

    /**  */
    public GititConfig config;

    /**
     * 
     */
    public GititOptions()
    {
        this.recentFiles = new MaxQueue<>( 20 );
        this.config = new GititConfig();
    }

    /**
     * @return
     */
    public List<File> getRecentFiles()
    {
        return recentFiles.toList();
    }

    /**
     * @return
     */
    public File getMostRecentFile()
    {
        return recentFiles.first();
    }
}
