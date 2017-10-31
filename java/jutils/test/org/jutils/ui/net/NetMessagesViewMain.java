package org.jutils.ui.net;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.IStringWriter;
import org.jutils.io.StringPrintStream;
import org.jutils.net.NetMessage;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.net.NetMessagesView.IMessageFields;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessagesViewMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new HexMessageApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HexMessageApp implements IFrameApp
    {
        private final Random rand = new Random( 42 );

        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();

            // HexMessagePanel panel = new HexMessagePanel();
            NetMessagesView view = new NetMessagesView( new MessageFields(),
                new MsgWriter() );

            frame.setContentPane( createContent( view ) );

            frame.setTitle( "Net Messages View Test" );
            frame.setIconImages( IconConstants.getPageMagImages() );
            frame.setSize( 680, 400 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            view.addMessage( buildMessage() );
            view.addMessage( buildMessage() );
            view.addMessage( buildMessage() );
            view.addMessage( buildMessage() );
            view.addMessage( buildMessage() );
            view.addMessage( buildMessage() );

            return frame;
        }

        private Container createContent( NetMessagesView view )
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createToolbar( view ), BorderLayout.NORTH );
            panel.add( view.getView(), BorderLayout.CENTER );

            return panel;
        }

        private Component createToolbar( NetMessagesView view )
        {
            JToolBar toolbar = new JGoodiesToolBar();

            Action action = new ActionAdapter(
                ( e ) -> view.addMessage( buildMessage() ), "Add Message",
                IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );

            SwingUtils.addActionToToolbar( toolbar, action );

            return toolbar;
        }

        private NetMessage buildMessage()
        {
            int len = 20 + rand.nextInt( 1004 );
            byte [] bytes = new byte[len];
            rand.nextBytes( bytes );

            NetMessage msg = new NetMessage( true, "127.0.0.1", 80, bytes );
            return msg;
        }

        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageFields implements IMessageFields
    {
        @Override
        public int getFieldCount()
        {
            return 1;
        }

        @Override
        public String getFieldName( int index )
        {
            if( index == 0 )
            {
                return "First Int";
            }

            return null;
        }

        @Override
        public String getFieldValue( NetMessage message, int index )
        {
            if( index == 0 )
            {
                return "" + ByteBuffer.wrap( message.contents ).getInt();
            }

            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class MsgWriter implements IStringWriter<NetMessage>
    {
        @Override
        public String toString( NetMessage item )
        {
            try( StringPrintStream str = new StringPrintStream() )
            {
                for( int i = 0; i < item.contents.length; i++ )
                {
                    str.println( "Byte %d = %02X", i, item.contents[i] );
                }
                // TODO Auto-generated method stub
                return str.toString();
            }
        }
    }
}
