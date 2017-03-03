package org.jutils.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeManager implements TreeSelectionListener
{
    /**  */
    private final CheckTreeSelectionModel selectionModel;

    /**  */
    private final JTree tree;

    /**  */
    private static final int HOTSPOT = new JCheckBox().getPreferredSize().width;

    /***************************************************************************
     * @param tree JTree
     **************************************************************************/
    public CheckTreeManager( JTree tree )
    {
        this.tree = tree;
        selectionModel = new CheckTreeSelectionModel( tree.getModel() );
        tree.setCellRenderer( new CheckTreeCellRenderer( tree.getCellRenderer(),
            selectionModel ) );
        tree.addMouseListener( new CheckTreeManager_mouseAdapter( this ) );
        selectionModel.addTreeSelectionListener( this );
    }

    /***************************************************************************
     * @param path
     * @param selected
     **************************************************************************/
    public void setPathSelected( TreePath path, boolean selected )
    {
        boolean isSelected = selectionModel.isPathSelected( path, true );

        if( isSelected != selected )
        {
            selectionModel.removeTreeSelectionListener( this );

            try
            {
                if( selected )
                {
                    selectionModel.removeSelectionPath( path );
                }
                else
                {
                    selectionModel.addSelectionPath( path );
                }
            }
            finally
            {
                selectionModel.addTreeSelectionListener( this );
                tree.treeDidChange();
            }
        }
    }

    /***************************************************************************
     * @return CheckTreeSelectionModel
     **************************************************************************/
    public CheckTreeSelectionModel getSelectionModel()
    {
        return selectionModel;
    }

    /***************************************************************************
     * @param e TreeSelectionEvent
     **************************************************************************/
    @Override
    public void valueChanged( TreeSelectionEvent e )
    {
        tree.treeDidChange();
    }

    /***************************************************************************
     * @param e
     * @return
     **************************************************************************/
    public TreePath getPath( MouseEvent e )
    {
        TreePath path = tree.getPathForLocation( e.getX(), e.getY() );

        if( path == null )
        {
            return null;
        }

        if( e.getX() > tree.getPathBounds( path ).x + HOTSPOT )
        {
            return null;
        }

        return path;
    }

    private final class CheckTreeManager_mouseAdapter extends MouseAdapter
    {
        private CheckTreeManager manager = null;

        private TreePath pressedPath = null;

        public CheckTreeManager_mouseAdapter( CheckTreeManager manager )
        {
            this.manager = manager;
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            pressedPath = manager.getPath( e );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            TreePath releasedPath = manager.getPath( e );

            if( releasedPath != null && releasedPath.equals( pressedPath ) )
            {
                boolean isSelected = manager.selectionModel.isPathSelected(
                    releasedPath );
                manager.setPathSelected( releasedPath, !isSelected );
            }

            pressedPath = null;
        }
    }

}
