package org.jutils.ui.explorer;

import java.io.File;

public interface ExplorerItem
{
    public File getFile();

    public String getFilename();

    public String getSystemName();

    public String getParentPath();

    public long getSizeInKb();

    public String getType();

    public String getLastModified();

    public String toString();
}
