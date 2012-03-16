package org.tuvok;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class ToDLsMenu extends PopupMenu
{
    /**  */
    private MenuItem openMenuItem = new MenuItem();
    /**  */
    private MenuItem loadTaskMenuItem = new MenuItem();
    /**  */
    private MenuItem exitMenuItem = new MenuItem();

    /**  */
    private ToDLsFrame frame = null;

    /***************************************************************************
     * @param frame ToDLsFrame
     **************************************************************************/
    public ToDLsMenu( ToDLsFrame frame )
    {
        this.frame = frame;

        openMenuItem.setLabel( "Open" );
        openMenuItem.addActionListener( new ToDLsMenu_openMenuItem_actionAdapter() );

        loadTaskMenuItem.setLabel( "Load Task" );

        exitMenuItem.setLabel( "Quit" );
        exitMenuItem.addActionListener( new ToDLsMenu_exitMenuItem_actionAdapter() );

        this.add( openMenuItem );
        this.add( loadTaskMenuItem );

        // ---------------------------------------------------------------------

        this.addSeparator();
        this.add( exitMenuItem );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ToDLsMenu_openMenuItem_actionAdapter implements
        ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            frame.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ToDLsMenu_exitMenuItem_actionAdapter implements
        ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            frame.doDefaultCloseOperation();
        }
    }
}
