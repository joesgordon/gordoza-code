package chatterbox.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import chatterbox.model.IChatRoom;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConfigDialog extends JDialog
{
    private JTextField nameField;

    private JTextField addressField;

    private JTextField portField;

    /**
     * @param frame
     */
    public ConfigDialog( final JFrame frame, final IChatRoom chat )
    {
        super( frame, "Chat Configuration", true );

        // ---------------------------------------------------------------------
        // Setup the button panel.
        // ---------------------------------------------------------------------
        JPanel buttonPanel = new JPanel( new GridBagLayout() );
        JButton okButton = new JButton( "Ok" );
        JButton cancelButton = new JButton( "Cancel" );

        okButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                String username = nameField.getText();
                String address = addressField.getText();
                int port = Integer.parseInt( portField.getText() );

                ConfigDialog.this.dispose();

                chat.getLocalUser().setDisplayName( username );

                if( !address.equals( chat.getAddress() ) ||
                    port != chat.getPort() )
                {
                    try
                    {
                        chat.disconnect();
                        chat.connect( address, port );
                    }
                    catch( IOException ex )
                    {
                        JOptionPane.showMessageDialog( frame, ex.getMessage(),
                            "Error Connecting", JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
        } );

        buttonPanel.add( okButton, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 10 ), 50, 5 ) );
        buttonPanel.add( cancelButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 30, 5 ) );

        // ---------------------------------------------------------------------
        // Setup the content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        JLabel usernameLabel = new JLabel( "Username: " );
        nameField = new JTextField( 5 );
        JLabel addressLabel = new JLabel( "Address: " );
        addressField = new JTextField( 5 );
        JLabel portLabel = new JLabel( "Port: " );
        portField = new JTextField( 5 );

        nameField.setText( chat.getLocalUser().getDisplayName() );
        addressField.setText( chat.getAddress() );
        portField.setText( "" + chat.getPort() );

        contentPanel.add( usernameLabel, new GridBagConstraints( 0, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 4, 10, 4, 4 ), 0, 0 ) );
        contentPanel.add( nameField, new GridBagConstraints( 1, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 10 ), 0, 0 ) );

        contentPanel.add( addressLabel, new GridBagConstraints( 0, 1, 1, 1,
            0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 4, 10, 4, 4 ), 0, 0 ) );
        contentPanel.add( addressField, new GridBagConstraints( 1, 1, 1, 1,
            1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 10 ), 0, 0 ) );

        contentPanel.add( portLabel, new GridBagConstraints( 0, 2, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                4, 10, 4, 4 ), 0, 0 ) );
        contentPanel.add( portField, new GridBagConstraints( 1, 2, 1, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 10 ), 0, 0 ) );

        contentPanel.add( buttonPanel, new GridBagConstraints( 0, 3, 2, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 10, 4, 10 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup the dialog.
        // ---------------------------------------------------------------------
        setSize( 400, 175 );
        setContentPane( contentPanel );
        validate();
        setLocationRelativeTo( frame );
        setVisible( true );
    }
}
