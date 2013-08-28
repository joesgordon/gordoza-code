package org.cc.edit.ui.panels.model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

import org.cc.creators.ProductCreator;
import org.cc.data.*;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.*;
import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.ui.DirectoryChooser;
import org.jutils.ui.event.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RepositoryPanel extends InfoPanel<Repository>
{
    /**  */
    private JTextField locationField;
    /**  */
    private NullableLockInfoPanel lockPanel;
    /**  */
    private JComboBox<String> trunkNameField;
    /**  */
    private StringListInfoPanel<Baseline> baselinesPanel;
    /**  */
    private NamedItemsListPanel<Product> productsPanel;
    /**  */
    private ItemActionList<File> locationChangedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public RepositoryPanel( UndoManager manager )
    {
        super( new GridBagLayout() );

        locationChangedListeners = new ItemActionList<File>();

        lockPanel = new NullableLockInfoPanel();
        trunkNameField = new JComboBox<String>();
        baselinesPanel = new StringListInfoPanel<Baseline>( "Baseline name?" );
        productsPanel = new NamedItemsListPanel<Product>( manager,
            new ProductCreator(), "Product name?" );

        lockPanel.addLockSelectedListener( new LockSelectedListener() );

        baselinesPanel.setBorder( new TitledBorder( "Baselines" ) );
        baselinesPanel.addAddItemListener( new BaselineAddedListener() );

        productsPanel.setBorder( new TitledBorder( "Products" ) );

        add( createLocationPanel(), new GridBagConstraints( 0, 1, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 0, 4 ), 0, 0 ) );

        add( lockPanel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 0, 4 ), 0, 0 ) );

        add( new JLabel( "Trunk Name :" ), new GridBagConstraints( 0, 3, 3, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 0, 4 ), 0, 0 ) );
        add( trunkNameField, new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 4, 4 ), 0, 0 ) );

        add( baselinesPanel, new GridBagConstraints( 0, 5, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 4,
                4, 4 ), 0, 0 ) );

        add( productsPanel, new GridBagConstraints( 0, 6, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 4,
                4, 4 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 7, 1, 1,
            1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    public void addLocationChangedListener( ItemActionListener<File> l )
    {
        locationChangedListeners.addListener( l );
    }

    public void removeLocationChangedListener( ItemActionListener<File> l )
    {
        locationChangedListeners.removeListener( l );
    }

    public void addProductAddedListener( ItemActionListener<Product> l )
    {
        productsPanel.addItemAddedListener( l );
    }

    public void removeProductAddedListener( ItemActionListener<Product> l )
    {
        productsPanel.removeItemAddedListener( l );
    }

    private JPanel createLocationPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        JLabel locationLabel = new JLabel( "Location :" );
        locationField = new JTextField( 10 );
        JButton locationButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );

        locationButton.setToolTipText( "Browse" );
        locationButton.addActionListener( new LocationButtonActionListener() );

        locationField.getDocument().addDocumentListener( new LocationUpdater() );

        panel.add( locationLabel, new GridBagConstraints( 0, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 0,
                0, 0 ), 0, 0 ) );
        panel.add( locationField, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 0, 0, 4 ), 0, 0 ) );
        panel.add( locationButton, new GridBagConstraints( 1, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 0, 0 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void displayData( Repository data )
    {
        locationField.setText( data.getLocation().getAbsolutePath() );
        lockPanel.setData( data.getLockInfo() );

        trunkNameField.removeAllItems();
        for( int i = 0; i < data.getBaselines().size(); i++ )
        {
            trunkNameField.addItem( data.getBaselines().get( i ).getName() );
        }

        if( data.getTrunkName() != null )
        {
            trunkNameField.setSelectedItem( data.getTrunkName() );
        }

        baselinesPanel.setData( data.getBaselines() );
        productsPanel.setData( data.getProducts() );
    }

    private class LocationButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Frame frame = Utils.getComponentsFrame( RepositoryPanel.this );
            DirectoryChooser fd = new DirectoryChooser( frame );
            fd.setSelectedPaths( locationField.getText() );
            fd.setVisible( true );

            File[] selectedFiles = fd.getSelected();

            if( selectedFiles != null )
            {
                if( selectedFiles.length == 1 )
                {
                    locationField.setText( selectedFiles[0].getAbsolutePath() );
                }
                else
                {
                    JOptionPane.showMessageDialog( frame,
                        "A repository cannot be in more than one path.",
                        "ERROR", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class LockSelectedListener implements ItemActionListener<LockInfo>
    {
        @Override
        public void actionPerformed( ItemActionEvent<LockInfo> event )
        {
            getData().setLockInfo( event.getItem() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BaselineAddedListener implements ItemActionListener<String>
    {
        @Override
        public void actionPerformed( ItemActionEvent<String> event )
        {
            Baseline baseline = new Baseline();
            baseline.setName( event.getItem() );
            baselinesPanel.addItem( baseline );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class LocationUpdater implements DocumentListener
    {
        private void updateData()
        {
            File file = new File( locationField.getText() );
            getData().setLocation( file );
            locationChangedListeners.fireListeners( RepositoryPanel.this, file );
        }

        @Override
        public void insertUpdate( DocumentEvent e )
        {
            updateData();
        }

        @Override
        public void removeUpdate( DocumentEvent e )
        {
            updateData();
        }

        @Override
        public void changedUpdate( DocumentEvent e )
        {
            updateData();
        }
    }
}
