package org.jutils.apps.jexplorer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.jutils.*;
import org.jutils.io.FileComparator;
import org.jutils.ui.*;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.explorer.*;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Frame that displays the contents of the file system in a explorer like
 * interface.
 ******************************************************************************/
public class JExplorerFrame implements IView<JFrame>
{
    /**  */
    private static final String NEXT_TIP = "You must go back before going forward";
    /**  */
    private static final String PREVIOUS_TIP = "You must go somewhere in order to go back";
    /**  */
    private static final String UP_TIP = "Go to parent directory";

    /**  */
    private final StandardFrameView view;

    /** The text field containing the path of the current directory. */
    private final JTextField addressField = new JTextField();
    /** The file tree displaying the directories in the given file system. */
    private final DirectoryTree dirTree = new DirectoryTree();
    /** The scroll pane for the file tree. */
    private final JScrollPane treeScrollPane = new JScrollPane(
        dirTree.getView() );

    /**
     * The file table displaying all the files and folder for the current
     * directory.
     */
    private final ExplorerTable fileTable = new ExplorerTable();

    /** The scrollpane for the file table. */
    private final JScrollPane tableScrollPane = new JScrollPane( fileTable );

    /**
     * The split pane containing the file tree on the left and the file table on
     * the right.
     */
    private final JSplitPane splitPane = new JSplitPane();

    /**  */
    private final ActionAdapter prevAction;
    /**  */
    private final ActionAdapter nextAction;
    /**  */
    private final ActionAdapter upAction;

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

    /**
     * The list of directories forward (in history) from the current directory.
     */
    private LinkedList<File> nextDirs = new LinkedList<File>();

    /***************************************************************************
     * Creates a JExplorer frame.
     **************************************************************************/
    public JExplorerFrame()
    {
        this.view = new StandardFrameView();
        this.prevAction = new ActionAdapter( ( e ) -> goPreviousDirectory(),
            "Previous", IconConstants.loader.getIcon( IconConstants.BACK_24 ) );
        this.nextAction = new ActionAdapter( ( e ) -> goNextDirectory(), "Next",
            IconConstants.loader.getIcon( IconConstants.FORWARD_24 ) );
        this.upAction = new ActionAdapter( ( e ) -> goDirectoryUp(),
            "Up a Directory",
            IconConstants.loader.getIcon( IconConstants.UP_24 ) );

        TreeSelectionListener dirTreeSelListener = ( e ) -> dirTreeChanged();
        MouseListener dirTreeMouseListener = new DirTreeMouseListener( this );
        MouseListener fileTableMouseListener = new FileTableMouseListener(
            this );

        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "JExplorer" );
        frame.setIconImages( IconConstants.loader.getImages(
            IconConstants.OPEN_FOLDER_16, IconConstants.OPEN_FOLDER_32 ) );
        frame.setSize( new Dimension( 600, 450 ) );

        createMenubar( view.getMenuBar() );
        view.setToolbar( createToolbar() );

        view.setContent( createContent() );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------

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

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------

        splitPane.setDividerLocation( 150 );
    }

    /***************************************************************************
     * @param menuBar
     * @param fileMenus
     **************************************************************************/
    private void createMenubar( JMenuBar menubar )
    {
        menubar.add( createToolsMenu() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        ActionListener optionsMenuItemListener = ( e ) -> showOptions();
        JMenu toolsMenu = new JMenu();
        JMenuItem optionsMenuItem = new JMenuItem();

        toolsMenu.setText( "Tools" );
        optionsMenuItem.setText( "Options" );
        optionsMenuItem.setIcon(
            IconConstants.loader.getIcon( IconConstants.CONFIG_16 ) );
        // optionsMenuItem.setIcon( IconLoader.getIcon( IconLoader.EDIT_16 ) );
        optionsMenuItem.addActionListener( optionsMenuItemListener );
        toolsMenu.add( optionsMenuItem );

        return toolsMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, prevAction );
        SwingUtils.setActionToolTip( prevAction, PREVIOUS_TIP );

        SwingUtils.addActionToToolbar( toolbar, nextAction );
        SwingUtils.setActionToolTip( nextAction, NEXT_TIP );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, upAction );
        SwingUtils.setActionToolTip( upAction, UP_TIP );

        toolbar.addSeparator();

        Action refreshAction = new ActionAdapter( ( e ) -> refreshDirectory(),
            "Refresh",
            IconConstants.loader.getIcon( IconConstants.REFRESH_24 ) );
        SwingUtils.addActionToToolbar( toolbar, refreshAction );
        SwingUtils.setActionToolTip( upAction,
            "Refresh the current directory" );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        panel.add( createAddressPanel(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        panel.add( splitPane,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createAddressPanel()
    {
        ActionListener addressFieldListener = ( e ) -> doAddress();
        JPanel panel = new JPanel( new GridBagLayout() );

        JLabel addressLabel = new JLabel( "Address: " );
        addressField.setText( "" );
        addressField.addActionListener( addressFieldListener );

        panel.add( addressLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( addressField,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        return panel;
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
            view.setStatusText(
                this.fileTable.getExplorerTableModel().getRowCount() +
                    " items." );
        }

        upAction.setEnabled( parent != null );
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
        File [] children = dir.listFiles();

        getView().setTitle( dir.getName() + " - JExplorer" );

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
            JOptionPane.showMessageDialog(
                getView(), "User does not have permissions to view: " +
                    Utils.NEW_LINE + dir.getAbsolutePath(),
                "ERROR", JOptionPane.ERROR_MESSAGE );
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
        File lastFile = lastDirs.peekFirst();

        if( lastDirectory != null )
        {
            if( lastFile == null || !lastFile.equals( lastDirectory ) )
            {
                lastDirs.push( lastDirectory );
            }
            nextDirs.clear();

            nextAction.setEnabled( false );
            prevAction.setEnabled( true );
            SwingUtils.setActionToolTip( prevAction,
                lastDirectory.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * Callback listener invoked when the next button is clicked.
     * @param e The ignored (can be null) ActionEvent that occurred.
     **************************************************************************/
    public void goNextDirectory()
    {
        File file = nextDirs.pollFirst();
        if( file != null )
        {
            lastDirs.push( currentDirectory );

            prevAction.setEnabled( true );
            SwingUtils.setActionToolTip( prevAction,
                currentDirectory.getAbsolutePath() );
            nextAction.setEnabled( !nextDirs.isEmpty() );
            SwingUtils.setActionToolTip( nextAction,
                currentDirectory.getAbsolutePath() );

            setDirectory( file );

            file = nextDirs.peekFirst();
            if( file != null )
            {
                SwingUtils.setActionToolTip( nextAction,
                    file.getAbsolutePath() );
            }
            else
            {
                SwingUtils.setActionToolTip( nextAction, NEXT_TIP );
            }
        }
    }

    public void goPreviousDirectory()
    {
        File file = lastDirs.pollFirst();
        if( file != null )
        {
            nextDirs.push( currentDirectory );

            prevAction.setEnabled( !lastDirs.isEmpty() );
            nextAction.setEnabled( true );
            SwingUtils.setActionToolTip( nextAction,
                currentDirectory.getAbsolutePath() );

            setDirectory( file );

            file = lastDirs.peekFirst();
            if( file != null )
            {
                SwingUtils.setActionToolTip( prevAction,
                    file.getAbsolutePath() );
            }
            else
            {
                SwingUtils.setActionToolTip( prevAction, PREVIOUS_TIP );
            }
        }
    }

    public void showOptions()
    {
        FileConfigurationDialog dialog = FileConfigurationDialog.showDialog(
            getView() );
        dialog.getClass();
    }

    private void doAddress()
    {
        String addy = getAddress();
        File file = new File( addy );

        if( file.isDirectory() )
        {
            setDirectory( file );
        }
        else
        {
            JOptionPane.showMessageDialog( getView(),
                file.getAbsolutePath() + " is not a directory!", "ERROR",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * Callback listener invoked when the file tree has been selected or
     * deselected by either the user or programmatically.
     **************************************************************************/
    private void dirTreeChanged()
    {
        File [] dirsSelected = dirTree.getSelected();
        if( dirsSelected.length == 1 )
        {
            File f = dirsSelected[dirsSelected.length - 1];
            setDirectory( f, false );
        }
    }

    /***************************************************************************
     * Callback listener invoked when the up button is pressed.
     * @param e The ignored (can be null) ActionEvent that occurred.
     **************************************************************************/
    public void goDirectoryUp()
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
    public void refreshDirectory()
    {
        // TODO Refresh the view.
        ;
    }

    /***************************************************************************
     * Callback listener invoked when any button of the mouse is clicked while
     * the cursor is above the directory tree.
     **************************************************************************/
    public void doTreeDirClicked()
    {
        File [] dirsSelected = dirTree.getSelected();

        if( dirsSelected.length > 0 )
        {
            addLastFile();
        }
    }

    @Override
    public JFrame getView()
    {
        return view.getView();
    }

    private static class DirTreeMouseListener extends MouseAdapter
    {
        private final JExplorerFrame view;

        public DirTreeMouseListener( JExplorerFrame adaptee )
        {
            this.view = adaptee;
        }

        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 1 )
            {
                view.doTreeDirClicked();
            }
        }
    }

    private static class FileTableMouseListener extends MouseAdapter
    {
        private final JExplorerFrame view;

        public FileTableMouseListener( JExplorerFrame adaptee )
        {
            this.view = adaptee;
        }

        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 )
            {
                view.doFileDoubleClick();
            }
        }
    }

    public void doFileDoubleClick()
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
                    JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                        "ERROR", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }
}
