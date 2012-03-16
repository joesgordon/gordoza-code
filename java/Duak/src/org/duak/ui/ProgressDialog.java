package org.duak.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.duak.model.IProgressReporter;

public class ProgressDialog extends JDialog
{
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private IProgressReporter reporter;
    private JButton cancelButton;

    public ProgressDialog( Frame parent, String title )
    {
        super( parent, title, true );

        setContentPane( createContentPanel() );
        reporter = new DialogProgressReporter();

        setSize( 400, 150 );
        validate();
    }

    private JPanel createContentPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        cancelButton = new JButton( "Cancel" );
        statusLabel = new JLabel();
        progressBar = new JProgressBar();
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );
        progressBar.setStringPainted( true );

        // cancelButton.

        panel.add( statusLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 2, 4 ), 0, 0 ) );

        panel.add( progressBar, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                2, 4, 4, 4 ), 0, 0 ) );

        panel.add( cancelButton, new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                4, 0, 4 ), 30, 15 ) );

        return panel;
    }

    public void addCancelListener( ActionListener l )
    {
        cancelButton.addActionListener( l );
    }

    public IProgressReporter getProgressReporter()
    {
        return reporter;
    }

    private class DialogProgressReporter implements IProgressReporter
    {
        @Override
        public void setProgressIndeterminate()
        {
            progressBar.setIndeterminate( true );
        }

        @Override
        public void setprogress( double percent )
        {
            if( progressBar.isIndeterminate() )
            {
                progressBar.setIndeterminate( false );
            }
            progressBar.setValue( ( int )( percent * 100.0 ) );
        }

        @Override
        public void setStatus( String status )
        {
            statusLabel.setText( status );
        }
    }
}
