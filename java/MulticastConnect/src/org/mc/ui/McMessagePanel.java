package org.mc.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.net.NetMessage;
import org.mc.io.TimeDescriptor;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMessagePanel implements ListCellRenderer<NetMessage>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel addressLabel;
    /**  */
    private final JTextArea contentsField;
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
    public McMessagePanel()
    {
        this.view = new JPanel( new GridBagLayout() );
        this.timeLabel = new JLabel( "Date/Time:" );
        this.timeField = new JLabel();
        this.addressLabel = new JLabel( "Address:" );
        this.contentsField = new JTextArea();
        this.altBgColor = new Color( 0xeeeeee );
        this.timeDesc = new TimeDescriptor();

        contentsField.setBorder( null );
        contentsField.setOpaque( false );
        contentsField.setEditable( false );

        view.add( addressLabel,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        view.add( timeLabel,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        view.add( timeField,
            new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        view.add( contentsField,
            new GridBagConstraints( 0, 2, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        view.add( Box.createVerticalStrut( 0 ),
            new GridBagConstraints( 1, 10, 4, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        backgroundColor = Color.white;
        foregroundColor = contentsField.getForeground();

        view.setOpaque( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getListCellRendererComponent(
        JList<? extends NetMessage> list, NetMessage value, int index,
        boolean isSelected, boolean cellHasFocus )
    {
        setMessage( value );

        Color fg = isSelected ? list.getSelectionForeground() : foregroundColor;
        Color bg = index % 2 == 1 ? altBgColor : backgroundColor;
        bg = isSelected ? list.getSelectionBackground() : bg;

        addressLabel.setForeground( fg );
        contentsField.setForeground( fg );
        timeLabel.setForeground( fg );
        timeField.setForeground( fg );

        view.setBackground( bg );
        contentsField.setBackground( bg );

        return view;
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void setMessage( NetMessage msg )
    {
        contentsField.setText(
            new String( msg.contents, 0, msg.contents.length - 1 ) );
        timeField.setText( timeDesc.getDescription( msg.time ) );
    }
}
