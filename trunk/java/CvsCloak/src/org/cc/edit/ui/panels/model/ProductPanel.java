package org.cc.edit.ui.panels.model;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

import org.cc.creators.ReleaseCreator;
import org.cc.creators.StringCreator;
import org.cc.data.Product;
import org.cc.data.Release;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.NamedItemsListPanel;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProductPanel extends InfoPanel<Product>
{
    /**  */
    private JTextField nameField;
    /**  */
    private NamedItemsListPanel<String> modulesPanel;
    /**  */
    private NamedItemsListPanel<Release> releasesPanel;
    /**  */
    private ItemActionList<String> nameChangedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public ProductPanel( UndoManager manager )
    {
        super( new GridBagLayout() );

        nameChangedListeners = new ItemActionList<String>();

        nameField = new JTextField( 10 );
        nameField.getDocument().addDocumentListener( new NameUpdater() );

        modulesPanel = new NamedItemsListPanel<String>( manager,
            new StringCreator(), "Enter module:" );
        modulesPanel.setBorder( new TitledBorder( "Modules" ) );

        releasesPanel = new NamedItemsListPanel<Release>( manager,
            new ReleaseCreator(), "Enter release name:" );
        releasesPanel.setBorder( new TitledBorder( "Releases" ) );

        add( new JLabel( "Name :" ), new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 4, 0, 4 ), 0, 0 ) );
        add( nameField, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 0, 4 ), 0, 0 ) );

        add( modulesPanel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 4, 4 ), 0, 0 ) );

        add( releasesPanel, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 4, 4 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 7, 1, 1,
            1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /**
     * @param l
     */
    public void addNameChangedListener( ItemActionListener<String> l )
    {
        nameChangedListeners.addListener( l );
    }

    public void removeNameChangedListener( ItemActionListener<String> l )
    {
        nameChangedListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void displayData( Product data )
    {
        nameField.setText( data.getName() );
        modulesPanel.setData( data.getModules() );
        releasesPanel.setData( data.getReleases() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NameUpdater implements DocumentListener
    {
        private void updateData()
        {
            String name = nameField.getText();
            getData().setName( name );
            nameChangedListeners.fireListeners( ProductPanel.this, name );
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
