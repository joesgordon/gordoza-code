package org.cc.edit.ui.nodes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.Product;
import org.cc.data.Repository;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.nodes.creators.ProductNodeCreator;
import org.cc.edit.ui.panels.model.RepositoryPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RepositoryNode extends InfoNode<Repository>
{
    private ProductsNode pn;
    private BaselinesNode bn;

    /***************************************************************************
     * @param node
     **************************************************************************/
    public RepositoryNode( TreeNode node )
    {
        super( node );

        pn = new ProductsNode( this );
        bn = new BaselinesNode( this );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( Repository data,
        DefaultTreeModel model )
    {
        List<TreeNode> chirdren = new ArrayList<TreeNode>();

        pn.setData( data.getProducts(), model );
        chirdren.add( pn );

        bn.setData( data.getBaselines(), model );
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
    public InfoPanel<Repository> createPanel( UndoManager manager )
    {
        RepositoryPanel panel = new RepositoryPanel( manager );

        panel.setData( getData() );
        panel.addLocationChangedListener( new NodeChangedListener<File>( this ) );
        panel.addProductAddedListener( new ChildAddedListener<Product>( pn,
            new ProductNodeCreator() ) );

        return panel;
    }
}
