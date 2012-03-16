package nmrc.data;

import java.util.ArrayList;
import java.util.List;

import nmrc.model.IPeakRecord;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeakRecord implements IPeakRecord
{
    /**  */
    private int index;
    /**  */
    private String name;
    /**  */
    private double hn;
    /**  */
    private double n15;
    /**  */
    private ListAggregator<Double> alphas;
    /**  */
    private ListAggregator<Double> betas;
    /**  */
    private ListAggregator<Double> previousAlphas;
    /**  */
    private ListAggregator<Double> previousBetas;
    /**  */
    private ListAggregator<Double> alternateBetas;
    /**  */
    private List<IPeakRecord> duplicatePeaks;

    /***************************************************************************
     * @param name
     * @param hn
     * @param n15
     * @param alphas
     * @param betas
     * @param previousAlphas
     * @param previousBetas
     * @param alternateBetas
     **************************************************************************/
    public PeakRecord( int index, String name, double hn, double n15,
        List<Double> alphas, List<Double> betas, List<Double> previousAlphas,
        List<Double> previousBetas, List<Double> alternateBetas )
    {
        this.index = index;
        this.name = name;
        this.hn = hn;
        this.n15 = n15;

        this.alphas = new ListAggregator<Double>();
        this.betas = new ListAggregator<Double>();
        this.previousAlphas = new ListAggregator<Double>();
        this.previousBetas = new ListAggregator<Double>();
        this.alternateBetas = new ListAggregator<Double>();

        this.alphas.addList( alphas );
        this.betas.addList( betas );
        this.previousAlphas.addList( previousAlphas );
        this.previousBetas.addList( previousBetas );
        this.alternateBetas.addList( alternateBetas );

        this.duplicatePeaks = new ArrayList<IPeakRecord>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIndex()
    {
        return index;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Double> getAlternateCarbonBeta()
    {
        return alternateBetas.getList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Double> getCarbonAlpha()
    {
        return alphas.getList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Double> getCarbonBeta()
    {
        return betas.getList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getHn()
    {
        return hn;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getN15()
    {
        return n15;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getPeakName()
    {
        StringBuilder builder = new StringBuilder( 255 );

        builder.append( name );

        for( IPeakRecord record : duplicatePeaks )
        {
            builder.append( ", " + record.getPeakName() );
        }

        return builder.toString();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Double> getPreviousCarbonAlpha()
    {
        return previousAlphas.getList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Double> getPreviousCarbonBeta()
    {
        return previousBetas.getList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean hasPrevious()
    {
        return !duplicatePeaks.isEmpty();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addDuplicate( IPeakRecord record )
    {
        duplicatePeaks.add( record );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<IPeakRecord> getDuplicates()
    {
        return duplicatePeaks;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeDuplicate( IPeakRecord record )
    {
        duplicatePeaks.remove( record );
    }
}

class ListAggregator<T>
{
    private List<List<T>> lists;

    public ListAggregator()
    {
        lists = new ArrayList<List<T>>();
    }

    public void addList( List<T> list )
    {
        if( list != null && !list.isEmpty() )
        {
            lists.add( list );
        }
    }

    public void removeList( List<T> list )
    {
        if( list != null && !list.isEmpty() )
        {
            lists.remove( list );
        }
    }

    public List<T> getList()
    {
        List<T> list = null;

        if( !lists.isEmpty() )
        {
            list = new ArrayList<T>();

            for( List<T> lst : lists )
            {
                list.addAll( lst );
            }
        }

        return list;
    }
}
