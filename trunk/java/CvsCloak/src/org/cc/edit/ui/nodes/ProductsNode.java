package org.cc.edit.ui.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.creators.ProductCreator;
import org.cc.data.Product;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.nodes.creators.ProductNodeCreator;
import org.cc.edit.ui.panels.NamedItemsListPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProductsNode extends InfoNode<List<Product>>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public ProductsNode( TreeNode node )
    {
        super( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( List<Product> data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        for( Product p : data )
        {
            ProductNode pn = new ProductNode( this );
            pn.setData( p, model );
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
        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public InfoPanel<List<Product>> createPanel( UndoManager manager )
    {
        NamedItemsListPanel<Product> pPanel = new NamedItemsListPanel<Product>(
            manager, new ProductCreator() );

        pPanel.setData( getData() );
        pPanel.addItemAddedListener( new ChildAddedListener<Product>( this,
            new ProductNodeCreator() ) );

        return pPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return "Products";
    }
}
