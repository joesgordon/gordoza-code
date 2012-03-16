package testbed;

import java.util.ArrayList;
import java.util.Random;

public class TestProb
{
    public static void main( String[] args )
    {
        Random rand = new Random();
        int numAssignments = 5;
        int numIterations = 100000;
        int[] actuals = getRandomArray( numAssignments, rand );
        int numMatched = 0;

        for( int i = 0; i < numIterations; i++ )
        {
            int[] guess = getRandomArray( numAssignments, rand );

            for( int j = 0; j < actuals.length; j++ )
            {
                if( actuals[j] == guess[j] )
                {
                    numMatched++;
                    break;
                }
            }
        }

        System.out.println( "# of matches: " + numMatched );
    }

    private static int[] getRandomArray( int len, Random rand )
    {
        int[] array = new int[len];
        ArrayList<Integer> ints = new ArrayList<Integer>( len );

        for( int i = 0; i < len; i++ )
        {
            ints.add( i );
        }

        for( int i = 0; i < len; i++ )
        {
            int index = rand.nextInt( ints.size() );
            array[i] = ints.remove( index );
        }

        return array;
    }
}
