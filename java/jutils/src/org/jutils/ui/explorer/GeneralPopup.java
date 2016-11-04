package org.jutils.ui.explorer;

import java.awt.Component;

import javax.swing.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class GeneralPopup extends JPopupMenu
{
    /**  */
    private ExplorerTable table = null;

    /**  */
    private JMenuItem copyItem = new JMenuItem();

    /**  */
    private JMenuItem pasteItem = new JMenuItem();

    /**  */
    private JMenu newMenu = new JMenu();

    /**  */
    private JMenuItem newFolderItem = new JMenuItem();

    /**  */
    private JMenuItem newFileItem = new JMenuItem();

    /**
     * @param table
     */
    public GeneralPopup( ExplorerTable table )
    {
        super();
        this.table = table;

        copyItem.setText( "Copy" );
        pasteItem.setText( "Paste" );
        newMenu.setText( "New" );
        newFolderItem.setText( "Folder" );
        newFileItem.setText( "File" );

        // super.show( new JLabel(), 0, 0 );
        super.show( this.table, 0, 0 );
    }

    /**
     * 
     */
    @Override
    public void show( Component comp, int x, int y )
    {
        // File file = table.getSelectedFile();
        super.show( comp, x, y );
    }
}
