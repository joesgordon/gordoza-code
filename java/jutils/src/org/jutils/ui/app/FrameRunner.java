package org.jutils.ui.app;

import javax.swing.SwingUtilities;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FrameRunner
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private FrameRunner()
    {
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    public static void invokeLater( IFrameApp app )
    {
        invokeLater( app, true, null );
    }

    /***************************************************************************
     * @param app
     * @param validate
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate )
    {
        invokeLater( app, validate, null );
    }

    /***************************************************************************
     * @param app
     * @param validate
     * @param lookAndFeel
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate,
        String lookAndFeel )
    {
        IApplication fApp = new FrameApplication( app, validate, lookAndFeel );
        Runnable runner = new AppRunnable( fApp );

        SwingUtilities.invokeLater( runner );
    }
}
