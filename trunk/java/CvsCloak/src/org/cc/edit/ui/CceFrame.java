package org.cc.edit.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.ByteOrder;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.UndoManager;

import org.cc.data.VersioningSystem;
import org.cc.edit.io.AppModelSerializer;
import org.cc.edit.ui.nodes.AppModelNode;
import org.cc.edit.ui.undo.ButtonLister;
import org.cc.edit.ui.undo.ItemPanel;
import org.jutils.IconConstants;
import org.jutils.io.*;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CceFrame extends JFrame
{
    /**  */
    private JTree tree;
    /**  */
    private JScrollPane rightScrollPane;
    /**  */
    private JScrollPane leftScrollPane;
    /**  */
    private AppModelNode rootNode;
    /**  */
    private ItemPanel<String> undoPopup;
    /**  */
    private ItemPanel<String> redoPopup;
    /**  */
    private final UndoManager manager;
    /**  */
    private VersioningSystem model;

    /***************************************************************************
     * 
     **************************************************************************/
    public CceFrame()
    {
        manager = new UndoManager();

        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.add( createToolBar(), BorderLayout.NORTH );
        contentPanel.add( createMainPanel(), BorderLayout.CENTER );
        contentPanel.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        setJMenuBar( createMenuBar() );
        setTitle( "CvsCloakEdit" );
        setContentPane( contentPanel );
        setSize( 600, 500 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JGoodiesMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        JMenuItem openMenuItem = new JMenuItem( "Open",
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        JMenuItem saveMenuItem = new JMenuItem( "Save",
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        JMenuItem exitMenuItem = new JMenuItem( "Exit",
            IconConstants.loader.getIcon( IconConstants.CLOSE_16 ) );

        openMenuItem.addActionListener( new OpenListener() );
        saveMenuItem.addActionListener( new SaveListener() );
        exitMenuItem.addActionListener( new ExitListener( this ) );

        fileMenu.add( openMenuItem );
        fileMenu.add( saveMenuItem );
        fileMenu.add( exitMenuItem );

        menuBar.add( fileMenu );

        return menuBar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();
        JButton newButton;
        JButton saveButton;
        JButton openButton;
        JButton undoButton;
        JButton redoButton;

        undoPopup = new ItemPanel<String>();
        redoPopup = new ItemPanel<String>();

        for( int i = 0; i < 20; i++ )
        {
            undoPopup.addItem( "Undo Selected " + i );
            redoPopup.addItem( "Redo Selected " + i );
        }

        ItemActionListener<String> doList = new ItemActionListener<String>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<String> event )
            {
                LogUtils.printDebug( event.getItem() + " was selected" );
            }
        };

        undoPopup.addItemSelectedListener( doList );
        redoPopup.addItemSelectedListener( doList );

        newButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        newButton.setToolTipText( "New" );
        newButton.addActionListener( new NewListener() );
        newButton.setFocusable( false );

        saveButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        saveButton.setToolTipText( "Save" );
        saveButton.addActionListener( new SaveListener() );
        saveButton.setFocusable( false );

        openButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        openButton.setToolTipText( "Open" );
        openButton.addActionListener( new OpenListener() );
        openButton.setFocusable( false );

        undoButton = new ButtonLister<String>(
            IconConstants.loader.getIcon( IconConstants.UNDO_16 ), undoPopup );
        // undoButton.setToolTipText( "Undo" );
        undoButton.addActionListener( new DoListener() );
        undoButton.setFocusable( false );

        redoButton = new ButtonLister<String>(
            IconConstants.loader.getIcon( IconConstants.REDO_16 ), redoPopup );
        // redoButton.setToolTipText( "Redo" );
        redoButton.addActionListener( new DoListener() );
        redoButton.setFocusable( false );

        toolbar.add( newButton );
        toolbar.add( openButton );
        toolbar.add( saveButton );
        toolbar.addSeparator();
        toolbar.add( undoButton );
        toolbar.addSeparator( new Dimension( 1, 1 ) );
        toolbar.add( redoButton );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createMainPanel()
    {
        rightScrollPane = new JScrollPane();
        leftScrollPane = new JScrollPane();
        JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
            leftScrollPane, rightScrollPane );

        splitPane.setDividerLocation( 200 );

        return splitPane;
    }

    /***************************************************************************
     * @param model
     **************************************************************************/
    public void setData( VersioningSystem model )
    {
        this.model = model;

        rootNode = new AppModelNode();
        DefaultTreeModel treeModel = new DefaultTreeModel( rootNode );
        rootNode.setData( model, treeModel );
        tree = new JTree( treeModel );

        tree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION );
        tree.addTreeSelectionListener( new NodeSelectionListener() );

        leftScrollPane.setViewportView( tree );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NodeSelectionListener implements TreeSelectionListener
    {
        @Override
        public void valueChanged( TreeSelectionEvent e )
        {
            if( e.isAddedPath() )
            {
                InfoNode<?> node = ( InfoNode<?> )e.getPath().getLastPathComponent();
                Component comp = node.createPanel( manager );
                rightScrollPane.setViewportView( comp );
            }
            else
            {
                rightScrollPane.setViewportView( Box.createVerticalBox() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NewListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            setData( new VersioningSystem() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser fc = new JFileChooser();

            if( fc.showSaveDialog( CceFrame.this ) == JFileChooser.APPROVE_OPTION )
            {
                File file = fc.getSelectedFile();

                try( IStream out = new FileStream( file );
                     IDataStream leout = new DataStream( out ) )
                {
                    AppModelSerializer serializer = new AppModelSerializer();
                    serializer.write( model, leout );
                }
                catch( FileNotFoundException ex )
                {
                    JOptionPane.showMessageDialog( CceFrame.this,
                        "File not found: " + file.getAbsolutePath(), "ERROR",
                        JOptionPane.ERROR_MESSAGE );
                }
                catch( IOException ex )
                {
                    JOptionPane.showMessageDialog( CceFrame.this,
                        ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
            JFileChooser fc = new JFileChooser();

            if( fc.showOpenDialog( CceFrame.this ) == JFileChooser.APPROVE_OPTION )
            {
                File file = fc.getSelectedFile();

                IStream stream = null;

                try( FileStream out = new FileStream( file );
                     DataStream leout = new DataStream( out,
                         ByteOrder.LITTLE_ENDIAN ) )
                {
                    stream = leout;

                    AppModelSerializer serializer = new AppModelSerializer();
                    setData( serializer.read( leout ) );
                }
                catch( RuntimeFormatException ex )
                {
                    String posStr = "????";
                    if( stream != null )
                    {
                        try
                        {
                            long l = stream.getPosition();
                            posStr = String.format( "0x%016X", l );
                        }
                        catch( IOException ex1 )
                        {
                        }
                    }

                    JOptionPane.showMessageDialog( CceFrame.this,
                        ex.getMessage() + " @ 0x" + posStr, "ERROR",
                        JOptionPane.ERROR_MESSAGE );
                }
                catch( FileNotFoundException ex )
                {
                    JOptionPane.showMessageDialog( CceFrame.this,
                        "File not found: " + file.getAbsolutePath(), "ERROR",
                        JOptionPane.ERROR_MESSAGE );
                }
                catch( IOException ex )
                {
                    JOptionPane.showMessageDialog( CceFrame.this,
                        "I/O Error: " + ex.getMessage(), "I/O ERROR",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DoListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }
}
