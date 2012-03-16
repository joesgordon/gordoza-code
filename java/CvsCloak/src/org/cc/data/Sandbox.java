package org.cc.data;

import java.io.File;

public class Sandbox
{
    private File loc;

    public Sandbox( File location )
    {
        loc = location;
    }

    public File getLocation()
    {
        return loc;
    }
}
