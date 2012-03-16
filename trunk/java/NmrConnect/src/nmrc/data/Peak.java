package nmrc.data;

import nmrc.model.IPeak;
import nmrc.model.IPeakRecord;

/***************************************************************************
 * 
 **************************************************************************/
public class Peak implements IPeak
{
    /**  */
    private IPeakRecord peakRecord;
    /**  */
    private IPeak previousPeak;
    /**  */
    private Double alpha;
    /**  */
    private Double beta;
    /**  */
    private Double previousAlpha;
    /**  */
    private Double previousBeta;
    /**  */
    private boolean isPrevious;

    /***************************************************************************
     * @param record
     **************************************************************************/
    public Peak( IPeakRecord record )
    {
        peakRecord = record;
        isPrevious = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getAlpha()
    {
        return alpha;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getBeta()
    {
        return beta;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getPreviousAlpha()
    {
        return previousAlpha;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getPreviousBeta()
    {
        return previousBeta;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IPeak getPrevious()
    {
        return previousPeak;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean hasPrevious()
    {
        return previousPeak != null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IPeakRecord getRecord()
    {
        return peakRecord;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setPrevious( IPeak peak, Double alpha, Double beta,
        Double previousAlpha, Double previousBeta )
    {
        if( previousPeak != null )
        {
            Peak p = ( Peak )previousPeak;

            p.alpha = null;
            p.beta = null;

            this.previousAlpha = null;
            this.previousBeta = null;

            previousPeak = null;

            p.isPrevious = false;
        }

        if( peak != null )
        {
            Peak p = ( Peak )peak;

            this.previousAlpha = alpha;
            this.previousBeta = beta;

            p.alpha = previousAlpha;
            p.beta = previousBeta;

            previousPeak = peak;

            p.isPrevious = true;
        }
    }

    @Override
    public boolean isPrevious()
    {
        return isPrevious;
    }
}
