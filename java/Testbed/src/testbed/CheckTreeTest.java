package testbed;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jutils.ui.CheckTreeManager;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeTest
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new CheckTreeTestApp() );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class CheckTreeTestApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            CheckTreeTestView view = new CheckTreeTestView();

            return view.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class CheckTreeTestView implements IView<JFrame>
    {
        private final StandardFrameView frame;
        private final JTree checkTree;
        private final CheckTreeManager checkManager;
        private final JScrollPane checkScrollPane;

        public CheckTreeTestView()
        {
            this.frame = new StandardFrameView();
            this.checkTree = new JTree();
            this.checkManager = new CheckTreeManager( checkTree );
            this.checkScrollPane = new JScrollPane( checkTree );

            JPanel mainPanel = new JPanel( new GridBagLayout() );

            mainPanel.add( checkScrollPane,
                new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets( 10, 10, 10, 10 ), 0, 0 ) );

            frame.setContent( mainPanel );
            frame.setTitle( "Check Tree Test" );
            frame.setSize( 200, 300 );

            selectSomething();
        }

        private void selectSomething()
        {
            ArrayList<TreePath> paths = new ArrayList<TreePath>();
            TreePath[] treePaths = null;
            DefaultMutableTreeNode root = ( DefaultMutableTreeNode )checkTree.getModel().getRoot();
            DefaultMutableTreeNode node = null;

            node = ( DefaultMutableTreeNode )root.getChildAt( 2 ).getChildAt(
                2 );
            paths.add( new TreePath( node.getPath() ) );

            node = ( DefaultMutableTreeNode )root.getChildAt( 1 ).getChildAt(
                2 );
            paths.add( new TreePath( node.getPath() ) );

            treePaths = paths.toArray( new TreePath[paths.size()] );
            checkManager.getSelectionModel().addSelectionPaths( treePaths );
        }

        @Override
        public JFrame getView()
        {
            return frame.getView();
        }
    }
}
