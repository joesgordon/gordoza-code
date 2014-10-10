package org.jutils.ui.progress;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.event.ActionListenerList;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * UI to show intermediate progress to the user.
 ******************************************************************************/
public class ProgressView implements IView<JPanel>
{
    /** The progress view. */
    private final JPanel view;
    /** The message label in the dialog. */
    private final JLabel msgLabel;
    /** The progress bar in the dialog. */
    private final JProgressBar progressBar;
    /** List of listeners to be called when the user cancels the action. */
    private final ActionListenerList cancelListeners;

    /***************************************************************************
     * Creates a new progress dialog.
     * @param parent the Window from which the dialog is displayed or null if
     * this dialog has no owner.
     * @param modalityType specifies whether dialog blocks input to other
     * windows when shown..
     **************************************************************************/
    public ProgressView()
    {
        this.msgLabel = new JLabel();
        this.progressBar = new JProgressBar();

        this.cancelListeners = new ActionListenerList();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton cancelButton;

        cancelButton = new JButton( "Cancel",
            IconConstants.loader.getIcon( IconConstants.STOP_16 ) );

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );

        cancelButton.addActionListener( new CancelListener( cancelListeners ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 4, 4 ), 0, 0 );
        contentPanel.add( msgLabel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        contentPanel.add( progressBar, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 10,
                4, 4, 4 ), 20, 20 );
        contentPanel.add( cancelButton, constraints );

        return contentPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setIndeterminate( boolean b )
    {
        if( progressBar.isIndeterminate() != b )
        {
            progressBar.setIndeterminate( b );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setPercentComplete( int percent )
    {
        progressBar.setValue( percent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setMessage( String text )
    {
        msgLabel.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addCancelListener( ActionListener l )
    {
        cancelListeners.addListener( l );
    }

    /***************************************************************************
     * Action listener to be added to the cancel button that, in turn, calls the
     * cancel listeners added to this view.
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private final ActionListenerList cancelListeners;

        public CancelListener( ActionListenerList cancelListeners )
        {
            this.cancelListeners = cancelListeners;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            cancelListeners.fireListeners( this, 0, null );
        }
    }
}
