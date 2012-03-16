package org.cc.edit.ui.nodes;

import java.util.ArrayList;
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
public class ClosedTasksNode extends InfoNode<List<ClosedTask>>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public ClosedTasksNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( List<ClosedTask> data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        for( ClosedTask t : data )
        {
            ClosedTaskNode bn = new ClosedTaskNode( this );
            bn.setData( t, model );
            chirdren.add( bn );
        }

        return chirdren;
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
    public InfoPanel<List<ClosedTask>> createPanel( UndoManager manager )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return "Closed Tasks";
    }
}
