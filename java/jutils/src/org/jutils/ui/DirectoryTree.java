package org.jutils.ui;

import java.awt.Component;
import java.io.File;
import java.util.*;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import org.jutils.SwingUtils;
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
    private final FileTreeNode root;
    /**  */
    private final FileTreeModel treeModel;
    /**  */
    private final ItemActionList<List<File>> selectedListeners;
    /**  */
    private final SelectionListener localSelectedListener;

    /***************************************************************************
     * 
     **************************************************************************/
    public DirectoryTree()
    {
        this( new FileTreeNode() );
    }

    /***************************************************************************
     * @param rootFile File
     **************************************************************************/
    public DirectoryTree( File rootFile )
    {
        this( new FileTreeNode( null, rootFile ) );
    }

    /***************************************************************************
     * @param root FileTreeNode
     **************************************************************************/
    private DirectoryTree( FileTreeNode root )
    {
        this.root = root;
        this.treeModel = new FileTreeModel( root );
        this.tree = new JTree( treeModel );
        this.selectedListeners = new ItemActionList<>();
        this.localSelectedListener = new SelectionListener( this );

        // super.putClientProperty( "JTree.lineStyle", "None" );

        tree.setShowsRootHandles( true );
        tree.setRootVisible( false );
        tree.setEditable( false );
        tree.expandPath( new TreePath( root ) );
        tree.setCellRenderer( new Renderer() );
        tree.addTreeWillExpandListener( new ExpansionListener() );

        tree.setModel( new DefaultTreeModel( root ) );

        tree.setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
        tree.addTreeSelectionListener( localSelectedListener );
    }

    /***************************************************************************
     * {@inheritDoc}
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
            return new File[] {};
        }

        List<File> files = new ArrayList<>( paths.length );

        for( int i = 0; i < paths.length; i++ )
        {
            Object lpc = paths[i].getLastPathComponent();

            if( lpc instanceof FileTreeNode )
            {
                FileTreeNode node = ( FileTreeNode )lpc;

                files.add( node.file );
            }
            else
            {
                LogUtils.printDebug( "Node not a DirNode: " + lpc.toString() +
                    " of class " + lpc.getClass().getName() );
            }
        }

        return files.toArray( new File[] {} );
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

        // LogUtils.printDebug( "DEBUG: Selecting " + dirs.length + "
        // directories"
        // );

        if( treePaths.length > 0 )
        {
            tree.scrollPathToVisible( treePaths[0] );
            tree.setSelectionPaths( treePaths );
        }

        localSelectedListener.setEnabled( true );
    }

    /***************************************************************************
     * @param l the callback invoked when the selection has changed.
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
                FileTreeNode node = ( FileTreeNode )lastComp;

                node.loadChildren();
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
        FileTreeNode dmtr = root;

        // LogUtils.printDebug( "Dir: " + dir.getAbsolutePath() );
        // for( int i = 0; i < filesPath.length; i++ )
        // {
        // LogUtils.printDebug( "\tDir: " + filesPath[i].getAbsolutePath() );
        // }

        for( int i = 0; i < filesPath.length; i++ )
        {
            tree.expandPath( SwingUtils.getPath( dmtr ) );

            FileTreeNode node = getNodeWithFile( dmtr, filesPath[i] );

            if( node == null )
            {
                System.err.println(
                    "Could not find path " + dir.getAbsolutePath() + ":" +
                        filesPath[i].getName() + " in " + dmtr.toString() );
                path = SwingUtils.getPath( dmtr );
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
    private static FileTreeNode getNodeWithFile( FileTreeNode node, File dir )
    {
        if( dir == null )
        {
            return null;
        }

        for( int i = 0; i < node.getChildCount(); i++ )
        {
            FileTreeNode fn = node.getChildAt( i );
            File file = fn.file;

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
     * @param path
     **************************************************************************/
    private static void expandDirectoryPath( TreePath path )
    {
        Object lastComp = path.getLastPathComponent();
        FileTreeNode node = ( FileTreeNode )lastComp;

        if( node.getChildCount() == 0 )
        {
            node.loadChildren();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FileTreeNode implements TreeNode
    {
        /**  */
        public final FileTreeNode parent;
        /**  */
        public final File file;

        /**  */
        private final List<FileTreeNode> children;

        /**
         * 
         */
        public FileTreeNode()
        {
            this( null, null );

            File [] roots = FILE_SYSTEM.getRoots();

            if( roots != null )
            {
                for( File root : roots )
                {
                    children.add( new FileTreeNode( this, root ) );
                }
            }
        }

        public Object [] getPath()
        {
            List<FileTreeNode> nodes = new ArrayList<>();

            FileTreeNode node = this;

            while( node != null )
            {
                nodes.add( node );
                node = node.parent;
            }

            Collections.reverse( nodes );

            return nodes.toArray();
        }

        public FileTreeNode( FileTreeNode parent, File file )
        {
            this.parent = parent;
            this.file = file;
            this.children = new ArrayList<>();
        }

        @Override
        public FileTreeNode getChildAt( int childIndex )
        {
            return children.get( childIndex );
        }

        @Override
        public int getChildCount()
        {
            return children.size();
        }

        @Override
        public TreeNode getParent()
        {
            return parent;
        }

        @Override
        public int getIndex( TreeNode node )
        {
            return children.indexOf( node );
        }

        @Override
        public boolean getAllowsChildren()
        {
            return file == null ? true : file.isDirectory();
        }

        @Override
        public boolean isLeaf()
        {
            return file == null ? false : !file.isDirectory();
        }

        @Override
        public Enumeration<FileTreeNode> children()
        {
            return new EnumeratedIterator<>( children.iterator() );
        }

        public void loadChildren()
        {
            File [] files;

            try
            {
                files = file.listFiles();
                files = files == null ? new File[0] : files;

            }
            catch( SecurityException ex )
            {
                files = new File[0];
            }

            Arrays.sort( files );

            children.clear();

            for( File f : files )
            {
                if( f.isDirectory() )
                {
                    children.add( new FileTreeNode( this, f ) );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class EnumeratedIterator<T> implements Enumeration<T>
    {
        private final Iterator<T> iterator;

        public EnumeratedIterator( Iterator<T> iterator )
        {
            this.iterator = iterator;
        }

        @Override
        public boolean hasMoreElements()
        {
            return iterator.hasNext();
        }

        @Override
        public T nextElement()
        {
            return iterator.next();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final class FileTreeModel implements TreeModel
    {
        private final DefaultTreeModel model;

        private FileTreeModel( FileTreeNode node )
        {
            this.model = new DefaultTreeModel( node );
        }

        @Override
        public Object getRoot()
        {
            return model.getRoot();
        }

        @Override
        public Object getChild( Object parent, int index )
        {
            return model.getChild( parent, index );
        }

        @Override
        public int getChildCount( Object parent )
        {
            return model.getChildCount( parent );
        }

        @Override
        public boolean isLeaf( Object node )
        {
            if( node instanceof FileTreeNode )
            {
                FileTreeNode dmtn = ( FileTreeNode )node;
                Object obj = dmtn.file;

                if( obj != null && obj instanceof File )
                {
                    File file = ( File )obj;

                    return !file.isDirectory();
                }
            }

            return model.isLeaf( node );
        }

        @Override
        public void valueForPathChanged( TreePath path, Object newValue )
        {
            model.valueForPathChanged( path, newValue );
        }

        @Override
        public int getIndexOfChild( Object parent, Object child )
        {
            return model.getIndexOfChild( parent, child );
        }

        @Override
        public void addTreeModelListener( TreeModelListener l )
        {
            model.addTreeModelListener( l );
        }

        @Override
        public void removeTreeModelListener( TreeModelListener l )
        {
            model.removeTreeModelListener( l );
        }

        public void reload( FileTreeNode node )
        {
            model.reload( node );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final class ExpansionListener implements TreeWillExpandListener
    {
        @Override
        public void treeWillExpand( TreeExpansionEvent event )
            throws ExpandVetoException
        {
            TreePath path = event.getPath();
            expandDirectoryPath( path );
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
    private static class Renderer implements TreeCellRenderer
    {
        private final DefaultTreeCellRenderer renderer;
        private final Map<File, FileData> iconCache;

        public Renderer()
        {
            this.renderer = new DefaultTreeCellRenderer();
            this.iconCache = new HashMap<>();
        }

        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus )
        {
            renderer.getTreeCellRendererComponent( tree, value, sel, expanded,
                leaf, row, hasFocus );

            if( value instanceof FileTreeNode )
            {
                FileTreeNode node = ( FileTreeNode )value;
                File f = node.file;

                FileData fd = iconCache.get( f );

                if( fd == null )
                {
                    fd = new FileData( f );
                    iconCache.put( f, fd );
                }

                renderer.setIcon( fd.icon );
                renderer.setText( fd.name );
            }

            return renderer;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FileData
    {
        public final Icon icon;
        public final String name;

        public FileData( File file )
        {
            this.icon = DirectoryTree.FILE_SYSTEM.getSystemIcon( file );
            this.name = DirectoryTree.FILE_SYSTEM.getSystemDisplayName( file );
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

    /***************************************************************************
     * 
     **************************************************************************/
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
                FileTreeNode node = ( FileTreeNode )path.getLastPathComponent();

                files.add( node.file );
            }

            dirTree.selectedListeners.fireListeners( dirTree, files );
        }
    }
}
