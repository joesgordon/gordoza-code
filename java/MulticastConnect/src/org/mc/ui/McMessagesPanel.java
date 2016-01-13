package org.mc.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexPanel;
import org.mc.McMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMessagesPanel extends JPanel
{
    /**  */
    private JCheckBox filterCheckBox;
    /**  */
    private JList<McMessage> displayList;
    /**  */
    private DefaultListModel<McMessage> msgModel;
    /**  */
    private List<McMessage> allMessages;

    /***************************************************************************
     * 
     **************************************************************************/
    public McMessagesPanel()
    {
        super( new GridBagLayout() );

        JButton clearButton = new JButton( "Clear" );
        GridBagConstraints constraints;

        msgModel = new DefaultListModel<McMessage>();
        allMessages = new ArrayList<McMessage>( 100 );

        displayList = new JList<McMessage>( msgModel );
        JScrollPane displayScrollPane = new JScrollPane( displayList );
        JScrollBar vScrollBar = displayScrollPane.getVerticalScrollBar();
        filterCheckBox = new JCheckBox( "Do not display sent messages" );

        clearButton.addActionListener( new ClearListener() );

        displayList.setCellRenderer( new HexMessagePanel() );
        displayList.addMouseListener( new MessageMouseListener() );
        vScrollBar.addAdjustmentListener( new EndScroller( vScrollBar ) );
        filterCheckBox.addActionListener( new FilterCheckListener() );

        setBorder(
            BorderFactory.createTitledBorder( "Sent/Received Messages" ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 6, 6, 6, 6 ), 0, 0 );
        add( filterCheckBox, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.CENTER,
            new Insets( 6, 0, 6, 6 ), 20, 10 );
        add( clearButton, constraints );

        constraints = new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        add( displayScrollPane, constraints );

        setMinimumSize( new Dimension( 200, 200 ) );
    }

    /***************************************************************************
     * @param hide
     **************************************************************************/
    private void hideSelfMessages( boolean hide )
    {
        msgModel.clear();

        for( McMessage msg : allMessages )
        {
            if( hide )
            {
                if( !msg.selfMessage )
                {
                    msgModel.addElement( msg );
                }
            }
            else
            {
                msgModel.addElement( msg );
            }
        }
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void addMessage( McMessage msg )
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

        public void adjustmentValueChanged( AdjustmentEvent e )
        {
            if( vScrollBar.getMaximum() > lastMaxScrollPos )
            {
                lastMaxScrollPos = vScrollBar.getMaximum();
                vScrollBar.setValue( lastMaxScrollPos );
            }
        }
    }

    private class MessageMouseListener extends MouseAdapter
    {
        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 )
            {
                Frame f = SwingUtils.getComponentsFrame( displayList );
                int index = displayList.locationToIndex( e.getPoint() );
                ListModel<McMessage> dlm = displayList.getModel();
                McMessage item = dlm.getElementAt( index );

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
