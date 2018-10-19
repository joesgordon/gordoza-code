package org.taskflow.ui;

import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.ExitListener;
import org.jutils.ui.RecentFilesMenuView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class TaskflowMenu implements IView<JPopupMenu>
{
    /**  */
    private final JPopupMenu menu;
    /**  */
    private final RecentFilesMenuView recentMenu;

    /**  */
    private final TaskflowFrameView frame;

    /***************************************************************************
     * @param frame ToDLsFrame
     **************************************************************************/
    public TaskflowMenu( TaskflowFrameView frame )
    {
        this.menu = new JPopupMenu();
        this.recentMenu = new RecentFilesMenuView();
        this.frame = frame;

        recentMenu.setIcon(
            IconConstants.getIcon( IconConstants.OPEN_FILE_16 ) );
        recentMenu.addSelectedListener( ( f, c ) -> frame.openFile( f ) );

        menu.add( createShowAction() );
        menu.add( frame.createOpenAction() );
        menu.add( recentMenu.getView() );

        // ---------------------------------------------------------------------

        menu.addSeparator();
        menu.add( createExitAction() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createShowAction()
    {
        ActionListener listener = ( e ) -> frame.getView().setVisible( true );
        return new ActionAdapter( listener, "Show", null );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createExitAction()
    {
        ActionListener listener = new ExitListener( frame.getView() );
        Icon icon = IconConstants.getIcon( IconConstants.CLOSE_16 );
        return new ActionAdapter( listener, "Exit", icon );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPopupMenu getView()
    {
        return menu;
    }
}
