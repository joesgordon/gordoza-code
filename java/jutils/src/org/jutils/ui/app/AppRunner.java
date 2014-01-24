package org.jutils.ui.app;

import javax.swing.UIManager;

import com.jgoodies.looks.Options;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppRunner implements Runnable
{
    /**  */
    private final IApplication app;

    /***************************************************************************
     * @param app
     **************************************************************************/
    public AppRunner( IApplication app )
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
            String lafName = app.getLookAndFeelName();

            if( lafName == null )
            {
                lafName = Options.PLASTICXP_NAME;
            }

            UIManager.setLookAndFeel( lafName );

            app.createAndShowUi();
        }
        catch( Throwable ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }
}
