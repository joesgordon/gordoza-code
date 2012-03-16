package utesting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.jutils.Utils;
import org.jutils.ui.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class BusyFrameTest extends BusyFrame
{
    /**  */
    private DirTree tree = new DirTree();
    /**  */
    private JScrollPane treeScrollPane = new JScrollPane( tree );
    /**  */
    private UTextArea textArea = new UTextArea();
    /**  */
    private JScrollPane textScrollPane = new JScrollPane( textArea );
    /**  */
    private JSplitPane splitPane = new JSplitPane();

    /***************************************************************************
     * 
     **************************************************************************/
    public BusyFrameTest()
    {
        getContentPane().setLayout( new GridBagLayout() );

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setTitle( "Checked Tree Tester" );

        tree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION );
        tree.addTreeSelectionListener( new CheckTreeTest_tree_treeSelectionAdapter(
            this ) );

        textArea.setText( "" );
        textScrollPane.getViewport().setBackground( Color.white );

        splitPane.setLeftComponent( treeScrollPane );
        splitPane.setRightComponent( textScrollPane );

        this.getContentPane().add(
            splitPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                    4, 4, 4, 4 ), 0, 0 ) );

        this.getContentPane().add(
            new StatusBarPanel().getView(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( com.jgoodies.looks.Options.getCrossPlatformLookAndFeelClassName() );
                    // UIManager.setLookAndFeel( UIManager.
                    // getCrossPlatformLookAndFeelClassName() );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                BusyFrameTest frame = new BusyFrameTest();

                frame.setSize( new Dimension( 600, 450 ) );

                // -------------------------------------------------------------
                // Validate frames that have preset sizes. Pack frames that have
                // useful preferred size info, e.g. from their layout.
                // -------------------------------------------------------------
                frame.validate();

                frame.splitPane.setDividerLocation( 150 );
                frame.setLocationRelativeTo( null );

                frame.setVisible( true );
            }
        } );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    public void dirButton_actionPerformed( ActionEvent e )
    {
        FolderDialog fd = new FolderDialog( Utils.getComponentsFrame( this ),
            "Choose Dir(s)", "Select 1 or more directories:" );
        fd.setVisible( true );

        textArea.setText( fd.getSelectedPaths() );
    }

    /***************************************************************************
     * @param buffer
     * @param f
     * @param name
     **************************************************************************/
    private void printFileInfo( StringBuffer buffer, File f, String name )
    {
        if( name != null && name.length() > 0 )
        {
            name += " ";
        }
        else
        {
            name = "";
        }

        if( f != null )
        {
            buffer.append( name + "Filename: " + f.getName() + Utils.NEW_LINE );
            buffer.append( name + "Abs Path: " + f.getAbsolutePath() +
                Utils.NEW_LINE );
            buffer.append( name + "Can Path: " );
            try
            {
                buffer.append( f.getCanonicalPath() + Utils.NEW_LINE );
            }
            catch( IOException ex )
            {
                buffer.append( "ERROR: " + ex.getMessage() + Utils.NEW_LINE );
            }
        }
        else
        {
            buffer.append( name + "Filename: null" + Utils.NEW_LINE );
            buffer.append( name + "Abs Path: null" + Utils.NEW_LINE );
            buffer.append( name + "Can Path: null" + Utils.NEW_LINE );
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    public void tree_valueChanged( TreeSelectionEvent e )
    {
        this.setFrameBusyLater( getGlassPane().isVisible() ^ true,
            "Waiting... Please click Cancel", true, null );

        File[] files = tree.getSelected();
        if( files.length == 1 )
        {
            StringBuffer buffer = new StringBuffer();
            File f = files[0];
            File p = f.getParentFile();
            File da = new File( f.getAbsolutePath() );
            File dap = da.getParentFile();
            File dc = null;
            File dcp = null;

            try
            {
                dc = new File( f.getCanonicalPath() );
                dcp = dc.getParentFile();
            }
            catch( IOException ex )
            {
            }

            printFileInfo( buffer, f, null );
            buffer.append( Utils.NEW_LINE );

            buffer.append( "Properties of the parent:" + Utils.NEW_LINE );
            printFileInfo( buffer, p, null );
            buffer.append( Utils.NEW_LINE );

            buffer.append( "Properties of the file created with the absolute path:" +
                Utils.NEW_LINE );
            printFileInfo( buffer, da, null );
            buffer.append( Utils.NEW_LINE );

            buffer.append( "Properties of the parent of the file created with the absolute path:" +
                Utils.NEW_LINE );
            printFileInfo( buffer, dap, null );
            buffer.append( Utils.NEW_LINE );

            buffer.append( "Properties of the file created with the canonical path:" +
                Utils.NEW_LINE );
            printFileInfo( buffer, dc, null );
            buffer.append( Utils.NEW_LINE );

            buffer.append( "Properties of the parent of the file created with the canonical path:" +
                Utils.NEW_LINE );
            printFileInfo( buffer, dcp, null );

            textArea.setText( buffer.toString() );
        }
        else if( files.length == 0 )
        {
            textArea.setText( "" );
        }
        else
        {
            JOptionPane.showMessageDialog( this, "Invalid number selected!",
                "ERROR:", JOptionPane.ERROR_MESSAGE );
        }
    }
}

class CheckTreeTest_tree_treeSelectionAdapter implements TreeSelectionListener
{
    private BusyFrameTest adaptee;

    CheckTreeTest_tree_treeSelectionAdapter( BusyFrameTest adaptee )
    {
        this.adaptee = adaptee;
    }

    public void valueChanged( TreeSelectionEvent e )
    {
        adaptee.tree_valueChanged( e );
    }
}
