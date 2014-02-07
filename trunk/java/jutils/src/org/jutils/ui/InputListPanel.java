package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 */
public class InputListPanel extends JPanel implements ListSelectionListener,
    ActionListener
{
    private JTextField textfield;

    private JList<String> list;

    private JScrollPane scroll;

    /***************************************************************************
     * @param data
     * @param title
     **************************************************************************/
    public InputListPanel( String [] data, String title )
    {
        textfield = new JTextField( 5 );
        list = new JList<String>( data );
        scroll = new JScrollPane( list );

        setLayout( new GridBagLayout() );

        textfield.addActionListener( this );
        // list.setVisibleRowCount( 4 );
        list.addListSelectionListener( this );

        add( textfield, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        add( scroll, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setToolTipText( String text )
    {
        super.setToolTipText( text );
        textfield.setToolTipText( text );
        list.setToolTipText( text );
    }

    public void setSelected( String sel )
    {
        list.setSelectedValue( sel, true );
        list.ensureIndexIsVisible( list.getSelectedIndex() );
        textfield.setText( sel );
    }

    public String getSelected()
    {
        return textfield.getText();
    }

    public void setSelectedInt( int value )
    {
        setSelected( Integer.toString( value ) );
    }

    public int getSelectedInt()
    {
        try
        {
            return Integer.parseInt( getSelected() );
        }
        catch( NumberFormatException ex )
        {
            return -1;
        }
    }

    public void valueChanged( ListSelectionEvent e )
    {
        Object obj = list.getSelectedValue();
        if( obj != null )
        {
            textfield.setText( obj.toString() );
        }
    }

    public void actionPerformed( ActionEvent e )
    {
        ListModel<String> model = list.getModel();
        String key = textfield.getText().toLowerCase();
        for( int k = 0; k < model.getSize(); k++ )
        {
            String data = ( String )model.getElementAt( k );
            if( data.toLowerCase().startsWith( key ) )
            {
                list.setSelectedValue( data, true );
                break;
            }
        }
    }

    public void addListSelectionListener( ListSelectionListener lst )
    {
        list.addListSelectionListener( lst );
    }
}
