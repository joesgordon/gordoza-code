package org.jutils.ui.net;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.IStringWriter;
import org.jutils.net.NetMessage;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.ResizingTableModelListener;
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
    private final JButton hexTextButton;
    /**  */
    private final NetMessageView msgView;

    /**  */
    private boolean isHex;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesView()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param fields
     **************************************************************************/
    public NetMessagesView( IMessageFields fields,
        IStringWriter<NetMessage> msgWriter )
    {
        this.tableCfg = new NetMessagesTableConfig( fields );
        this.tableModel = new ItemsTableModel<>( tableCfg );
        this.table = new JTable( tableModel );
        this.hexTextButton = new JButton();
        this.msgView = new NetMessageView( msgWriter );
        this.view = createView();
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

        JScrollPane displayScrollPane = new JScrollPane( table );
        JScrollBar vScrollBar = displayScrollPane.getVerticalScrollBar();

        vScrollBar.addAdjustmentListener( new EndScroller( vScrollBar ) );

        displayScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        displayScrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        panel.setBorder(
            BorderFactory.createTitledBorder( "Sent/Received Messages" ) );

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

        SwingUtils.addActionToToolbar( toolbar, createClearAction() );

        SwingUtils.addActionToToolbar( toolbar, createTextHexAction(),
            hexTextButton );
        hexTextButton.setText( "" );

        return toolbar;
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
     * @return
     **************************************************************************/
    private Action createClearAction()
    {
        ActionListener listener = ( e ) -> clearMessages();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 );

        return new ActionAdapter( listener, "Clear", icon );
    }

    public void showMessage( int index )
    {
        Frame f = SwingUtils.getComponentsFrame( table );
        NetMessage item = tableModel.getItem( index );

        JDialog d = new JDialog( f, "Message Contents", true );

        msgView.setData( item );
        d.setContentPane( msgView.getView() );
        d.setSize( 675, 400 );
        d.setLocationRelativeTo( f );
        d.setVisible( true );
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
        tableModel.addItem( msg );

        // LogUtils.printDebug( "Resizing table" );

        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearMessages()
    {
        tableModel.clearItems();
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
    private static class EndScroller implements AdjustmentListener
    {
        /**  */
        private JScrollBar vScrollBar;
        /**  */
        private int lastMaxScrollPos = 0;

        public EndScroller( JScrollBar vert )
        {
            vScrollBar = vert;
        }

        @Override
        public void adjustmentValueChanged( AdjustmentEvent e )
        {
            if( vScrollBar.getMaximum() > lastMaxScrollPos )
            {
                lastMaxScrollPos = vScrollBar.getMaximum();
                vScrollBar.setValue( lastMaxScrollPos );
            }
        }
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
                int index = view.table.rowAtPoint( e.getPoint() );

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
}
