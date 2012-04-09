package org.jutils.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardUncaughtExceptionHandler implements
    Thread.UncaughtExceptionHandler
{
    /**  */
    private final JFrame frame;

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public StandardUncaughtExceptionHandler( JFrame frame )
    {
        this.frame = frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class QuitListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            System.exit( 1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
