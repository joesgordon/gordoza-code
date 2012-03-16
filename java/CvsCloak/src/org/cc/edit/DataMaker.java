package org.cc.edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cc.data.*;

/*******************************************************************************
 * A test class for define a mock set of data.
 ******************************************************************************/
public class DataMaker
{
    /***************************************************************************
     * Creates the data maker.
     **************************************************************************/
    public DataMaker()
    {
        ;
    }

    /***************************************************************************
     * Creates an AppModel.
     * @return an AppModel.
     **************************************************************************/
    public VersioningSystem createModel()
    {
        VersioningSystem model = new VersioningSystem();
        File cwd = new File( "" ).getAbsoluteFile();

        model.getRepositories().add( createRepository( cwd ) );
        model.getRepositories().add( createRepository( cwd.getParentFile() ) );
        model.setDefaultRepository( cwd.getParentFile().getAbsolutePath() );

        return model;
    }

    /***************************************************************************
     * @param location
     * @return
     **************************************************************************/
    private Repository createRepository( File location )
    {
        Repository repo = new Repository( location );
        List<Baseline> baselines = new ArrayList<Baseline>();
        List<Product> products = new ArrayList<Product>();

        baselines.add( createBaseline( "Baseline_1" ) );
        baselines.add( createBaseline( "Baseline_2" ) );

        products.add( createProduct( "Software", "sw" ) );
        products.add( createProduct( "Data", "data" ) );

        repo.setBaselines( baselines );
        repo.setProducts( products );
        repo.setTrunkName( "Baseline_1" );

        return repo;
    }

    /***************************************************************************
     * @param name
     * @param module
     * @return
     **************************************************************************/
    private Product createProduct( String name, String... module )
    {
        Product p = new Product();
        List<Release> releases = new ArrayList<Release>();
        List<String> modules = new ArrayList<String>();

        releases.add( createRelease( "Release_1_0" ) );
        releases.add( createRelease( "Release_1_1" ) );
        releases.add( createRelease( "Release_2_0" ) );

        for( String m : module )
        {
            modules.add( m );
        }

        p.setName( name );
        p.setModules( modules );
        p.setReleases( releases );

        return p;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    private Release createRelease( String name )
    {
        Release r = new Release();

        r.setName( name );
        r.getClosedTasks().add( createClosedTask( "Task_0" ) );
        r.getClosedTasks().add( createClosedTask( "Task_6" ) );
        r.getClosedTasks().add( createClosedTask( "Task_2" ) );

        return r;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    private ClosedTask createClosedTask( String name )
    {
        ClosedTask task = new ClosedTask();
        List<VersionedFile> files = new ArrayList<VersionedFile>();

        files.add( createVersionedFile( "foo/bar.c", "1.5" ) );
        files.add( createVersionedFile( "foo/makefile", "1.524" ) );

        task.setName( name );
        task.setFiles( files );

        return task;
    }

    /***************************************************************************
     * @param path
     * @param version
     * @return
     **************************************************************************/
    private VersionedFile createVersionedFile( String path, String version )
    {
        VersionedFile file = new VersionedFile();

        file.setRepositoryPath( path );
        file.setVersion( version );

        return file;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    private Baseline createBaseline( String name )
    {
        Baseline b = new Baseline();

        b.setName( name );

        b.getClosedTasks().add( createClosedTask( "Task_1" ) );
        b.getClosedTasks().add( createClosedTask( "Task_3" ) );
        b.getClosedTasks().add( createClosedTask( "Task_4" ) );

        b.getOpenTasks().add( createOpenTask( "Task_5" ) );
        b.getOpenTasks().add( createOpenTask( "Task_7" ) );

        return b;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    private OpenTask createOpenTask( String name )
    {
        OpenTask task = new OpenTask();
        List<VersionedFile> files = new ArrayList<VersionedFile>();

        files.add( createVersionedFile( "bar/foo.c", "1.5" ) );
        files.add( createVersionedFile( "bar/makefile", "1.524" ) );

        task.setName( name );
        task.setFiles( files );

        return task;
    }
}
