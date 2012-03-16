package org.cc.edit.ui.nodes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.data.Repository;
import org.cc.data.VersioningSystem;
import org.cc.edit.ui.InfoNode;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.nodes.creators.RepositoryNodeCreator;
import org.cc.edit.ui.panels.model.AppModelPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppModelNode extends InfoNode<VersioningSystem>
{
    /***************************************************************************
     * @param node
     **************************************************************************/
    public AppModelNode()
    {
        super();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected List<TreeNode> createChildren( VersioningSystem data,
        DefaultTreeModel model )
    {
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        List<Repository> repos = data.getRepositories();

        for( int i = 0; i < repos.size(); i++ )
        {
            RepositoryNode node = new RepositoryNode( this );
            node.setData( repos.get( i ), model );
            nodes.add( node );
        }

        return nodes;
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
    public InfoPanel<VersioningSystem> createPanel( UndoManager manager )
    {
        AppModelPanel panel = new AppModelPanel( manager );

        panel.addRepoAddedListener( new ChildAddedListener<Repository>( this,
            new RepositoryNodeCreator() ) );
        panel.addRepoRemovedListener( new ChildRemovedListener<Repository>(
            this ) );
        panel.setData( getData() );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return "Configuration";
    }
}
