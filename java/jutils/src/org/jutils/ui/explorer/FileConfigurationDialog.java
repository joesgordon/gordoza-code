package org.jutils.ui.explorer;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.IconConstants;
import org.jutils.ui.*;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;

/*******************************************************************************
 * 
 *
 ******************************************************************************/
public class FileConfigurationDialog extends JDialog
{
    /**  */
    private final JCheckBox useCustomCheckBox;
    /**  */
    private final ExtensionsPanel extPanel;

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public FileConfigurationDialog( JFrame parent )
    {
        super( parent, "File Configuration", ModalityType.DOCUMENT_MODAL );

        this.useCustomCheckBox = new JCheckBox();
        this.extPanel = new ExtensionsPanel();

        JPanel contentPanel = ( JPanel )this.getContentPane();

        contentPanel.setLayout( new GridBagLayout() );

        useCustomCheckBox.setText( "Use Custom File Manager" );

        contentPanel.add( useCustomCheckBox,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 10, 20, 10, 10 ), 0, 0 ) );

        contentPanel.add( extPanel,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( FileConfigurationData data )
    {
        useCustomCheckBox.setSelected( data.useCustom() );

        extPanel.setData( data );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        SwingUtilities.invokeLater( new AppRunner( new TestMainApp() ) );
    }

    public static FileConfigurationDialog showDialog( JFrame frame )
    {
        FileConfigurationDialog dialog = new FileConfigurationDialog( frame );
        dialog.setSize( new Dimension( 600, 400 ) );
        dialog.validate();
        dialog.setLocationRelativeTo( frame );
        dialog.setVisible( true );
        return dialog;
    }

    public static FileConfigurationData getUnitTestData()
    {
        FileConfigurationData configData = new FileConfigurationData();
        ProgramData pgm = null;
        ExtensionData ext = null;

        // Create java files and associated programs.

        ext = new ExtensionData( "java", "Java Source File", null );
        configData.addExtension( ext );

        pgm = new ProgramData();
        pgm.setName( "gedit" );
        pgm.setPath( new File( "/usr/bin/gedit" ) );
        pgm.setArguments( "-hoopde" );
        ext.addProgram( pgm );

        // Create txt files and associated programs.

        ext = new ExtensionData( "txt", "Ascii Text File", null );
        configData.addExtension( ext );

        pgm = new ProgramData();
        pgm.setName( "file-roller" );
        pgm.setPath( new File( "/usr/bin/file-roller" ) );
        ext.addProgram( pgm );

        pgm = new ProgramData();
        pgm.setName( "Firefox" );
        pgm.setPath( new File( "/usr/bin/firefox" ) );
        ext.addProgram( pgm );

        return configData;
    }

    private static class TestMainApp implements IApplication
    {
        @Override
        public void createAndShowUi()
        {
            FileConfigurationDialog dialog = new FileConfigurationDialog(
                null );
            dialog.setData( getUnitTestData() );
            dialog.setTitle( "File Config Test" );
            dialog.setSize( new Dimension( 600, 400 ) );
            dialog.validate();
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }
    }
}

class ProgramPanel extends JPanel
{
    private JLabel nameLabel = new JLabel();

    private JTextField pathField = new JTextField();

    private JButton browseButton = new JButton();

    private JTextArea argArea = new JTextArea();

    private JScrollPane argScrollPane = new JScrollPane( argArea );

    public ProgramPanel()
    {
        this.setLayout( new GridBagLayout() );

        this.add( new JLabel( "Name :" ),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        this.add( nameLabel,
            new GridBagConstraints( 1, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( new JLabel( "Path :" ),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        this.add( pathField,
            new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        this.add( browseButton,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( new JLabel( "Extension Specific Arguments" ),
            new GridBagConstraints( 0, 2, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( argScrollPane,
            new GridBagConstraints( 0, 3, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    }
}

class ExtPanel extends JPanel
{
    // -------------------------------------------------------------------------
    // Main panel components.
    // -------------------------------------------------------------------------
    /**  */
    private DefaultListModel<ProgramData> pgmListModel = new DefaultListModel<ProgramData>();

    /**  */
    private JList<ProgramData> pgmList = new JList<ProgramData>( pgmListModel );

    /**  */
    private JScrollPane pgmScrollPane = new JScrollPane( pgmList );

    // -------------------------------------------------------------------------
    // Title panel components.
    // -------------------------------------------------------------------------
    /**  */
    private GradientPanel extPanel = new GradientPanel();

    /**  */
    private JLabel extLabel = new JLabel();

    // -------------------------------------------------------------------------
    // Button panel components.
    // -------------------------------------------------------------------------
    /**  */
    private JPanel buttonPanel = new JPanel();

    /**  */
    private JButton addButton = new JButton();

    /**  */
    private JButton removeButton = new JButton();

    /**  */
    private JButton editButton = new JButton();

    /**  */
    private JButton defaultButton = new JButton();

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtPanel()
    {
        // ---------------------------------------------------------------------
        // Setup the extension panel
        // ---------------------------------------------------------------------
        extPanel.setLayout( new GridBagLayout() );

        extLabel.setText( "Programs" );

        extLabel.setForeground( Color.white );

        extPanel.add( extLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        buttonPanel.setLayout( new GridBagLayout() );

        addButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add a new program" );

        removeButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );
        removeButton.setToolTipText( "Remove an existing program" );

        editButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_16 ) );
        editButton.setToolTipText( "Edit an existing program" );

        defaultButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.CHECK_16 ) );
        defaultButton.setToolTipText( "Make program extension default" );

        buttonPanel.add( addButton,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        buttonPanel.add( removeButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        buttonPanel.add( editButton,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        buttonPanel.add( defaultButton,
            new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 8, 2, 2 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        this.setLayout( new GridBagLayout() );
        this.setBorder( new ShadowBorder() );

        pgmScrollPane.setMinimumSize( new Dimension( 100, 100 ) );
        pgmScrollPane.setPreferredSize( new Dimension( 200, 100 ) );

        this.add( extPanel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        this.add( buttonPanel,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( pgmScrollPane,
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 2, 0, 0, 0 ), 0, 0 ) );
    }

    public void setExtension( ExtensionData ext )
    {
        extLabel.setText( "Programs for " + ext.getExtension() + " files" );
        java.util.List<ProgramData> programs = ext.getPrograms();
        pgmListModel.removeAllElements();
        for( int i = 0; i < programs.size(); i++ )
        {
            pgmListModel.addElement( programs.get( i ) );
        }
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class ExtensionsPanel extends JPanel
{
    private AltSplitPane splitPane = new AltSplitPane();

    private JPanel leftPanel = new JPanel();

    private ExtPanel rightPanel = new ExtPanel();

    private JPanel blankPanel = new JPanel();

    // -------------------------------------------------------------------------
    // Left Panel Components
    // -------------------------------------------------------------------------
    /**  */
    private JButton addButton = new JButton();

    private JButton removeButton = new JButton();

    private DefaultListModel<ExtensionData> extListModel = new DefaultListModel<ExtensionData>();

    private JList<ExtensionData> extList = new JList<ExtensionData>(
        extListModel );

    private JScrollPane extScrollPane = new JScrollPane( extList );

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensionsPanel()
    {
        // ---------------------------------------------------------------------
        // Setup the extension panel
        // ---------------------------------------------------------------------
        GradientPanel titlePanel = new GradientPanel();
        JLabel titleLabel = new JLabel();
        titlePanel.setLayout( new GridBagLayout() );

        titleLabel.setText( "Extensions" );
        titleLabel.setForeground( Color.white );

        titlePanel.add( titleLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup the left panel.
        // ---------------------------------------------------------------------
        leftPanel.setLayout( new GridBagLayout() );
        leftPanel.setBorder( new ShadowBorder() );

        addButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add a new extension" );

        removeButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );
        removeButton.setToolTipText( "Remove an existing extension" );

        extList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        extList.addListSelectionListener( new ListSelectionListener()
        {

            public void valueChanged( ListSelectionEvent e )
            {
                if( !e.getValueIsAdjusting() )
                {
                    int idx = extList.getSelectedIndex();
                    if( idx > -1 )
                    {
                        if( splitPane.getRightComponent() == blankPanel )
                        {
                            splitPane.setRightComponent( rightPanel );
                        }
                        extensionSelected( idx );
                    }
                    else if( splitPane.getRightComponent() == rightPanel )
                    {
                        splitPane.setRightComponent( blankPanel );
                    }
                }
            }
        } );

        extScrollPane.setMinimumSize( new Dimension( 200, 100 ) );
        extScrollPane.setPreferredSize( new Dimension( 200, 100 ) );
        // extScrollPane.setBorder( BorderFactory.createEmptyBorder() );

        leftPanel.add( titlePanel,
            new GridBagConstraints( 0, 0, 3, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        leftPanel.add( addButton,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 2 ), 0, 0 ) );

        leftPanel.add( removeButton,
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 2, 4, 4 ), 0, 0 ) );

        leftPanel.add( extScrollPane,
            new GridBagConstraints( 0, 2, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 0, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup this panel.
        // ---------------------------------------------------------------------
        this.setLayout( new GridBagLayout() );

        splitPane.setBorderless( true );
        splitPane.updateUI();
        splitPane.setLeftComponent( leftPanel );
        splitPane.setRightComponent( blankPanel );

        this.add( splitPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 10, 10, 10, 10 ), 0, 0 ) );
    }

    private void extensionSelected( int idx )
    {
        Object element = extListModel.elementAt( idx );

        rightPanel.setExtension( ( ExtensionData )element );
    }

    public void setData( FileConfigurationData data )
    {
        java.util.List<ExtensionData> extList = data.getExtensions();
        extListModel.removeAllElements();
        for( int i = 0; i < extList.size(); i++ )
        {
            extListModel.addElement( extList.get( i ) );
        }
    }
}
