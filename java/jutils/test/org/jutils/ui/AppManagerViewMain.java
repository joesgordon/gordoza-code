package org.jutils.ui;

import java.io.*;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.XStreamUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.*;
import org.jutils.ui.explorer.AppManagerView;
import org.jutils.ui.explorer.data.*;

import com.thoughtworks.xstream.XStreamException;

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
            frameView.setToolbar( createToolbar( appmanView ) );
            frameView.setContent( appmanView.getView() );
            frameView.setSize( 600, 550 );

            return frameView.getView();
        }

        @Override
        public void finalizeGui()
        {
        }

        private JToolBar createToolbar( AppManagerView appmanView )
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar,
                createOpenAction( appmanView ) );
            SwingUtils.addActionToToolbar( toolbar,
                createSaveAction( appmanView ) );

            return toolbar;
        }

        private Action createOpenAction( AppManagerView appmanView )
        {
            Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
            FileChooserListener fcl = new FileChooserListener(
                appmanView.getView(), "Open Application Manager Configuration",
                new OpenListener( appmanView ), false );
            return new ActionAdapter( fcl, "Open", icon );
        }

        private Action createSaveAction( AppManagerView appmanView )
        {
            Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
            FileChooserListener fcl = new FileChooserListener(
                appmanView.getView(), "Save Application Manager Configuration",
                new SaveListener( appmanView ), true );
            return new ActionAdapter( fcl, "Save", icon );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class OpenListener implements IFileSelectionListener
    {
        private AppManagerView view;

        public OpenListener( AppManagerView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            return null;
        }

        @Override
        public void filesChosen( File [] files )
        {
            File file = files[0];

            try
            {
                AppManagerConfig cfg = XStreamUtils.readObjectXStream( file );

                view.setData( cfg );
            }
            catch( XStreamException ex )
            {
                SwingUtils.showErrorMessage( view.getView(),
                    "Error reading from file: " + file.getAbsolutePath(),
                    "Read Error" );
            }
            catch( FileNotFoundException ex )
            {
                SwingUtils.showErrorMessage( view.getView(),
                    "File not found: " + file.getAbsolutePath(),
                    "File Not Found Error" );
            }
            catch( IOException ex )
            {
                SwingUtils.showErrorMessage( view.getView(),
                    "Error reading from file: " + file.getAbsolutePath(),
                    "I/O Error" );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SaveListener implements IFileSelectionListener
    {
        private AppManagerView view;

        public SaveListener( AppManagerView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            return null;
        }

        @Override
        public void filesChosen( File [] files )
        {
            File file = files[0];
            AppManagerConfig cfg = view.getData();

            try
            {
                XStreamUtils.writeObjectXStream( cfg, file );
            }
            catch( XStreamException e )
            {
                SwingUtils.showErrorMessage( view.getView(),
                    "Error reading from file: " + file.getAbsolutePath(),
                    "Serialization Error" );
            }
            catch( IOException e )
            {
                SwingUtils.showErrorMessage( view.getView(),
                    "Error reading from file: " + file.getAbsolutePath(),
                    "I/O Error" );
            }
        }
    }
}
