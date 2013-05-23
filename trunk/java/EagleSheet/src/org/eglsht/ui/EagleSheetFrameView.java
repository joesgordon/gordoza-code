package org.eglsht.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.eglsht.AppConstants;
import org.eglsht.EagleSheetIconConstants;
import org.eglsht.data.EagleSheet;
import org.eglsht.data.SheetSize;
import org.jutils.IconConstants;
import org.jutils.ui.*;
import org.jutils.ui.model.IView;

public class EagleSheetFrameView implements IView<JFrame>
{
    private final JFrame frame;

    private final SpreadSheetView sheetView;

    public EagleSheetFrameView()
    {
        this.frame = new JFrame();
        this.sheetView = new SpreadSheetView();

        frame.setTitle( AppConstants.APP_NAME );
        frame.setIconImages( EagleSheetIconConstants.getApplicationImages() );

        frame.setJMenuBar( createMenuBar() );
        frame.setContentPane( createContentPane() );

        sheetView.setData( new EagleSheet() );
    }

    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new JGoodiesMenuBar();

        menubar.add( createFileMenu() );
        menubar.add( createEditMenu() );
        menubar.add( createViewMenu() );

        return menubar;
    }

    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu( "File" );
        JMenuItem item;

        item = new JMenuItem( "New",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new NewDocumentListener( this ) );
        menu.add( item );

        item = new JMenuItem( "Open",
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        item.addActionListener( new OpenDocumentListener( this ) );
        menu.add( item );

        menu.addSeparator();

        item = new JMenuItem( "Exit",
            IconConstants.loader.getIcon( IconConstants.STOP_16 ) );
        item.addActionListener( new ExitListener( frame ) );
        menu.add( item );

        return menu;
    }

    private JMenu createEditMenu()
    {
        JMenu menu = new JMenu( "Edit" );
        JMenuItem item;

        item = new JMenuItem( "Font",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new FontEditListener( this ) );
        menu.add( item );

        return menu;
    }

    private JMenu createViewMenu()
    {
        JMenu menu = new JMenu( "View" );
        JMenuItem item;

        item = new JMenuItem( "Auto Layout",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new AutoLayoutListener( this ) );
        menu.add( item );

        menu.addSeparator();

        item = new JMenuItem( "No Headers",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new HeaderListener( this ) );
        menu.add( item );

        item = new JMenuItem( "Row Headers",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new HeaderListener( this ) );
        menu.add( item );

        item = new JMenuItem( "Column Headers",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new HeaderListener( this ) );
        menu.add( item );

        item = new JMenuItem( "Row and Column Headers",
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        item.addActionListener( new HeaderListener( this ) );
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
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5,
                5, 5, 5 ), 0, 0 );
        panel.add( titlePanel.getView(), constraints );

        return panel;
    }

    private Component createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();
        JButton button;

        button = new JButton( EagleSheetIconConstants.getInsertRowBeforeIcon() );
        button.setToolTipText( "Insert Row Before" );
        button.setFocusable( false );
        button.addActionListener( new InsertRowListener( this ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIconConstants.getInsertRowAfterIcon() );
        button.setToolTipText( "Insert Row After" );
        button.setFocusable( false );
        button.addActionListener( new InsertRowListener( this, 1 ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIconConstants.getDeleteRowIcon() );
        button.setToolTipText( "Delete Row" );
        button.setFocusable( false );
        button.addActionListener( new DeleteRowListener( this ) );
        toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton(
            EagleSheetIconConstants.getInsertColumnBeforeIcon() );
        button.setToolTipText( "Insert Column Before" );
        button.setFocusable( false );
        button.addActionListener( new InsertColumnListener( this ) );
        toolbar.add( button );

        button = new JButton(
            EagleSheetIconConstants.getInsertColumnAfterIcon() );
        button.setToolTipText( "Insert Column After" );
        button.setFocusable( false );
        button.addActionListener( new InsertColumnListener( this, 1 ) );
        toolbar.add( button );

        button = new JButton( EagleSheetIconConstants.getDeleteColumnIcon() );
        button.setToolTipText( "Delete Column" );
        button.setFocusable( false );
        button.addActionListener( new DeleteColumnListener( this ) );
        toolbar.add( button );

        toolbar.addSeparator();

        button = new JButton( EagleSheetIconConstants.getEditTableIcon() );
        button.setToolTipText( "Change Size" );
        button.setFocusable( false );
        button.addActionListener( new EditTableListener( this ) );
        toolbar.add( button );

        return toolbar;
    }

    @Override
    public JFrame getView()
    {
        return frame;
    }

    private static class NewDocumentListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public NewDocumentListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            SheetSizeView sizeView = new SheetSizeView();

            int option = JOptionPane.showOptionDialog( view.frame,
                sizeView.getView(), "Enter Size", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null );

            if( option == JOptionPane.OK_OPTION )
            {
                SheetSize size = sizeView.getData();
                EagleSheet sheet = new EagleSheet( size.rows, size.cols );

                view.sheetView.setData( sheet );
            }
        }
    }

    private static class OpenDocumentListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public OpenDocumentListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class InsertRowListener implements ActionListener
    {
        private final EagleSheetFrameView view;
        private final int offset;

        public InsertRowListener( EagleSheetFrameView view )
        {
            this( view, 0 );
        }

        public InsertRowListener( EagleSheetFrameView view, int offset )
        {
            this.view = view;
            this.offset = offset;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class InsertColumnListener implements ActionListener
    {
        private final EagleSheetFrameView view;
        private final int offset;

        public InsertColumnListener( EagleSheetFrameView view )
        {
            this( view, 0 );
        }

        public InsertColumnListener( EagleSheetFrameView view, int offset )
        {
            this.view = view;
            this.offset = offset;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class DeleteColumnListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public DeleteColumnListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class DeleteRowListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public DeleteRowListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class EditTableListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public EditTableListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class FontEditListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public FontEditListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class AutoLayoutListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public AutoLayoutListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }

    private static class HeaderListener implements ActionListener
    {
        private final EagleSheetFrameView view;

        public HeaderListener( EagleSheetFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // TODO Auto-generated method stub
        }
    }
}
