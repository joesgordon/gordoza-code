package org.jutils.ui.net;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jutils.*;
import org.jutils.data.UIProperty;
import org.jutils.io.*;
import org.jutils.net.NetMessage;
import org.jutils.net.NetMessageSerializer;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.RowHeaderRenderer;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.model.*;
import org.jutils.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessagesView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final NetMessagesTableConfig tableCfg;
    /**  */
    private final ItemsTableModel<NetMessage> tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final JList<String> rowHeader;
    /**  */
    private final RowListModel rowModel;

    /**  */
    private final JButton navFirstButton;
    /**  */
    private final JButton navPreviousButton;
    /**  */
    private final JButton navNextButton;
    /**  */
    private final JButton navLastButton;
    /**  */
    private final JLabel pageLabel;
    /**  */
    private final JButton openButton;
    /**  */
    private final JButton hexTextButton;

    /**  */
    private OkDialogView dialog;
    /**  */
    private final MessageNavView msgView;

    /**  */
    private final IReferenceStream<NetMessage> msgsStream;

    /**  */
    private boolean isHex;

    /**  */
    private int msgsPerPage;
    /**  */
    private long pageStartIndex;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesView()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param fields
     * @param msgWriter
     **************************************************************************/
    public NetMessagesView( IMessageFields fields,
        IStringWriter<NetMessage> msgWriter )
    {
        ReferenceStream<NetMessage> refStream = null;

        try
        {
            refStream = new ReferenceStream<>( new NetMessageSerializer() );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Unable to create temp files", ex );
        }

        this.msgsStream = refStream;

        this.tableCfg = new NetMessagesTableConfig( fields );
        this.tableModel = new ItemsTableModel<>( tableCfg );
        this.table = new JTable( tableModel );
        this.rowModel = new RowListModel();
        this.rowHeader = new JList<>( rowModel );
        this.navFirstButton = new JButton();
        this.navPreviousButton = new JButton();
        this.navNextButton = new JButton();
        this.navLastButton = new JButton();
        this.pageLabel = new JLabel( "Page 0 of 0" );
        this.openButton = new JButton();
        this.hexTextButton = new JButton();
        this.msgView = new MessageNavView( this, msgWriter );
        this.view = createView();

        this.msgsPerPage = 500;
        this.pageStartIndex = -1;

        setOpenVisible( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        table.setDefaultRenderer( LocalDateTime.class,
            new LabelTableCellRenderer( new LocalDateTimeDecorator() ) );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        table.addMouseListener( new MessageMouseListener( this ) );

        TableColumnModel colModel = table.getColumnModel();
        int lastColIdx = tableCfg.getColumnNames().length - 1;
        TableColumn column = colModel.getColumn( lastColIdx );
        LabelTableCellRenderer renderer = new LabelTableCellRenderer(
            new FontLabelTableCellRenderer( SwingUtils.getFixedFont( 12 ) ) );
        column.setCellRenderer( renderer );

        JScrollPane displayScrollPane = new JScrollPane( table );
        JScrollBar vScrollBar = displayScrollPane.getVerticalScrollBar();

        vScrollBar.addAdjustmentListener( new BottomScroller( vScrollBar ) );

        rowHeader.setCellRenderer( new RowHeaderRenderer( table ) );
        rowHeader.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        rowHeader.setFixedCellHeight( table.getRowHeight() );

        displayScrollPane.setRowHeaderView( rowHeader );

        displayScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        displayScrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        // panel.setBorder( new TitledBorder( "Sent/Received Messages" ) );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( displayScrollPane, BorderLayout.CENTER );

        panel.setMinimumSize( new Dimension( 625, 200 ) );
        panel.setPreferredSize( new Dimension( 625, 200 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true, false ),
            navFirstButton );
        navFirstButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false, false ),
            navPreviousButton );
        navPreviousButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false, true ),
            navNextButton );
        navNextButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true, true ),
            navLastButton );
        navLastButton.setText( "" );

        toolbar.addSeparator();

        toolbar.add( pageLabel );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        SwingUtils.addActionToToolbar( toolbar, createOpenAction(),
            openButton );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createClearAction() );

        SwingUtils.addActionToToolbar( toolbar, createTextHexAction(),
            hexTextButton );
        hexTextButton.setText( "" );

        return toolbar;
    }

    /***************************************************************************
     * @param absolute
     * @param forward
     * @return
     **************************************************************************/
    private Action createNavAction( boolean absolute, boolean forward )
    {
        ActionListener listener = ( e ) -> navigatePage( absolute, forward );
        String iconName;
        Icon icon;
        String actionName;

        if( absolute && !forward )
        {
            iconName = IconConstants.NAV_FIRST_16;
            actionName = "First Page";
        }
        else if( !absolute && !forward )
        {
            iconName = IconConstants.NAV_PREVIOUS_16;
            actionName = "Previous Page";
        }
        else if( !absolute && forward )
        {
            iconName = IconConstants.NAV_NEXT_16;
            actionName = "Next Page";
        }
        else
        {
            iconName = IconConstants.NAV_LAST_16;
            actionName = "Last Page";
        }

        icon = IconConstants.getIcon( iconName );

        return new ActionAdapter( listener, actionName, icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        IFileSelected ifs = ( f ) -> saveFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose File", true, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );

        return new ActionAdapter( listener, "Save", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose File", false, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        return new ActionAdapter( listener, "Open", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createClearAction()
    {
        ActionListener listener = ( e ) -> clearMessages();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 );

        return new ActionAdapter( listener, "Clear", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTextHexAction()
    {
        ActionListener listener = ( e ) -> toggleHexText();
        Icon icon = IconConstants.getIcon( IconConstants.FONT_16 );

        isHex = true;

        return new ActionAdapter( listener, "Show Text Contents", icon );
    }

    /***************************************************************************
     * @param absolute
     * @param forward
     * @return
     **************************************************************************/
    private void navigatePage( boolean absolute, boolean forward )
    {
        long index = -1;

        if( absolute && !forward )
        {
            index = 0;
        }
        else if( !absolute && !forward )
        {
            index = pageStartIndex - msgsPerPage;
        }
        else if( !absolute && forward )
        {
            index = pageStartIndex + msgsPerPage;
        }
        else
        {
            index = msgsStream.getCount() - 1;
            index = index - index % msgsPerPage;
        }

        if( index > -1 && index < msgsStream.getCount() )
        {
            navigatePage( index );
        }
    }

    /***************************************************************************
     * @param page
     **************************************************************************/
    private void navigatePage( long startIndex )
    {
        if( this.pageStartIndex == startIndex )
        {
            return;
        }

        int count = ( int )Math.min( msgsPerPage,
            msgsStream.getCount() - startIndex );

        // LogUtils.printDebug( "Setting start index to %d from %d of %d for
        // %d",
        // index, pageStartIndex, msgsStream.getCount(), count );

        this.pageStartIndex = startIndex;

        try
        {
            List<NetMessage> msgs = msgsStream.read( pageStartIndex, count );
            tableModel.setItems( msgs );
            updateRowHeader( count );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
        }

        setNavButtonsEnabled();
    }

    /***************************************************************************
     * @param count
     **************************************************************************/
    private void updateRowHeader( int count )
    {
        rowModel.setStart( pageStartIndex );
        rowModel.setSize( count );
        rowHeader.setFixedCellWidth( -1 );
        rowHeader.setFixedCellWidth( rowHeader.getPreferredSize().width + 16 );
        rowHeader.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setNavButtonsEnabled()
    {
        boolean hasPrev = hasPrevious();
        boolean hasNext = hasNext();

        navFirstButton.setEnabled( hasPrev );
        navPreviousButton.setEnabled( hasPrev );
        navNextButton.setEnabled( hasNext );
        navLastButton.setEnabled( hasNext );

        int pageCount = getPageCount();
        int pageIndex = ( int )( ( pageStartIndex + msgsPerPage - 1 ) /
            msgsPerPage );
        int pageNum = pageCount == 0 ? 0 : ( pageIndex + 1 );

        pageLabel.setText( String.format( "Page %d of %d (%d messages)",
            pageNum, pageCount, msgsStream.getCount() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean hasPrevious()
    {
        return pageStartIndex > 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean hasNext()
    {
        long lastMsg = msgsStream.getCount() - 1;
        long maxStartIndex = lastMsg - lastMsg % msgsPerPage;
        return pageStartIndex < maxStartIndex;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private int getPageCount()
    {
        long count = msgsStream.getCount();

        int max = ( int )( ( count + msgsPerPage - 1 ) / msgsPerPage );

        return max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void toggleHexText()
    {
        isHex = !isHex;

        Icon icon;
        String text;

        if( isHex )
        {
            icon = IconConstants.getIcon( IconConstants.FONT_16 );
            text = "Show Text Contents";
        }
        else
        {
            icon = IconConstants.getIcon( "hex_016.png" );
            text = "Show Hex Contents";
        }

        hexTextButton.setIcon( icon );
        hexTextButton.setToolTipText( text );
        tableCfg.setHexText( isHex );
        tableModel.fireTableDataChanged();

        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @param msgIndex
     **************************************************************************/
    private void showMessage( long msgIndex )
    {
        if( msgIndex < 0 || msgIndex >= msgsStream.getCount() )
        {
            return;
        }

        if( !isPaged( msgIndex ) )
        {
            long start = msgIndex - msgIndex % msgsPerPage;
            navigatePage( start );
        }

        int tableIndex = ( int )( msgIndex - pageStartIndex );

        Frame f = SwingUtils.getComponentsFrame( table );
        NetMessage item = tableModel.getItem( tableIndex );

        msgView.setData( item );

        if( this.dialog == null )
        {
            this.dialog = new OkDialogView( getView(), msgView.getView(),
                ModalityType.MODELESS, OkDialogButtons.OK_ONLY );
        }

        JDialog d = dialog.getView();

        if( d.isVisible() )
        {
            d.toFront();
        }
        else
        {
            d.setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
            d.setSize( 675, 400 );
            d.setLocationRelativeTo( f );
        }

        d.setTitle( String.format( "Message %d", msgIndex + 1 ) );
        d.setVisible( true );

        table.setRowSelectionInterval( tableIndex, tableIndex );
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    private boolean isPaged( long index )
    {
        return index > pageStartIndex &&
            index < ( pageStartIndex + msgsPerPage );
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    private void saveFile( File file )
    {
        byte [] buf = new byte[IOUtils.DEFAULT_BUF_SIZE];

        try( FileStream stream = new FileStream( file ) )
        {
            @SuppressWarnings( "resource")
            IStream input = msgsStream.getItemsStream();

            input.seek( 0L );

            long length = input.getLength();
            long written = 0;

            while( written < length )
            {
                int count = input.read( buf );

                stream.write( buf, 0, count );

                written += count;
            }
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /**
     * @param file
     */
    private void openFile( File file )
    {
        clearMessages();

        try
        {
            msgsStream.setItemsFile( file );
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        pageStartIndex = -1;
        navigatePage( true, true );
        ResizingTableModelListener.resizeTable( table );
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
     * @param msg
     **************************************************************************/
    public void addMessage( NetMessage msg )
    {
        long count = msgsStream.getCount();
        long lastStartIndex = Math.max( count - 1, 0 );
        lastStartIndex = lastStartIndex - lastStartIndex % msgsPerPage;

        try
        {
            msgsStream.write( msg );
            count++;
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }

        long nextIndex = pageStartIndex + msgsPerPage;

        // LogUtils.printDebug(
        // "Adding message; last start = %d, next start = %d, current start =
        // %d, count = %d",
        // lastStartIndex, nextIndex, pageStartIndex, msgsStream.getCount() );

        if( lastStartIndex == pageStartIndex )
        {
            if( count > nextIndex )
            {
                navigatePage( count - 1 );
            }
            else
            {
                tableModel.addItem( msg );
                updateRowHeader( tableModel.getRowCount() );
            }
            ResizingTableModelListener.resizeTable( table );
        }

        setNavButtonsEnabled();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearMessages()
    {
        tableModel.clearItems();

        try
        {
            msgsStream.removeAll();
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }

        pageStartIndex = 0L;
        setNavButtonsEnabled();
        updateRowHeader( 0 );

        if( dialog != null && dialog.getView().isVisible() )
        {
            dialog.getView().setVisible( false );
        }
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setOpenVisible( boolean visible )
    {
        openButton.setVisible( visible );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IMessageFields
    {
        public int getFieldCount();

        public String getFieldName( int index );

        public String getFieldValue( NetMessage message, int index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageMouseListener extends MouseAdapter
    {
        private final NetMessagesView view;

        public MessageMouseListener( NetMessagesView view )
        {
            this.view = view;
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                long index = view.table.rowAtPoint( e.getPoint() ) +
                    view.pageStartIndex;

                view.showMessage( index );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class LocalDateTimeDecorator
        implements ITableCellLabelDecorator
    {
        private final DateTimeFormatter dtf;

        public LocalDateTimeDecorator()
        {
            this.dtf = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss.SSS" );
        }

        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            String text = "";

            if( value != null )
            {
                LocalDateTime ldt = ( LocalDateTime )value;
                text = ldt.format( dtf );
            }

            label.setText( text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class MessageNavView implements IDataView<NetMessage>
    {
        private final NetMessagesView msgsView;

        private final JPanel view;
        private final NetMessageView msgView;

        private final JButton prevButton;
        private final JButton nextButton;

        public MessageNavView( NetMessagesView msgsView,
            IStringWriter<NetMessage> msgWriter )
        {
            this.msgsView = msgsView;
            this.msgView = new NetMessageView( msgWriter );
            this.prevButton = new JButton();
            this.nextButton = new JButton();

            this.view = createView();

            setButtonsEnabled();
        }

        private JPanel createView()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( msgView.getView(), BorderLayout.CENTER );

            return panel;
        }

        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JToolBar();

            SwingUtils.setToolbarDefaults( toolbar );

            SwingUtils.addActionToToolbar( toolbar, createNavAction( false ),
                prevButton );

            SwingUtils.addActionToToolbar( toolbar, createNavAction( true ),
                nextButton );

            return toolbar;
        }

        private Action createNavAction( boolean forward )
        {
            ActionListener listener = ( e ) -> navigate( forward );
            Icon icon = IconConstants.getIcon(
                forward ? IconConstants.NAV_NEXT_16
                    : IconConstants.NAV_PREVIOUS_16 );
            String name = forward ? "Next Message" : "Previous Message";

            return new ActionAdapter( listener, name, icon );
        }

        private void navigate( boolean forward )
        {
            int inc = forward ? 1 : -1;
            long index = msgsView.table.getSelectedRow() +
                msgsView.pageStartIndex;
            long nextIndex = index + inc;

            msgsView.showMessage( nextIndex );

            setButtonsEnabled();
        }

        private void setButtonsEnabled()
        {
            long index = msgsView.table.getSelectedRow() +
                msgsView.pageStartIndex;
            long maxRow = msgsView.msgsStream.getCount() - 1;

            prevButton.setEnabled( index > 0 );
            nextButton.setEnabled( index > -1 && index < maxRow );
        }

        @Override
        public JPanel getView()
        {
            return view;
        }

        @Override
        public NetMessage getData()
        {
            return msgView.getData();
        }

        @Override
        public void setData( NetMessage data )
        {
            msgView.setData( data );

            setButtonsEnabled();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FontLabelTableCellRenderer
        implements ITableCellLabelDecorator
    {
        private final Font font;

        public FontLabelTableCellRenderer( Font font )
        {
            this.font = font;
        }

        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            label.setFont( font );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RowListModel extends AbstractListModel<String>
    {
        /**  */
        private static final long serialVersionUID = 404977197801943790L;
        /**  */
        private long rowStart;
        /**  */
        private int rowCount;

        public RowListModel()
        {
            rowStart = 0;
            rowCount = 1;
        }

        @Override
        public int getSize()
        {
            return rowCount;
        }

        public void setSize( int count )
        {
            rowCount = count;
        }

        public void setStart( long start )
        {
            rowStart = start;
        }

        @Override
        public String getElementAt( int index )
        {
            return Long.toString( rowStart + index + 1 );
        }
    }
}
