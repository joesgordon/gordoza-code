package testbed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestProb3
{
    private static class TestResults
    {
        public int numCombinations;
        public int numMatched;

        public TestResults()
        {
            numCombinations = 0;
            numMatched = 0;
        }
    }

    public static void main( String[] args )
    {
        List<Integer> values = Arrays.asList( 1, 2, 3, 4, 5 );
        TestResults results = new TestResults();
        testCombinations( values, results );

        double percent = results.numMatched / ( double )results.numCombinations;

        System.out.println( "Matched " + results.numMatched + " out of " +
            results.numCombinations + ": " + percent + "%" );
    }

    private static <T> boolean testCombination( List<T> values, List<T> guess )
    {
        for( int i = 0; i < values.size(); i++ )
        {
            if( values.get( i ) == guess.get( i ) )
            {
                return true;
            }
        }

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
        testCombinations( values, new ArrayList<T>(), values, results );
    }

    private static <T> void testCombinations( List<T> values, List<T> comb,
        List<T> choices, TestResults results )
    {
        if( choices.size() == 0 )
        {
            results.numCombinations++;
            if( testCombination( values, comb ) )
            {
                results.numMatched++;
            }

            return;
        }

        for( T t : choices )
        {
            List<T> newComb = new ArrayList<T>( comb.size() + 1 );
            List<T> newChoices = new ArrayList<T>( choices );

            newChoices.remove( t );
            newComb.addAll( comb );
            newComb.add( t );
            testCombinations( values, newComb, newChoices, results );
        }
    }

}
