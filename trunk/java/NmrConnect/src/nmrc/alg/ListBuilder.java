package nmrc.alg;

import java.util.List;

/*******************************************************************************
 * Provides a null-safe method of adding all the elements of one list to
 * another.
 ******************************************************************************/
public class ListBuilder<T>
{
    /***************************************************************************
     * Adds all the elements in <code>src</code> to <code>dest</code>.
     * <code>src</code> is allowed to be <code>null</code>.
     * @param src
     * @param dest
     **************************************************************************/
    public void addAllItems( List<T> src, List<T> dest )
    {
        if( src != null )
        {
            dest.addAll( src );
        }
    }
}
