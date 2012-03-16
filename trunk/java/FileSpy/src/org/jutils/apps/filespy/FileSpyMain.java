package org.jutils.apps.filespy;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.io.IOUtils;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyMain extends FrameRunner
{
    /**  */
    public static final String CONFIG_FILENAME = ".fileSpy.conf";
    /**  */
    private static FileSpyData configData = null;

    /***************************************************************************
     * @return FileSpyData
     **************************************************************************/
    public static FileSpyData getConfigData()
    {
        if( configData == null )
        {
            File file = new File( IOUtils.USERS_DIR, CONFIG_FILENAME );
            try
            {
                configData = ( FileSpyData )FileSpyData.read( file );
            }
            catch( IOException ex )
            {
                configData = new FileSpyData();
                configData.setFile( file );
            }
        }

        return configData;
    }

    /***************************************************************************
     * Construct and show the application.
     **************************************************************************/
    @Override
    protected JFrame createFrame()
    {
        FileSpyFrame frame = new FileSpyFrame();
        FileSpyUncaughtExceptionHandler fsue = new FileSpyUncaughtExceptionHandler(
            frame );
        Thread.setDefaultUncaughtExceptionHandler( fsue );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected boolean validate()
    {
        return true;
    }

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new FileSpyMain() );
    }

    private static class FileSpyUncaughtExceptionHandler implements
        Thread.UncaughtExceptionHandler
    {
        private FileSpyFrame frame = null;

        public FileSpyUncaughtExceptionHandler( FileSpyFrame frame )
        {
            this.frame = frame;
        }

        public void uncaughtException( Thread t, Throwable e )
        {
            e.printStackTrace();

            JOptionPane optionPane = new JOptionPane(
                "The following error has occurred. You may " +
                    "choose to ignore and continue or quit." + Utils.NEW_LINE +
                    e.getMessage(), JOptionPane.ERROR_MESSAGE );
            JButton continueButton = new JButton( "Continue" );
            JButton quitButton = new JButton( "Quit" );

            Dimension cDim = continueButton.getPreferredSize();
            Dimension qDim = quitButton.getPreferredSize();

            cDim.width = Math.max( cDim.width, qDim.width ) + 10;
            cDim.height = Math.max( cDim.height, qDim.height ) + 10;

            continueButton.setPreferredSize( cDim );
            continueButton.setMinimumSize( cDim );

            quitButton.setPreferredSize( cDim );
            quitButton.setMinimumSize( cDim );
            quitButton.addActionListener( new QuitListener() );

            optionPane.setOptions( new Object[] { continueButton, quitButton } );

            JDialog dialog = optionPane.createDialog( frame, "ERROR" );

            continueButton.addActionListener( new ContinueListener( dialog ) );

            quitButton.requestFocus();
            dialog.setVisible( true );
        }
    }

    private static class QuitListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            System.exit( 1 );
        }
    }

    private static class ContinueListener implements ActionListener
    {
        private final JDialog dialog;

        public ContinueListener( JDialog dialog )
        {
            this.dialog = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            dialog.dispose();
        }
    }
}
