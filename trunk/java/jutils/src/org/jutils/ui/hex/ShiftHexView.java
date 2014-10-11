package org.jutils.ui.hex;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.*;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;
import org.jutils.utils.BitArray;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShiftHexView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final HexPanel hexPanel;
    /**  */
    private final JButton leftButton;
    /**  */
    private final JButton rightButton;
    /**  */
    private final JLabel offLabel;

    /**  */
    private BitBuffer orig;
    /**  */
    private BitBuffer buffer;

    /**  */
    private int bitOffset;

    /***************************************************************************
     * 
     **************************************************************************/
    public ShiftHexView()
    {
        this.hexPanel = new HexPanel();
        this.leftButton = new JButton();
        this.rightButton = new JButton();
        this.offLabel = new JLabel();

        this.view = createView();

        this.bitOffset = 0;

        setData( new byte[] { 0 } );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createButtons( panel ), BorderLayout.NORTH );
        panel.add( hexPanel.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createButtons( JPanel panel )
    {
        JToolBar toolbar = new JToolBar();
        Action action;
        Icon icon;
        KeyStroke key;
        InputMap inMap = panel.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = panel.getActionMap();

        SwingUtils.setToolbarDefaults( toolbar );

        // ---------------------------------------------------------------------
        // Setup left shift.
        // ---------------------------------------------------------------------
        icon = IconConstants.loader.getIcon( IconConstants.BACK_16 );
        action = new ActionAdapter( new ShiftListener( this, -1 ),
            "Shift Left", icon );
        SwingUtils.addActionToToolbar( toolbar, action, leftButton );

        key = KeyStroke.getKeyStroke( KeyEvent.VK_LEFT,
            InputEvent.ALT_DOWN_MASK, false );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "prevAction" );
        acMap.put( "prevAction", action );

        // ---------------------------------------------------------------------
        // Setup right shift.
        // ---------------------------------------------------------------------
        icon = IconConstants.loader.getIcon( IconConstants.FORWARD_16 );
        action = new ActionAdapter( new ShiftListener( this, 1 ),
            "Shift Right", icon );
        SwingUtils.addActionToToolbar( toolbar, action, rightButton );

        key = KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT,
            InputEvent.ALT_DOWN_MASK, false );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "nextAction" );
        acMap.put( "nextAction", action );

        // ---------------------------------------------------------------------
        // Setup find.
        // ---------------------------------------------------------------------
        icon = IconConstants.loader.getIcon( IconConstants.FIND_16 );
        action = new ActionAdapter( new FindListener( this ), "Find Bits", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        key = KeyStroke.getKeyStroke( "control F" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findAction" );
        acMap.put( "findAction", action );
        // action.putValue( Action.MNEMONIC_KEY, ( int )'n' );

        // key = KeyStroke.getKeyStroke( "F3" );
        // action.putValue( Action.ACCELERATOR_KEY, key );
        // inMap.put( key, "findNextAction" );
        // acMap.put( "findNextAction", action );
        //
        // key = KeyStroke.getKeyStroke( "shift F3" );
        // action.putValue( Action.ACCELERATOR_KEY, key );
        // inMap.put( key, "findPrevAction" );
        // acMap.put( "findPrevAction", action );

        toolbar.addSeparator();

        toolbar.add( offLabel );

        return toolbar;
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public void setData( byte [] bytes )
    {
        this.orig = new BitBuffer( bytes );
        this.buffer = new BitBuffer( Arrays.copyOf( bytes, bytes.length + 1 ) );
        this.bitOffset = 0;

        resetData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetData()
    {
        leftButton.setEnabled( bitOffset > 0 );
        rightButton.setEnabled( bitOffset < 7 );
        offLabel.setText( "bit: " + bitOffset );

        hexPanel.setBuffer( new ByteBuffer( buffer.buffer ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void shitTo()
    {
        int bit = bitOffset % 8;
        int b = bitOffset / 8;

        buffer.buffer[0] = 0;
        buffer.buffer[buffer.buffer.length - 1] = 0;

        orig.setPosition( 0, 0 );
        buffer.setPosition( b, bit );
        orig.writeTo( buffer, orig.bitCount() );

        resetData();
    }

    /***************************************************************************
     * @param parent
     * @return
     **************************************************************************/
    public static BitArray promptForBinaryString( Component parent )
    {
        BitArray bits = new BitArray();
        String binaryString = JOptionPane.showInputDialog( parent,
            "Enter binary string to be found:", "Enter Search String",
            JOptionPane.QUESTION_MESSAGE );

        if( binaryString != null )
        {
            try
            {
                binaryString = binaryString.replace( " ", "" );

                bits.set( binaryString );
            }
            catch( NumberFormatException ex )
            {
                JOptionPane.showMessageDialog( parent,
                    "Cannot parse " + binaryString + " as a binary string:" +
                        Utils.NEW_LINE + ex.getMessage(), "Parse Error",
                    JOptionPane.ERROR_MESSAGE );
                bits = null;
            }
        }
        else
        {
            bits = null;
        }

        return bits;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ShiftListener implements ActionListener
    {
        private final ShiftHexView view;
        private final int dir;

        public ShiftListener( ShiftHexView view, int dir )
        {
            this.view = view;
            this.dir = dir;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int off = view.bitOffset + dir;

            if( off > -1 && off < 8 )
            {
                view.bitOffset += dir;

                view.shitTo();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindListener implements ActionListener
    {
        private final ShiftHexView view;

        public FindListener( ShiftHexView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            BitArray bits = promptForBinaryString( view.view );

            if( bits != null )
            {
                try
                {
                    int start = view.hexPanel.getSelectedByte();

                    start = start > -1 ? start : 0;

                    LogUtils.printDebug( "Starting from " + start );

                    BitPosition pos = view.orig.find( bits, start );

                    if( pos != null )
                    {
                        view.bitOffset = ( 8 - pos.getBit() ) % 8;

                        view.shitTo();

                        view.resetData();

                        int off = pos.getByte();

                        off += pos.getBit() == 0 ? 0 : 1;

                        LogUtils.printDebug( "Found " + bits.toString() +
                            " @ " + off );

                        view.hexPanel.setSelected( off, off );
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( view.view,
                            "Pattern not found: " + bits.toString(),
                            "Pattern Not Found", JOptionPane.ERROR_MESSAGE );
                    }
                }
                catch( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog( view.view, "Cannot parse " +
                        bits.toString() + " as a binary string:" +
                        Utils.NEW_LINE + ex.getMessage(), "Parse Error",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }
}
