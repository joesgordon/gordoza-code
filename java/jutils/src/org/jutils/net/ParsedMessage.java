package org.jutils.net;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ParsedMessage<T> extends NetMessage
{
    /**  */
    public final T message;

    /***************************************************************************
     * @param message
     * @param netMsg
     **************************************************************************/
    public ParsedMessage( T message, NetMessage netMsg )
    {
        super( netMsg );

        this.message = message;
    }
}
