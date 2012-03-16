package nmrc.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import nmrc.NmrIconLoader;
import nmrc.data.NmrcUserPrefs;
import nmrc.model.INmrData;
import nmrc.ui.panels.*;

import org.jutils.IconConstants;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class NmrConnectFrame extends UFrame
{
    // -------------------------------------------------------------------------
    // Widgets.
    // -------------------------------------------------------------------------
    /**  */
    private RecordsPanel recordsPanel;
    /**  */
    private PeaksPanel peaksPanel;
    /**  */
    private ShifxPanel shiftxPanel;
    /**  */
    private AminoAcidsPanel aasPanel;
    /**  */
    private JButton saveButton;

    // -------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------
    /**  */
    private INmrData nmrData;
    /**  */
    private NmrcUserPrefs userPrefs;
    /**  */
    private ItemActionList<File> newListeners;
    /**  */
    private ItemActionList<File> openListeners;
    /**  */
    private ItemActionList<File> saveListeners;

    /***************************************************************************
     * @param prefs
     **************************************************************************/
    public NmrConnectFrame( NmrcUserPrefs prefs )
    {
        userPrefs = prefs;

        newListeners = new ItemActionList<File>();
        openListeners = new ItemActionList<File>();
        saveListeners = new ItemActionList<File>();

        ActionListener newListener = new NewListener();
        ActionListener openListener = new OpenListener();
        ActionListener saveListener = new SaveListener();

        // ---------------------------------------------------------------------
        // Setup menu bar.
        // ---------------------------------------------------------------------
        JToolBar toolbar = createToolbar( newListener, openListener,
            saveListener );

        // ---------------------------------------------------------------------
        // Setup tab pane
        // ---------------------------------------------------------------------
        JTabbedPane mainTabbedPane = new JTabbedPane();

        recordsPanel = new RecordsPanel();
        peaksPanel = new PeaksPanel();
        shiftxPanel = new ShifxPanel();
        aasPanel = new AminoAcidsPanel( prefs );

        mainTabbedPane.addTab( "Records", recordsPanel );
        mainTabbedPane.addTab( "Peaks", peaksPanel );
        mainTabbedPane.addTab( "Amino Acids", aasPanel );
        mainTabbedPane.addTab( "Shift-X", shiftxPanel );

        // ---------------------------------------------------------------------
        // Setup content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.add( toolbar, BorderLayout.NORTH );
        contentPanel.add( mainTabbedPane, BorderLayout.CENTER );
        contentPanel.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        // ---------------------------------------------------------------------
        // Setup this frame.
        // ---------------------------------------------------------------------
        setContentPane( contentPanel );
        setTitle( "Connect The Dots (CTD)" );
        setJMenuBar( createMenuBar() );

        NmrIconLoader iconLoader = new NmrIconLoader();
        setIconImages( iconLoader.getNmrImages() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new UMenuBar();

        JMenu fileMenu = new JMenu( "File" );
        JMenuItem exitMenuItem = new JMenuItem( "Exit",
            IconConstants.getIcon( IconConstants.CLOSE_16 ) );

        exitMenuItem.addActionListener( new ExitListener() );

        fileMenu.add( exitMenuItem );

        menubar.add( fileMenu );

        return menubar;
    }

    /***************************************************************************
     * @param openListener
     * @param saveListener
     * @return
     **************************************************************************/
    private JToolBar createToolbar( ActionListener newListener,
        ActionListener openListener, ActionListener saveListener )
    {
        JToolBar toolbar = new UToolBar();

        JButton newButton = new JButton(
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        JButton openButton = new JButton(
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        saveButton = new JButton( IconConstants.getIcon( IconConstants.SAVE_16 ) );

        newButton.addActionListener( newListener );
        newButton.setFocusable( false );
        newButton.setToolTipText( "Open a file of peaks" );

        openButton.addActionListener( openListener );
        openButton.setFocusable( false );
        openButton.setToolTipText( "Open a file of peaks" );

        saveButton.addActionListener( saveListener );
        saveButton.setFocusable( false );
        saveButton.setEnabled( false );
        saveButton.setToolTipText( "Save a file of peaks" );

        toolbar.add( newButton );
        toolbar.add( openButton );
        toolbar.add( saveButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    /***************************************************************************
     * @param data INmrData
     **************************************************************************/
    public void setData( INmrData data )
    {
        nmrData = data;

        recordsPanel.setData( data );
        peaksPanel.setData( data.getPeaks() );
        shiftxPanel.setData( data.getShiftX() );
        aasPanel.setData( data );

        saveButton.setEnabled( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public INmrData getData()
    {
        return nmrData;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addNewListener( ItemActionListener<File> l )
    {
        newListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addOpenListener( ItemActionListener<File> l )
    {
        openListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSaveListener( ItemActionListener<File> l )
    {
        saveListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public RecordsPanel getRecordsPanel()
    {
        return recordsPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public PeaksPanel getPeaksPanel()
    {
        return peaksPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public AminoAcidsPanel getAminoAcidsPanel()
    {
        return aasPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NewListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            String lastFolder = userPrefs.getDefaultFolder();

            if( lastFolder != null )
            {
                jfc.setCurrentDirectory( new File( lastFolder ) );
            }
            jfc.setDialogTitle( "Select Peaks File" );
            jfc.setFileFilter( new FileNameExtensionFilter(
                "Peaks Files (*.pks)", "pks" ) );
            jfc.setAcceptAllFileFilterUsed( false );
            int choice = jfc.showOpenDialog( NmrConnectFrame.this );

            File file = jfc.getSelectedFile();

            if( choice == JFileChooser.APPROVE_OPTION && file != null )
            {
                userPrefs.setDefaultFolder( file.getParentFile().getAbsolutePath() );
                newListeners.fireListeners( this, file );
            }
        }
    }

    private class OpenListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            String lastFolder = userPrefs.getDefaultFolder();

            if( lastFolder != null )
            {
                jfc.setCurrentDirectory( new File( lastFolder ) );
            }
            jfc.setDialogTitle( "Select NMR Connect File" );
            jfc.setFileFilter( new FileNameExtensionFilter(
                "NMR Connect Files (*.nmrc)", "nmrc" ) );
            jfc.setAcceptAllFileFilterUsed( false );
            int choice = jfc.showOpenDialog( NmrConnectFrame.this );

            File file = jfc.getSelectedFile();

            if( choice == JFileChooser.APPROVE_OPTION && file != null )
            {
                userPrefs.setDefaultFolder( file.getParentFile().getAbsolutePath() );
                openListeners.fireListeners( this, file );
            }
        }
    }

    private class SaveListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();
            String lastFolder = userPrefs.getDefaultFolder();

            if( lastFolder != null )
            {
                jfc.setCurrentDirectory( new File( lastFolder ) );
            }
            jfc.setFileFilter( new FileNameExtensionFilter(
                "NMR Connect Files (*.nmrc)", "nmrc" ) );
            jfc.setAcceptAllFileFilterUsed( false );
            jfc.setDialogTitle( "Save NMR Connect File" );
            jfc.showSaveDialog( NmrConnectFrame.this );

            File file = jfc.getSelectedFile();

            if( file != null )
            {
                File parent = file.getParentFile();

                if( !file.getName().endsWith( ".nmrc" ) )
                {
                    file = new File( parent, file.getName() + ".nmrc" );
                }

                userPrefs.setDefaultFolder( parent.getAbsolutePath() );
                saveListeners.fireListeners( this, file );
            }
        }
    }
}
