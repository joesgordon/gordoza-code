package org.cc.edit.ui.nodes;

import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.ClosedTask;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ClosedTaskNode extends InfoNode<ClosedTask>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public ClosedTaskNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( ClosedTask data,
        DefaultTreeModel model )
    {
        return null;
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
    public InfoPanel<ClosedTask> createPanel( UndoManager manager )
    {
        return null;
    }
}
