package org.jutils.ui.progress;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.ui.event.ActionListenerList;
import org.jutils.ui.model.IDataView;

public class SubtaskView implements IDataView<TaskStatus>
{
    /**  */
    private final JPanel view;

    /** The title field. */
    private final JLabel titleField;
    /** The progress bar. */
    private final JProgressBar progressBar;
    /** The message field. */
    private final JLabel messageField;
    /** The message field. */
    private final JButton cancelButton;

    /** List of listeners to be called when the user cancels the action. */
    private final ActionListenerList cancelListeners;

    /**  */
    private TaskStatus status;

    /***************************************************************************
     * 
     **************************************************************************/
    public SubtaskView()
    {
        this.titleField = new JLabel();
        this.messageField = new JLabel();
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

        progressBar.setStringPainted( true );
        progressBar.setString( "" );
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );

        cancelButton.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        cancelButton.setIcon( IconConstants.loader.getIcon( IconConstants.STOP_16 ) );
        cancelButton.addActionListener( new CancelListener( cancelListeners ) );
        cancelButton.setOpaque( false );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 2, 4 ), 0, 0 );
        contentPanel.add( titleField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 2, 4 ), 0, 0 );
        contentPanel.add( progressBar, constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 2, 4 ), 0, 0 );
        contentPanel.add( cancelButton, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 4, 4 ), 0, 0 );
        contentPanel.add( messageField, constraints );

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
    public TaskStatus getData()
    {
        return status;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( TaskStatus data )
    {
        this.status = data;

        titleField.setText( data.title );
        if( data.percent < 0 )
        {
            progressBar.setIndeterminate( true );
        }
        else
        {
            progressBar.setIndeterminate( false );
            progressBar.setValue( data.percent );
            progressBar.setString( data.percent + "%" );
        }
        messageField.setText( data.message );
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

    // private static class SubtaskViewApp implements IApplication
    // {
    // @Override
    // public String getLookAndFeelName()
    // {
    // return null;
    // }
    //
    // @Override
    // public void createAndShowUi()
    // {
    // SubtaskView view = new SubtaskView();
    // OkDialogView odv = new OkDialogView( null, view.getView(),
    // OkDialogButtons.OK_CANCEL );
    //
    // TaskStatus status = new TaskStatus();
    //
    // status.title = "Stuff!";
    // status.message = "kine";
    // status.percent = 34;
    //
    // view.setData( status );
    //
    // JDialog dialog = odv.getView();
    //
    // dialog.setSize( 400, 200 );
    // dialog.validate();
    // dialog.setLocationRelativeTo( null );
    // dialog.setVisible( true );
    // }
    // }
    //
    // public static void main( String [] args )
    // {
    // AppRunner.invokeLater( new SubtaskViewApp() );
    // }

    public void setBackground( Color background )
    {
        view.setBackground( background );
        cancelButton.setBackground( background );
    }

    public void setForeground( Color fg )
    {
        titleField.setForeground( fg );
        messageField.setForeground( fg );
        cancelButton.setForeground( fg );
    }
}
