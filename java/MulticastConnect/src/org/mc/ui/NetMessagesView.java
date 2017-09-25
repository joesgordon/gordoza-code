package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.net.NetMessage;
import org.jutils.ui.event.ResizingTableModelListener;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexPanel;
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
    private final ITableItemsConfig<NetMessage> tableCfg;
    /**  */
    private final ItemsTableModel<NetMessage> tableModel;
    /**  */
    private final JTable table;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesView()
    {
        this.view = new JPanel( new BorderLayout() );
        this.tableCfg = new NetMessagesTableConfig();
        this.tableModel = new ItemsTableModel<>( tableCfg );
        this.table = new JTable( tableModel );

        table.setDefaultRenderer( LocalDateTime.class,
            new LabelTableCellRenderer( new LocalDateTimeDecorator() ) );
        table.setDefaultRenderer( InetAddress.class,
            new LabelTableCellRenderer( new InetAddressDecorator() ) );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        JButton clearButton = new JButton( "Clear" );

        JScrollPane displayScrollPane = new JScrollPane( table );
        JScrollBar vScrollBar = displayScrollPane.getVerticalScrollBar();

        clearButton.addActionListener( new ClearListener() );

        table.addMouseListener( new MessageMouseListener( this ) );
        vScrollBar.addAdjustmentListener( new EndScroller( vScrollBar ) );

        view.setBorder(
            BorderFactory.createTitledBorder( "Sent/Received Messages" ) );

        view.add( displayScrollPane, BorderLayout.CENTER );

        view.setMinimumSize( new Dimension( 625, 200 ) );
        view.setPreferredSize( new Dimension( 625, 200 ) );
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
    private class ClearListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            clearMessages();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class EndScroller implements AdjustmentListener
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
            if( e.getClickCount() == 2 )
            {
                Frame f = SwingUtils.getComponentsFrame( view.table );
                int index = view.table.rowAtPoint( e.getPoint() );
                NetMessage item = view.tableModel.getItem( index );

                JDialog d = new JDialog( f, "Message Contents", true );
                HexPanel p = new HexPanel();

                p.setBuffer( new ByteBuffer( item.contents ) );
                d.setContentPane( p.getView() );
                d.setSize( 640, 300 );
                d.setLocationRelativeTo( f );
                d.setVisible( true );
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
    private static class InetAddressDecorator
        implements ITableCellLabelDecorator
    {
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            String text = "";

            if( value != null )
            {
                InetAddress address = ( InetAddress )value;
                text = address.getHostAddress();
            }

            label.setText( text );
        }
    }
}
