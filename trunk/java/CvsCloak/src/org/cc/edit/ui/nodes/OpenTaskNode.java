package org.cc.edit.ui.nodes;

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
public class OpenTaskNode extends InfoNode<OpenTask>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public OpenTaskNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( OpenTask data,
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
    public InfoPanel<OpenTask> createPanel( UndoManager manager )
    {
        return null;
    }
}
