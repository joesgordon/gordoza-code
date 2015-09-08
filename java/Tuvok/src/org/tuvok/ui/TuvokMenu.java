package org.tuvok.ui;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jutils.ui.ExitListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class TuvokMenu extends PopupMenu
{
    /**  */
    private MenuItem openMenuItem = new MenuItem();
    /**  */
    private MenuItem loadTaskMenuItem = new MenuItem();
    /**  */
    private MenuItem exitMenuItem = new MenuItem();

    /**  */
    private TuvokFrameView frame = null;

    /***************************************************************************
     * @param frame ToDLsFrame
     **************************************************************************/
    public TuvokMenu( TuvokFrameView frame )
    {
        this.frame = frame;

        openMenuItem.setLabel( "Open" );
        openMenuItem.addActionListener(
            new ToDLsMenu_openMenuItem_actionAdapter() );

        loadTaskMenuItem.setLabel( "Load Task" );

        exitMenuItem.setLabel( "Quit" );
        exitMenuItem.addActionListener( new ExitListener( frame.getView() ) );

        this.add( openMenuItem );
        this.add( loadTaskMenuItem );

        // ---------------------------------------------------------------------

        this.addSeparator();
        this.add( exitMenuItem );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ToDLsMenu_openMenuItem_actionAdapter implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            frame.getView().setVisible( true );
        }
    }
}
