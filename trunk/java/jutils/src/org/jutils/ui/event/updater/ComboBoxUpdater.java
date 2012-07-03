package org.jutils.ui.event.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class ComboBoxUpdater<T> implements ActionListener
{
    private final IDataUpdater<T> updater;
    private final JComboBox comboBox;

    public ComboBoxUpdater( JComboBox comboBox, IDataUpdater<T> dataUpdater )
    {
        this.comboBox = comboBox;
        this.updater = dataUpdater;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        @SuppressWarnings( "unchecked")
        T selectedObject = ( T )comboBox.getSelectedItem();
        updater.updateData( selectedObject );
    }
}
