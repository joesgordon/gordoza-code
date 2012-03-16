package org.cc.cvs;

import java.io.File;

import org.cc.model.IFileResource;
import org.cc.model.ResourceStatus;

public class CvsResource implements IFileResource
{
    private File file;
    private String path;
    private ResourceStatus status;

    public CvsResource( File file, String path, ResourceStatus status )
    {
        this.file = file;
        this.path = path;
        this.status = status;
    }

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public String getRepositoryPath()
    {
        return path;
    }

    @Override
    public ResourceStatus getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        return getRepositoryPath();
    }
}
