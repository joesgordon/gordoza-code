package org.mc.ui;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.ui.hex.ByteBuffer;
import org.jutils.ui.hex.HexTable;
import org.jutils.ui.model.IView;
import org.mc.McMessage;
import org.mc.io.TimeDescriptor;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexMessagePanel
    implements IView<JPanel>, ListCellRenderer<McMessage>
{
    /**  */
    private final JPanel view;
    /**  */
    private final HexTable contentsField;
    /**  */
    private final JLabel timeLabel;
    /**  */
    private final JLabel timeField;
    /**  */
    private final Color backgroundColor;
    /**  */
    private final Color foregroundColor;
    /**  */
    private final Color altBgColor;

    /**  */
    private final TimeDescriptor timeDesc;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexMessagePanel()
    {
        this.view = new JPanel();
        this.timeLabel = new JLabel( "Date/Time:" );
        this.timeField = new JLabel();
        this.contentsField = new HexTable();
        this.altBgColor = new Color( 0xeeeeee );
        this.timeDesc = new TimeDescriptor();

        view.setLayout( new GridBagLayout() );

        // contentsField.setBorder( BorderFactory.createLineBorder( Color.red )
        // );
        contentsField.setOpaque( false );
        contentsField.setFocusable( false );
        contentsField.setCellSelectionEnabled( false );

        view.add( timeLabel,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        view.add( timeField,
            new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        view.add( contentsField,
            new GridBagConstraints( 0, 2, 4, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 1, 10, 4,
        // 1,
        // 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
        // new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        backgroundColor = Color.white;
        foregroundColor = contentsField.getForeground();

        view.setOpaque( true );
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

        contentsField.setForeground( fg );
        timeLabel.setForeground( fg );
        timeField.setForeground( fg );

        view.setBackground( bg );
        contentsField.setBackground( bg );

        return view;
    }

    public void setMessage( McMessage msg )
    {
        byte[] bytes = msg.contents;
        int len = bytes.length > 0x20 ? 0x20 : bytes.length;
        bytes = Arrays.copyOf( msg.contents, len );

        contentsField.setBuffer( new ByteBuffer( bytes ) );
        timeField.setText( timeDesc.getDescription( msg.time ) );
    }

    @Override
    public JPanel getView()
    {
        return view;
    }
}
