package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*******************************************************************************
 * Helper class to provide a standard way of displaying a frame that optionally
 * has a tray icon.
 ******************************************************************************/
public abstract class FrameRunner extends MainRunner
{
    /** The frame displayed. */
    private JFrame frame;

    /***************************************************************************
     * @return
     **************************************************************************/
    protected TrayIcon createTrayIcon( Image img, String tooltip, JFrame frame,
        PopupMenu popup )
    {
        TrayIcon icon = null;

        if( SystemTray.isSupported() )
        {
            ActionListener trayListener = new TrayIconListener( frame );

            SystemTray tray = SystemTray.getSystemTray();

            icon = new TrayIcon( img, tooltip );
            icon.addActionListener( trayListener );
            icon.setImageAutoSize( true );
            try
            {
                tray.add( icon );
                frame.addWindowListener( new MinimizeListener( frame ) );
            }
            catch( AWTException ex )
            {
                // Cannot add Icon. Warn user if they want to continue.
                int ans = JOptionPane.showConfirmDialog( frame,
                    "Cannot load icon! Continue?", "WARNING",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
                if( ans == JOptionPane.NO_OPTION )
                {
                    System.exit( 0 );
                }
            }

            icon.setPopupMenu( popup );
        }

        return icon;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void createAndShowGui()
    {
        frame = createFrame();

        // ---------------------------------------------------------------------
        // Validate frames that have preset sizes. Pack frames that have
        // useful preferred size info, e.g. from their layout.
        // ---------------------------------------------------------------------
        if( validate() )
        {
            frame.validate();
        }
        else
        {
            frame.pack();
        }

        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        finalizeGui( frame );
    }

    protected void finalizeGui( JFrame frame )
    {
        ;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JFrame getFrame()
    {
        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    protected abstract JFrame createFrame();

    /***************************************************************************
     * @return
     **************************************************************************/
    protected abstract boolean validate();

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class TrayIconListener implements ActionListener
    {
        private JFrame f;

        public TrayIconListener( JFrame frame )
        {
            f = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            f.setVisible( !f.isVisible() );
            if( f.isVisible() )
            {
                // -------------------------------------------------------------
                // This kinda stinks because it cannot retain the extended
                // state. I've tried all sorts of ways and this is the only
                // thing that doesn't pop the window up behind other things a
                // still give it focus. So, don't remove it.
                // -------------------------------------------------------------
                f.setExtendedState( JFrame.NORMAL );
                f.requestFocus();
                f.toFront();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class MinimizeListener extends WindowAdapter
    {
        private JFrame frame;

        public MinimizeListener( JFrame f )
        {
            frame = f;
        }

        public void windowIconified( WindowEvent e )
        {
            frame.setVisible( false );
        }
    }
}
