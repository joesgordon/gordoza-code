package org.jutils.ui;

import java.awt.Component;
import java.io.File;
import java.util.*;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.DropActionType;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DirectoryTree implements IView<JTree>
{
    /**  */
    public static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

    /**  */
    private final JTree tree;
    /**  */
    private final DefaultMutableTreeNode root;
    /**  */
    private final DefaultTreeModel treeModel;
    /**  */
    private final ItemActionList<List<File>> selectedListeners;
    /**  */
    private final SelectionListener localSelectedListener;

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
    public DirectoryTree( File [] rootFiles )
    {
        this.root = new DefaultMutableTreeNode();
        this.treeModel = new DefaultTreeModel( root );
        this.tree = new JTree( treeModel );
        this.selectedListeners = new ItemActionList<>();
        this.localSelectedListener = new SelectionListener( this );

        if( rootFiles != null )
        {
            for( int i = 0; i < rootFiles.length; i++ )
            {
                addFile( rootFiles[i], root );
            }
        }

        // super.putClientProperty( "JTree.lineStyle", "None" );

        tree.setShowsRootHandles( true );
        tree.setRootVisible( false );
        tree.setEditable( false );
        tree.expandPath( new TreePath( root ) );
        tree.setCellRenderer( new Renderer() );
        tree.addTreeWillExpandListener( new ExpansionListener() );

        tree.setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
        tree.addTreeSelectionListener( localSelectedListener );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JTree getView()
    {
        return tree;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearSelection()
    {
        tree.clearSelection();
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        File [] selected = getSelected();

        return IOUtils.getStringFromFiles( selected );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File [] getSelected()
    {
        TreePath [] paths = tree.getSelectionPaths();
        if( paths == null )
        {
            return new File[0];
        }
        File [] files = new File[paths.length];

        for( int i = 0; i < paths.length; i++ )
        {
            FolderNode node = ( FolderNode )paths[i].getLastPathComponent();
            files[i] = node.getFolder();
        }

        return files;
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        File [] dirs = null;

        if( paths != null )
        {
            dirs = IOUtils.getFilesFromString( paths );
        }

        setSelected( dirs );
    }

    /***************************************************************************
     * @param dirs File[]
     **************************************************************************/
    public void setSelected( File [] dirs )
    {
        localSelectedListener.setEnabled( false );

        if( dirs == null )
        {
            clearSelection();
            return;
        }

        TreePath [] treePaths = getTreePaths( dirs );

        // LogUtils.printDebug( "DEBUG: Selecting " + dirs.length + " folders"
        // );

        if( treePaths.length > 0 )
        {
            tree.scrollPathToVisible( treePaths[0] );
            tree.setSelectionPaths( treePaths );
        }

        localSelectedListener.setEnabled( true );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSelectedListener( ItemActionListener<List<File>> l )
    {
        selectedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshSelected()
    {
        TreePath [] paths = tree.getSelectionPaths();

        if( paths != null )
        {
            for( TreePath path : paths )
            {
                Object lastComp = path.getLastPathComponent();
                FolderNode node = ( FolderNode )lastComp;

                node.removeAllChildren();
                expandFolderPath( path );
                treeModel.reload( node );
            }
        }
    }

    /***************************************************************************
     * @param dirs
     * @return
     **************************************************************************/
    private TreePath [] getTreePaths( File [] dirs )
    {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();
        TreePath [] treePaths = null;

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
        File [] filesPath = getParentage( dir );
        DefaultMutableTreeNode dmtr = root;

        // LogUtils.printDebug( "Dir: " + dir.getAbsolutePath() );
        // for( int i = 0; i < filesPath.length; i++ )
        // {
        // LogUtils.printDebug( "\tDir: " + filesPath[i].getAbsolutePath() );
        // }

        for( int i = 0; i < filesPath.length; i++ )
        {
            tree.expandPath( new TreePath( dmtr.getPath() ) );

            FolderNode node = getNodeWithFile( dmtr, filesPath[i] );

            if( node == null )
            {
                System.err.println(
                    "Could not find path " + dir.getAbsolutePath() + ":" +
                        filesPath[i].getName() + " in " + dmtr.toString() );
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
    private static File [] getParentage( File dir )
    {
        ArrayList<File> files = new ArrayList<File>( 10 );
        File [] array;

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
    private static FolderNode getNodeWithFile( DefaultMutableTreeNode node,
        File dir )
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
            else if( file.getAbsolutePath().compareTo(
                dir.getAbsolutePath() ) == 0 )
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
     * @param f File
     * @param node DefaultMutableTreeNode
     * @param recurse boolean
     **************************************************************************/
    private static void addFile( File f, DefaultMutableTreeNode node )
    {
        FolderNode newNode = new FolderNode( f );
        node.add( newNode );

        // LogUtils.printDebug( "DEBUG: Adding folder: " + newNode );
    }

    /***************************************************************************
     * @param node FolderNode
     **************************************************************************/
    private static void addChildren( FolderNode node )
    {
        File f = node.getFolder();
        File [] chillen = f.listFiles();
        chillen = chillen == null ? new File[0] : chillen;

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
     * @param event TreeExpansionEvent
     * @throws ExpandVetoException
     **************************************************************************/
    private static void expandFolderPath( TreePath path )
    {
        Object lastComp = path.getLastPathComponent();
        FolderNode node = ( FolderNode )lastComp;

        if( node.getChildCount() == 0 )
        {
            addChildren( node );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FolderNode extends DefaultMutableTreeNode
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
                desc = DirectoryTree.FILE_SYSTEM.getSystemDisplayName(
                    getFolder() );
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class Renderer extends DefaultTreeCellRenderer
    {
        public Renderer()
        {
        }

        public Component getTreeCellRendererComponent( JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus )
        {
            super.getTreeCellRendererComponent( tree, value, sel, expanded,
                leaf, row, hasFocus );

            if( value instanceof FolderNode )
            {
                FolderNode node = ( FolderNode )value;

                setIcon( node.getIcon() );
            }

            return this;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileDroppedListener
        implements ItemActionListener<IFileDropEvent>
    {
        private final DirectoryTree tree;

        public FileDroppedListener( DirectoryTree tree )
        {
            this.tree = tree;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent drop = event.getItem();
            List<File> files = drop.getFiles();

            if( drop.getActionType() == DropActionType.COPY )
            {
                List<File> selected = new ArrayList<>();
                selected.addAll( Arrays.asList( tree.getSelected() ) );
                selected.addAll( files );
                tree.setSelected( selected.toArray( new File[0] ) );
            }
            else
            {
                tree.setSelected( files.toArray( new File[0] ) );
            }
        }
    }

    private static class SelectionListener implements TreeSelectionListener
    {
        private final DirectoryTree dirTree;

        private boolean enabled;

        public SelectionListener( DirectoryTree dirTree )
        {
            this.dirTree = dirTree;
        }

        public void setEnabled( boolean enabled )
        {
            this.enabled = enabled;
        }

        @Override
        public void valueChanged( TreeSelectionEvent e )
        {
            TreePath [] paths = dirTree.tree.getSelectionPaths();

            if( !enabled || paths == null || paths.length == 0 )
            {
                return;
            }

            List<File> files = new ArrayList<>( paths.length );

            for( TreePath path : paths )
            {
                FolderNode node = ( FolderNode )path.getLastPathComponent();

                files.add( node.getFolder() );
            }

            dirTree.selectedListeners.fireListeners( dirTree, files );
        }
    }
}
