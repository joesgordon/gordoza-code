package org.jutils.ui.app;

public class AppRunnable implements Runnable
{
    /**  */
    private final IApplication app;

    /***************************************************************************
     * @param app
     **************************************************************************/
    public AppRunnable( IApplication app )
    {
        this.app = app;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run()
    {
        try
        {
            AppRunner.setDefaultLaf( app.getLookAndFeelName() );

            app.createAndShowUi();
        }
        catch( IllegalStateException ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }
}
