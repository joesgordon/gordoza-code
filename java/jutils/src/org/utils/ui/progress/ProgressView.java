package org.utils.ui.progress;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.concurrent.*;
import org.jutils.ui.ExtensiveErrorView;
import org.jutils.ui.MessageExceptionView;
import org.jutils.ui.event.ActionListenerList;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * UI to show intermediate progress to the user.
 ******************************************************************************/
public class ProgressView implements IProgressView, IView<JDialog>
{
    /** The progress dialog. */
    private final JDialog dialog;
    /** The message label in the dialog. */
    private final JLabel msgLabel;
    /** The progress bar in the dialog. */
    private final JProgressBar progressBar;
    /** List of listeners to be called when the user cancels the action. */
    private final ActionListenerList cancelListeners;
    /** List of listeners to be called when completion is signaled. */
    private final ActionListenerList completeListeners;

    /***************************************************************************
     * Creates a new progress dialog.
     * @param parent the Window from which the dialog is displayed or null if
     * this dialog has no owner.
     * @param modalityType specifies whether dialog blocks input to other
     * windows when shown..
     **************************************************************************/
    public ProgressView( Window parent, ModalityType modalityType )
    {
        JPanel contentPanel;
        GridBagConstraints constraints;
        JButton cancelButton;

        dialog = new JDialog( parent, modalityType );
        contentPanel = new JPanel( new GridBagLayout() );
        msgLabel = new JLabel();
        progressBar = new JProgressBar();
        cancelButton = new JButton( "Cancel",
            IconConstants.loader.getIcon( IconConstants.STOP_16 ) );

        cancelListeners = new ActionListenerList();
        completeListeners = new ActionListenerList();

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );

        cancelButton.addActionListener( new CancelListener( cancelListeners ) );

        dialog.setSize( 450, 150 );
        dialog.setLocationRelativeTo( parent );
        dialog.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
        dialog.addWindowListener( new DialogCloseListener( this ) );

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

        dialog.setContentPane( contentPanel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JDialog getView()
    {
        return dialog;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
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
    @Override
    public void setPercentComplete( int percent )
    {
        progressBar.setValue( percent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setMessage( String text )
    {
        msgLabel.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addCancelListener( ActionListener l )
    {
        cancelListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalComplete()
    {
        dialog.dispose();
        completeListeners.fireListeners( this, 0, null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setTitle( String title )
    {
        dialog.setTitle( title );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setVisible( boolean visible )
    {
        dialog.setVisible( visible );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( String title, String message )
    {
        JOptionPane.showMessageDialog( dialog, message, title,
            JOptionPane.ERROR_MESSAGE );
        signalComplete();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalLargeError( String title, String message, String errors )
    {
        ExtensiveErrorView view = new ExtensiveErrorView();

        view.setErrors( message, errors );

        JOptionPane.showMessageDialog( dialog, view.getView(), title,
            JOptionPane.ERROR_MESSAGE );
        signalComplete();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalException( String title, Throwable ex )
    {
        signalException( title, null, ex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalException( String title, String message, Throwable ex )
    {
        MessageExceptionView.showExceptionDialog( dialog, message, title, ex );
        ex.printStackTrace();
        signalComplete();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addCompleteListener( ActionListener l )
    {
        completeListeners.addListener( l );
    }

    /***************************************************************************
     * @param comp
     * @param task
     **************************************************************************/
    public static ProgressView startAndShow( Component comp,
        IProgressTask task, String title )
    {
        Window parent = Utils.getComponentsWindow( comp );
        ProgressView view = new ProgressView( parent,
            ModalityType.APPLICATION_MODAL );
        EdtProgressViewAdapter edtView = new EdtProgressViewAdapter( view );
        TaskStoppable istoppable = new TaskStoppable( edtView, task );
        Stoppable stoppable = new Stoppable( istoppable );

        view.addCancelListener( new DefaultCancelListener( stoppable ) );

        Thread thread = new Thread( stoppable );

        view.setIndeterminate( true );
        view.setTitle( title );
        thread.start();
        view.setVisible( true );

        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IProgressTask
    {
        public void run( ITaskStopManager stopManager, IProgressView progress );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DefaultCancelListener implements ActionListener
    {
        private final Stoppable stoppable;

        public DefaultCancelListener( Stoppable stoppable )
        {
            this.stoppable = stoppable;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // LogUtils.printDebug( "Stopping" );

            stoppable.stop();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DialogCloseListener extends WindowAdapter
    {
        private final ProgressView view;

        public DialogCloseListener( ProgressView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            view.cancelListeners.fireListeners( view, 0, null );
        }
    }
}
