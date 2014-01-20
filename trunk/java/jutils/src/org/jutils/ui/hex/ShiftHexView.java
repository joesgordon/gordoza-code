package org.jutils.ui.hex;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.io.BitBuffer;
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

        resetToolbar();
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

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        leftButton.setIcon( IconConstants.loader.getIcon( IconConstants.BACK_16 ) );
        leftButton.addActionListener( new ShiftListener( this, -1 ) );
        toolbar.add( leftButton );

        rightButton.setIcon( IconConstants.loader.getIcon( IconConstants.FORWARD_16 ) );
        rightButton.addActionListener( new ShiftListener( this, 1 ) );
        toolbar.add( rightButton );

        toolbar.addSeparator();

        toolbar.add( offLabel );

        return toolbar;
    }

    public void setData( byte[] bytes )
    {
        this.orig = new BitBuffer( bytes );
        this.buffer = new BitBuffer( Arrays.copyOf( bytes, bytes.length + 1 ) );
        this.bitOffset = 0;

        hexPanel.setBuffer( new ByteBuffer( buffer.buffer ) );

        resetToolbar();
    }

    private void resetToolbar()
    {
        leftButton.setEnabled( bitOffset > 0 );
        rightButton.setEnabled( bitOffset < 7 );
        offLabel.setText( "bit: " + bitOffset );
    }

    @Override
    public JComponent getView()
    {
        return view;
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

            int bit = view.bitOffset % 8;
            int b = view.bitOffset / 8;

            view.buffer.buffer[0] = 0;
            view.buffer.buffer[view.buffer.buffer.length - 1] = 0;

            view.orig.setPosition( 0, 0 );
            view.buffer.setPosition( b, bit );
            view.orig.writeTo( view.buffer, view.orig.bitCount() );

            view.resetToolbar();

            view.hexPanel.setBuffer( new ByteBuffer( view.buffer.buffer ) );
        }
    }
}
