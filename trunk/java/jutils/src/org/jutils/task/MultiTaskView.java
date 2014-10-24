package org.jutils.task;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.concurrent.TaskStopManager;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskView implements IView<JPanel>, IMultiTaskView
{
    /**  */
    private final JPanel view;

    /** The title field. */
    private final JLabel titleField;
    /** The progress bar. */
    private final JProgressBar progressBar;

    /**  */
    private final ViewList progressList;

    /**  */
    private final TaskStopManager stopManager;

    /***************************************************************************
     * 
     **************************************************************************/
    public MultiTaskView()
    {
        this.titleField = new JLabel();
        this.progressBar = new JProgressBar();
        this.progressList = new ViewList();

        this.view = createView();

        this.stopManager = new TaskStopManager();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        progressBar.setStringPainted( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 2, 4 ), 0, 0 );
        panel.add( titleField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 2, 4 ), 0, 0 );
        panel.add( progressBar, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets( 4, 4,
                2, 4 ), 0, 0 );
        panel.add( progressList.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ITaskView addTask( String message )
    {
        TaskView statusView = new TaskView( true );

        statusView.signalMessage( message );
        statusView.signalPercentComplete( -1 );

        progressList.addView( statusView );

        statusView.addCancelListener( new TaskCancelledListener( this,
            statusView ) );

        return statusView;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeTask( ITaskView view )
    {
        progressList.removeView( view );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setTitle( String title )
    {
        titleField.setText( title );
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
     * 
     **************************************************************************/
    @Override
    public void setPercent( int percent )
    {
        if( percent > -1 )
        {
            progressBar.setIndeterminate( false );
            progressBar.setValue( percent );
            progressBar.setString( percent + "%" );
        }
        else
        {
            progressBar.setIndeterminate( true );
            progressBar.setString( "" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return stopManager.continueProcessing();
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
     * @return
     **************************************************************************/
    public static IMultiTaskView createEdtView()
    {
        return new EdtMpv( new MultiTaskView() );
    }

    public static void startAndShow( Component comp, ITasker tasker,
        String title, int numThreads )
    {
        Window parent = Utils.getComponentsWindow( comp );
        MultiTaskView view = new MultiTaskView();
        JDialog dialog = new JDialog( parent, ModalityType.DOCUMENT_MODAL );

        dialog.setTitle( title );
        dialog.setContentPane( view.getView() );
        dialog.pack();
        dialog.setSize( 400, 600 );
        dialog.setLocationRelativeTo( parent );

        MultiTaskRunner runner = new MultiTaskRunner( tasker, view, numThreads );

        Thread thread = new Thread( runner, title );

        thread.start();

        dialog.setVisible( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EdtMpv implements IMultiTaskView
    {
        private IMultiTaskView view;

        private int lastPercent = -1;

        public EdtMpv( IMultiTaskView view )
        {
            this.view = view;
        }

        @Override
        public ITaskView addTask( final String message )
        {
            EdtAddTask r = new EdtAddTask( view, message );

            try
            {
                SwingUtilities.invokeAndWait( r );
            }
            catch( InvocationTargetException e )
            {
            }
            catch( InterruptedException e )
            {
            }

            return r.taskView;
        }

        @Override
        public void setTitle( final String title )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.setTitle( title );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void setPercent( final int percent )
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
                    view.setPercent( percent );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public void removeTask( final ITaskView itv )
        {
            Runnable r = new Runnable()
            {
                @Override
                public void run()
                {
                    view.removeTask( itv );
                }
            };
            SwingUtilities.invokeLater( r );
        }

        @Override
        public boolean canContinue()
        {
            return view.canContinue();
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EdtAddTask implements Runnable
    {
        private final IMultiTaskView view;
        private final String message;

        public ITaskView taskView = null;

        public EdtAddTask( IMultiTaskView view, String message )
        {
            this.view = view;
            this.message = message;
        }

        @Override
        public void run()
        {
            taskView = TaskView.createEdtView( view.addTask( message ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TaskCancelledListener implements ActionListener
    {
        private final MultiTaskView view;
        private final ITaskView taskView;

        public TaskCancelledListener( MultiTaskView view, ITaskView taskView )
        {
            this.view = view;
            this.taskView = taskView;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.removeTask( taskView );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MultiProgressViewApp implements IApplication
    {
        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            MultiTaskView view = new MultiTaskView();

            String message;
            ITaskView taskView;

            message = "Set 7 of 120: 24 Parameters" + Utils.NEW_LINE;
            message += "A01, A02, A03, A04, A05, A06, A07, A08, A09, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24";
            taskView = view.addTask( message );
            taskView.signalPercentComplete( 34 );

            message = "Set 6 of 120: 24 Parameters" + Utils.NEW_LINE;
            message += "GPS01, GPS02, GPS03, GPS04, GPS05, GPS06, GPS07, GPS08, GPS09, GPS10, GPS11, GPS12, GPS13, GPS14, GPS15, GPS16, GPS17, GPS18, GPS19, GPS20, GPS21";
            taskView = view.addTask( message );

            view.setTitle( "Sets 5 of 120 completed" );
            view.setPercent( 500 / 120 );

            OkDialogView okView = new OkDialogView( null, view.getView(),
                ModalityType.APPLICATION_MODAL, OkDialogButtons.OK_CANCEL );
            JDialog dialog = okView.getView();

            dialog.setTitle( "Decoding 2849 parameters" );
            dialog.setSize( 400, 400 );
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new MultiProgressViewApp() );
    }
}
