package org.jutils.utils;

public class RunningStat
{
    private int count;
    private double m_oldM, m_newM, m_oldS, m_newS;
    private double min;
    private double max;

    public RunningStat()
    {
        this.count = 0;
        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;
    }

    public void account( double x )
    {
        count++;

        min = Math.min( x, min );
        max = Math.max( x, max );

        // See Knuth TAOCP vol 2, 3rd edition, page 232
        if( count == 1 )
        {
            m_oldM = m_newM = x;
            m_oldS = 0.0;
        }
        else
        {
            m_newM = m_oldM + ( x - m_oldM ) / count;
            m_newS = m_oldS + ( x - m_oldM ) * ( x - m_newM );

            // set up for next iteration
            m_oldM = m_newM;
            m_oldS = m_newS;
        }
    }

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
    }

    public double calcMean()
    {
        return ( count > 0 ) ? m_newM : 0.0;
    }

    public double calcVariance()
    {
        return ( ( count > 1 ) ? m_newS / ( count - 1 ) : 0.0 );
    }

    public double calcStandardDeviation()
    {
        return Math.sqrt( calcVariance() );
    }
}
