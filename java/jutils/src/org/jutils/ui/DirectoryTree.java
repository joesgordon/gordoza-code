package org.jutils.ui;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import org.jutils.io.LogUtils;
import org.jutils.io.UFile;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DirectoryTree extends JTree
{
    /**  */
    public static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

    /**  */
    private DefaultMutableTreeNode root = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public DirectoryTree()
    {
        this( FILE_SYSTEM.getRoots() );
    }

    /***************************************************************************
     * @param rootFile File
     **************************************************************************/
    public DirectoryTree( File rootFile )
    {
        this( new File[] { rootFile } );
    }

    /***************************************************************************
     * @param rootFiles File[]
     **************************************************************************/
    public DirectoryTree( File[] rootFiles )
    {
        super( new DefaultMutableTreeNode() );
        root = ( DefaultMutableTreeNode )super.getModel().getRoot();

        if( rootFiles != null )
        {
            for( int i = 0; i < rootFiles.length; i++ )
            {
                addFile( rootFiles[i], root );
            }
        }

        // super.putClientProperty( "JTree.lineStyle", "None" );

        super.setShowsRootHandles( true );
        super.setRootVisible( false );
        super.setEditable( false );
        super.expandPath( new TreePath( root ) );
        super.setCellRenderer( new Renderer() );
        super.addTreeWillExpandListener( new ExpansionListener() );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File[] getSelected()
    {
        TreePath[] paths = this.getSelectionPaths();
        if( paths == null )
        {
            return null;
        }
        File[] files = new File[paths.length];

        for( int i = 0; i < paths.length; i++ )
        {
            FolderNode node = ( FolderNode )paths[i].getLastPathComponent();
            files[i] = node.getFolder();
        }

        return files;
    }

    /***************************************************************************
     * @param dirs File[]
     **************************************************************************/
    public void setSelected( File[] dirs )
    {
        TreePath[] treePaths = getTreePaths( dirs );

        // LogUtils.printDebug( "DEBUG: Selecting " + dirs.length + " folders"
        // );
        if( treePaths.length > 0 )
        {
            scrollPathToVisible( treePaths[0] );
            setSelectionPaths( treePaths );
        }
    }

    /***************************************************************************
     * @param dirs
     * @return
     **************************************************************************/
    private TreePath[] getTreePaths( File[] dirs )
    {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();
        TreePath[] treePaths = null;

        // For each directory to be selected
        for( int i = 0; i < dirs.length; i++ )
        {
            File dir = dirs[i];
            TreePath path = getTreePath( dir );

            if( path != null )
            {
                paths.add( path );
            }
            else
            {
                System.err.println( dir.getAbsolutePath() +
                    " is not an ancestor of any system root" );
            }
        }

        treePaths = new TreePath[paths.size()];
        paths.toArray( treePaths );

        return treePaths;
    }

    /***************************************************************************
     * @param dir
     * @return
     **************************************************************************/
    private TreePath getTreePath( File dir )
    {
        TreePath path = null;
        File[] filesPath = getParentage( dir );
        DefaultMutableTreeNode dmtr = root;

        // LogUtils.printDebug( "Dir: " + dir.getAbsolutePath() );
        // for( int i = 0; i < filesPath.length; i++ )
        // {
        // LogUtils.printDebug( "\tDir: " + filesPath[i].getAbsolutePath() );
        // }

        for( int i = 0; i < filesPath.length; i++ )
        {
            expandPath( new TreePath( dmtr.getPath() ) );

            FolderNode node = getNodeWithFile( dmtr, filesPath[i] );

            if( node == null )
            {
                System.err.println( "Could not find path " +
                    dir.getAbsolutePath() + ":" + filesPath[i].getName() +
                    " in " + dmtr.toString() );
                path = new TreePath( dmtr.getPath() );
                break;
            }
            dmtr = node;

            if( i == filesPath.length - 1 )
            {
                path = new TreePath( node.getPath() );
            }
        }

        return path;
    }

    /***************************************************************************
     * @param dir
     * @return
     **************************************************************************/
    private static File[] getParentage( File dir )
    {
        ArrayList<File> files = new ArrayList<File>( 10 );
        File[] array;

        // LogUtils.printDebug( "Dir: " + dir.getAbsolutePath() );

        while( dir != null && !dir.isDirectory() )
        {
            dir = dir.getParentFile();
            // LogUtils.printDebug( "\tDNE-Parent: " + dir.getAbsolutePath() );
        }

        if( dir != null )
        {
            files.add( dir );
        }

        while( ( dir = FILE_SYSTEM.getParentDirectory( dir ) ) != null )
        {
            // LogUtils.printDebug( "\tParent: " + dir.getAbsolutePath() );
            files.add( dir );
        }

        Collections.reverse( files );
        array = new File[files.size()];
        files.toArray( array );

        return array;
    }

    /***************************************************************************
     * @param node
     * @param dir
     * @return
     **************************************************************************/
    private FolderNode getNodeWithFile( DefaultMutableTreeNode node, File dir )
    {
        if( dir == null )
        {
            return null;
        }

        for( int i = 0; i < node.getChildCount(); i++ )
        {
            FolderNode fn = ( FolderNode )node.getChildAt( i );
            File file = fn.getFolder();

            if( file.equals( dir ) )
            {
                return fn;
            }
            else if( file.getAbsoluteFile().equals( dir.getAbsoluteFile() ) )
            {
                LogUtils.printDebug( "DEBUG: Same path different files ? " +
                    dir.getPath() + ":" + file.getPath() );
                return fn;
            }
            else if( file.getAbsolutePath().compareTo( dir.getAbsolutePath() ) == 0 )
            {
                LogUtils.printDebug( "DEBUG: Somehow " +
                    file.getAbsolutePath() + " which is a " + file.getClass() +
                    " != " + dir.getAbsolutePath() + " which is a " +
                    file.getClass() );
                return fn;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        setSelected( UFile.getFilesFromString( paths ) );
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        File[] selected = getSelected();

        if( selected == null )
        {
            selected = new File[0];
        }

        return UFile.getStringFromFiles( selected );
    }

    /***************************************************************************
     * @param f File
     * @param node DefaultMutableTreeNode
     * @param recurse boolean
     **************************************************************************/
    private void addFile( File f, DefaultMutableTreeNode node )
    {
        FolderNode newNode = new FolderNode( f );
        node.add( newNode );

        // LogUtils.printDebug( "DEBUG: Adding folder: " + newNode );
    }

    /***************************************************************************
     * @param node FolderNode
     **************************************************************************/
    private void addChildren( FolderNode node )
    {
        File f = node.getFolder();
        File[] chillen = f.listFiles();

        java.util.Arrays.sort( chillen );

        // node.removeAllChildren();
        for( int i = 0; i < chillen.length; i++ )
        {
            if( chillen[i].isDirectory() )
            {
                addFile( chillen[i], node );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshSelected()
    {
        ;
    }

    /***************************************************************************
     * @param event TreeExpansionEvent
     * @throws ExpandVetoException
     **************************************************************************/
    private void expandFolderPath( TreePath path ) throws ExpandVetoException
    {
        Object lastComp = path.getLastPathComponent();
        FolderNode node = null;

        node = ( FolderNode )lastComp;

        if( node.getChildCount() == 0 )
        {
            addChildren( node );
        }
    }

    private class ExpansionListener implements TreeWillExpandListener
    {
        @Override
        public void treeWillExpand( TreeExpansionEvent event )
            throws ExpandVetoException
        {
            TreePath path = event.getPath();
            expandFolderPath( path );
        }

        @Override
        public void treeWillCollapse( TreeExpansionEvent event )
            throws ExpandVetoException
        {
            // Intentionally not used.
        }
    }
}

class FolderNode extends DefaultMutableTreeNode
{
    private String desc = null;

    private Icon icon = null;

    public FolderNode( File dir )
    {
        super( dir );
    }

    public String toString()
    {
        if( desc == null )
        {
            desc = DirectoryTree.FILE_SYSTEM.getSystemDisplayName( getFolder() );
        }

        return desc;
    }

    public boolean isLeaf()
    {
        return !getFolder().isDirectory();
    }

    public File getFolder()
    {
        return ( File )super.getUserObject();
    }

    public Icon getIcon()
    {
        if( icon == null )
        {
            icon = DirectoryTree.FILE_SYSTEM.getSystemIcon( getFolder() );
        }
        return icon;
    }

    public void setIcon( Icon icon )
    {
        this.icon = icon;
    }
}

class Renderer extends DefaultTreeCellRenderer
{
    public Renderer()
    {
    }

    public Component getTreeCellRendererComponent( JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus )
    {
        super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf,
            row, hasFocus );

        if( value instanceof FolderNode )
        {
            FolderNode node = ( FolderNode )value;

            setIcon( node.getIcon() );
        }

        return this;
    }
}
