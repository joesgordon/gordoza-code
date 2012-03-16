package org.cc.edit.ui.nodes.creators;

import javax.swing.tree.TreeNode;

import org.cc.data.Product;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.nodes.ProductNode;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProductNodeCreator implements NodeCreator<Product>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public InfoNode<Product> createNode( TreeNode parent )
    {
        ProductNode pn = new ProductNode( parent );
        return pn;
    }
}
