package org.jutils.ui.event.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class ComboBoxUpdater<T> implements ActionListener
{
    private final IUpdater<T> updater;
    private final JComboBox<T> comboBox;

    public ComboBoxUpdater( JComboBox<T> comboBox, IUpdater<T> dataUpdater )
    {
        this.comboBox = comboBox;
        this.updater = dataUpdater;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        @SuppressWarnings( "unchecked")
        T selectedObject = ( T )comboBox.getSelectedItem();
        updater.update( selectedObject );
    }
}
