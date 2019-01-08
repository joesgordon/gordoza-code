package org.jutils.net;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.jutils.ValidationException;
import org.jutils.io.*;
import org.jutils.ui.event.updater.UpdaterList;

/*******************************************************************************
 * @param <T> The base type of messages to be sent/received.
 ******************************************************************************/
public class MessageThread<T>
{
    /**  */
    private final IDataSerializer<T> msgSerializer;
    /**  */
    private final ConnectionListener listener;

    /**  */
    public final MessageHandlerList<T> messageHandlers;
    /**  */
    public final UpdaterList<String> errorListeners;
    /**  */
    public final UpdaterList<SocketTimeoutException> timeoutListeners;

    /***************************************************************************
     * @param msgSerializer
     **************************************************************************/
    public MessageThread( IDataSerializer<T> msgSerializer )
    {
        this.msgSerializer = msgSerializer;
        this.listener = new ConnectionListener();
        this.messageHandlers = new MessageHandlerList<>();
        this.errorListeners = new UpdaterList<>();
        this.timeoutListeners = listener.timeoutListeners;

        listener.addErrorListener( ( e ) -> handleError( e ) );
        listener.addMessageListener( ( m ) -> handleMessage( m ) );
    }

    /***************************************************************************
     * @param msg
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public NetMessage sendMessage( T msg ) throws IOException
    {
        NetMessage netMsg = listener.sendMessage( write( msg ) );

        messageHandlers.fireHandlers( netMsg, msg );

        return netMsg;
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public void start( IConnection connection )
    {
        listener.start( connection );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void stop() throws IOException
    {
        listener.close();
    }

    /***************************************************************************
     * @param error
     **************************************************************************/
    private void handleError( String error )
    {
        errorListeners.fire( error );
    }

    /***************************************************************************
     * @param netMsg
     **************************************************************************/
    private void handleMessage( NetMessage netMsg )
    {
        T msg = null;

        try
        {
            msg = read( netMsg.contents );

            if( msg == null )
            {
                return;
            }
        }
        catch( IOException ex )
        {
            handleError( ex.getMessage() );
        }
        catch( ValidationException ex )
        {
            handleError( ex.getMessage() );
        }
        finally
        {
            messageHandlers.fireHandlers( netMsg, msg );
        }
    }

    /***************************************************************************
     * @param contents
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private T read( byte [] contents ) throws IOException, ValidationException
    {
        try( ByteArrayStream bas = new ByteArrayStream( contents );
             DataStream stream = new DataStream( bas ) )
        {
            return msgSerializer.read( stream );
        }
    }

    /***************************************************************************
     * @param msg
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private byte [] write( T msg ) throws IOException
    {
        try( ByteArrayStream bas = new ByteArrayStream( 1024 );
             DataStream stream = new DataStream( bas ) )
        {
            msgSerializer.write( msg, stream );

            return bas.toByteArray();
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface IMessageHandler<T>
    {
        /**
         * @param netMsg
         * @param msg
         */
        public void handleMessage( NetMessage netMsg, T msg );
    }

    /**
     * @param <T>
     */
    public static class MessageHandlerList<T>
    {
        /**  */
        private final List<IMessageHandler<T>> handlers;

        /**
         * 
         */
        public MessageHandlerList()
        {
            this.handlers = new ArrayList<>();
        }

        /**
         * @param handler
         */
        public void add( IMessageHandler<T> handler )
        {
            handlers.add( handler );
        }

        /**
         * @param handler
         */
        public void remove( IMessageHandler<T> handler )
        {
            handlers.remove( handler );
        }

        /**
         * 
         */
        public void removeAll()
        {
            handlers.clear();
        }

        /**
         * @param netMsg
         * @param msg
         */
        public void fireHandlers( NetMessage netMsg, T msg )
        {
            for( IMessageHandler<T> handler : handlers )
            {
                handler.handleMessage( netMsg, msg );
            }
        }
    }
}
