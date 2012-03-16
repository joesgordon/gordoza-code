package org.tuvok;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 *
 */
public class NewTaskDialog extends JDialog
{
    private JPanel panel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel nameLabel = new JLabel();
    private JTextField jTextField1 = new JTextField();
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();

    public NewTaskDialog( Frame owner, String title, boolean modal )
    {
        super( owner, title, modal );
        try
        {
            setDefaultCloseOperation( DISPOSE_ON_CLOSE );
            jbInit();
            pack();
        }
        catch( Exception exception )
        {
            exception.printStackTrace();
        }
    }

    public NewTaskDialog()
    {
        this( new Frame(), "NewTaskDialog", false );
    }

    private void jbInit() throws Exception
    {
        panel1.setLayout( gridBagLayout1 );
        nameLabel.setText( "Name :" );
        jTextField1.setText( "" );
        jTextField1.setColumns( 15 );
        jButton1.setText( "OK" );
        jButton2.setText( "Cancel" );
        jButton2.addActionListener( new NewTaskDialog_jButton2_actionAdapter(
            this ) );
        getContentPane().add( panel1 );
        panel1.add( nameLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 5, 5,
                5, 5 ), 0, 0 ) );
        panel1.add( jTextField1, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 5, 5,
                5, 5 ), 0, 0 ) );

        panel1.add( jPanel2, new GridBagConstraints( 0, 1, 3, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        panel1.add( jPanel1, new GridBagConstraints( 0, 2, 3, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );
        jPanel2.add( jButton1 );
        jPanel2.add( jButton2 );
    }

    public void jButton2_actionPerformed( ActionEvent e )
    {
        ;
    }
}

class NewTaskDialog_jButton2_actionAdapter implements ActionListener
{
    private NewTaskDialog adaptee;

    NewTaskDialog_jButton2_actionAdapter( NewTaskDialog adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.jButton2_actionPerformed( e );
    }
}
