package testbed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestProb2
{
    public static void main( String[] args )
    {
        List<Integer> values = Arrays.asList( 1, 2, 3, 4, 5 );
        List<List<Integer>> combinations = generateCombinations( values );
        int numMatched = 0;

        boolean matched;

        for( List<Integer> guess : combinations )
        {
            matched = testCombination( values, guess );
            if( matched )
            {
                numMatched++;
            }
            printComb( guess, matched );
        }

        double percent = numMatched / ( double )combinations.size();

        System.out.println( "Matched " + numMatched + " out of " +
            combinations.size() + ": " + percent + "%" );
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

    private static <T> List<List<T>> generateCombinations( List<T> values )
    {
        ArrayList<List<T>> combs = new ArrayList<List<T>>();

        for( int i = 0; i < values.size(); i++ )
        {
            T t = values.get( i );
            List<T> newValues = new ArrayList<T>( values.size() - 1 );

            // -----------------------------------------------------------------
            // Create the choice list
            // -----------------------------------------------------------------
            for( int j = 0; j < values.size(); j++ )
            {
                if( j != i )
                {
                    newValues.add( values.get( j ) );
                }
            }

            // -----------------------------------------------------------------
            // Create the subCombinations
            // -----------------------------------------------------------------
            List<List<T>> subCombs;
            if( newValues.size() > 1 )
            {
                subCombs = generateCombinations( newValues );
            }
            else
            {
                subCombs = new ArrayList<List<T>>( 1 );
                subCombs.add( newValues );
            }

            // -----------------------------------------------------------------
            // Add all the sub-combinations to this choice.
            // -----------------------------------------------------------------
            for( List<T> subComb : subCombs )
            {
                ArrayList<T> newComb = new ArrayList<T>( values.size() );
                newComb.add( t );
                newComb.addAll( subComb );
                combs.add( newComb );
            }
        }

        return combs;
    }
}
