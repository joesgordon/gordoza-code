package org.jutils.io;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IStringSerializer<T>
{
    /***************************************************************************
     * @param item
     * @return
     **************************************************************************/
    public String toString( T item );
}
