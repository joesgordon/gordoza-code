package chatterbox.ui;

import java.awt.*;
import java.time.LocalTime;

import javax.swing.*;

import org.jutils.ui.model.IDataView;

import chatterbox.data.ChatInfo;

public class ChatInfoView implements IDataView<ChatInfo>
{
    private static final int SIDE = 4;

    private final JPanel view;
    private final JLabel nameField;
    private final JLabel messageField;

    private ChatInfo info;

    public ChatInfoView()
    {
        this.nameField = new JLabel();
        this.messageField = new JLabel();
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        nameField.setFont(
            nameField.getFont().deriveFont( 16.0f ).deriveFont( Font.BOLD ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 2, SIDE, 2, SIDE ), 0, 0 );
        panel.add( nameField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, SIDE, 6, SIDE ), 0, 0 );
        panel.add( messageField, constraints );

        return panel;
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    @Override
    public ChatInfo getData()
    {
        return info;
    }

    @Override
    public void setData( ChatInfo data )
    {
        this.info = data;

        nameField.setText( data.name );
        messageField.setText(
            "The current time is" + LocalTime.now().toString() );
    }

    public void setColors( Color bgColor, Color fgColor )
    {
        nameField.setForeground( fgColor );
        messageField.setForeground( fgColor );
        view.setBackground( bgColor );
    }
}
