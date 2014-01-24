package org.jutils.ui.app;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class FrameApplication implements IApplication
{
    /**  */
    private final IFrameApp frameApp;
    /**  */
    private final boolean validate;
    /**  */
    private final String lookAndFeel;

    /**  */
    private JFrame frame;

    public FrameApplication( IFrameApp frameApp )
    {
        this( frameApp, true );
    }

    public FrameApplication( IFrameApp frameApp, boolean validate )
    {
        this( frameApp, validate, null );
    }

    public FrameApplication( IFrameApp frameApp, boolean validate,
        String lookAndFeel )
    {
        this.frameApp = frameApp;
        this.validate = validate;
        this.lookAndFeel = lookAndFeel;
    }

    @Override
    public String getLookAndFeelName()
    {
        return lookAndFeel;
    }

    @Override
    public void createAndShowUi()
    {
        frame = frameApp.createFrame();

        // ---------------------------------------------------------------------
        // Validate frames that have preset sizes. Pack frames that have
        // useful preferred size info, e.g. from their layout.
        // ---------------------------------------------------------------------
        if( validate )
        {
            frame.validate();
        }
        else
        {
            frame.pack();
        }

        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        frameApp.finalizeGui();
    }

    // TODO use it or lose it.
    // public JFrame getFrame()
    // {
    // return frame;
    // }

    /***************************************************************************
     * Creates and displays a tray icon with the provided image, tool tip, and
     * popup menu which displays/hides the provided frame when double-clicked.
     * @param img The image (icon) to be displayed.
     * @param tooltip The tool tip to be displayed.
     * @param frame The frame to be displayed/hidden.
     * @param popup The popup menu to be displayed on right-click.
     * @return The tray icon created or {@code null} if the system tray is not
     * supported.
     * @throws UnsupportedOperationException if {@link SystemTray#add(TrayIcon)}
     * throws an {@link AWTException}.
     **************************************************************************/
    public static final TrayIcon createTrayIcon( Image img, String tooltip,
        JFrame frame, PopupMenu popup ) throws UnsupportedOperationException
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
                throw new UnsupportedOperationException(
                    "Cannot load icon in tray", ex );
            }

            icon.setPopupMenu( popup );
        }

        return icon;
    }

    public static void invokeLater( IFrameApp app )
    {
        invokeLater( app, true, null );
    }

    public static void invokeLater( IFrameApp app, boolean validate )
    {
        invokeLater( app, validate, null );
    }

    public static void invokeLater( IFrameApp app, boolean validate,
        String lookAndFeel )
    {
        FrameApplication fApp = new FrameApplication( app, validate,
            lookAndFeel );
        AppRunner runner = new AppRunner( fApp );

        SwingUtilities.invokeLater( runner );
    }

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
