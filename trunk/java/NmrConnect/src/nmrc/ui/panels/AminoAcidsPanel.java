package nmrc.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import nmrc.data.NmrcUserPrefs;
import nmrc.model.IAminoAcid;
import nmrc.model.INmrData;
import nmrc.ui.tables.ItemTable;
import nmrc.ui.tables.models.AminoAcidTableModel;

import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

public class AminoAcidsPanel extends JPanel
{
    /**  */
    private ItemTable<IAminoAcid> table;
    /**  */
    private JButton saveButton;
    /**  */
    private JButton sortButton;
    /**  */
    private JButton assignButton;
    /**  */
    private JButton unassignButton;
    /**  */
    private ItemActionList<File> saveListeners;
    /**  */
    private NmrcUserPrefs userPrefs;

    /**  */
    private ItemActionList<IAminoAcid> assignListeners;
    /**  */
    private ItemActionList<IAminoAcid> unassignListeners;

    /**  */
    private INmrData nmrData;

    /***************************************************************************
     * 
     **************************************************************************/
    public AminoAcidsPanel( NmrcUserPrefs prefs )
    {
        super();

        userPrefs = prefs;

        saveListeners = new ItemActionList<File>();
        assignListeners = new ItemActionList<IAminoAcid>();
        unassignListeners = new ItemActionList<IAminoAcid>();

        ActionListener saveListener = new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                showSaveDialog();
            }
        };

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------
        JToolBar toolbar = new JToolBar();

        saveButton = new JButton( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        sortButton = new JButton(
            IconConstants.getIcon( IconConstants.SORT_DOWN_16 ) );
        assignButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        unassignButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );

        saveButton.setFocusable( false );
        saveButton.setEnabled( false );
        saveButton.setToolTipText( "Save amino acid file" );
        saveButton.addActionListener( saveListener );

        sortButton.setFocusable( false );
        sortButton.setEnabled( false );
        sortButton.setToolTipText( "Assign peaks" );

        assignButton.setFocusable( false );
        assignButton.setEnabled( false );
        assignButton.setToolTipText( "Assign peaks" );
        assignButton.addActionListener( new AssignListener() );

        unassignButton.setFocusable( false );
        unassignButton.setEnabled( false );
        unassignButton.setToolTipText( "Unassign peak" );
        unassignButton.addActionListener( new UnassignListener() );

        toolbar.add( saveButton );
        toolbar.add( sortButton );
        toolbar.add( assignButton );
        toolbar.add( unassignButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        // ---------------------------------------------------------------------
        // Setup records scroll pane.
        // ---------------------------------------------------------------------
        table = new ItemTable<IAminoAcid>( new AminoAcidTableModel() );
        JScrollPane recordsScrollPane = new JScrollPane( table );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------
        setLayout( new BorderLayout() );

        add( toolbar, BorderLayout.NORTH );
        add( recordsScrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param aminoAcids
     **************************************************************************/
    public void setData( INmrData data )
    {
        nmrData = data;
        table.getTableModel().setItems( data.getAminoAcids() );

        saveButton.setEnabled( true );
        sortButton.setEnabled( true );
        assignButton.setEnabled( true );
        unassignButton.setEnabled( true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public INmrData getData()
    {
        return nmrData;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSaveListener( ItemActionListener<File> l )
    {
        saveListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSortListener( ActionListener l )
    {
        sortButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addAssignListener( ItemActionListener<IAminoAcid> l )
    {
        assignListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addUnassignListener( ItemActionListener<IAminoAcid> l )
    {
        unassignListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showSaveDialog()
    {
        JFileChooser jfc = new JFileChooser();
        String lastFolder = userPrefs.getDefaultFolder();

        if( lastFolder != null )
        {
            jfc.setCurrentDirectory( new File( lastFolder ) );
        }
        jfc.setFileFilter( new FileNameExtensionFilter( "CSV Files", "csv" ) );
        jfc.setAcceptAllFileFilterUsed( false );
        jfc.setDialogTitle( "Save CSV File" );
        jfc.showSaveDialog( this );

        File file = jfc.getSelectedFile();

        if( file != null )
        {
            File parent = file.getParentFile();

            if( !file.getName().endsWith( ".csv" ) )
            {
                file = new File( parent, file.getName() + ".csv" );
            }

            userPrefs.setDefaultFolder( parent.getAbsolutePath() );
            saveListeners.fireListeners( this, file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AssignListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int row = table.getSelectedRow();
            if( row > -1 )
            {
                IAminoAcid aa = table.getTableModel().getRow( row );
                assignListeners.fireListeners( this, aa );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UnassignListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int row = table.getSelectedRow();
            if( row > -1 )
            {
                IAminoAcid aa = table.getTableModel().getRow( row );
                unassignListeners.fireListeners( this, aa );
            }
        }
    }
}
