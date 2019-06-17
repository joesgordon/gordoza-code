package org.cojo.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.cojo.data.Finding;
import org.cojo.data.Project;
import org.cojo.ui.tableModels.FindingTableModel;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.ItemsTableModel;

/*******************************************************************************
 *
 ******************************************************************************/
public class FindingsPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final FindingTableModel tableConfig;
    /**  */
    private final ItemsTableModel<Finding> findingTableModel;
    /**  */
    private final JTable findingTable;

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public FindingsPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        JButton addButton = new JButton();
        JButton editButton = new JButton();
        JButton deleteButton = new JButton();

        tableConfig = new FindingTableModel();
        findingTableModel = new ItemsTableModel<>( tableConfig );
        findingTable = new JTable( findingTableModel );
        JScrollPane findingScrollPane = new JScrollPane( findingTable );

        addButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        editButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_16 ) );
        deleteButton.setIcon(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );

        findingTable.addMouseListener( new MouseAdapter()
        {
            @Override
            public void mouseClicked( MouseEvent e )
            {
                if( e.getClickCount() == 2 )
                {
                    showFindingDialog();
                }
            }
        } );

        view.add( addButton,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );
        view.add( editButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );
        view.add( deleteButton,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );

        view.add( findingScrollPane,
            new GridBagConstraints( 0, 1, 4, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param findings
     **************************************************************************/
    public void setData( List<Finding> findings )
    {
        findingTableModel.setItems( findings );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showFindingDialog()
    {
        Frame frame = SwingUtils.getComponentsFrame( view );
        FindingPanel findingPanel = new FindingPanel();
        OkDialogView dialog = new OkDialogView( frame, findingPanel.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        int row = findingTable.getSelectedRow();
        row = findingTable.convertRowIndexToModel( row );
        if( row > -1 )
        {
            Finding finding = findingTableModel.getItem( row );

            findingPanel.setProject( project );
            findingPanel.setData( finding );

            dialog.show( 400, 400 );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param project
     **************************************************************************/
    public void setProject( Project project )
    {
        this.project = project;

        tableConfig.setProject( project );
    }
}
