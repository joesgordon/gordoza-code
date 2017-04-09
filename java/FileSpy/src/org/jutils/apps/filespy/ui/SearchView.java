package org.jutils.apps.filespy.ui;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

import javax.swing.*;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.io.parsers.StringParser;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.calendar.DateField;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchView implements IDataView<SearchParams>
{
    /**  */
    private final JPanel view;

    /**  */
    private final ComboFormField<String> filenameField;
    /**  */
    private final UsableFormField<String> contentsField;
    /**  */
    private final FileFormField pathField;

    /**  */
    private final UsableFormField<Long> moreThanField;
    /**  */
    private final UsableFormField<Long> lessThanField;

    /**  */
    private final UsableFormField<LocalDate> afterField;
    /**  */
    private final UsableFormField<LocalDate> beforeField;

    /**  */
    private final BooleanFormField fileNotCheckBox;
    /**  */
    private final BooleanFormField fileRegexCheckBox;
    /**  */
    private final BooleanFormField fileMatchCheckBox;
    /**  */
    private final BooleanFormField subfoldersField;

    /**  */
    private final BooleanFormField contentsRegexCheckBox;
    /**  */
    private final BooleanFormField contentsMatchCheckBox;

    /**  */
    private final ItemActionList<SearchParams> startListeners;

    /**  */
    private SearchParams params;

    /***************************************************************************
     *
     **************************************************************************/
    public SearchView( OptionsSerializer<FileSpyData> options )
    {
        FileSpyData userData = options.getOptions();

        ComboFormField<String> contentsComboField = new ComboFormField<>(
            "Contents", userData.contents.toList() );

        this.filenameField = new ComboFormField<String>( "Filename",
            userData.filenames.toList() );
        this.contentsField = new UsableFormField<>( contentsComboField );
        this.pathField = new FileFormField( "Search In",
            ExistenceType.DIRECTORY_ONLY );
        this.subfoldersField = new BooleanFormField( "Search Sub-directories" );
        this.moreThanField = new UsableFormField<>(
            new LongFormField( "More Than", null, 10, null ) );
        this.lessThanField = new UsableFormField<>(
            new LongFormField( "Less Than", null, 10, null ) );
        this.afterField = new UsableFormField<>( new DateField( "After" ) );
        this.beforeField = new UsableFormField<>( new DateField( "Before" ) );
        this.fileNotCheckBox = new BooleanFormField( "Specify Not Condition" );
        this.fileRegexCheckBox = new BooleanFormField(
            "Use Regular Expressions" );
        this.fileMatchCheckBox = new BooleanFormField( "Match Case" );
        this.contentsRegexCheckBox = new BooleanFormField(
            "Use Regular Expressions" );
        this.contentsMatchCheckBox = new BooleanFormField( "Match Case" );

        this.view = createView( new StartKeyListener( this ) );

        this.startListeners = new ItemActionList<>();

        filenameField.setUserEditable( new StringParser() );
        contentsComboField.setUserEditable( new StringParser() );

        setData( new SearchParams() );

        filenameField.setUpdater( ( s ) -> params.filename = s );
        contentsField.setUpdater( ( u ) -> params.contents.set( u ) );
        pathField.setUpdater( ( f ) -> params.path = f );
        subfoldersField.setUpdater( ( b ) -> params.searchSubfolders = b );

        moreThanField.setUpdater( ( u ) -> params.moreThan.set( u ) );
        lessThanField.setUpdater( ( u ) -> params.lessThan.set( u ) );

        afterField.setUpdater( ( u ) -> params.after.set( u ) );
        beforeField.setUpdater( ( u ) -> params.before.set( u ) );

        fileRegexCheckBox.setUpdater( ( b ) -> params.filenameRegex = b );
        fileMatchCheckBox.setUpdater( ( b ) -> params.filenameMatch = b );
        fileNotCheckBox.setUpdater( ( b ) -> params.filenameNot = b );

        contentsRegexCheckBox.setUpdater( ( b ) -> params.contentsRegex = b );
        contentsMatchCheckBox.setUpdater( ( b ) -> params.contentsMatch = b );
    }

    /***************************************************************************
     * @param enterListener
     * @return
     **************************************************************************/
    private JPanel createView( KeyListener enterListener )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createSearchPanel( enterListener ), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 4, 4 ), 0, 0 );
        panel.add( createOptionsPanel(), constraints );

        return panel;
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
            new Insets( 0, 0, 0, 2 ), 0, 0 );
        optionsPanel.add( createFileOptionsPanel(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 2 ), 0, 0 );
        optionsPanel.add( createContentOptionsPanel(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 2 ), 0, 0 );
        optionsPanel.add( createSizePanel(), constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( createTimePanel(), constraints );

        constraints = new GridBagConstraints( 4, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        optionsPanel.add( Box.createHorizontalStrut( 0 ), constraints );

        optionsPanel.setMinimumSize( optionsPanel.getPreferredSize() );

        return optionsPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createSizePanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( moreThanField );
        form.addField( lessThanField );

        JPanel panel = form.getView();

        panel.setBorder( BorderFactory.createTitledBorder( "Size (kb)" ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTimePanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( afterField );
        form.addField( beforeField );

        JPanel panel = form.getView();

        panel.setBorder( BorderFactory.createTitledBorder( "Time Options" ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createFileOptionsPanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( fileRegexCheckBox );
        form.addField( fileMatchCheckBox );
        form.addField( fileNotCheckBox );

        JPanel panel = form.getView();

        panel.setBorder(
            BorderFactory.createTitledBorder( "Filename Options" ) );

        return panel;
    }

    private JPanel createContentOptionsPanel()
    {
        StandardFormView form = new StandardFormView();

        form.addField( contentsRegexCheckBox );
        form.addField( contentsMatchCheckBox );

        JPanel panel = form.getView();

        panel.setBorder(
            BorderFactory.createTitledBorder( "Content Options" ) );

        return panel;
    }

    /***************************************************************************
     * @param enterListener
     * @return
     **************************************************************************/
    private JPanel createSearchPanel( KeyListener enterListener )
    {
        StandardFormView form = new StandardFormView();

        form.addField( filenameField );
        form.addField( contentsField );
        form.addField( pathField );
        form.addField( subfoldersField );

        return form.getView();
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
     * 
     **************************************************************************/
    @Override
    public SearchParams getData()
    {
        return params;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( SearchParams params )
    {
        params = params != null ? params : new SearchParams();

        this.params = params;

        filenameField.setValue( params.filename );
        contentsField.setValue( params.contents );

        pathField.setValue( params.path );

        moreThanField.setValue( params.moreThan );
        lessThanField.setValue( params.lessThan );

        afterField.setValue( params.after );
        beforeField.setValue( params.before );

        fileRegexCheckBox.setValue( params.filenameRegex );
        fileMatchCheckBox.setValue( params.filenameMatch );
        fileNotCheckBox.setValue( params.filenameNot );
        subfoldersField.setValue( params.searchSubfolders );

        contentsRegexCheckBox.setValue( params.contentsRegex );
        contentsMatchCheckBox.setValue( params.contentsMatch );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void checkForStartKey( KeyEvent e )
    {
        if( e.getKeyCode() == KeyEvent.VK_ENTER && params != null )
        {
            startListeners.fireListeners( this, params );
        }
    }

    public void addStartListener( ItemActionListener<SearchParams> l )
    {
        startListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class StartKeyListener extends KeyAdapter
    {
        private final SearchView view;

        public StartKeyListener( SearchView view )
        {
            this.view = view;
        }

        @Override
        public void keyReleased( KeyEvent e )
        {
            view.checkForStartKey( e );
        }
    }
}
