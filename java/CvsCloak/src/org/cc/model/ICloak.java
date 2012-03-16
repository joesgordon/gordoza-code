package org.cc.model;

import java.io.File;
import java.util.List;

import org.cc.data.VersioningSystem;

public interface ICloak
{
    public VersioningSystem getVersioningSystem();

    public boolean createSandbox( String baseline, File location );

    public List<IFileResource> getStatus( File sb, IFileResource... dir );

    public void diffFile( File sb, IFileResource file );

    public void addFile( File sb, IFileResource... files );

    public void moveFile( File sb, IFileResource file, IFileResource dest );

    public void removeFile( File sb, IFileResource... file );

    public void updateFile( File sb, IFileResource... file );

    public void checkinFile( File sb, IFileResource... file );

    public void checkoutFile( File sb, IFileResource... file );

    public List<File> getRecentSandboxes();
}
