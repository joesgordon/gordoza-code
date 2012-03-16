package org.jutils.ui.event.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComboBoxUpdater implements ActionListener
{
    private IDataUpdater updater;

    public ComboBoxUpdater( IDataUpdater dataUpdater )
    {
        updater = dataUpdater;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        updater.updateData();
    }
}
