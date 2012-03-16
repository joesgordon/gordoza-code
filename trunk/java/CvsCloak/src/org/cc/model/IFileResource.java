package org.cc.model;

import java.io.File;


public interface IFileResource
{
    public File getFile();

    public String getRepositoryPath();

    public ResourceStatus getStatus();
}
