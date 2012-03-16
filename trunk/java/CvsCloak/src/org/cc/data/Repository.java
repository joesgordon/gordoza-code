package org.cc.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Repository
{
    /**  */
    private File location;
    /**  */
    private String name;
    /**  */
    private String trunkName;
    /**  */
    private LockInfo lockInfo;
    /**  */
    private List<Baseline> baselines;
    /**  */
    private List<Product> products;

    /***************************************************************************
     * @param location
     **************************************************************************/
    public Repository( File location )
    {
        this.location = location;
        this.baselines = new ArrayList<Baseline>();
        this.products = new ArrayList<Product>();

        if( location == null )
        {
            throw new NullPointerException(
                "Repository created with null location" );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getLocation()
    {
        return location;
    }

    /***************************************************************************
     * @param loc
     **************************************************************************/
    public void setLocation( File loc )
    {
        location = loc;
    }

    /**
     * @return
     */
    public String getTrunkName()
    {
        return trunkName;
    }

    /**
     * @param name
     */
    public void setTrunkName( String name )
    {
        trunkName = name;
    }

    public LockInfo getLockInfo()
    {
        return lockInfo;
    }

    public void setLockInfo( LockInfo info )
    {
        lockInfo = info;
    }

    public List<Baseline> getBaselines()
    {
        return baselines;
    }

    public void setBaselines( List<Baseline> bls )
    {
        baselines = bls;
    }

    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts( List<Product> pds )
    {
        products = pds;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return location.getName();
    }

    public String getName()
    {
        return name;
    }

    public void setName( String str )
    {
        name = str;
    }

    public OpenTask getOpenTaskBySandbox( File location2 )
    {
        OpenTask ot = null;

        for( Baseline b : baselines )
        {
            ot = b.getOpenTaskBySandbox( location );
            if( ot != null )
            {
                break;
            }
        }

        return ot;
    }
}
