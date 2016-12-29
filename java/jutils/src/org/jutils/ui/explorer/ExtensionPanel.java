package org.jutils.ui.explorer;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.GradientPanel;
import org.jutils.ui.ShadowBorder;
import org.jutils.ui.explorer.data.ExtensionConfig;

public class ExtensionPanel extends JPanel
{
    // -------------------------------------------------------------------------
    // Main panel components.
    // -------------------------------------------------------------------------
    /**  */
    private final DefaultListModel<String> pgmListModel = new DefaultListModel<>();

    /**  */
    private final JList<String> pgmList = new JList<>( pgmListModel );

    /**  */
    private final JScrollPane pgmScrollPane = new JScrollPane( pgmList );

    // -------------------------------------------------------------------------
    // Title panel components.
    // -------------------------------------------------------------------------
    /**  */
    private final GradientPanel extPanel = new GradientPanel();

    /**  */
    private final JLabel extLabel = new JLabel();

    // -------------------------------------------------------------------------
    // Button panel components.
    // -------------------------------------------------------------------------
    /**  */
    private final JPanel buttonPanel = new JPanel();

    /**  */
    private final JButton addButton = new JButton();

    /**  */
    private final JButton removeButton = new JButton();

    /**  */
    private final JButton editButton = new JButton();

    /**  */
    private final JButton defaultButton = new JButton();

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensionPanel()
    {
        // ---------------------------------------------------------------------
        // Setup the extension panel
        // ---------------------------------------------------------------------
        extPanel.setLayout( new GridBagLayout() );

        extLabel.setText( "Programs" );

        extLabel.setForeground( Color.white );

        extPanel.add( extLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        buttonPanel.setLayout( new GridBagLayout() );

        addButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add a new program" );

        removeButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );
        removeButton.setToolTipText( "Remove an existing program" );

        editButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_16 ) );
        editButton.setToolTipText( "Edit an existing program" );

        defaultButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.CHECK_16 ) );
        defaultButton.setToolTipText( "Make program extension default" );

        buttonPanel.add( addButton,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        buttonPanel.add( removeButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        buttonPanel.add( editButton,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        buttonPanel.add( defaultButton,
            new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 8, 2, 2 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        this.setLayout( new GridBagLayout() );
        this.setBorder( new ShadowBorder() );

        pgmScrollPane.setMinimumSize( new Dimension( 100, 100 ) );
        pgmScrollPane.setPreferredSize( new Dimension( 200, 100 ) );

        this.add( extPanel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        this.add( buttonPanel,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( pgmScrollPane,
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 2, 0, 0, 0 ), 0, 0 ) );
    }

    public void setExtension( ExtensionConfig ext )
    {
        extLabel.setText( "Programs for " + ext.ext + " files" );
        List<String> programs = ext.programs;
        pgmListModel.removeAllElements();
        for( int i = 0; i < programs.size(); i++ )
        {
            pgmListModel.addElement( programs.get( i ) );
        }
    }
}
