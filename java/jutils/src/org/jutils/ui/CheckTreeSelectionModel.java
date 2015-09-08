package org.jutils.ui;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.tree.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeSelectionModel extends DefaultTreeSelectionModel
{
    /**  */
    private TreeModel model;

    /***************************************************************************
     * @param model TreeModel
     **************************************************************************/
    public CheckTreeSelectionModel( TreeModel model )
    {
        this.model = model;
        setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
    }

    /***************************************************************************
     * tests whether there is any unselected node in the subtree of given path
     * @param path TreePath
     * @return boolean
     **************************************************************************/
    public boolean isPartiallySelected( TreePath path )
    {
        if( isPathSelected( path, true ) )
        {
            return false;
        }
        TreePath [] selectionPaths = getSelectionPaths();
        if( selectionPaths == null )
        {
            return false;
        }
        for( int j = 0; j < selectionPaths.length; j++ )
        {
            if( isDescendant( selectionPaths[j], path ) )
            {
                return true;
            }
        }
        return false;
    }

    /***************************************************************************
     * tells whether given path is selected. if dig is true, then a path is
     * assumed to be selected if one of its ancestors is selected.
     * @param path TreePathd
     * @param dig boolean
     * @return boolean
     **************************************************************************/
    public boolean isPathSelected( TreePath path, boolean dig )
    {
        if( !dig )
        {
            return super.isPathSelected( path );
        }
        while( path != null && !super.isPathSelected( path ) )
        {
            path = path.getParentPath();
        }
        return path != null;
    }

    /***************************************************************************
     * is path1 descendant of path2
     * @param path1 TreePath
     * @param path2 TreePath
     * @return boolean
     **************************************************************************/
    private boolean isDescendant( TreePath path1, TreePath path2 )
    {
        Object obj1[] = path1.getPath();
        Object obj2[] = path2.getPath();
        for( int i = 0; i < obj2.length; i++ )
        {
            if( obj1[i] != obj2[i] )
            {
                return false;
            }
        }
        return true;
    }

    /***************************************************************************
     * @param pPaths TreePath[]
     **************************************************************************/
    public void setSelectionPaths( TreePath [] paths )
    {
        this.clearSelection();
        this.addSelectionPaths( paths );
    }

    /***************************************************************************
     * @param paths TreePath[]
     **************************************************************************/
    public void addSelectionPaths( TreePath [] paths )
    {
        // unselect all descendants of paths[]
        for( int i = 0; i < paths.length; i++ )
        {
            TreePath path = paths[i];
            TreePath [] selectionPaths = getSelectionPaths();
            if( selectionPaths == null )
            {
                break;
            }
            ArrayList<TreePath> toBeRemoved = new ArrayList<TreePath>();
            for( int j = 0; j < selectionPaths.length; j++ )
            {
                if( isDescendant( selectionPaths[j], path ) )
                {
                    toBeRemoved.add( selectionPaths[j] );
                }
            }
            super.removeSelectionPaths(
                ( TreePath [] )toBeRemoved.toArray( new TreePath[0] ) );
        }

        // if all siblings are selected then unselect them and select parent
        // recursively otherwize just select that path.
        for( int i = 0; i < paths.length; i++ )
        {
            TreePath path = paths[i];
            TreePath temp = null;
            while( areSiblingsSelected( path ) )
            {
                temp = path;
                if( path.getParentPath() == null )
                {
                    break;
                }
                path = path.getParentPath();
            }
            if( temp != null )
            {
                if( temp.getParentPath() != null )
                {
                    addSelectionPath( temp.getParentPath() );
                }
                else
                {
                    if( !isSelectionEmpty() )
                    {
                        removeSelectionPaths( getSelectionPaths() );
                    }
                    super.addSelectionPaths( new TreePath[] { temp } );
                }
            }
            else
            {
                super.addSelectionPaths( new TreePath[] { path } );
            }
        }
    }

    /***************************************************************************
     * tells whether all siblings of given path are selected.
     * @param path TreePath
     * @return boolean
     **************************************************************************/
    private boolean areSiblingsSelected( TreePath path )
    {
        TreePath parent = path.getParentPath();
        if( parent == null )
        {
            return true;
        }
        Object node = path.getLastPathComponent();
        Object parentNode = parent.getLastPathComponent();

        int childCount = model.getChildCount( parentNode );
        for( int i = 0; i < childCount; i++ )
        {
            Object childNode = model.getChild( parentNode, i );
            if( childNode == node )
            {
                continue;
            }
            if( !isPathSelected( parent.pathByAddingChild( childNode ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /***************************************************************************
     * @param paths TreePath[]
     **************************************************************************/
    public void removeSelectionPaths( TreePath [] paths )
    {
        for( int i = 0; i < paths.length; i++ )
        {
            TreePath path = paths[i];
            if( path.getPathCount() == 1 )
            {
                super.removeSelectionPaths( new TreePath[] { path } );
            }
            else
            {
                toggleRemoveSelection( path );
            }
        }
    }

    /***************************************************************************
     * If any ancestor node of given path is selected then unselect it and
     * selection all its descendants except given path and descendants.
     * otherwise just unselect the given path
     * @param path TreePath
     **************************************************************************/
    private void toggleRemoveSelection( TreePath path )
    {
        Stack<TreePath> stack = new Stack<TreePath>();
        TreePath parent = path.getParentPath();
        while( parent != null && !isPathSelected( parent ) )
        {
            stack.push( parent );
            parent = parent.getParentPath();
        }
        if( parent != null )
        {
            stack.push( parent );
        }
        else
        {
            super.removeSelectionPaths( new TreePath[] { path } );
            return;
        }

        while( !stack.isEmpty() )
        {
            TreePath temp = ( TreePath )stack.pop();
            TreePath peekPath = stack.isEmpty() ? path
                : ( TreePath )stack.peek();
            Object node = temp.getLastPathComponent();
            Object peekNode = peekPath.getLastPathComponent();
            int childCount = model.getChildCount( node );
            for( int i = 0; i < childCount; i++ )
            {
                Object childNode = model.getChild( node, i );
                if( childNode != peekNode )
                {
                    super.addSelectionPaths( new TreePath[] {
                        temp.pathByAddingChild( childNode ) } );
                }
            }
        }
        super.removeSelectionPaths( new TreePath[] { parent } );
    }
}
