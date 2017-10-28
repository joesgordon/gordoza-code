package org.jutils.ui.net;

import java.nio.ByteBuffer;
import java.util.Random;

import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.io.IStringWriter;
import org.jutils.io.StringPrintStream;
import org.jutils.net.NetMessage;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
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
            NetMessagesView panel = new NetMessagesView( new MessageFields(),
                new MsgWriter() );

            frame.setContentPane( panel.getView() );

            frame.setTitle( "Net Messages View Test" );
            frame.setIconImages( IconConstants.getPageMagImages() );
            frame.setSize( 680, 400 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );
            panel.addMessage( buildMessage() );

            return frame;
        }

        private NetMessage buildMessage()
        {
            int len = rand.nextInt( 1024 );
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
