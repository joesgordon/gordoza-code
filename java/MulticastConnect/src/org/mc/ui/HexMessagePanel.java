package org.mc.ui;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexTable;
import org.mc.McMessage;

public class HexMessagePanel extends JPanel implements
    ListCellRenderer<McMessage>
{
    private JLabel addressLabel;

    private JLabel addressField;

    private HexTable contentsField;

    private JLabel timeLabel;

    private JLabel timeField;

    private Color backgroundColor;

    private Color foregroundColor;

    private Color altBgColor;

    public HexMessagePanel()
    {
        timeLabel = new JLabel( "Date/Time:" );
        timeField = new JLabel();
        addressLabel = new JLabel( "Address:" );
        addressField = new JLabel();
        contentsField = new HexTable();
        altBgColor = new Color( 0xeeeeee );

        setLayout( new GridBagLayout() );

        // contentsField.setBorder( BorderFactory.createLineBorder( Color.red )
        // );
        contentsField.setOpaque( false );
        contentsField.setFocusable( false );
        contentsField.setCellSelectionEnabled( false );

        add( addressLabel, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        add( addressField, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );

        add( timeLabel, new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        add( timeField, new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );

        add( contentsField, new GridBagConstraints( 0, 2, 4, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );

        // add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 1, 10, 4,
        // 1,
        // 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
        // new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        backgroundColor = Color.white;
        foregroundColor = contentsField.getForeground();

        this.setOpaque( true );
    }

    @Override
    public Component getListCellRendererComponent(
        JList<? extends McMessage> list, McMessage value, int index,
        boolean isSelected, boolean cellHasFocus )
    {
        setMessage( value );

        Color fg = isSelected ? list.getSelectionForeground() : foregroundColor;
        Color bg = index % 2 == 1 ? altBgColor : backgroundColor;
        bg = isSelected ? list.getSelectionBackground() : bg;

        addressLabel.setForeground( fg );
        addressField.setForeground( fg );
        contentsField.setForeground( fg );
        timeLabel.setForeground( fg );
        timeField.setForeground( fg );

        this.setBackground( bg );
        contentsField.setBackground( bg );

        return this;
    }

    public void setMessage( McMessage msg )
    {
        String selfStr = msg.selfMessage ? "(Self) " : "";
        byte[] bytes = msg.contents;
        int len = bytes.length > 0x20 ? 0x20 : bytes.length;
        bytes = Arrays.copyOf( msg.contents, len );

        addressField.setText( selfStr + msg.address );
        contentsField.setBuffer( new ByteBuffer( bytes ) );
        timeField.setText( msg.getDateTime() );
    }

    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new HexMessageApp() );
    }
}

class HexMessageApp implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();

        HexMessagePanel panel = new HexMessagePanel();

        frame.setContentPane( panel );

        frame.setSize( 600, 200 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        McMessage msg = new McMessage();

        msg.contents = "So long and thanks f".getBytes();

        panel.setMessage( msg );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
