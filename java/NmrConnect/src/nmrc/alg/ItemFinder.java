package nmrc.alg;

import java.util.ArrayList;
import java.util.List;

import nmrc.model.IMatcher;

/*******************************************************************************
 * Provides a standard way of finding a list of equal objects in a list of those
 * objects.
 ******************************************************************************/
public class ItemFinder<T>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public ItemFinder()
    {
    }

    /***************************************************************************
     * Finds all items in the provided list that are equal according to the
     * provided tester.
     * @param tester
     * @param start
     * @param items
     * @return
     **************************************************************************/
    public List<T> findItems( IMatcher<T> tester, int start, List<T> items )
    {
        List<T> dups = new ArrayList<T>();

        // for( T t : ts )
        // {
        // if( tester.equals( t ) )
        // {
        // dups.add( t );
        // }
        // }

        for( int i = start; i < items.size(); i++ )
        {
            T t = items.get( i );
            if( tester.matches( t ) )
            {
                dups.add( t );
            }
        }

        return dups;
    }
}
