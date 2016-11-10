package org.tuvok.ui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class TaskMenu implements IView<JMenu>
{
    /**  */
    private final JMenu menu;

    /***************************************************************************
     *
     **************************************************************************/
    public TaskMenu()
    {
        this.menu = new JMenu();

        JMenuItem addUpdateMenuItem = new JMenuItem();
        JMenuItem addNoteMenuItem = new JMenuItem();
        JMenuItem editTaskMenuItem = new JMenuItem();

        addUpdateMenuItem.setText( "Add Update" );
        addNoteMenuItem.setText( "Add Note" );
        editTaskMenuItem.setText( "Edit" );

        menu.add( addUpdateMenuItem );
        menu.add( addNoteMenuItem );
        menu.add( editTaskMenuItem );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JMenu getView()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
