package org.cc.cvs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cc.data.VersioningSystem;
import org.cc.model.*;
import org.jutils.utils.UniqueMaxQueue;

public class CvsCloak implements ICloak
{
    private VersioningSystem vs;
    private UniqueMaxQueue<File> recentSandboxes;

    public CvsCloak()
    {
        vs = new VersioningSystem();
        recentSandboxes = new UniqueMaxQueue<File>();
    }

    @Override
    public boolean createSandbox( String baseline, File location )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<IFileResource> getStatus( File sb, IFileResource... dir )
    {
        List<IFileResource> files = new ArrayList<IFileResource>();
        getStatus( sb, "", 0, files );
        return files;
    }

    private void getStatus( File dir, String path, int depth,
        List<IFileResource> files )
    {
        if( depth == 3 )
        {
            files.add( new CvsResource( new File( dir, "xyqwk" ), path +
                "xyqwk", genStatus() ) );
            return;
        }

        File[] children = dir.listFiles();

        for( File child : children )
        {
            String childPath = path + child.getName();
            ResourceStatus status = genStatus();
            files.add( new CvsResource( child, childPath, status ) );

            if( child.isDirectory() )
            {
                getStatus( child, childPath + File.separator, depth + 1, files );
            }
        }
    }

    private ResourceStatus genStatus()
    {
        double p = Math.random();

        if( p < 1.0 / 7.0 )
        {
            return ResourceStatus.ADDED;
        }
        else if( p < 2.0 / 7.0 )
        {
            return ResourceStatus.CONFLICT;
        }
        else if( p < 3.0 / 7.0 )
        {
            return ResourceStatus.MODIFIED;
        }
        else if( p < 4.0 / 7.0 )
        {
            return ResourceStatus.NEEDS_UPDATE;
        }
        else if( p < 5.0 / 7.0 )
        {
            return ResourceStatus.REMOVED;
        }
        else if( p < 6.0 / 7.0 )
        {
            return ResourceStatus.UNKNOWN;
        }

        return ResourceStatus.UP_TO_DATE;
    }

    @Override
    public void diffFile( File sb, IFileResource file )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void addFile( File sb, IFileResource... files )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveFile( File sb, IFileResource file, IFileResource dest )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeFile( File sb, IFileResource... file )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateFile( File sb, IFileResource... file )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkinFile( File sb, IFileResource... file )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkoutFile( File sb, IFileResource... file )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public VersioningSystem getVersioningSystem()
    {
        return vs;
    }

    @Override
    public List<File> getRecentSandboxes()
    {
        return recentSandboxes;
    }
}
