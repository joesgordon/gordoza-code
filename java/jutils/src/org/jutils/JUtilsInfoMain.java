package org.jutils;

import org.jutils.ui.BuildInfoView;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JUtilsInfoMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new BuildInfoMainApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class BuildInfoMainApp implements IApplication
    {
        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            BuildInfoView.show();
        }
    }
}
