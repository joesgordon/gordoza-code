package org.eglsht.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.eglsht.AppConstants;
import org.eglsht.EagleSheetIcons;
import org.eglsht.data.EagleSheet;
import org.eglsht.data.SheetSize;
import org.jutils.IconConstants;
import org.jutils.OptionUtils;
import org.jutils.ui.ExitListener;
import org.jutils.ui.JGoodiesMenuBar;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.ShadowBorder;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.model.IView;
import org.jutils.ui.sheet.SpreadSheetView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EagleSheetFrameView implements IView<JFrame>
{
    /**  */
    private final JFrame frame;

    /**  */
    private final SpreadSheetView sheetView;

    /***************************************************************************
     * 
     **************************************************************************/
    public EagleSheetFrameView()
    {
        this.frame = new JFrame();
        this.sheetView = new SpreadSheetView();

        frame.setTitle( AppConstants.APP_NAME );
        frame.setIconImages( EagleSheetIcons.getApplicationImages() );

        frame.setJMenuBar( createMenuBar() );
        frame.setContentPane( createContentPane() );

        sheetView.setData( new EagleSheet() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new JGoodiesMenuBar();

        menubar.add( createFileMenu() );
        menubar.add( createEditMenu() );
        menubar.add( createViewMenu() );

        return menubar;
    }

    /**
     * @return
     */
    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu( "File" );
        JMenuItem item;

        menu.add( createNewAction() );

        menu.add( createOpenAction() );

        menu.addSeparator();

        item = new JMenuItem( "Exit",
            IconConstants.getIcon( IconConstants.STOP_16 ) );
        item.addActionListener( new ExitListener( frame ) );
        menu.add( item );

        return menu;
    }

    private Action createNewAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
        ActionListener listener = ( e ) -> createNewSheet();
        return new ActionAdapter( listener, "New", icon );
    }

    private Action createOpenAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        IFileSelected ifs = ( f ) -> openFile( f );
        ActionListener listener = new FileChooserListener( frame, "Open File",
            false, ifs );
        return new ActionAdapter( listener, "Open", icon );
    }

    private JMenu createEditMenu()
    {
        JMenu menu = new JMenu( "Edit" );
        JMenuItem item;

        item = new JMenuItem( "Font",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( ( e ) -> editFont() );
        menu.add( item );

        return menu;
    }

    private JMenu createViewMenu()
    {
        JMenu menu = new JMenu( "View" );
        JMenuItem item;

        item = new JMenuItem( "Auto Layout",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( ( e ) -> sheetView.autoLayout() );
        menu.add( item );

        menu.addSeparator();

        item = new JMenuItem( "No Headers",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( ( e ) -> setHeadersVisible( false, false ) );
        menu.add( item );

        item = new JMenuItem( "Row Headers",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( ( e ) -> setHeadersVisible( true, false ) );
        menu.add( item );

        item = new JMenuItem( "Column Headers",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( ( e ) -> setHeadersVisible( false, true ) );
        menu.add( item );

        item = new JMenuItem( "Row and Column Headers",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( ( e ) -> setHeadersVisible( true, true ) );
        menu.add( item );

        return menu;
    }

    private Container createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StatusBarPanel statusBar = new StatusBarPanel();

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( createMainPanel(), BorderLayout.CENTER );
        panel.add( statusBar.getView(), BorderLayout.SOUTH );

        return panel;
    }

    private Component createMainPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        TitleView titlePanel = new TitleView();
        GridBagConstraints constraints;

        titlePanel.setTitle( "New Sheet" );
        titlePanel.setComponent( sheetView.getView() );
        titlePanel.getView().setBorder( new ShadowBorder() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 5, 5, 5, 5 ), 0, 0 );
        panel.add( titlePanel.getView(), constraints );

        return panel;
    }

    private Component createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();
        JButton button;

        button = new JButton( EagleSheetIcons.getInsertRowBeforeIcon() );
        button.setToolTipText( "Insert Row Before" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> insertRow( false ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIcons.getInsertRowAfterIcon() );
        button.setToolTipText( "Insert Row After" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> insertRow( true ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIcons.getDeleteRowIcon() );
        button.setToolTipText( "Delete Row" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> removeSelectedRows() );
        toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton( EagleSheetIcons.getInsertColumnBeforeIcon() );
        button.setToolTipText( "Insert Column Before" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> insertCol( false ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIcons.getInsertColumnAfterIcon() );
        button.setToolTipText( "Insert Column After" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> insertCol( true ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIcons.getDeleteColumnIcon() );
        button.setToolTipText( "Delete Column" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> removeSelectedCols() );
        toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton( EagleSheetIcons.getEditTableIcon() );
        button.setToolTipText( "Change Size" );
        button.setFocusable( false );
        button.addActionListener( ( e ) -> showEditSize() );
        toolbar.add( button );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createNewSheet()
    {
        SheetSizeView sizeView = new SheetSizeView();

        boolean createSheet = OptionUtils.showOkCancelDialog( frame,
            sizeView.getView(), "Enter Size", "Create Sheet", () -> {
            } );

        if( createSheet )
        {
            SheetSize size = sizeView.getData();
            EagleSheet sheet = new EagleSheet( size.rows, size.cols );

            sheetView.setData( sheet );
        }
    }

    /**
     * @param f
     */
    @SuppressWarnings( "unused")
    public void openFile( File f )
    {
        // TODO Auto-generated method stub
    }

    /**
     * 
     */
    private void editFont()
    {
        // TODO Auto-generated method stub
    }

    @SuppressWarnings( "unused")
    private void setHeadersVisible( boolean row, boolean col )
    {
        // TODO Auto-generated method stub
    }

    @SuppressWarnings( "unused")
    private void insertRow( boolean after )
    {
        // TODO Auto-generated method stub
    }

    private void removeSelectedRows()
    {
        // TODO Auto-generated method stub
    }

    @SuppressWarnings( "unused")
    private void insertCol( boolean after )
    {
        // TODO Auto-generated method stub
    }

    private void removeSelectedCols()
    {
        // TODO Auto-generated method stub
    }

    private void showEditSize()
    {
        // TODO Auto-generated method stub
    }
}
