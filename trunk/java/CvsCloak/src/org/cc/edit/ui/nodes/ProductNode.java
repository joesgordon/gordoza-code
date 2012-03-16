package org.cc.edit.ui.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.Product;
import org.cc.data.Release;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.model.ProductPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProductNode extends InfoNode<Product>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public ProductNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( Product data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        for( Release r : data.getReleases() )
        {
            ReleaseNode rn = new ReleaseNode( this );
            rn.setData( r, model );
            chirdren.add( rn );
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
    public InfoPanel<Product> createPanel( UndoManager manager )
    {
        ProductPanel panel = new ProductPanel( manager );

        panel.setData( getData() );
        panel.addNameChangedListener( new NodeChangedListener<String>( this ) );

        return panel;
    }
}
