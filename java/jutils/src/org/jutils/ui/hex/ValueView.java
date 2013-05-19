package org.jutils.ui.hex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.ByteBuffer;

import javax.swing.*;

import org.jutils.ui.model.IView;

public class ValueView implements IView<JPanel>
{
    private static final int DEFAULT_COLS = 17;
    private static final Color[] DEFAULT_PALETTE = new Color[] {
        new Color( 0xFF0000 ), new Color( 0x00FF00 ), new Color( 0x0000FF ),
        new Color( 0xF00FF0 ), new Color( 0x3A8EA7 ), new Color( 0x770000 ),
        new Color( 0x007700 ), new Color( 0x000077 ), new Color( 0xFFFF00 ),
        new Color( 0xe74265 ) };

    private final JPanel view;

    private final ButtonGroup highlightGroup;

    private final JTextField sint08Field;
    private final JTextField uint08Field;

    private final JTextField sint16Field;
    private final JTextField uint16Field;

    private final JTextField sint32Field;
    private final JTextField uint32Field;

    private final JTextField sint64Field;
    private final JTextField uint64Field;

    private final JTextField floatField;
    private final JTextField doubleField;

    public ValueView()
    {
        this.highlightGroup = new ButtonGroup();

        this.sint08Field = new JTextField( DEFAULT_COLS );
        this.uint08Field = new JTextField( DEFAULT_COLS );

        this.sint16Field = new JTextField( DEFAULT_COLS );
        this.uint16Field = new JTextField( DEFAULT_COLS );

        this.sint32Field = new JTextField( DEFAULT_COLS );
        this.uint32Field = new JTextField( DEFAULT_COLS );

        this.sint64Field = new JTextField( DEFAULT_COLS );
        this.uint64Field = new JTextField( DEFAULT_COLS );

        this.floatField = new JTextField( DEFAULT_COLS );
        this.doubleField = new JTextField( DEFAULT_COLS );

        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JRadioButton rbutton;

        int row = 0;

        rbutton = new JRadioButton();
        rbutton.setSelected( true );
        addField( panel, "Signed Byte:", row++, rbutton, sint08Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Unsigned Byte:", row++, rbutton, uint08Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Signed Short:", row++, rbutton, sint16Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Unsigned Short:", row++, rbutton, uint16Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Signed Int:", row++, rbutton, sint32Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Unsigned Int:", row++, rbutton, uint32Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Signed Long:", row++, rbutton, sint64Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Unsigned Long:", row++, rbutton, uint64Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Float:", row++, rbutton, floatField );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        addField( panel, "Double:", row++, rbutton, doubleField );
        highlightGroup.add( rbutton );

        return panel;
    }

    private void addField( JPanel panel, String fieldName, int row,
        JRadioButton rbutton, JTextField field )
    {
        JLabel label = new JLabel( fieldName );
        GridBagConstraints constraints;

        rbutton.setMinimumSize( rbutton.getPreferredSize() );
        field.setMinimumSize( field.getPreferredSize() );

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 2 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 2,
                4, 2 ), 0, 0 );
        panel.add( field, constraints );

        constraints = new GridBagConstraints( 2, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 2 ), 0, 0 );
        panel.add( rbutton, constraints );

        constraints = new GridBagConstraints( 3, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 2,
                4, 4 ), 0, 0 );
        panel.add( createColorButton( row ), constraints );
    }

    private Component createColorButton( int row )
    {
        JButton button = new JButton( " " );

        Dimension dim = button.getPreferredSize();
        dim.width = dim.height;
        button.setPreferredSize( dim );
        button.setMinimumSize( dim );
        button.setMaximumSize( dim );

        button.setBackground( DEFAULT_PALETTE[row] );

        button.addActionListener( new ColorButtonListener( button ) );

        return button;
    }

    public void setBytes( byte[] bytes, int offset )
    {
        sint08Field.setText( "N/A" );
        uint08Field.setText( "N/A" );
        sint16Field.setText( "N/A" );
        uint16Field.setText( "N/A" );
        sint32Field.setText( "N/A" );
        uint32Field.setText( "N/A" );
        sint64Field.setText( "N/A" );
        uint64Field.setText( "N/A" );
        floatField.setText( "N/A" );
        doubleField.setText( "N/A" );

        int remaining = bytes.length - offset;
        ByteBuffer buf = ByteBuffer.wrap( bytes, offset, remaining > 8 ? 8
            : remaining );

        sint08Field.setText( "" + bytes[offset] );
        uint08Field.setText( toUint08( bytes[offset] ) );
    }

    private String toUint08( byte b )
    {
        int i = b;

        if( i < 0 )
        {
            i += 512;
        }

        return "" + i;
    }

    @Override
    public JPanel getView()
    {
        return view;
    }

    private class ColorButtonListener implements ActionListener
    {
        private final JButton button;

        public ColorButtonListener( JButton button )
        {
            this.button = button;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Color c = JColorChooser.showDialog( button, "Choose new color",
                button.getBackground() );

            button.setBackground( c );
        }
    }
}
