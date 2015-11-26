package org.jutils.apps.filespy.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import org.jutils.*;
import org.jutils.apps.filespy.data.*;
import org.jutils.apps.filespy.search.Searcher;
import org.jutils.io.*;
import org.jutils.ui.*;
import org.jutils.ui.calendar.CalendarField;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.explorer.*;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.ItemComboBoxModel;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchView implements IDataView<SearchParams>
{
    /**  */
    private final JComboBox<String> filenameComboBox;
    /**  */
    private final JCheckBox contentsCheckBox;
    /**  */
    private final JComboBox<String> contentsComboBox;
    /**  */
    private final JButton startButton;
    /**  */
    private final JComboBox<String> searchInComboBox;
    /**  */
    private final JCheckBox subfoldersCheckBox;
    /**  */
    private final JButton browseButton;
    /**  */
    private final JCheckBox moreThanCheckBox;
    /**  */
    private final JTextField moreThanTextField;
    /**  */
    private final JCheckBox lessThanCheckBox;
    /**  */
    private final JTextField lessThanTextField;
    /**  */
    private final JCheckBox afterCheckBox;
    /**  */
    private final CalendarField afterTextField;
    /**  */
    private final JCheckBox beforeCheckBox;
    /**  */
    private final CalendarField beforeTextField;
    /**  */
    private final JCheckBox fileNotCheckBox;
    /**  */
    private final JCheckBox fileRegexCheckBox;
    /**  */
    private final JCheckBox fileMatchCheckBox;
    /**  */
    private final JCheckBox contentRegexCheckBox;
    /**  */
    private final JCheckBox contentMatchCheckBox;
    /**  */
    private final JScrollPane leftResultsScroll;
    /**  */
    private final ExplorerTable resultsTable;
    /**  */
    private final ExplorerTableModel resultsTableModel;
    /**  */
    private final JScrollPane rightResultsScroll;
    /**  */
    private final AltEditorPane rightResultsPane;
    /**  */
    private final DefaultStyledDocument defStyledDocument;
    /**  */
    private final Icon startIcon;
    /**  */
    private final ActionListener browseListener;
    /**  */
    private final ActionListener startListener;
    /**  */
    private final ItemComboBoxModel<String> filenameModel;
    /**  */
    private final ItemComboBoxModel<String> contentsModel;
    /**  */
    private final ItemComboBoxModel<String> searchInModel;
    /**  */
    private final JPanel view;
    /**  */
    private final StatusBarPanel statusBar;

    /**  */
    private final OptionsSerializer<FileSpyData> options;

    /**  */
    private Searcher searcher;

    /***************************************************************************
     *
     **************************************************************************/
    public SearchView( StatusBarPanel statusBar,
        OptionsSerializer<FileSpyData> options )
    {
        this.statusBar = statusBar;
        this.options = options;

        view = new JPanel( new GridBagLayout() );
        filenameComboBox = new JComboBox<String>();
        contentsCheckBox = new JCheckBox();
        startButton = new JButton();
        contentsComboBox = new JComboBox<String>();
        searchInComboBox = new JComboBox<String>();
        browseButton = new JButton();
        subfoldersCheckBox = new JCheckBox();
        moreThanCheckBox = new JCheckBox();
        moreThanTextField = new JTextField();
        lessThanCheckBox = new JCheckBox();
        lessThanTextField = new JTextField();
        afterCheckBox = new JCheckBox();
        afterTextField = new CalendarField();
        beforeCheckBox = new JCheckBox();
        beforeTextField = new CalendarField();
        fileNotCheckBox = new JCheckBox();
        fileRegexCheckBox = new JCheckBox();
        fileMatchCheckBox = new JCheckBox();
        contentRegexCheckBox = new JCheckBox();
        contentMatchCheckBox = new JCheckBox();
        leftResultsScroll = new JScrollPane();
        resultsTable = new ExplorerTable();
        rightResultsScroll = new JScrollPane();
        rightResultsPane = new AltEditorPane();
        defStyledDocument = new DefaultStyledDocument();
        startIcon = IconConstants.loader.getIcon( IconConstants.FIND_32 );
        browseListener = new BrowseButtonListener( this );
        startListener = new StartButtonListener();

        KeyListener enterListener = new StartKeyListener();

        // ---------------------------------------------------------------------
        // Setup search panel
        // ---------------------------------------------------------------------
        FileSpyData configData = options.getOptions();

        filenameModel = new ItemComboBoxModel<String>( configData.filenames );
        contentsModel = new ItemComboBoxModel<String>( configData.contents );
        searchInModel = new ItemComboBoxModel<String>( configData.folders );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        resultsTableModel = resultsTable.getExplorerTableModel();
        // resultsTable.setAutoCreateRowSorter( true );
        resultsTable.getSelectionModel().addListSelectionListener(
            new SearchPanel_listSelectionAdapter( this ) );
        ToolTipManager.sharedInstance().unregisterComponent( resultsTable );
        ToolTipManager.sharedInstance().unregisterComponent(
            resultsTable.getTableHeader() );
        resultsTable.setAutoCreateRowSorter( true );
        resultsTable.setBackground( Color.white );
        resultsTable.addMouseListener( new OpenResultsListener( this ) );

        leftResultsScroll.setViewportView( resultsTable );
        leftResultsScroll.getViewport().setBackground( Color.white );
        leftResultsScroll.setMinimumSize( new Dimension( 150, 150 ) );

        rightResultsScroll.setViewportView( rightResultsPane );
        rightResultsScroll.setMinimumSize( new Dimension( 150, 150 ) );

        rightResultsPane.setDocument( defStyledDocument );
        rightResultsPane.setEditable( false );
        rightResultsPane.setBackground( Color.white );

        JSplitPane resultsPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        resultsPane.setResizeWeight( 0.0 );
        resultsPane.setLeftComponent( leftResultsScroll );
        resultsPane.setRightComponent( rightResultsScroll );
        resultsPane.setDividerLocation( 300 );

        view.add( createSearchPanel( enterListener ),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        view.add( createBrowsePanel( enterListener ),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        view.add( createOptionsPanel(),
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        view.add( resultsPane,
            new GridBagConstraints( 0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        setData( new SearchParams() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createOptionsPanel()
    {
        JPanel optionsPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( createSizePanel(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( createTimePanel(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( createFileOptionsPanel(), constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( createContentOptionsPanel(), constraints );

        constraints = new GridBagConstraints( 4, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( Box.createHorizontalStrut( 0 ), constraints );

        optionsPanel.setMinimumSize( optionsPanel.getPreferredSize() );
        view.setMinimumSize( optionsPanel.getMinimumSize() );

        return optionsPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createSizePanel()
    {
        JPanel sizePanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        sizePanel.setBorder( BorderFactory.createTitledBorder( "Size (kb)" ) );

        moreThanCheckBox.setText( "More Than :" );
        moreThanCheckBox.setToolTipText(
            "Specifies that files should be " + "larger than given." );
        moreThanCheckBox.addActionListener(
            new CheckBoxEnabler_actionAdapter( moreThanTextField ) );
        moreThanTextField.setColumns( 6 );
        moreThanTextField.setToolTipText(
            "The amount the file should be " + "larger than." );
        moreThanTextField.setHorizontalAlignment( JTextField.RIGHT );

        lessThanCheckBox.setText( "Less Than :" );
        lessThanCheckBox.setToolTipText(
            "Specifies that files should be " + "smaller than given." );
        lessThanCheckBox.addActionListener(
            new CheckBoxEnabler_actionAdapter( lessThanTextField ) );
        lessThanTextField.setColumns( 6 );
        lessThanTextField.setToolTipText(
            "The amount the file should be " + "smaller than." );
        lessThanTextField.setHorizontalAlignment( JTextField.RIGHT );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        sizePanel.add(
            createRightSidedPanel( moreThanCheckBox, moreThanTextField ),
            constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        sizePanel.add(
            createRightSidedPanel( lessThanCheckBox, lessThanTextField ),
            constraints );

        constraints = new GridBagConstraints( 0, 2, 3, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        sizePanel.add( Box.createHorizontalStrut( 0 ), constraints );

        sizePanel.setMinimumSize( sizePanel.getPreferredSize() );

        return sizePanel;
    }

    private JPanel createTimePanel()
    {
        JPanel advancedPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        advancedPanel.setBorder(
            BorderFactory.createTitledBorder( "Time Options" ) );

        afterCheckBox.setText( "After :" );
        afterCheckBox.setToolTipText( "Specifies that files should be " +
            "modified after the date given." );
        afterCheckBox.addActionListener(
            new CheckBoxEnabler_actionAdapter( afterTextField.getView() ) );
        afterTextField.setToolTipText(
            "The date the file should be " + "modified after." );

        beforeCheckBox.setText( "Before :" );
        beforeCheckBox.setToolTipText( "Specifies that files should be " +
            "modified before the date given." );
        beforeCheckBox.addActionListener(
            new CheckBoxEnabler_actionAdapter( beforeTextField.getView() ) );
        beforeTextField.setToolTipText(
            "The date the file should be " + "modified before." );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        advancedPanel.add(
            createRightSidedPanel( afterCheckBox, afterTextField.getView() ),
            constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        advancedPanel.add(
            createRightSidedPanel( beforeCheckBox, beforeTextField.getView() ),
            constraints );

        constraints = new GridBagConstraints( 0, 2, 3, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        advancedPanel.add( Box.createHorizontalStrut( 0 ), constraints );

        return advancedPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createFileOptionsPanel()
    {
        JPanel fileOptionsPanel = new JPanel( new GridBagLayout() );

        fileOptionsPanel.setBorder(
            BorderFactory.createTitledBorder( "Filename Options" ) );

        fileRegexCheckBox.setText( "Use Regular Expressions" );
        fileRegexCheckBox.setToolTipText(
            "Specifies to use regular instead " + "of literal expressions." );
        fileMatchCheckBox.setText( "Match Case" );
        fileMatchCheckBox.setToolTipText(
            "Specifies to match the case of " + "the filename search." );
        fileNotCheckBox.setText( "Specify Not Condition" );
        fileNotCheckBox.setToolTipText( "Specifies to return all files " +
            "NOT matched by the filename expression." );

        fileOptionsPanel.add( fileRegexCheckBox,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        fileOptionsPanel.add( fileMatchCheckBox,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        fileOptionsPanel.add( fileNotCheckBox,
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        fileOptionsPanel.add( Box.createHorizontalStrut( 1 ),
            new GridBagConstraints( 0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return fileOptionsPanel;
    }

    private JPanel createContentOptionsPanel()
    {
        JPanel contentOptionsPanel = new JPanel( new GridBagLayout() );
        contentOptionsPanel.setBorder(
            BorderFactory.createTitledBorder( "Content Options" ) );

        contentRegexCheckBox.setText( "Use Regular Expressions" );
        contentRegexCheckBox.setToolTipText(
            "Specifies to use regular " + "instead of literal expressions." );
        contentMatchCheckBox.setText( "Match Case" );
        contentMatchCheckBox.setToolTipText(
            "Specifies to match the case of " + "the filename search." );

        contentOptionsPanel.add( contentRegexCheckBox,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        contentOptionsPanel.add( contentMatchCheckBox,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        contentOptionsPanel.add( Box.createHorizontalStrut( 1 ),
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return contentOptionsPanel;
    }

    /***************************************************************************
     * @param enterListener
     * @return
     **************************************************************************/
    private JPanel createBrowsePanel( KeyListener enterListener )
    {
        JPanel browsePanel = new JPanel();
        GridBagLayout browseLayout = new GridBagLayout();
        JLabel browseLabel = new JLabel();

        browsePanel.setLayout( browseLayout );

        browseLabel.setText( "Search In :" );
        searchInComboBox.setToolTipText(
            "The directory in which the " + "search will be performed." );
        searchInComboBox.setModel( searchInModel );
        searchInComboBox.setEditable( true );
        searchInComboBox.getEditor().getEditorComponent().addKeyListener(
            enterListener );

        subfoldersCheckBox.setText( "Search Subfolders" );
        subfoldersCheckBox.setToolTipText(
            "Signifies that sub folders " + "should be searched." );
        browseButton.setToolTipText( "Choose the directory to be searched." );
        browseButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        browseButton.addActionListener( browseListener );

        browsePanel.add( browseLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        browsePanel.add( searchInComboBox,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        browsePanel.add( subfoldersCheckBox,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        browsePanel.add( browseButton,
            new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        return browsePanel;
    }

    /***************************************************************************
     * @param enterListener
     * @return
     **************************************************************************/
    private JPanel createSearchPanel( KeyListener enterListener )
    {
        JPanel searchPanel = new JPanel( new GridBagLayout() );
        JLabel filenameLabel = new JLabel();

        filenameLabel.setText( "Filename : " );
        filenameComboBox.setToolTipText(
            "Literal or regular expression for " + "a filename." );
        filenameComboBox.setModel( filenameModel );
        filenameComboBox.setEditable( true );
        filenameComboBox.getEditor().getEditorComponent().addKeyListener(
            enterListener );

        startButton.setText( "Start" );
        startButton.setToolTipText( "Begin the search." );
        startButton.setActionCommand( "Start" );
        startButton.setIcon( startIcon );
        startButton.addActionListener( startListener );

        contentsCheckBox.setText( "Containing Text :" );
        contentsCheckBox.setToolTipText(
            "Signifies that contents should " + "also be checked." );
        contentsCheckBox.addActionListener(
            new CheckBoxEnabler_actionAdapter( contentsComboBox ) );
        contentsComboBox.setToolTipText(
            "Literal or regular expression " + "for a line within a file." );
        contentsComboBox.setModel( contentsModel );
        contentsComboBox.setEditable( true );
        contentsComboBox.getEditor().getEditorComponent().addKeyListener(
            enterListener );

        searchPanel.add( filenameLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        searchPanel.add( filenameComboBox,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        searchPanel.add( startButton,
            new GridBagConstraints( 2, 0, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 10, 0 ) );

        searchPanel.add( contentsCheckBox,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        searchPanel.add( contentsComboBox,
            new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        return searchPanel;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void clearPanel()
    {
        resultsTableModel.clearModel();

        GcThread.createAndStart();
    }

    /***************************************************************************
     * @param left JComponent
     * @param right JComponent
     * @return JPanel
     **************************************************************************/
    private JPanel createRightSidedPanel( JComponent left, JComponent right )
    {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();

        panel.setLayout( layout );

        panel.add( left,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        panel.add( right,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public SearchParams getData()
    {
        SearchParams params = new SearchParams();
        Object obj = filenameComboBox.getSelectedItem();

        params.filename = obj != null ? obj.toString() : "";

        if( contentsCheckBox.isSelected() )
        {
            params.contents = contentsComboBox.getSelectedItem().toString();
        }
        else
        {
            params.contents = null;
        }

        params.searchSubfolders = subfoldersCheckBox.isSelected();

        params.setSearchFolders( IOUtils.getFilesFromString(
            searchInComboBox.getSelectedItem().toString() ) );

        if( moreThanCheckBox.isSelected() )
        {
            try
            {
                params.moreThan = Long.valueOf( moreThanTextField.getText() );
            }
            catch( NumberFormatException ex )
            {
                params.moreThan = null;
            }
        }
        else
        {
            params.moreThan = null;
        }

        if( lessThanCheckBox.isSelected() )
        {
            try
            {
                params.lessThan = Long.valueOf( lessThanTextField.getText() );
            }
            catch( NumberFormatException ex )
            {
                params.lessThan = null;
            }
        }
        else
        {
            params.lessThan = null;
        }

        if( afterCheckBox.isSelected() )
        {
            params.after = afterTextField.getDate();
        }
        else
        {
            params.after = null;
        }

        if( beforeCheckBox.isSelected() )
        {
            params.before = beforeTextField.getDate();
        }
        else
        {
            params.before = null;
        }

        params.filenameRegex = fileRegexCheckBox.isSelected();
        params.filenameMatch = fileMatchCheckBox.isSelected();
        params.filenameNot = fileNotCheckBox.isSelected();

        params.contentsRegex = contentRegexCheckBox.isSelected();
        params.contentsMatch = contentMatchCheckBox.isSelected();

        return params;
    }

    private void setContents( String contents )
    {
        boolean contentsValid = contents != null;

        contentsCheckBox.setSelected( contentsValid );
        contentsComboBox.setSelectedItem( contents );
    }

    private void setMoreThan( Long moreThan )
    {
        boolean moreThanValid = moreThan != null;

        moreThanCheckBox.setSelected( moreThanValid );
        moreThanTextField.setEnabled( moreThanValid );

        if( moreThanValid )
        {
            moreThanTextField.setText( moreThan.toString() );
        }
    }

    private void setLessThan( Long lessThan )
    {
        boolean lessThanValid = lessThan != null;

        lessThanCheckBox.setSelected( lessThanValid );
        lessThanTextField.setEnabled( lessThanValid );

        if( lessThanValid )
        {
            lessThanTextField.setText( lessThan.toString() );
        }
    }

    private void setAfter( Calendar after )
    {
        boolean afterValid = after != null;

        afterCheckBox.setSelected( afterValid );
        afterTextField.setEnabled( afterValid );

        if( afterValid )
        {
            afterTextField.setDate( after );
        }
    }

    private void setBefore( Calendar before )
    {
        boolean beforeValid = before != null;

        beforeCheckBox.setSelected( beforeValid );
        beforeTextField.setEnabled( beforeValid );

        if( beforeValid )
        {
            beforeTextField.setDate( before );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( SearchParams params )
    {
        params = params != null ? params : new SearchParams();

        filenameComboBox.setSelectedItem( params.filename );

        setContents( params.contents );

        subfoldersCheckBox.setSelected( params.searchSubfolders );

        String path = IOUtils.getStringFromFiles( params.getSearchFolders() );

        if( path != null && path.length() > 0 )
        {
            searchInComboBox.setSelectedItem( path );
        }
        else
        {
            if( searchInComboBox.getItemCount() > 0 )
            {
                searchInComboBox.setSelectedIndex( 0 );
            }
        }

        setMoreThan( params.moreThan );

        setLessThan( params.lessThan );

        setAfter( params.after );

        setBefore( params.before );

        fileRegexCheckBox.setSelected( params.filenameRegex );
        fileMatchCheckBox.setSelected( params.filenameMatch );
        fileNotCheckBox.setSelected( params.filenameNot );

        contentRegexCheckBox.setSelected( params.contentsRegex );
        contentMatchCheckBox.setSelected( params.contentsMatch );

        contentsComboBox.setEnabled( params.contentsMatch );
    }

    /***************************************************************************
     * @param records List
     **************************************************************************/
    public void addRecords( java.util.List<? extends ExplorerItem> records )
    {
        resultsTableModel.addFiles( records );
    }

    /***************************************************************************
     * @param record SearchRecord
     **************************************************************************/
    public void addRecord( SearchRecord record )
    {
        resultsTableModel.addFile( record );
    }

    private boolean checkParams( SearchParams params )
    {
        StringBuffer errBuffer = new StringBuffer();

        // ---------------------------------------------------------------------
        // Check the files
        // ---------------------------------------------------------------------
        List<File> files = Arrays.asList( params.getSearchFolders() );
        for( File f : files )
        {
            if( !f.isDirectory() )
            {
                errBuffer.append(
                    "Non-existant directory: " + f.getAbsolutePath() );
            }
        }

        // ---------------------------------------------------------------------
        // Check regex.
        // ---------------------------------------------------------------------
        try
        {
            params.getFilenamePattern();
        }
        catch( PatternSyntaxException ex )
        {
            errBuffer.append( "Invalid filename pattern: " + Utils.NEW_LINE );
            errBuffer.append( ex.getMessage() );
        }
        try
        {
            params.getContentsPattern();
        }
        catch( PatternSyntaxException ex )
        {
            if( errBuffer.length() > 0 )
            {
                errBuffer.append( Utils.NEW_LINE );
                errBuffer.append( Utils.NEW_LINE );
            }
            errBuffer.append( "Invalid contents pattern: " + Utils.NEW_LINE );
            errBuffer.append( ex.getMessage() );
        }

        if( errBuffer.length() > 0 )
        {
            JOptionPane.showMessageDialog( view, errBuffer.toString(), "ERROR",
                JOptionPane.ERROR_MESSAGE );
            return false;
        }

        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void listener_startButton_actionPerformed()
    {
        SearchParams params = getData();

        if( !checkParams( params ) )
        {
            return;
        }

        searcher = new Searcher( resultsTable, this, statusBar );

        searcher.search( params, new FinishedListener( this ) );

        setSearchStarted();

        FileSpyData configData = options.getOptions();
        Object contents = contentsComboBox.getSelectedItem();
        Object filename = filenameComboBox.getSelectedItem();
        Object folder = searchInComboBox.getSelectedItem();

        if( filename != null && filename.toString().length() > 0 )
        {
            configData.filenames.push( filename.toString() );
        }

        if( contents != null )
        {
            configData.contents.push( contents.toString() );
        }

        configData.folders.push( folder.toString() );

        options.write();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void listener_stopButton_actionPerformed()
    {
        searcher.cancel();
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void listener_resultsTable_valueChanged( ListSelectionEvent e )
    {
        int index = resultsTable.getSelectedRow();

        if( !e.getValueIsAdjusting() && index > -1 )
        {
            SearchRecord record = ( SearchRecord )resultsTable.getSelectedItem();
            java.util.List<LineMatch> lines = record.getLinesFound();
            Style plain = StyleContext.getDefaultStyleContext().getStyle(
                StyleContext.DEFAULT_STYLE );
            Style matchStyle = rightResultsPane.addStyle( "match", plain );
            Style headerStyle = rightResultsPane.addStyle( "header", plain );

            StyleConstants.setBold( matchStyle, true );
            StyleConstants.setUnderline( matchStyle, true );
            StyleConstants.setForeground( matchStyle, new Color( 0x0A246A ) );

            StyleConstants.setBold( headerStyle, true );
            StyleConstants.setUnderline( headerStyle, true );
            StyleConstants.setFontSize( headerStyle, 16 );

            // LogUtils.printDebug( record.getFile().getAbsolutePath() +
            // " clicked." );

            rightResultsPane.setText( "" );

            // Remove exception.

            try
            {
                rightResultsPane.appendText( record.getFile().getName(),
                    headerStyle );
                rightResultsPane.appendText( Utils.NEW_LINE, null );
                rightResultsPane.appendText( Utils.NEW_LINE, null );

                for( int i = 0; i < lines.size(); i++ )
                {
                    LineMatch line = ( LineMatch )lines.get( i );
                    // LogUtils.printDebug( "\tWriting line:" + i );

                    rightResultsPane.appendText( line.lineNumber + ": \t",
                        null );
                    rightResultsPane.appendText( line.preMatch, null );
                    rightResultsPane.appendText( line.match, matchStyle );
                    rightResultsPane.appendText( line.postMatch, null );
                    rightResultsPane.appendText( Utils.NEW_LINE, null );
                }

                // LogUtils.printDebug( "Text:" + rightResultsPane.getText() );
            }
            catch( Exception ex )
            {
                LogUtils.printError( "\tGot an exception!" );
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setSearchStarted()
    {
        startButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.STOP_16 ) );
        startButton.setText( "Stop" );
        startButton.setActionCommand( "Stop" );
    }

    /***************************************************************************
     * @param millis
     **************************************************************************/
    private void setSearchFinished( long millis )
    {
        startButton.setIcon( startIcon );
        startButton.setText( "Start" );
        startButton.setActionCommand( "Start" );

        int rowCount = resultsTableModel.getRowCount();
        String elapsed = Utils.getElapsedString( new Date( millis ) );

        statusBar.setText( rowCount + " file(s) found in " + elapsed + "." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class BrowseButtonListener implements ActionListener
    {
        private final SearchView panel;

        public BrowseButtonListener( SearchView panel )
        {
            this.panel = panel;
        }

        public void actionPerformed( ActionEvent e )
        {
            DirectoryChooser chooser = new DirectoryChooser(
                Utils.getComponentsFrame( panel.view ), "Choose Directories",
                "Choose one or more directories in which to search:" );
            String selectedItemStr = panel.searchInComboBox.getSelectedItem().toString();
            File curFile = new File( selectedItemStr );
            String paths = "";

            curFile = curFile.isDirectory() ? curFile : null;

            chooser.setSelectedPaths( selectedItemStr );
            chooser.setVisible( true );

            paths = chooser.getSelectedPaths();

            if( paths != null && paths.length() > 0 )
            {
                panel.searchInComboBox.setSelectedItem( paths );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class StartButtonListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            String str = e.getActionCommand();

            if( str.compareTo( "Start" ) == 0 )
            {
                listener_startButton_actionPerformed();
            }
            else if( str.compareTo( "Stop" ) == 0 )
            {
                listener_stopButton_actionPerformed();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class StartKeyListener extends KeyAdapter
    {
        public void keyReleased( KeyEvent e )
        {
            if( e.getKeyCode() == KeyEvent.VK_ENTER )
            {
                listener_startButton_actionPerformed();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SearchPanel_listSelectionAdapter
        implements ListSelectionListener
    {
        private SearchView adaptee;

        SearchPanel_listSelectionAdapter( SearchView adaptee )
        {
            this.adaptee = adaptee;
        }

        public void valueChanged( ListSelectionEvent e )
        {
            adaptee.listener_resultsTable_valueChanged( e );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CheckBoxEnabler_actionAdapter implements ActionListener
    {
        private JComponent [] components;

        public CheckBoxEnabler_actionAdapter( JComponent component )
        {
            this( new JComponent[] { component } );
        }

        public CheckBoxEnabler_actionAdapter( JComponent [] components )
        {
            this.components = components;
        }

        public void actionPerformed( ActionEvent e )
        {
            JCheckBox box = ( JCheckBox )e.getSource();
            for( int i = 0; i < components.length; i++ )
            {
                components[i].setEnabled( box.isSelected() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenResultsListener extends MouseAdapter
    {
        private final SearchView panel;

        // private final JExplorerFrame explorer;

        public OpenResultsListener( SearchView adaptee )
        {
            this.panel = adaptee;
            // this.explorer = new JExplorerFrame();
        }

        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 && !e.isPopupTrigger() )
            {
                File file = panel.resultsTable.getSelectedFile();

                if( file != null )
                {
                    // if( file.isDirectory() )
                    // {
                    // Desktop.getDesktop().open( file );
                    // // showFile( file );
                    // if( explorer == null )
                    // {
                    // explorer.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE
                    // );
                    // }
                    // else
                    // {
                    // explorer.setVisible( true );
                    // }
                    // }
                    // else
                    // {
                    try
                    {
                        Desktop.getDesktop().open( file );
                    }
                    catch( Exception ex )
                    {
                        JOptionPane.showMessageDialog( panel.view,
                            ex.getMessage(), "ERROR",
                            JOptionPane.ERROR_MESSAGE );
                    }
                    // }
                }

            }
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class FinishedListener implements ItemActionListener<Long>
    {
        private final SearchView panel;

        public FinishedListener( SearchView panel )
        {
            this.panel = panel;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Long> event )
        {
            SearchFinishedHandler runnable = new SearchFinishedHandler( panel,
                event.getItem() );
            SwingUtilities.invokeLater( runnable );
        }
    }

    private static class SearchFinishedHandler implements Runnable
    {
        private final SearchView searchPanel;
        private final long millis;

        public SearchFinishedHandler( SearchView searchPanel,
            long durationInMillis )
        {
            this.searchPanel = searchPanel;
            this.millis = durationInMillis;
        }

        public void run()
        {
            searchPanel.setSearchFinished( millis );
        }
    }
}
