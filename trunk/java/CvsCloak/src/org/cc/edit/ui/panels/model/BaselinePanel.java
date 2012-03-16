package org.cc.edit.ui.panels.model;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

import org.cc.data.Baseline;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.NullableLockInfoPanel;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BaselinePanel extends InfoPanel<Baseline>
{
    /**  */
    private JTextField nameField;
    /**  */
    private NullableLockInfoPanel lockPanel;
    /**  */
    private ItemActionList<String> nameChangedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public BaselinePanel( UndoManager manager )
    {
        super( new GridBagLayout() );

        nameChangedListeners = new ItemActionList<String>();

        nameField = new JTextField( 10 );
        nameField.getDocument().addDocumentListener( new NameUpdater() );
        lockPanel = new NullableLockInfoPanel();

        add( new JLabel( "Name :" ), new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 4, 0, 4 ), 0, 0 ) );
        add( nameField, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 0, 4 ), 0, 0 ) );

        add( lockPanel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 0, 4 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 7, 1, 1,
            1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    public void addNameChangedListeners( ItemActionListener<String> l )
    {
        nameChangedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void displayData( Baseline data )
    {
        nameField.setText( data.getName() );
        lockPanel.setData( data.getLockInfo() );
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
            nameChangedListeners.fireListeners( BaselinePanel.this, name );
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
