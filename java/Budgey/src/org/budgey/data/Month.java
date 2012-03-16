package org.budgey.data;

public enum Month
{
    JANUARY( 0 ),
    FEBRUARY( 1 ),
    MARCH( 2 ),
    APRIL( 3 ),
    MAY( 4 ),
    JUNE( 5 ),
    JULY( 6 ),
    AUGUST( 7 ),
    SEPTEMBER( 8 ),
    OCTOBER( 9 ),
    NOVEMBER( 10 ),
    DECEMBER( 11 );

    private int ord;

    private Month( int ordinal )
    {
        ord = ordinal;
    }

    public int toCalendarMonth()
    {
        return ord;
    }

    public String toString()
    {
        String s = super.toString();

        s = s.charAt( 0 ) + s.substring( 1 ).toLowerCase();

        return s;
    }

    public static Month fromCalendarMonth( int i )
    {
        Month[] vals = Month.values();

        for( Month m : vals )
        {
            if( m.ord == i )
            {
                return m;
            }
        }

        throw new IllegalArgumentException( "Unknown calendar month: " + i );
    }
}
