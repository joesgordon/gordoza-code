package org.jutils.ui;

import java.io.File;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.explorer.AppManagerView;
import org.jutils.ui.explorer.data.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppManagerViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new TestMainApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static AppManagerConfig getUnitTestData()
    {
        AppManagerConfig configData = new AppManagerConfig();
        ApplicationConfig pgm = null;
        ExtensionConfig ext = null;

        // Create java files and associated programs.

        ext = new ExtensionConfig( "java", "Java Source File" );
        configData.exts.add( ext );

        pgm = new ApplicationConfig();
        pgm.name = ( "gedit" );
        pgm.path = new File( "/usr/bin/gedit" );
        pgm.args = ( "-hoopde" );

        configData.apps.add( pgm );
        ext.apps.add( pgm.name );

        // Create txt files and associated programs.

        ext = new ExtensionConfig( "txt", "Ascii Text File" );
        configData.exts.add( ext );

        pgm = new ApplicationConfig();
        pgm.name = "file-roller";
        pgm.path = new File( "/usr/bin/file-roller" );

        configData.apps.add( pgm );
        ext.apps.add( pgm.name );

        pgm = new ApplicationConfig();
        pgm.name = "Firefox";
        pgm.path = new File( "/usr/bin/firefox" );

        configData.apps.add( pgm );
        ext.apps.add( pgm.name );

        return configData;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestMainApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            AppManagerView appmanView = new AppManagerView();
            appmanView.setData( getUnitTestData() );

            frameView.setTitle( "File Config Test" );
            frameView.setContent( appmanView.getView() );
            frameView.setSize( 600, 550 );

            return frameView.getView();
        }

        @Override
        public void finalizeGui()
        {
            // TODO Auto-generated method stub

        }
    }
}
