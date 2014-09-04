package org.cc.edit.ui.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.OpenTask;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class OpenTasksNode extends InfoNode<List<OpenTask>>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public OpenTasksNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( List<OpenTask> data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        for( OpenTask t : data )
        {
            OpenTaskNode bn = new OpenTaskNode( this );
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
    public InfoPanel<List<OpenTask>> createPanel( UndoManager manager )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return "Open Tasks";
    }
}
