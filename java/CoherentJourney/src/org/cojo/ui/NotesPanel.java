package org.cojo.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.cojo.data.Project;
import org.cojo.data.Task;
import org.jutils.IconConstants;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NotesPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public NotesPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        JPanel notesPanel = createNotesPanel();
        JPanel attachPanel = createAttachPanel();

        view.add( notesPanel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 4, 2, 4 ), 0, 0 ) );

        view.add( attachPanel,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param changeRequest
     **************************************************************************/
    public void setData( Task task )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JPanel createNotesPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JButton addButton = new JButton();
        JButton editButton = new JButton();
        JButton deleteButton = new JButton();
        JList<String> notesList = new JList<String>();
        JScrollPane notesScrollPane = new JScrollPane( notesList );

        addButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        editButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_16 ) );
        deleteButton.setIcon(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );

        panel.setBorder( new TitledBorder( "Notes" ) );

        panel.add( addButton,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );
        panel.add( editButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );
        panel.add( deleteButton,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );

        panel.add( notesScrollPane,
            new GridBagConstraints( 0, 1, 4, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JPanel createAttachPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JButton addButton = new JButton();
        JButton deleteButton = new JButton();
        JList<String> notesList = new JList<String>();
        JScrollPane notesScrollPane = new JScrollPane( notesList );

        addButton.setIcon(
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        deleteButton.setIcon(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );

        panel.setBorder( new TitledBorder( "Attachments" ) );

        panel.add( addButton,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );
        panel.add( deleteButton,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );

        panel.add( notesScrollPane,
            new GridBagConstraints( 0, 1, 4, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 0, 2, 2 ), 0, 0 ) );

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
     * @param project
     **************************************************************************/
    public void setProject( Project project )
    {
        // TODO Auto-generated method stub
    }
}
