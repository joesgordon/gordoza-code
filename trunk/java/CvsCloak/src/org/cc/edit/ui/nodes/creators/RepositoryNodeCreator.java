package org.cc.edit.ui.nodes.creators;

import javax.swing.tree.TreeNode;

import org.cc.data.Repository;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.nodes.RepositoryNode;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RepositoryNodeCreator implements NodeCreator<Repository>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public InfoNode<Repository> createNode( TreeNode parent )
    {
        return new RepositoryNode( parent );
    }
}
