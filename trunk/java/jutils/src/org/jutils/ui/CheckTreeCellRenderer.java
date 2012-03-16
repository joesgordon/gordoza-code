package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/***************************************************************************
 *
 **************************************************************************/
public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer
{
    /**  */
    private CheckTreeSelectionModel selectionModel;

    /**  */
    private TreeCellRenderer delegate;

    /**  */
    private TristateCheckBox checkBox = new TristateCheckBox();

    /***************************************************************************
     * @param delegate TreeCellRenderer
     * @param selectionModel CheckTreeSelectionModel
     **************************************************************************/
    public CheckTreeCellRenderer( TreeCellRenderer delegate,
        CheckTreeSelectionModel selectionModel )
    {
        this.delegate = delegate;
        this.selectionModel = selectionModel;
        setLayout( new BorderLayout() );
        setOpaque( false );
        checkBox.setOpaque( false );
    }

    /***************************************************************************
     * @param tree JTree
     * @param value Object
     * @param selected boolean
     * @param expanded boolean
     * @param leaf boolean
     * @param row int
     * @param hasFocus boolean
     * @return Component
     **************************************************************************/
    public Component getTreeCellRendererComponent( JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row,
        boolean hasFocus )
    {
        Component renderer = delegate.getTreeCellRendererComponent( tree,
            value, selected, expanded, leaf, row, hasFocus );

        TreePath path = tree.getPathForRow( row );
        if( path != null )
        {
            if( selectionModel.isPathSelected( path, true ) )
            {
                checkBox.setState( Boolean.TRUE );
            }
            else
            {
                checkBox.setState( selectionModel.isPartiallySelected( path ) ? null
                    : Boolean.FALSE );
            }
        }
        removeAll();
        add( checkBox, BorderLayout.WEST );
        add( renderer, BorderLayout.CENTER );
        return this;
    }
}
