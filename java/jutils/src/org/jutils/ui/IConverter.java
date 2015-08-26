package org.jutils.ui;

/*******************************************************************************
 * @param <F> Type to be converted from.
 * @param <T> Type to be converted to.
 ******************************************************************************/
public interface IConverter<F, T>
{
    /***************************************************************************
     * Converts {@code to} to {@code from}, setting {@code from} if possible.
     * @param to
     * @param from
     * @return
     **************************************************************************/
    public F convTo( T to, F from );

    /***************************************************************************
     * Converts {@code from} to {@code to}, setting {@code to} if possible.
     * @param from
     * @param to
     * @return
     **************************************************************************/
    public T convFrom( F from, T to );
}
