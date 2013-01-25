package org.cojo.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.cojo.model.IChangeRequest;
import org.jutils.IconConstants;

public class NotesPanel extends JPanel
{
    public NotesPanel()
    {
        super( new GridBagLayout() );

        JPanel notesPanel = createNotesPanel();
        JPanel attachPanel = createAttachPanel();

        add( notesPanel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 4,
                2, 4 ), 0, 0 ) );

        add( attachPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 2, 4,
                4, 4 ), 0, 0 ) );
    }

    public void setData( IChangeRequest changeRequest )
    {
        // TODO Auto-generated method stub

    }

    private JPanel createNotesPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JButton addButton = new JButton();
        JButton editButton = new JButton();
        JButton deleteButton = new JButton();
        JList notesList = new JList();
        JScrollPane notesScrollPane = new JScrollPane( notesList );

        addButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        editButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_16 ) );
        deleteButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );

        panel.setBorder( new TitledBorder( "Notes" ) );

        panel.add( addButton, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );
        panel.add( editButton, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );
        panel.add( deleteButton, new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );

        panel.add( notesScrollPane, new GridBagConstraints( 0, 1, 4, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                4, 0, 2, 2 ), 0, 0 ) );

        return panel;
    }

    private JPanel createAttachPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JButton addButton = new JButton();
        JButton deleteButton = new JButton();
        JList notesList = new JList();
        JScrollPane notesScrollPane = new JScrollPane( notesList );

        addButton.setIcon( IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        deleteButton.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );

        panel.setBorder( new TitledBorder( "Attachments" ) );

        panel.add( addButton, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );
        panel.add( deleteButton, new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 0,
                2, 2 ), 0, 0 ) );

        panel.add( notesScrollPane, new GridBagConstraints( 0, 1, 4, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                4, 0, 2, 2 ), 0, 0 ) );

        return panel;
    }
}
