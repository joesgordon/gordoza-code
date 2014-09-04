package org.cc.edit.ui.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.Baseline;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BaselinesNode extends InfoNode<List<Baseline>>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public BaselinesNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( List<Baseline> data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        for( Baseline b : data )
        {
            BaselineNode pn = new BaselineNode( this );
            pn.setData( b, model );
            chirdren.add( pn );
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
    public InfoPanel<List<Baseline>> createPanel( UndoManager manager )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return "Baselines";
    }
}
