package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.*;

import org.jutils.IconConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McConfigurationPanel extends JPanel
{
    /**  */
    public static final String bindText = "Bind";
    /**  */
    public static final String unbindText = "Unbind";

    /**  */
    private final JButton bindButton;
    /**  */
    private final ImageIcon checkIcon;
    /**  */
    private final ImageIcon deleteIcon;
    /**  */
    private final JTextField addressField;
    /**  */
    private final JTextField portField;
    /**  */
    private final JTextField ttlField;
    /**  */
    private final JTextField msgSizeField;
    /**  */
    private final JComboBox nicComboBox;

    /***************************************************************************
     * 
     **************************************************************************/
    public McConfigurationPanel()
    {
        checkIcon = IconConstants.getIcon( IconConstants.CHECK_16 );
        deleteIcon = IconConstants.getIcon( IconConstants.STOP_16 );

        addressField = new JTextField( "224.69.69.69" );
        portField = new JTextField( "6969" );
        ttlField = new JTextField( "10" );
        msgSizeField = new JTextField( "65535" );
        bindButton = new JButton();

        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createTitledBorder( "Configuration" ) );

        addressField.setColumns( 10 );
        portField.setColumns( 10 );
        ttlField.setColumns( 10 );
        msgSizeField.setColumns( 10 );

        addressField.setMinimumSize( addressField.getPreferredSize() );
        portField.setMinimumSize( portField.getPreferredSize() );
        ttlField.setMinimumSize( ttlField.getPreferredSize() );
        msgSizeField.setMinimumSize( msgSizeField.getPreferredSize() );

        bindButton.setIcon( checkIcon );
        bindButton.setText( bindText );
        nicComboBox = new JComboBox();

        try
        {
            Enumeration<NetworkInterface> nets;
            boolean selected = false;
            NicComboItem defaultNicItem = null;

            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
                if( inetAddresses.hasMoreElements() )
                {
                    NicComboItem nicItem = new NicComboItem( nic );
                    nicComboBox.addItem( nicItem );

                    if( !selected )
                    {
                        if( !nic.isVirtual() && !nic.isLoopback() )
                        {
                            defaultNicItem = nicItem;
                            selected = true;
                        }
                        else
                        {
                            defaultNicItem = nicItem;
                        }
                    }
                }
            }

            nicComboBox.setSelectedItem( defaultNicItem );
        }
        catch( SocketException ex )
        {
            ;
        }

        add( new JLabel( "Address:" ), new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                6, 6, 6, 6 ), 0, 0 ) );
        add( addressField, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 6, 0,
                6, 6 ), 0, 0 ) );
        add( new JLabel( "TTL:" ), new GridBagConstraints( 2, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                6, 0, 6, 6 ), 0, 0 ) );
        add( ttlField, new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 6, 0,
                6, 6 ), 0, 0 ) );

        add( new JLabel( "Port:" ), new GridBagConstraints( 0, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                0, 6, 6, 6 ), 0, 0 ) );
        add( portField, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 0,
                6, 6 ), 0, 0 ) );
        add( new JLabel( "Message Size:" ), new GridBagConstraints( 2, 1, 1, 1,
            0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 6, 6 ), 0, 0 ) );
        add( msgSizeField, new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 0,
                6, 6 ), 0, 0 ) );

        add( new JLabel( "NIC:" ), new GridBagConstraints( 0, 2, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                0, 6, 6, 6 ), 0, 0 ) );
        add( nicComboBox, new GridBagConstraints( 1, 2, 4, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 6, 6 ), 0, 0 ) );

        add( bindButton, new GridBagConstraints( 4, 0, 1, 2, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 6,
                0, 6, 6 ), 0, 10 ) );
    }

    public NetworkInterface getNic()
    {
        NetworkInterface nic = null;

        if( nicComboBox.getItemCount() > 0 )
        {
            nic = ( ( NicComboItem )nicComboBox.getSelectedItem() ).getNic();
        }

        return nic;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBound()
    {
        return bindButton.getText().equals( bindText );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getAddress()
    {
        return addressField.getText();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getPort()
    {
        return Integer.parseInt( portField.getText() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTTL()
    {
        return Integer.parseInt( ttlField.getText() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMessageSize()
    {
        return Integer.parseInt( msgSizeField.getText() );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addBindActionListener( ActionListener l )
    {
        bindButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeBindActionListener( ActionListener l )
    {
        bindButton.removeActionListener( l );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setBindEnabled( boolean enabled )
    {
        bindButton.setEnabled( enabled );
    }

    /***************************************************************************
     * @param bound
     **************************************************************************/
    public void setBound( boolean bound )
    {
        bindButton.setText( bound ? unbindText : bindText );
        bindButton.setIcon( bound ? deleteIcon : checkIcon );

        addressField.setEnabled( !bound );
        portField.setEnabled( !bound );
        ttlField.setEnabled( !bound );
        msgSizeField.setEnabled( !bound );
        nicComboBox.setEnabled( !bound );
    }

    private static class NicComboItem
    {
        private final NetworkInterface nic;

        public NicComboItem( NetworkInterface nic )
        {
            this.nic = nic;
        }

        public NetworkInterface getNic()
        {
            return nic;
        }

        public String toString()
        {
            return nic.getDisplayName();
        }
    }
}
