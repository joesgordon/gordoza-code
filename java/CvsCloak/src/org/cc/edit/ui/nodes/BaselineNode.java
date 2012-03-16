package org.cc.edit.ui.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.Baseline;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.model.BaselinePanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BaselineNode extends InfoNode<Baseline>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public BaselineNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( Baseline data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        OpenTasksNode pn = new OpenTasksNode( this );
        pn.setData( data.getOpenTasks(), model );
        chirdren.add( pn );

        ClosedTasksNode bn = new ClosedTasksNode( this );
        bn.setData( data.getClosedTasks(), model );
        chirdren.add( bn );

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
    public InfoPanel<Baseline> createPanel( UndoManager manager )
    {
        BaselinePanel panel = new BaselinePanel( manager );

        panel.setData( getData() );
        panel.addNameChangedListeners( new NodeChangedListener<String>( this ) );

        return panel;
    }
}
