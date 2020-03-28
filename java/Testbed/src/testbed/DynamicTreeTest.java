package testbed;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.parsers.StringLengthParser;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.event.RightClickListener;
import org.jutils.core.ui.model.IView;
import org.jutils.core.utils.IteratorEnumerationAdapter;

/*******************************************************************************
 *
 ******************************************************************************/
public class DynamicTreeTest
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new DynamicTreeApp() );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class DynamicTreeApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            DynamicTreeTestFrame frameView = new DynamicTreeTestFrame();

            return frameView.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class DynamicTreeTestFrame implements IView<JFrame>
    {
        /**  */
        private final StandardFrameView frameView;
        /**  */
        private final DynamicTreeView<String> treeView;

        /**  */
        private final JPopupMenu nodeMenu;
        /**  */
        private final JMenuItem addMenu;
        /**  */
        private final JMenuItem removeMenu;
        /**  */
        private final JCheckBoxMenuItem allowsChildrenMenu;
        /**  */
        private final JCheckBoxMenuItem isLeafMenu;

        /**  */
        private DynamicNode<String> nodeClicked;

        /**
         * 
         */
        public DynamicTreeTestFrame()
        {
            this.frameView = new StandardFrameView();
            this.treeView = new DynamicTreeView<>();
            this.addMenu = new JMenuItem();
            this.removeMenu = new JMenuItem();
            this.allowsChildrenMenu = new JCheckBoxMenuItem();
            this.isLeafMenu = new JCheckBoxMenuItem();
            this.nodeMenu = createMenu();

            treeView.addRightClickListener(
                ( e, n ) -> nodeRightClicked( e, n ) );

            frameView.setContent( treeView.getView() );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 600, 600 );
            frameView.setTitle( "Dynamic Tree Test" );
        }

        /**
         * @return
         */
        private JPopupMenu createMenu()
        {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem item;

            item = addMenu;
            item.setIcon( IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
            item.setText( "Add" );
            item.addActionListener( ( e ) -> addNode() );
            menu.add( item );

            item = removeMenu;
            item.setIcon(
                IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );
            item.setText( "Remove" );
            item.addActionListener( ( e ) -> removeNode() );
            menu.add( item );

            menu.addSeparator();

            item = allowsChildrenMenu;
            item.setText( "Allows Children" );
            item.addActionListener( (
                e ) -> nodeClicked.allowsChildren = allowsChildrenMenu.isSelected() );
            menu.add( item );

            item = isLeafMenu;
            item.setText( "Is Leaf" );
            item.addActionListener(
                ( e ) -> nodeClicked.isLeaf = isLeafMenu.isSelected() );
            menu.add( item );

            return menu;
        }

        private void addNode()
        {
            DynamicNode<String> parent = nodeClicked;
            nodeClicked = null;

            StringLengthParser parser = new StringLengthParser( 3, null );

            String name = OptionUtils.promptForValue( getView(), "Node Name",
                parser, "Enter the new name:" );

            DynamicNode<String> child = treeView.addChild( parent, name );

            treeView.setSelected( child );
            treeView.tree.expandPath(
                new TreePath( treeView.model.getPathToRoot( parent ) ) );
        }

        private void removeNode()
        {
            DynamicNode<String> child = nodeClicked;
            nodeClicked = null;

            treeView.removeChild( child );
        }

        /**
         * @param evt
         * @param node
         */
        private void nodeRightClicked( MouseEvent evt,
            DynamicNode<String> node )
        {
            this.nodeClicked = node;

            Component comp = evt.getComponent();
            Point p = evt.getPoint();

            addMenu.setEnabled( node.getAllowsChildren() );
            allowsChildrenMenu.setSelected( node.allowsChildren );
            isLeafMenu.setSelected( node.isLeaf );

            nodeMenu.show( comp, p.x, p.y );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame getView()
        {
            return frameView.getView();
        }
    }

    /***************************************************************************
     * @param <T> the type of item found in each node of the tree.
     **************************************************************************/
    private static final class DynamicTreeView<T> implements IView<JComponent>
    {
        /**  */
        private final JScrollPane view;
        /**  */
        private final JTree tree;
        /**  */
        private final DefaultTreeModel model;
        /**  */
        private final List<INodeRightClickListener<T>> rightClickListeners;

        /**
         * @param root
         */
        public DynamicTreeView()
        {
            this( new DynamicNode<>( null ) );
        }

        /**
         * @param root
         */
        public DynamicTreeView( DynamicNode<T> root )
        {
            this.model = new DefaultTreeModel( root );
            this.tree = new JTree( model );
            this.view = new JScrollPane( tree );

            this.rightClickListeners = new ArrayList<>();

            this.tree.addMouseListener(
                new RightClickListener( ( e ) -> handleRightClick( e ) ) );
        }

        /**
         * @param node
         */
        public void setSelected( DynamicNode<String> node )
        {
            TreePath path = SwingUtils.getPath( node );
            int row = tree.getRowForPath( path );

            if( row > -1 )
            {
                tree.setSelectionRow( row );
            }
        }

        /**
         * @param e
         */
        private void handleRightClick( MouseEvent e )
        {
            int row = tree.getClosestRowForLocation( e.getX(), e.getY() );

            if( row > -1 )
            {
                tree.setSelectionRow( row );
                TreePath path = tree.getPathForRow( row );

                @SuppressWarnings( "unchecked")
                DynamicNode<T> node = ( DynamicNode<T> )path.getLastPathComponent();
                rightClickListeners.forEach(
                    ( l ) -> l.nodeClicked( e, node ) );
            }
        }

        /**
         * @param listener
         */
        public void addRightClickListener( INodeRightClickListener<T> listener )
        {
            rightClickListeners.add( listener );
        }

        /**
         * @param parent
         * @param childItem
         * @return
         */
        public DynamicNode<T> addChild( DynamicNode<T> parent, T childItem )
        {
            DynamicNode<T> child = parent.addChild( childItem );
            // int index = parent.getChildCount() - 1;
            // int[] indeces = new int[] { index };

            // model.insertNodeInto( child, parent, index );
            // model.nodesWereInserted( parent, indeces );
            model.nodeStructureChanged( parent );
            // model.reload( parent );
            // model.reload( child );

            return child;
        }

        /**
         * @param child
         */
        public void removeChild( DynamicNode<String> child )
        {
            model.removeNodeFromParent( child );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }
    }

    /**
     * @param <T>
     */
    private static interface INodeRightClickListener<T>
    {
        /**
         * @param evt
         * @param node
         */
        public void nodeClicked( MouseEvent evt, DynamicNode<T> node );
    }

    /***************************************************************************
     * @param <T> the type of item found in each node of the tree.
     **************************************************************************/
    private static final class DynamicNode<T> implements MutableTreeNode
    {
        /**  */
        private final DynamicNode<T> parent;
        /**  */
        private final List<DynamicNode<T>> children;

        /**  */
        private boolean allowsChildren;
        /**  */
        private boolean isLeaf;
        /**  */
        private T item;

        /**
         * @param item
         */
        public DynamicNode( T item )
        {
            this( item, null );
        }

        /**
         * @param item
         * @param parent
         */
        private DynamicNode( T item, DynamicNode<T> parent )
        {
            this.parent = parent;
            this.children = new ArrayList<>();
            this.allowsChildren = true;
            this.isLeaf = false;
            this.item = item;
        }

        /**
         * @param item
         * @return
         * @throws IllegalStateException if the node does not support children.
         */
        public DynamicNode<T> addChild( T item ) throws IllegalStateException
        {
            DynamicNode<T> child = null;

            if( allowsChildren )
            {
                child = new DynamicNode<>( item, this );

                children.add( child );

                isLeaf = false;

                return child;
            }

            throw new IllegalStateException(
                "Unable to add a child to a node that doesn't allow children" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TreeNode getChildAt( int childIndex )
        {
            return children.get( childIndex );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getChildCount()
        {
            return children.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TreeNode getParent()
        {
            return parent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndex( TreeNode node )
        {
            return children.indexOf( node );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getAllowsChildren()
        {
            return allowsChildren;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isLeaf()
        {
            return isLeaf;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Enumeration<? extends TreeNode> children()
        {
            return new IteratorEnumerationAdapter<>( children.iterator() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return "" + item;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insert( MutableTreeNode child, int index )
        {
            @SuppressWarnings( "unchecked")
            DynamicNode<T> dynamicChild = ( DynamicNode<T> )child;
            children.add( index, dynamicChild );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove( int index )
        {
            children.remove( index );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove( MutableTreeNode child )
        {
            @SuppressWarnings( "unchecked")
            DynamicNode<T> dynamicChild = ( DynamicNode<T> )child;

            children.remove( dynamicChild );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setUserObject( Object object )
        {
            @SuppressWarnings( "unchecked")
            T item = ( T )object;

            this.item = item;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeFromParent()
        {
            parent.remove( this );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setParent( MutableTreeNode newParent )
        {
            throw new UnsupportedOperationException(
                getClass().getName() + " are immutable wrt. parent nodes" );
        }
    }
}
