package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.net.NetMessage;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexPanel;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessagesPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JCheckBox filterCheckBox;
    /**  */
    private final JList<NetMessage> displayList;
    /**  */
    private final DefaultListModel<NetMessage> msgModel;
    /**  */
    private final List<NetMessage> allMessages;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        JButton clearButton = new JButton( "Clear" );
        GridBagConstraints constraints;

        msgModel = new DefaultListModel<NetMessage>();
        allMessages = new ArrayList<NetMessage>( 100 );

        displayList = new JList<NetMessage>( msgModel );
        JScrollPane displayScrollPane = new JScrollPane( displayList );
        JScrollBar vScrollBar = displayScrollPane.getVerticalScrollBar();
        filterCheckBox = new JCheckBox( "Do not display sent messages" );

        clearButton.addActionListener( new ClearListener() );

        displayList.setCellRenderer( new HexMessagePanel() );
        displayList.addMouseListener( new MessageMouseListener() );
        vScrollBar.addAdjustmentListener( new EndScroller( vScrollBar ) );
        filterCheckBox.addActionListener( new FilterCheckListener() );

        view.setBorder(
            BorderFactory.createTitledBorder( "Sent/Received Messages" ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 6, 6, 6, 6 ), 0, 0 );
        view.add( filterCheckBox, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.CENTER,
            new Insets( 6, 0, 6, 6 ), 20, 10 );
        view.add( clearButton, constraints );

        constraints = new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        view.add( displayScrollPane, constraints );

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
     * @param hide
     **************************************************************************/
    private void hideSelfMessages( boolean hide )
    {
        msgModel.clear();

        for( NetMessage msg : allMessages )
        {
            msgModel.addElement( msg );
        }
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void addMessage( NetMessage msg )
    {
        allMessages.add( msg );
        if( !filterCheckBox.isSelected() )
        {
            msgModel.addElement( msg );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearMessages()
    {
        msgModel.clear();
        allMessages.clear();
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
    private class FilterCheckListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JCheckBox cb = ( JCheckBox )e.getSource();
            hideSelfMessages( cb.isSelected() );
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
    private class MessageMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 )
            {
                Frame f = SwingUtils.getComponentsFrame( displayList );
                int index = displayList.locationToIndex( e.getPoint() );
                ListModel<NetMessage> dlm = displayList.getModel();
                NetMessage item = dlm.getElementAt( index );

                displayList.ensureIndexIsVisible( index );

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
}
