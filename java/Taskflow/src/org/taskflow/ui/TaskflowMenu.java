package org.taskflow.ui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jutils.ui.ExitListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class TaskflowMenu implements IView<JPopupMenu>
{
    /**  */
    private final JPopupMenu menu;
    /**  */
    private final JMenuItem openMenuItem = new JMenuItem();
    /**  */
    private final JMenuItem loadTaskMenuItem = new JMenuItem();
    /**  */
    private final JMenuItem exitMenuItem = new JMenuItem();

    /***************************************************************************
     * @param frame ToDLsFrame
     **************************************************************************/
    public TaskflowMenu( TaskflowFrameView frame )
    {
        this.menu = new JPopupMenu();

        openMenuItem.setText( "Open" );
        openMenuItem.addActionListener(
            ( e ) -> frame.getView().setVisible( true ) );

        loadTaskMenuItem.setText( "Load Task" );

        exitMenuItem.setText( "Quit" );
        exitMenuItem.addActionListener( new ExitListener( frame.getView() ) );

        menu.add( openMenuItem );
        menu.add( loadTaskMenuItem );

        // ---------------------------------------------------------------------

        menu.addSeparator();
        menu.add( exitMenuItem );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPopupMenu getView()
    {
        return menu;
    }
}
