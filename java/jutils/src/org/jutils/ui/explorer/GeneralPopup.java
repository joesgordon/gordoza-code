package org.jutils.ui.explorer;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class GeneralPopup implements IView<JPopupMenu>
{
    /**  */
    private final JPopupMenu menu;
    /**  */
    private final JMenuItem copyItem;
    /**  */
    private final JMenuItem pasteItem;
    /**  */
    private final JMenuItem newFolderItem;
    /**  */
    private final JMenuItem newFileItem;

    /**
     * @param table
     */
    public GeneralPopup()
    {
        this.menu = new JPopupMenu();

        copyItem = new JMenuItem( "Copy" );
        pasteItem = new JMenuItem( "Paste" );
        newFolderItem = new JMenuItem( "Folder" );
        newFileItem = new JMenuItem( "File" );

        menu.add( copyItem );
        menu.add( pasteItem );
        menu.addSeparator();
        menu.add( newFolderItem );
        menu.add( newFileItem );
    }

    @Override
    public JPopupMenu getView()
    {
        return menu;
    }
}
