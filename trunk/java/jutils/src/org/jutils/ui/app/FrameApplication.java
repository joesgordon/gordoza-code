package org.jutils.ui.app;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.StandardUncaughtExceptionHandler;

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
        createAndShowFrame();
    }

    public JFrame createAndShowFrame()
    {
        JFrame frame = frameApp.createFrame();

        UncaughtExceptionHandler h;
        h = new StandardUncaughtExceptionHandler( frame );
        Thread.setDefaultUncaughtExceptionHandler( h );

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

        return frame;
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
}
