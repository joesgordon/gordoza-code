package org.jutils.ui.hex;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.*;
import org.jutils.ui.model.IView;

//TODO comments

public class ShiftHexView implements IView<JComponent>
{
    private final JPanel view;
    private final HexPanel hexPanel;
    private final JButton leftButton;
    private final JButton rightButton;
    private final JLabel offLabel;

    private BitBuffer orig;
    private BitBuffer buffer;

    private int bitOffset;

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

    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createButtons(), BorderLayout.NORTH );
        panel.add( hexPanel.getView(), BorderLayout.CENTER );

        return panel;
    }

    private Component createButtons()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        leftButton.setIcon( IconConstants.loader.getIcon( IconConstants.BACK_16 ) );
        leftButton.addActionListener( new ShiftListener( this, -1 ) );
        toolbar.add( leftButton );

        rightButton.setIcon( IconConstants.loader.getIcon( IconConstants.FORWARD_16 ) );
        rightButton.addActionListener( new ShiftListener( this, 1 ) );
        toolbar.add( rightButton );

        JButton button = new JButton();
        button.setIcon( IconConstants.loader.getIcon( IconConstants.FIND_16 ) );
        button.addActionListener( new FindListener( this ) );
        toolbar.add( button );

        toolbar.addSeparator();

        toolbar.add( offLabel );

        return toolbar;
    }

    public void setData( byte [] bytes )
    {
        this.orig = new BitBuffer( bytes );
        this.buffer = new BitBuffer( Arrays.copyOf( bytes, bytes.length + 1 ) );
        this.bitOffset = 0;

        resetData();
    }

    private void resetData()
    {
        leftButton.setEnabled( bitOffset > 0 );
        rightButton.setEnabled( bitOffset < 7 );
        offLabel.setText( "bit: " + bitOffset );

        hexPanel.setBuffer( new ByteBuffer( buffer.buffer ) );
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

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
            view.bitOffset += dir;

            view.shitTo();
        }
    }

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
            String binaryString = JOptionPane.showInputDialog( view.view,
                "Enter binary string to be found:", "Enter Search String",
                JOptionPane.QUESTION_MESSAGE );

            if( binaryString != null )
            {
                List<Boolean> bits;

                try
                {
                    binaryString = binaryString.replace( " ", "" );

                    bits = NumberParsingUtils.fromBinaryString( binaryString );

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

                        LogUtils.printDebug( "Found " + binaryString + " @ " +
                            off );

                        view.hexPanel.setSelected( off, off );
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( view.view,
                            "Pattern not found: " + binaryString,
                            "Pattern Not Found", JOptionPane.ERROR_MESSAGE );
                    }
                }
                catch( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog( view.view, "Cannot parse " +
                        binaryString + " as a binary string:" + Utils.NEW_LINE +
                        ex.getMessage(), "Parse Error",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }
}
