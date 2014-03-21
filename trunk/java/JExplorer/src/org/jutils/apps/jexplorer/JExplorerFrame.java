package org.jutils.apps.jexplorer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.io.FileComparator;
import org.jutils.ui.*;
import org.jutils.ui.explorer.*;

// TODO Remove inheritance from JFrame.

/*******************************************************************************
 * Frame that displays the contents of the file system in a explorer like
 * interface.
 ******************************************************************************/
public class JExplorerFrame extends JFrame
{
    /**  */
    private static final String FORWARD_TIP = "You must go back before going forward";

    /**  */
    private static final String BACKWARD_TIP = "You must go somewhere in order to go back";

    // -------------------------------------------------------------------------
    // Menu bar widgets
    // -------------------------------------------------------------------------
    /** The main menu bar for this frame. */
    private JMenuBar menubar = new JGoodiesMenuBar();

    /** Menus containing items normally found under the name 'File'. */
    private JMenu fileMenu = new JMenu();

    /** Allows the user to exit this application. */
    private JMenuItem exitMenuItem = new JMenuItem();

    /** Menu that displays the tools of this application. */
    private JMenu toolsMenu = new JMenu();

    /** Allows the user to view and edit the options of this application. */
    private JMenuItem optionsMenuItem = new JMenuItem();

    // -------------------------------------------------------------------------
    // Toolbar widgets
    // -------------------------------------------------------------------------
    /** The main toolbar for this frame. */
    private JToolBar toolbar = new JGoodiesToolBar();

    /** Allows the user to navigate to past shown directories. */
    private JButton backButton = new JButton();

    /**
     * Allows the user to navigate to future shown directories (only works if
     * the user has previously navigated backwards).
     */
    private JButton nextButton = new JButton();

    /** Allows the user to navigate to the current directories parent. */
    private JButton upButton = new JButton();

    /**
     * Allows the user to refresh the current directory in both the file table
     * and file tree.
     */
    private JButton refreshButton = new JButton();

    // -------------------------------------------------------------------------
    // Address panel.
    // -------------------------------------------------------------------------
    /** The address panel. */
    private JPanel addressPanel = new JPanel();

    /** The label for the address field. */
    private JLabel addressLabel = new JLabel();

    /** The text field containing the path of the current directory. */
    private JTextField addressField = new JTextField();

    // -------------------------------------------------------------------------
    // Main panel widgets
    // -------------------------------------------------------------------------
    /** The main panel of this frame. */
    private JPanel mainPanel = new JPanel();

    /** The file tree displaying the directories in the given file system. */
    private DirectoryTree dirTree = new DirectoryTree();

    /** The scroll pane for the file tree. */
    private JScrollPane treeScrollPane = new JScrollPane( dirTree.getView() );

    /**
     * The file table displaying all the files and folder for the current
     * directory.
     */
    private ExplorerTable fileTable = new ExplorerTable();

    /** The scrollpane for the file table. */
    private JScrollPane tableScrollPane = new JScrollPane( fileTable );

    /**
     * The split pane containing the file tree on the left and the file table on
     * the right.
     */
    private JSplitPane splitPane = new JSplitPane();

    /** The panel at the bottom of the frame. */
    private StatusBarPanel statusPanel = new StatusBarPanel();

    // -------------------------------------------------------------------------
    // Supporting data.
    // -------------------------------------------------------------------------
    /** The directory currently displayed. */
    private File currentDirectory = null;

    /** The directory currently displayed. */
    private File lastDirectory = null;

    /**
     * The list of directories backward (in history) from the current directory.
     */
    private LinkedList<File> lastDirs = new LinkedList<File>();

    /** The list of directories forward (in history) from the current directory. */
    private LinkedList<File> nextDirs = new LinkedList<File>();

    /***************************************************************************
     * Creates a JExplorer frame.
     **************************************************************************/
    public JExplorerFrame()
    {
        ActionListener upButtonListener = new JExplorer_upButton_actionAdapter(
            this );
        ActionListener nextButtonListener = new JExplorer_nextButton_actionAdapter(
            this );
        ActionListener backButtonListener = new JExplorer_backButton_actionAdapter(
            this );
        ActionListener optionsMenuItemListener = new JExplorer_optionsMenuItem_actionAdapter(
            this );
        ActionListener addressFieldListener = new JExplorer_addressField_actionAdapter(
            this );
        TreeSelectionListener dirTreeSelListener = new JExplorer_dirTree_SelectionAdapter(
            this );
        MouseListener dirTreeMouseListener = new JExplorer_dirTree_mouseAdapter(
            this );
        MouseListener fileTableMouseListener = new JExplorer_fileTable_mouseAdapter(
            this );

        // ----------------------------------------------------------------------
        // Setup menu bar
        // ----------------------------------------------------------------------
        fileMenu.setText( "File" );

        exitMenuItem.setText( "Exit" );
        exitMenuItem.addActionListener( new ExitListener( this ) );
        exitMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.CLOSE_16 ) );
        fileMenu.add( exitMenuItem );

        toolsMenu.setText( "Tools" );
        optionsMenuItem.setText( "Options" );
        optionsMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.CONFIG_16 ) );
        // optionsMenuItem.setIcon( IconLoader.getIcon( IconLoader.EDIT_16 ) );
        optionsMenuItem.addActionListener( optionsMenuItemListener );
        toolsMenu.add( optionsMenuItem );

        menubar.add( fileMenu );
        menubar.add( toolsMenu );

        this.setJMenuBar( menubar );

        // ---------------------------------------------------------------------
        // Setup tool bar
        // ---------------------------------------------------------------------
        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        backButton.setText( "" );
        backButton.setToolTipText( BACKWARD_TIP );
        backButton.setIcon( IconConstants.loader.getIcon( IconConstants.BACK_24 ) );
        backButton.setFocusable( false );
        backButton.setEnabled( false );
        backButton.addActionListener( backButtonListener );

        nextButton.setText( "" );
        nextButton.setToolTipText( FORWARD_TIP );
        nextButton.setIcon( IconConstants.loader.getIcon( IconConstants.FORWARD_24 ) );
        nextButton.setFocusable( false );
        nextButton.setEnabled( false );
        nextButton.addActionListener( nextButtonListener );

        upButton.setText( "" );
        upButton.setToolTipText( "Go to parent directory" );
        upButton.setIcon( IconConstants.loader.getIcon( IconConstants.UP_24 ) );
        upButton.setFocusable( false );
        upButton.addActionListener( upButtonListener );

        refreshButton.setText( "" );
        refreshButton.setToolTipText( "Refresh the current directory" );
        refreshButton.setIcon( IconConstants.loader.getIcon( IconConstants.REFRESH_24 ) );
        refreshButton.setFocusable( false );

        toolbar.add( backButton );
        toolbar.add( nextButton );
        toolbar.addSeparator();
        toolbar.add( upButton );
        toolbar.addSeparator();
        toolbar.add( refreshButton );

        // ---------------------------------------------------------------------
        // Setup address panel.
        // ---------------------------------------------------------------------
        addressPanel.setLayout( new GridBagLayout() );

        addressLabel.setText( "Address: " );
        addressField.setText( "" );
        addressField.addActionListener( addressFieldListener );

        addressPanel.add( addressLabel, new GridBagConstraints( 0, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        addressPanel.add( addressField, new GridBagConstraints( 1, 0, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        mainPanel.setLayout( new GridBagLayout() );

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setTitle( "JExplorer" );

        dirTree.getView().getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION );
        dirTree.getView().addTreeSelectionListener( dirTreeSelListener );
        dirTree.getView().addMouseListener( dirTreeMouseListener );

        fileTable.setAutoCreateRowSorter( true );
        fileTable.setBackground( Color.white );
        fileTable.addMouseListener( fileTableMouseListener );
        tableScrollPane.getViewport().setBackground( Color.white );

        splitPane.setLeftComponent( treeScrollPane );
        splitPane.setRightComponent( tableScrollPane );

        mainPanel.add( addressPanel, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        mainPanel.add( splitPane, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        this.getContentPane().setLayout( new BorderLayout() );

        this.getContentPane().add( toolbar, BorderLayout.NORTH );
        this.getContentPane().add( mainPanel, BorderLayout.CENTER );
        this.getContentPane().add( statusPanel.getView(), BorderLayout.SOUTH );

        setIconImages( IconConstants.loader.getImages(
            IconConstants.OPEN_FOLDER_16, IconConstants.OPEN_FOLDER_32 ) );

        setSize( new Dimension( 600, 450 ) );
        splitPane.setDividerLocation( 150 );
    }

    /***************************************************************************
     * Sets the directory shown in this frame.
     * @param dir The directory to be shown.
     **************************************************************************/
    public void setDirectory( File dir )
    {
        setDirectory( dir, true );
    }

    /***************************************************************************
     * Sets the current directory either by setting the selected file in the
     * tree (which sets the files in the table) or showing the files contents in
     * the table.
     * @param dir The directory to be shown.
     * @param setTree Displays the folder contents by setting the selected file
     * in the tree (true) or setting the table directly (false).
     **************************************************************************/
    private void setDirectory( File dir, boolean setTree )
    {
        File parent = dir.getParentFile();
        if( currentDirectory != null && !currentDirectory.equals( dir ) )
        {
            lastDirectory = currentDirectory;
        }
        currentDirectory = dir;

        if( dir.isDirectory() )
        {
            if( setTree )
            {
                dirTree.setSelected( new File[] { dir } );
            }
            else
            {
                showDirInTable( dir );
            }
            addressField.setText( dir.getAbsolutePath() );
            statusPanel.setText( this.fileTable.getExplorerTableModel().getRowCount() +
                " items." );
        }

        upButton.setEnabled( parent != null );
    }

    /***************************************************************************
     * Returns the current directory displayed.
     * @return The current directory displayed.
     **************************************************************************/
    public File getDirectory()
    {
        return currentDirectory;
    }

    /***************************************************************************
     * Returns the value of the address field.
     * @return The value of the address field.
     **************************************************************************/
    public String getAddress()
    {
        return addressField.getText();
    }

    /***************************************************************************
     * Displays the given directory's contents in the table.
     * @param f The file to be displayed.
     **************************************************************************/
    private void showDirInTable( File dir )
    {
        ArrayList<DefaultExplorerItem> list = new ArrayList<DefaultExplorerItem>();
        File[] children = dir.listFiles();

        this.setTitle( dir.getName() + " - JExplorer" );

        if( children != null )
        {
            Arrays.sort( children, new FileComparator() );

            for( int i = 0; i < children.length; i++ )
            {
                list.add( new DefaultExplorerItem( children[i] ) );
            }

            fileTable.clearTable();
            fileTable.addFiles( list );
        }
        else
        {
            JOptionPane.showMessageDialog( this,
                "User does not have permissions to view: " + Utils.NEW_LINE +
                    dir.getAbsolutePath(), "ERROR", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * Adds the given file to the history of folders traversed. The file is NOT
     * null checked and is added only if it is not equal to the last file in the
     * history or the current directory.
     * @param file The file to be added.
     **************************************************************************/
    private void addLastFile()
    {
        File lastFile = ( File )lastDirs.peekFirst();

        if( lastDirectory != null )
        {
            if( lastFile == null || !lastFile.equals( lastDirectory ) )
            {
                lastDirs.push( lastDirectory );
            }
            nextDirs.clear();

            nextButton.setEnabled( false );
            backButton.setEnabled( true );
            backButton.setToolTipText( lastDirectory.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * Callback listener invoked when the mouse has clicked on the file table.
     * @param e The MouseEvent (may NOT be null) that occurred.
     **************************************************************************/
    public void listener_fileTable_mouseClicked( MouseEvent e )
    {
        if( e.getClickCount() == 2 )
        {
            File file = fileTable.getSelectedFile();
            if( file != null )
            {
                if( file.isDirectory() )
                {
                    // showFile( file );
                    setDirectory( file );
                    addLastFile();
                }
                else
                {
                    try
                    {
                        Desktop.getDesktop().open( file );
                    }
                    catch( Exception ex )
                    {
                        JOptionPane.showMessageDialog( this, ex.getMessage(),
                            "ERROR", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        }
    }

    /***************************************************************************
     * Callback listener invoked when the next button is clicked.
     * @param e The ignored (can be null) ActionEvent that occurred.
     **************************************************************************/
    public void listener_nextButton_actionPerformed( ActionEvent e )
    {
        File file = ( File )nextDirs.pollFirst();
        if( file != null )
        {
            lastDirs.push( currentDirectory );

            backButton.setEnabled( true );
            backButton.setToolTipText( currentDirectory.getAbsolutePath() );
            nextButton.setEnabled( !nextDirs.isEmpty() );
            backButton.setToolTipText( currentDirectory.getAbsolutePath() );

            setDirectory( file );

            file = ( File )nextDirs.peekFirst();
            if( file != null )
            {
                nextButton.setToolTipText( file.getAbsolutePath() );
            }
            else
            {
                nextButton.setToolTipText( FORWARD_TIP );

            }
        }
    }

    /***************************************************************************
     * Callback listener invoked when the file tree has been selected or
     * deselected by either the user or programatically.
     * @param e The ignored (can be null) TreeSelectionEvent that occurred.
     **************************************************************************/
    public void listener_dirTree_valueChanged( TreeSelectionEvent e )
    {
        File[] dirsSelected = dirTree.getSelected();
        if( dirsSelected != null && dirsSelected.length == 1 )
        {
            File f = dirsSelected[dirsSelected.length - 1];
            setDirectory( f, false );
        }
    }

    /***************************************************************************
     * Callback listener invoked when the up button is pressed.
     * @param e The ignored (can be null) ActionEvent that occurred.
     **************************************************************************/
    public void listener_upButton_actionPerformed( ActionEvent e )
    {
        File parent = getDirectory().getParentFile();
        if( parent != null )
        {
            setDirectory( parent );
            addLastFile();
        }
    }

    /***************************************************************************
     * Callback listener invoked when the refresh button is pressed.
     * @param e The ignored (can be null) ActionEvent that occurred.
     **************************************************************************/
    public void listener_refreshButton_actionPerformed( ActionEvent e )
    {
        // TODO Refresh the view.
        ;
    }

    /***************************************************************************
     * Callback listener invoked when any button of the mouse is clicked while
     * the cursor is above the directory tree.
     * @param e The ignored (can be null) ActionEvent that occurred.
     **************************************************************************/
    public void listener_dirTree_mouseClicked( MouseEvent e )
    {
        File[] dirsSelected = dirTree.getSelected();

        if( dirsSelected != null && dirsSelected.length > 0 )
        {
            addLastFile();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class JExplorer_backButton_actionAdapter implements
        ActionListener
    {
        private JExplorerFrame adaptee;

        public JExplorer_backButton_actionAdapter( JExplorerFrame adaptee )
        {
            this.adaptee = adaptee;
        }

        public void actionPerformed( ActionEvent e )
        {
            File file = ( File )adaptee.lastDirs.pollFirst();
            if( file != null )
            {
                adaptee.nextDirs.push( adaptee.currentDirectory );

                adaptee.backButton.setEnabled( !adaptee.lastDirs.isEmpty() );
                adaptee.nextButton.setEnabled( true );
                adaptee.nextButton.setToolTipText( adaptee.currentDirectory.getAbsolutePath() );

                adaptee.setDirectory( file );

                file = ( File )adaptee.lastDirs.peekFirst();
                if( file != null )
                {
                    adaptee.backButton.setToolTipText( file.getAbsolutePath() );
                }
                else
                {
                    adaptee.backButton.setToolTipText( BACKWARD_TIP );
                }
            }
        }
    }
}

class JExplorer_nextButton_actionAdapter implements ActionListener
{
    private JExplorerFrame adaptee;

    public JExplorer_nextButton_actionAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.listener_nextButton_actionPerformed( e );
    }
}

class JExplorer_optionsMenuItem_actionAdapter implements ActionListener
{
    private JExplorerFrame adaptee;

    public JExplorer_optionsMenuItem_actionAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        FileConfigurationDialog dialog = FileConfigurationDialog.showDialog( adaptee );
        dialog.getClass();
    }
}

class JExplorer_addressField_actionAdapter implements ActionListener
{
    private JExplorerFrame adaptee;

    public JExplorer_addressField_actionAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        String addy = adaptee.getAddress();
        File file = new File( addy );

        if( file.isDirectory() )
        {
            adaptee.setDirectory( file );
        }
        else
        {
            JOptionPane.showMessageDialog( adaptee, file.getAbsolutePath() +
                " is not a directory!", "ERROR", JOptionPane.ERROR_MESSAGE );
        }
    }
}

class JExplorer_upButton_actionAdapter implements ActionListener
{
    private JExplorerFrame adaptee;

    public JExplorer_upButton_actionAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.listener_upButton_actionPerformed( e );
    }
}

class JExplorer_refreshButton_actionAdapter implements ActionListener
{
    private JExplorerFrame adaptee;

    public JExplorer_refreshButton_actionAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.listener_refreshButton_actionPerformed( e );
    }
}

class JExplorer_dirTree_SelectionAdapter implements TreeSelectionListener
{
    private JExplorerFrame adaptee;

    public JExplorer_dirTree_SelectionAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void valueChanged( TreeSelectionEvent e )
    {
        adaptee.listener_dirTree_valueChanged( e );
    }
}

class JExplorer_dirTree_mouseAdapter extends MouseAdapter
{
    private JExplorerFrame adaptee;

    public JExplorer_dirTree_mouseAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void mouseClicked( MouseEvent e )
    {
        adaptee.listener_dirTree_mouseClicked( e );
    }
}

class JExplorer_fileTable_mouseAdapter extends MouseAdapter
{
    private JExplorerFrame adaptee;

    public JExplorer_fileTable_mouseAdapter( JExplorerFrame adaptee )
    {
        this.adaptee = adaptee;
    }

    public void mouseClicked( MouseEvent e )
    {
        adaptee.listener_fileTable_mouseClicked( e );
    }
}
