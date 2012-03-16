package org.tuvok;

import java.awt.Menu;
import java.awt.MenuItem;

/*******************************************************************************
 *
 ******************************************************************************/
public class TaskMenu extends Menu
{
    /**  */
    private MenuItem addUpdateMenuItem = new MenuItem();
    /**  */
    private MenuItem addNoteMenuItem = new MenuItem();
    /**  */
    private MenuItem editTaskMenuItem = new MenuItem();

    /***************************************************************************
     *
     **************************************************************************/
    public TaskMenu()
    {
        addUpdateMenuItem.setLabel( "Add Update" );
        addNoteMenuItem.setLabel( "Add Note" );
        editTaskMenuItem.setLabel( "Edit" );

        this.add( addUpdateMenuItem );
        this.add( addNoteMenuItem );
        this.add( editTaskMenuItem );
    }
}
