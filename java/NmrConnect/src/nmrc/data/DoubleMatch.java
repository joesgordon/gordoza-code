package nmrc.data;

/*******************************************************************************
 * Stores the comparison between two double and whether they match within a
 * specified tolerance.
 ******************************************************************************/
public class DoubleMatch
{
    /** The double to which other will be compared. */
    private Double base;
    /** The double to which base will be compared. */
    private Double other;
    /** The delta between base and other. */
    private Double delta;
    /** The user-specified tolerance to be used for determining equality. */
    private Double tolerance;
    /** Flag that indicates a the double match. */
    private boolean matches;

    /***************************************************************************
     * Initializes with no specified tolerance.
     * @param base
     * @param other
     **************************************************************************/
    public DoubleMatch( Double base, Double other )
    {
        this( base, other, null );
    }

    /***************************************************************************
     * Initializes with the specified tolerance.
     * @param base
     * @param other
     * @param tolerance
     **************************************************************************/
    public DoubleMatch( Double base, Double other, Double tolerance )
    {
        Double delta = null;
        this.tolerance = tolerance;
        matches = false;

        if( base != null && other != null )
        {
            delta = Math.abs( base - other );
        }
        else if( base != null || other != null )
        {
            delta = Double.POSITIVE_INFINITY;
        }

        if( tolerance == null )
        {
            matches = true;
        }
        else
        {
            if( delta == null )
            {
                matches = false;
            }
            else
            {
                matches = delta <= tolerance;
            }
        }

        this.base = base;
        this.other = other;
        this.delta = delta;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Double getBase()
    {
        return base;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Double getDelta()
    {
        return delta;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Double getOther()
    {
        return other;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Double getTolerance()
    {
        return tolerance;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean matches()
    {
        return matches;
    }
}
