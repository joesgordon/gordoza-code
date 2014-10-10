package org.jutils.ui.progress;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiProgressView implements IView<JPanel>
{
    /**  */
    private final JPanel view;

    /** The title field. */
    private final JLabel titleField;
    /** The progress bar. */
    private final JProgressBar progressBar;
    /**  */
    private final DefaultListModel<TaskStatus> listModel;
    /**  */
    private final JList<TaskStatus> progressList;

    /***************************************************************************
     * 
     **************************************************************************/
    public MultiProgressView()
    {
        this.listModel = new DefaultListModel<>();
        this.titleField = new JLabel();
        this.progressBar = new JProgressBar();
        this.progressList = new JList<>( listModel );
        this.view = createView();

        progressBar.setStringPainted( true );
        progressList.setCellRenderer( new TaskCellRenderer() );
    }
    
    public void addStatus( TaskStatus status )
    {
        ;
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        JScrollPane scrollpane = new JScrollPane( progressList );

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
        panel.add( scrollpane, constraints );

        return panel;
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
    private static class TaskCellRenderer implements
        ListCellRenderer<TaskStatus>
    {
        private final SubtaskView view;

        public TaskCellRenderer()
        {
            this.view = new SubtaskView();
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends TaskStatus> list, TaskStatus value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            Color bg = isSelected ? list.getSelectionBackground()
                : list.getBackground();
            Color fg = isSelected ? list.getSelectionForeground()
                : list.getForeground();

            view.setBackground( bg );
            view.setForeground( fg );
            view.setData( value );

            return view.getView();
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
            MultiProgressView view = new MultiProgressView();
            OkDialogView odv = new OkDialogView( null, view.getView(),
                OkDialogButtons.OK_CANCEL );

            TaskStatus status;

            status = new TaskStatus();
            status.title = "Stuff!";
            status.message = "kine";
            status.percent = 34;
            view.listModel.addElement( status );

            status = new TaskStatus();
            status.title = "Stuff2!";
            status.message = "blah";
            status.percent = 58;
            view.listModel.addElement( status );

            view.titleField.setText( "Decoding 2849 parameters" );
            view.progressBar.setValue( 33 );
            view.progressBar.setString( "33%" );

            JDialog dialog = odv.getView();

            dialog.setSize( 400, 400 );
            dialog.validate();
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
        }
    }

    public static void main( String [] args )
    {
        AppRunner.invokeLater( new MultiProgressViewApp() );
    }
}
