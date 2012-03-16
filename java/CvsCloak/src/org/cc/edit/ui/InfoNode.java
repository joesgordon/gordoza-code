package org.cc.edit.ui;

import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.undo.UndoManager;

import org.cc.edit.ui.nodes.RepositoryNode;
import org.cc.edit.ui.nodes.creators.NodeCreator;
import org.jutils.IteratorEnumerationAdapter;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public abstract class InfoNode<T> implements TreeNode
{
    /**  */
    private T data;
    /**  */
    private TreeNode parent;
    /**  */
    private List<TreeNode> children;
    /**  */
    private DefaultTreeModel model;

    /***************************************************************************
     * @param panel
     **************************************************************************/
    public InfoNode()
    {
        this( null );
    }

    /***************************************************************************
     * @param panel
     * @param parent
     **************************************************************************/
    public InfoNode( TreeNode parent )
    {
        this.parent = parent;
    }

    /***************************************************************************
     * @param node
     **************************************************************************/
    protected void addNewChild( TreeNode node )
    {
        children.add( node );
    }

    /***************************************************************************
     * @param i
     **************************************************************************/
    protected void removeChildAt( int i )
    {
        children.remove( i );
    }

    /***************************************************************************
     * @param data
     * @param model
     * @return
     **************************************************************************/
    protected abstract List<TreeNode> createChildren( T data,
        DefaultTreeModel model );

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract InfoPanel<T> createPanel( UndoManager manager );

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( T data, DefaultTreeModel treeModel )
    {
        this.data = data;
        this.model = treeModel;

        refresh();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DefaultTreeModel getTreeModel()
    {
        return model;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refresh()
    {
        children = createChildren( data, model );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getData()
    {
        return data;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return data.toString();
    }

    /***************************************************************************
     * The rawtypes suppression annotation is added because the overridden
     * function cannot specify generics in the return value if the super class
     * did not.
     **************************************************************************/
    @Override
    @SuppressWarnings( "rawtypes")
    public Enumeration children()
    {
        return children == null ? null
            : new IteratorEnumerationAdapter<TreeNode>( children.iterator() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public TreeNode getChildAt( int childIndex )
    {
        return children == null ? null : children.get( childIndex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getChildCount()
    {
        return children == null ? 0 : children.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIndex( TreeNode node )
    {

        return children == null ? -1 : children.indexOf( node );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public TreeNode getParent()
    {
        return parent;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isLeaf()
    {
        return children == null ? true : children.isEmpty();
    }

    /***************************************************************************
     *
     **************************************************************************/
    protected class DataChangedListener implements ItemActionListener<T>
    {
        private DefaultTreeModel treeModel;

        public DataChangedListener( DefaultTreeModel model )
        {
            treeModel = model;
        }

        @Override
        public void actionPerformed( ItemActionEvent<T> event )
        {
            treeModel.nodeChanged( InfoNode.this );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected class NodeChangedListener<E> implements ItemActionListener<E>
    {
        private TreeNode treeNode;

        public NodeChangedListener( TreeNode node )
        {
            treeNode = node;
        }

        @Override
        public void actionPerformed( ItemActionEvent<E> event )
        {
            getTreeModel().nodeChanged( treeNode );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class ChildAddedListener<E> implements
        ItemActionListener<E>
    {
        private InfoNode<?> pnt;
        private NodeCreator<E> nCreator;

        public ChildAddedListener( InfoNode<?> parent,
            NodeCreator<E> nodeCreator )
        {
            pnt = parent;
            nCreator = nodeCreator;
        }

        @Override
        public void actionPerformed( ItemActionEvent<E> event )
        {
            DefaultTreeModel model = pnt.getTreeModel();
            InfoNode<E> node = nCreator.createNode( pnt );
            int[] childIndexes;

            node.setData( event.getItem(), model );
            pnt.addNewChild( node );
            childIndexes = new int[] { pnt.getChildCount() - 1 };
            model.nodesWereInserted( pnt, childIndexes );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected class ChildRemovedListener<E> implements ItemActionListener<E>
    {
        private TreeNode pnt;

        public ChildRemovedListener( TreeNode parent )
        {
            pnt = parent;
        }

        @Override
        public void actionPerformed( ItemActionEvent<E> event )
        {
            RepositoryNode node = null;

            for( int i = 0; i < getChildCount(); i++ )
            {
                node = ( RepositoryNode )getChildAt( i );

                if( node.getData().equals( event.getItem() ) )
                {
                    removeChildAt( i );
                    getTreeModel().nodesWereRemoved( pnt, new int[] { i },
                        new Object[] { node } );
                    break;
                }
            }
        }
    }
}
