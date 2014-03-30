package testbed;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jutils.ui.CheckTreeManager;

import com.jgoodies.looks.Options;

public class CheckTreeTest extends JFrame
{
    private JTree checkTree = new JTree();

    private CheckTreeManager checkManager = new CheckTreeManager( checkTree );

    private JScrollPane checkScrollPane = new JScrollPane( checkTree );

    public CheckTreeTest()
    {
        JPanel mainPanel = ( JPanel )this.getContentPane();
        mainPanel.setLayout( new GridBagLayout() );

        mainPanel.add( checkScrollPane, new GridBagConstraints( 0, 0, 1, 1,
            1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 10, 10, 10 ), 0, 0 ) );

        this.setTitle( "Check Tree Test" );
        this.setSize( 200, 300 );
    }

    public void selectSomething()
    {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();
        TreePath[] treePaths = null;
        DefaultMutableTreeNode root = ( DefaultMutableTreeNode )checkTree.getModel().getRoot();
        DefaultMutableTreeNode node = null;

        node = ( DefaultMutableTreeNode )root.getChildAt( 2 ).getChildAt( 2 );
        paths.add( new TreePath( node.getPath() ) );

        node = ( DefaultMutableTreeNode )root.getChildAt( 1 ).getChildAt( 2 );
        paths.add( new TreePath( node.getPath() ) );

        treePaths = paths.toArray( new TreePath[paths.size()] );
        checkManager.getSelectionModel().addSelectionPaths( treePaths );

    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );

                    // UIManager.setLookAndFeel( UIManager.
                    // getCrossPlatformLookAndFeelClassName() );
                }
                catch( Exception ex )
                {
                    ex.printStackTrace();
                }

                CheckTreeTest frame = new CheckTreeTest();
                frame.setLocationRelativeTo( null );
                frame.validate();
                frame.setVisible( true );

                frame.selectSomething();
            }
        } );
    }
}
