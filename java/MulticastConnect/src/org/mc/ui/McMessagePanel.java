package org.mc.ui;

import java.awt.*;

import javax.swing.*;

import org.mc.McMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMessagePanel extends JPanel
    implements ListCellRenderer<McMessage>
{
    /**  */
    private JLabel addressLabel;

    /**  */
    private JLabel addressField;

    /**  */
    private JTextArea contentsField;

    /**  */
    private JLabel timeLabel;

    /**  */
    private JLabel timeField;

    /**  */
    private Color backgroundColor;

    /**  */
    private Color foregroundColor;

    /**  */
    private Color altBgColor;

    /***************************************************************************
     * 
     **************************************************************************/
    public McMessagePanel()
    {
        timeLabel = new JLabel( "Date/Time:" );
        timeField = new JLabel();
        addressLabel = new JLabel( "Address:" );
        addressField = new JLabel();
        contentsField = new JTextArea();
        altBgColor = new Color( 0xeeeeee );

        setLayout( new GridBagLayout() );

        contentsField.setBorder( null );
        contentsField.setOpaque( false );
        contentsField.setEditable( false );

        add( addressLabel,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        add( addressField,
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        add( timeLabel,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        add( timeField,
            new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        add( contentsField,
            new GridBagConstraints( 0, 2, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ),
            new GridBagConstraints( 1, 10, 4, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        backgroundColor = Color.white;
        foregroundColor = contentsField.getForeground();

        this.setOpaque( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void setMessage( McMessage msg )
    {
        String selfStr = msg.selfMessage ? "(Self) " : "";
        addressField.setText( selfStr + msg.address );
        contentsField.setText(
            new String( msg.contents, 0, msg.contents.length - 1 ) );
        timeField.setText( msg.getDateTime() );
    }
}
