package org.jutils;

import org.jutils.ui.BuildInfoView;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;

/*******************************************************************************
 * Defines an application to display the current version of this library.
 ******************************************************************************/
public class JUtilsInfoMain
{
    /***************************************************************************
     * Defines the application entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new BuildInfoMainApp() );
    }

    /***************************************************************************
     * Defines the app that builds the UI to be displayed.
     **************************************************************************/
    private static final class BuildInfoMainApp implements IApplication
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void createAndShowUi()
        {
            BuildInfoView.show();
        }
    }
}
