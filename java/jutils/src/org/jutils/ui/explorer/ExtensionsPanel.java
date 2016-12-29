package org.jutils.ui.explorer;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.IconConstants;
import org.jutils.ui.*;
import org.jutils.ui.explorer.data.ExtensionConfig;
import org.jutils.ui.explorer.data.FileAppConfiguration;

public class ExtensionsPanel extends JPanel
{
    private final AltSplitPane splitPane = new AltSplitPane();

    private final JPanel leftPanel = new JPanel();

    private final ExtensionPanel rightPanel = new ExtensionPanel();

    private final JPanel blankPanel = new JPanel();

    // -------------------------------------------------------------------------
    // Left Panel Components
    // -------------------------------------------------------------------------
    /**  */
    private final JButton addButton = new JButton();

    private final JButton removeButton = new JButton();

    private final DefaultListModel<ExtensionConfig> extListModel = new DefaultListModel<ExtensionConfig>();

    private final JList<ExtensionConfig> extList = new JList<ExtensionConfig>(
        extListModel );

    private final JScrollPane extScrollPane = new JScrollPane( extList );

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensionsPanel()
    {
        // ---------------------------------------------------------------------
        // Setup the extension panel
        // ---------------------------------------------------------------------
        GradientPanel titlePanel = new GradientPanel();
        JLabel titleLabel = new JLabel();
        titlePanel.setLayout( new GridBagLayout() );

        titleLabel.setText( "Extensions" );
        titleLabel.setForeground( Color.white );

        titlePanel.add( titleLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup the left panel.
        // ---------------------------------------------------------------------
        leftPanel.setLayout( new GridBagLayout() );
        leftPanel.setBorder( new ShadowBorder() );

        addButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add a new extension" );

        removeButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );
        removeButton.setToolTipText( "Remove an existing extension" );

        extList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        extList.addListSelectionListener( new ListSelectionListener()
        {

            @Override
            public void valueChanged( ListSelectionEvent e )
            {
                if( !e.getValueIsAdjusting() )
                {
                    int idx = extList.getSelectedIndex();
                    if( idx > -1 )
                    {
                        if( splitPane.getRightComponent() == blankPanel )
                        {
                            splitPane.setRightComponent( rightPanel );
                        }
                        extensionSelected( idx );
                    }
                    else if( splitPane.getRightComponent() == rightPanel )
                    {
                        splitPane.setRightComponent( blankPanel );
                    }
                }
            }
        } );

        extScrollPane.setMinimumSize( new Dimension( 200, 100 ) );
        extScrollPane.setPreferredSize( new Dimension( 200, 100 ) );
        // extScrollPane.setBorder( BorderFactory.createEmptyBorder() );

        leftPanel.add( titlePanel,
            new GridBagConstraints( 0, 0, 3, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        leftPanel.add( addButton,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 2 ), 0, 0 ) );

        leftPanel.add( removeButton,
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 2, 4, 4 ), 0, 0 ) );

        leftPanel.add( extScrollPane,
            new GridBagConstraints( 0, 2, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 0, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup this panel.
        // ---------------------------------------------------------------------
        this.setLayout( new GridBagLayout() );

        splitPane.setBorderless( true );
        splitPane.updateUI();
        splitPane.setLeftComponent( leftPanel );
        splitPane.setRightComponent( blankPanel );

        this.add( splitPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 10, 10, 10, 10 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param idx
     **************************************************************************/
    private void extensionSelected( int idx )
    {
        Object element = extListModel.elementAt( idx );

        rightPanel.setExtension( ( ExtensionConfig )element );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( FileAppConfiguration data )
    {
        java.util.List<ExtensionConfig> extList = data.exts;
        extListModel.removeAllElements();
        for( int i = 0; i < extList.size(); i++ )
        {
            extListModel.addElement( extList.get( i ) );
        }
    }
}
