package org.jutils.ui.net;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
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
        this.tableCfg = new NetMessagesTableConfig();
        this.tableModel = new ItemsTableModel<>( tableCfg );
        this.table = new JTable( tableModel );
        this.view = createView();
    }

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

    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createClearAction() );

        return toolbar;
    }

    private Action createClearAction()
    {
        ActionListener listener = ( e ) -> clearMessages();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 );

        return new ActionAdapter( listener, "Clear", icon );
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
            if( SwingUtilities.isLeftMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                Frame f = SwingUtils.getComponentsFrame( view.table );
                int index = view.table.rowAtPoint( e.getPoint() );
                NetMessage item = view.tableModel.getItem( index );

                JDialog d = new JDialog( f, "Message Contents", true );
                NetMessageView p = new NetMessageView();

                p.setData( item );
                d.setContentPane( p.getView() );
                d.setSize( 675, 400 );
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
}
