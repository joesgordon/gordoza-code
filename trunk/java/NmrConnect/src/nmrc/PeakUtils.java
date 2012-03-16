package nmrc;

import java.util.ArrayList;
import java.util.List;

import nmrc.data.DoubleMatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class PeakUtils
{
    /***************************************************************************
     * @param match
     * @param tolerance
     * @return
     **************************************************************************/
    public static boolean isWithinTolerance( DoubleMatch match, double tolerance )
    {
        if( match == null )
        {
            return false;
        }
        else if( match.getDelta() == null )
        {
            return true;
        }

        return match.getDelta() <= tolerance;
    }

    /***************************************************************************
     * @param these
     * @param those
     * @param tol
     * @return
     **************************************************************************/
    public static DoubleMatch getBestMatch( List<Double> these,
        List<Double> those )
    {
        DoubleMatch match = null;

        if( these == null || those == null )
        {
            if( these == null && those == null )
            {
                match = new DoubleMatch( null, null );
            }
        }
        else
        {
            match = new DoubleMatch( null, null );
            DoubleMatch newMatch;

            for( double thisd : these )
            {
                for( double thatd : those )
                {
                    newMatch = new DoubleMatch( thisd, thatd );

                    if( match.getDelta() == null ||
                        newMatch.getDelta() < match.getDelta() )
                    {
                        match = newMatch;
                    }
                }
            }
        }

        return match;
    }

    /***************************************************************************
     * @param dest
     * @param src
     * @param tolerance
     * @return
     **************************************************************************/
    public static List<Double> addUnique( List<Double> dest, List<Double> src,
        double tolerance )
    {
        if( dest == null && src == null )
        {
            return null;
        }
        else if( src == null )
        {
            return dest;
        }
        else if( dest == null )
        {
            dest = new ArrayList<Double>();
        }

        for( double d : src )
        {
            addUnique( dest, d, tolerance );
        }

        return dest;
    }

    /***************************************************************************
     * @param dest
     * @param val
     * @param tolerance
     **************************************************************************/
    static void addUnique( List<Double> dest, double val, double tolerance )
    {
        if( !contains( dest, val, tolerance ) )
        {
            dest.add( val );
        }
    }

    /***************************************************************************
     * @param list
     * @param val
     * @param tolerance
     * @return
     **************************************************************************/
    public static boolean contains( List<Double> list, double val,
        double tolerance )
    {
        for( double d : list )
        {
            if( doubleEquals( d, val, tolerance ) )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * @param thisd
     * @param thatd
     * @param tol
     * @return
     **************************************************************************/
    public static boolean doubleEquals( double thisd, double thatd, double tol )
    {
        double diff = thisd - thatd;

        if( Math.abs( diff ) <= tol )
        {
            return true;
        }

        return false;
    }
}
