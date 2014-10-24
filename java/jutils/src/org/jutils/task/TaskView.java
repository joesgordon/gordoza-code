package org.jutils.task;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.ui.event.ActionListenerList;

public class TaskView implements ITaskView
{
    /**  */
    private final JPanel view;

    /** The message field. */
    private final JTextArea messageField;
    /** The progress bar. */
    private final JProgressBar progressBar;
    /** The message field. */
    private final JButton cancelButton;

    /** List of listeners to be called when the user cancels the action. */
    private final ActionListenerList cancelListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public TaskView()
    {
        this.messageField = new JTextArea();
        this.progressBar = new JProgressBar();
        this.cancelListeners = new ActionListenerList();
        this.cancelButton = new JButton();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        messageField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        messageField.setEditable( false );
        messageField.setLineWrap( true );
        messageField.setWrapStyleWord( true );

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );

        cancelButton.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        cancelButton.setIcon( IconConstants.loader.getIcon( IconConstants.STOP_16 ) );
        cancelButton.addActionListener( new CancelListener( this ) );
        cancelButton.setOpaque( false );

        constraints = new GridBagConstraints( 0, 0, 2, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 4, 4 ), 0, 0 );
        contentPanel.add( messageField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 2, 4 ), 0, 0 );
        contentPanel.add( progressBar, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 2, 4 ), 0, 0 );
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
    @Override
    public void signalMessage( String message )
    {
        messageField.setText( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalPercentComplete( int percent )
    {
        if( percent < 0 )
        {
            progressBar.setIndeterminate( true );
            progressBar.setString( "" );
        }
        else
        {
            progressBar.setIndeterminate( false );
            progressBar.setValue( percent );
            progressBar.setString( percent + "%" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        JOptionPane.showMessageDialog( this.getView(), error.message,
            error.name, JOptionPane.ERROR_MESSAGE );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addCancelListener( ActionListener l )
    {
        cancelListeners.addListener( l );
    }

    /***************************************************************************
     * Listener to be added to the cancel button that, in turn, calls the cancel
     * listeners added to this view.
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private final TaskView view;

        public CancelListener( TaskView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.cancelListeners.fireListeners( this, 0, null );
        }
    }

    /***************************************************************************
     * @param background
     **************************************************************************/
    public void setBackground( Color background )
    {
        view.setBackground( background );
        cancelButton.setBackground( background );
    }

    /***************************************************************************
     * @param fg
     **************************************************************************/
    public void setForeground( Color fg )
    {
        messageField.setForeground( fg );
        cancelButton.setForeground( fg );
    }

    /***************************************************************************
     * @param view
     * @return
     **************************************************************************/
    public static ITaskView createEdtView( ITaskView view )
    {
        return new EdtTv( view );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EdtTv implements ITaskView
    {
        private final ITaskView view;

        private int lastPercent = -1;

        public EdtTv( ITaskView view )
        {
            this.view = view;
        }

        @Override
        public void signalPercentComplete( final int percent )
        {
            if( percent == lastPercent )
            {
                return;
            }

            lastPercent = percent;

            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.signalPercentComplete( percent );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void signalMessage( final String message )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.signalMessage( message );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void signalError( final TaskError error )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.signalError( error );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public Component getView()
        {
            return view.getView();
        }

        @Override
        public void addCancelListener( ActionListener listener )
        {
            view.addCancelListener( listener );
        }
    }
}
