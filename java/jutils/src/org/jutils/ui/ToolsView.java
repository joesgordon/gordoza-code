package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.SwingUtils;
import org.jutils.Utils;
import org.jutils.ui.event.ResizingTableModelListener;
import org.jutils.ui.model.*;
import org.jutils.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ToolsView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTable table;
    /**  */
    private final ItemsTableModel<IToolView> tableModel;
    /**  */
    private final String appName;

    /***************************************************************************
     * @param tools
     * @param appName
     **************************************************************************/
    public ToolsView( List<? extends IToolView> tools, String appName )
    {
        this.appName = appName;
        this.tableModel = new ItemsTableModel<IToolView>(
            new ToolsTableModel() );
        this.table = new JTable( tableModel );
        this.view = createView();

        tableModel.setItems( tools );

        tableModel.addTableModelListener(
            new ResizingTableModelListener( table ) );

        table.getColumnModel().getColumn( 0 ).setCellRenderer(
            new LabelTableCellRenderer( new ToolCellRenderer() ) );
        table.setRowHeight( 32 );
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        table.addMouseListener( new TableMouseListener( this ) );
        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );

        SwingUtils.addKeyListener( table, "ENTER", false,
            ( e ) -> showSelectedRow(), "Enter to display tool" );

        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane pane = new JScrollPane( table );

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        pane.setPreferredSize( new Dimension( 400, 400 ) );
        pane.getViewport().setBackground( table.getBackground() );

        table.setShowVerticalLines( false );

        panel.add( pane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resizeTable()
    {
        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @param menubar
     **************************************************************************/
    public static void fixMenuBar( JMenuBar menubar )
    {
        menubar.setBorder(
            new javax.swing.border.MatteBorder( 0, 0, 1, 0, Color.gray ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showSelectedRow()
    {
        int row = table.getSelectedRow();

        if( row > -1 )
        {
            IToolView tool = tableModel.getItem( row );

            showTool( tool );
        }
    }

    /***************************************************************************
     * @param tool
     **************************************************************************/
    public void showTool( IToolView tool )
    {
        view.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
        Container comp = tool.getView();

        if( comp != null )
        {
            JFrame frame;

            if( comp instanceof JFrame )
            {
                frame = ( JFrame )comp;
            }
            else
            {
                frame = new JFrame( tool.getName() );
                JPanel panel = new JPanel( new BorderLayout() );
                JScrollPane pane = new JScrollPane( comp );

                pane.getVerticalScrollBar().setUnitIncrement( 10 );

                pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

                panel.add( pane, BorderLayout.CENTER );

                frame.setIconImages( tool.getImages() );
                frame.setContentPane( panel );

                frame.pack();
            }

            String title = frame.getTitle();

            if( !appName.isEmpty() )
            {
                title = appName + " - " + tool.getName();
            }

            frame.setTitle( title );
            frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
            frame.setLocationRelativeTo( view );
            frame.setVisible( true );
        }

        view.setCursor( Cursor.getDefaultCursor() );
    }

    /***************************************************************************
     * @param tools
     * @return
     **************************************************************************/
    public JMenu createMenu()
    {
        return createMenu( "Tools" );
    }

    /***************************************************************************
     * @param tools
     * @param menuName
     * @return
     **************************************************************************/
    public JMenu createMenu( String menuName )
    {
        JMenu menu = new JMenu( menuName );

        addToMenu( menu );

        return menu;
    }

    /***************************************************************************
     * @param tools
     * @param menu
     **************************************************************************/
    private void addToMenu( JMenu menu )
    {
        JMenuItem item;

        for( IToolView tool : tableModel.getItems() )
        {
            item = new JMenuItem( tool.getName(), tool.getIcon24() );
            item.setFont( item.getFont().deriveFont( 16.0f ) );
            item.addActionListener( new ShowToolListener( this, tool ) );
            menu.add( item );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static ImageIcon createDefaultIcon()
    {
        int iconSize = 24;

        BufferedImage image = Utils.createTransparentImage( iconSize,
            iconSize );

        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );

        int shapeSize = 24 - 2;

        graphics.setColor( new Color( 58, 110, 167 ) );
        graphics.fillRoundRect( 1, 1, shapeSize, shapeSize, 4, 4 );

        return new ImageIcon( image );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ToolsTableModel implements ITableItemsConfig<IToolView>
    {
        /**  */
        public final static String [] COL_NAMES = { "Name", "Description" };
        /**  */
        public final static Class<?> [] COL_CLASSES = { String.class,
            String.class };

        /**
         * {@inheritDoc}
         */
        @Override
        public String [] getColumnNames()
        {
            return COL_NAMES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> [] getColumnClasses()
        {
            return COL_CLASSES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getItemData( IToolView tool, int col )
        {
            switch( col )
            {
                case 0:
                    return tool.getName();
                case 1:
                    return tool.getDescription();
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setItemData( IToolView tool, int col, Object data )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( IToolView tool, int col )
        {
            return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ToolCellRenderer implements ITableCellLabelDecorator
    {
        /**  */
        private static final Icon TOOL_ICON = createDefaultIcon();

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            @SuppressWarnings( "unchecked")
            ItemsTableModel<IToolView> model = ( ItemsTableModel<IToolView> )table.getModel();

            if( row > -1 )
            {
                IToolView tool = model.getItem( row );
                Icon icon = tool.getIcon24();

                if( icon == null )
                {
                    icon = TOOL_ICON;
                }

                label.setIcon( icon );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TableMouseListener extends MouseAdapter
    {
        /**  */
        private final ToolsView view;

        /**
         * @param view
         */
        public TableMouseListener( ToolsView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 && !e.isPopupTrigger() )
            {
                view.showSelectedRow();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ShowToolListener implements ActionListener
    {
        /**  */
        private final ToolsView view;
        /**  */
        private final IToolView tool;

        /**
         * @param view
         * @param tool
         */
        public ShowToolListener( ToolsView view, IToolView tool )
        {
            this.view = view;
            this.tool = tool;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.showTool( tool );
        }
    }
}
