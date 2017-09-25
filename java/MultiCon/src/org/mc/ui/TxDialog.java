package org.mc.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.concurrent.ITaskStopManager;

public class TxDialog
{
    private final JProgressBar progressBar;

    private final JLabel progressLabel;

    private int value;

    private String message;

    private String maxText;

    private final ITaskStopManager stopper;

    /**  */
    private final JDialog dialog;

    public TxDialog( Window parent, ITaskStopManager stopper )
    {
        dialog = new JDialog( parent, "Sending Messages...",
            ModalityType.DOCUMENT_MODAL );

        progressBar = new JProgressBar();
        progressLabel = new JLabel();
        JButton cancelButton = new JButton( "Cancel" );
        JPanel mainPanel = new JPanel();
        maxText = "";
        this.stopper = stopper;

        mainPanel.setLayout( new GridBagLayout() );

        progressBar.setMinimum( 0 );
        cancelButton.setIcon( IconConstants.getIcon( IconConstants.CLOSE_16 ) );
        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                try
                {
                    TxDialog.this.stopper.stopAndWait();
                }
                catch( InterruptedException ex )
                {
                }
                TxDialog.this.dialog.setVisible( false );
            }
        } );

        mainPanel.add( progressLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 4, 4, 4 ), 0, 0 ) );
        mainPanel.add( progressBar,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 10, 4 ), 0, 0 ) );
        mainPanel.add( cancelButton,
            new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 10, 4, 0, 4 ), 0, 10 ) );

        dialog.setContentPane( mainPanel );
        dialog.setResizable( false );
    }

    public JDialog getView()
    {
        return dialog;
    }

    public void setMax( int max )
    {
        maxText = " of " + max + ")";
        buildLabel();
        progressBar.setMaximum( max );
    }

    public void setValue( int value )
    {
        this.value = value;
        buildLabel();
        progressBar.setValue( value );
    }

    public void setMessage( String message )
    {
        this.message = message + ( message.length() > 0 ? " " : "" ) + "(";
        buildLabel();
    }

    private void buildLabel()
    {
        progressLabel.setText( message + value + maxText );
    }
}
