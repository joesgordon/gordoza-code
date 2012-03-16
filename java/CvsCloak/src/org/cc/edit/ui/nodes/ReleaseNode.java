package org.cc.edit.ui.nodes;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.Release;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ReleaseNode extends InfoNode<Release>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public ReleaseNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( Release data,
        DefaultTreeModel model )
    {
        ClosedTasksNode bn = new ClosedTasksNode( this );

        return bn.createChildren( data.getClosedTasks(), null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean getAllowsChildren()
    {
        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public InfoPanel<Release> createPanel( UndoManager manager )
    {
        // InfoPanel<Release> panel = new ReleasePanel();
        // panel.setData( getData() );
        // return panel;
        return null;
    }
}
