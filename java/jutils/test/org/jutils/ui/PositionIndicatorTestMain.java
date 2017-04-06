package org.jutils.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PositionIndicatorTestMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PositionIndicatorApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PositionIndicatorApp implements IFrameApp
    {
        @Override
        public void finalizeGui()
        {
        }

        @Override
        public JFrame createFrame()
        {
            StandardFrameView view = new StandardFrameView();
            JFrame frame = view.getView();

            frame.setTitle( "Position Test Frame" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            frame.setSize( 700, 500 );

            JPanel panel = new JPanel( new BorderLayout() );
            final PositionIndicator pi = new PositionIndicator();

            final long len = 865716124;
            final long size = 4 * 1024 * 1024;

            pi.setLength( len );
            pi.setUnitLength( size );

            pi.addPositionListener( new ItemActionListener<Long>()
            {
                @Override
                public void actionPerformed( ItemActionEvent<Long> event )
                {
                    final long position = event.getItem();

                    final double percent = position / ( double )len;
                    final int uc = ( int )( ( len + size - 1 ) / size );
                    final int ui = ( int )( percent * uc );

                    final long offset = ui * size;

                    pi.setOffset( offset );
                }
            } );

            panel.add( pi, BorderLayout.CENTER );

            frame.setContentPane( panel );

            return frame;
        }
    }
}
