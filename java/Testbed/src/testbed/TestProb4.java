package testbed;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class TestProb4
{
    private static class TestResults
    {
        public long numCombinations;
        public long numMatched;

        public TestResults()
        {
            numCombinations = 0;
            numMatched = 0;
        }
    }

    public static void main( String[] args )
    {
        PeriodFormatterBuilder formatter = new PeriodFormatterBuilder();
        formatter.appendMinutes();
        formatter.appendSuffix( " m", " m" );
        formatter.appendSeparator( " " );
        formatter.appendSeconds();
        formatter.appendSuffix( " s", " s" );
        formatter.appendSeparator( " " );
        formatter.appendMillis();
        formatter.appendSuffix( " ms", " ms" );
        PeriodFormatter daysHoursMinutes = formatter.toFormatter();

        List<Integer> values;
        DateTime start;
        DateTime stop;
        Period p;
        for( int i = 2; i < 15; i++ )
        {
            values = genList( i );

            start = new DateTime();

            testValues( values );
            stop = new DateTime();

            p = new Period( start, stop );

            System.out.println(
                "The calculation took: " + daysHoursMinutes.print( p ) );

            p = new Period(
                ( stop.getMillis() - start.getMillis() ) * ( i + 1 ) );
            System.out.println( "The calculation for " + ( i + 1 ) +
                " should take: " + daysHoursMinutes.print( p ) );

            System.out.println();
        }
    }

    public static List<Integer> genList( int size )
    {
        List<Integer> values = new ArrayList<Integer>( size );

        for( int i = 0; i < size; i++ )
        {
            values.add( i + 1 );
        }

        return values;
    }

    public static void testValues( List<Integer> values )
    {
        TestResults results = new TestResults();
        testCombinations( values, results );

        double percent = results.numMatched /
            ( double )results.numCombinations * 100.0;

        System.out.println( "Matched " + results.numMatched + " out of " +
            results.numCombinations + " with " + values.size() + " numbers: " +
            percent + "%" );
    }

    private static <T> boolean testCombination( List<T> values, List<T> guess )
    {
        for( int i = 0; i < values.size(); i++ )
        {
            if( values.get( i ) == guess.get( i ) )
            {
                // printComb( guess, true );
                return true;
            }
        }

        // printComb( guess, false );

        return false;
    }

    @SuppressWarnings( "unused")
    private static <T> void printComb( List<T> items, boolean matched )
    {
        System.out.print( matched ? "Y: " : "N: " );

        for( int i = 0; i < items.size(); i++ )
        {
            if( i > 0 )
            {
                System.out.print( ", " );
            }
            System.out.print( items.get( i ).toString() );
        }

        System.out.println();
    }

    private static <T> void testCombinations( List<T> values,
        TestResults results )
    {
        testCombinations( values, new ArrayList<T>( values ), 0, results );
    }

    private static <T> void swapValues( List<T> values, int index1, int index2 )
    {
        if( index1 != index2 )
        {
            T t2 = values.get( index2 );
            values.set( index2, values.get( index1 ) );
            values.set( index1, t2 );
        }
    }

    private static <T> void testCombinations( List<T> values, List<T> comb,
        int depth, TestResults results )
    {
        if( depth >= values.size() )
        {
            results.numCombinations++;
            if( testCombination( values, comb ) )
            {
                results.numMatched++;
            }

            return;
        }

        for( int i = depth; i < values.size(); i++ )
        {
            swapValues( comb, depth, i );
            testCombinations( values, comb, depth + 1, results );
            swapValues( comb, depth, i );
        }
    }

}
