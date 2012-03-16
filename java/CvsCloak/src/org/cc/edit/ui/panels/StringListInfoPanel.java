package org.cc.edit.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringListInfoPanel<T> extends ListInfoPanel<T>
{
    private ItemActionList<String> addActions;
    private String question;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringListInfoPanel( String question )
    {
        super();

        this.addActions = new ItemActionList<String>();
        this.question = question;

        super.addAddItemListener( new AddItemListener() );
    }

    public final void addAddItemListener( ItemActionListener<String> l )
    {
        addActions.addListener( l );
    }

    public final void removeAddItemListener( ItemActionListener<String> l )
    {
        addActions.removeListener( l );
    }

    private class AddItemListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            String newItem = JOptionPane.showInputDialog(
                StringListInfoPanel.this, question );

            if( newItem != null )
            {
                addActions.fireListeners( StringListInfoPanel.this, newItem );
            }
        }
    }
}
