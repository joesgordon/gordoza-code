package org.jutils.ui.explorer;

import java.awt.*;

import javax.swing.*;

public class ProgramPanel extends JPanel
{
    private JLabel nameLabel = new JLabel();

    private JTextField pathField = new JTextField();

    private JButton browseButton = new JButton();

    private JTextArea argArea = new JTextArea();

    private JScrollPane argScrollPane = new JScrollPane( argArea );

    public ProgramPanel()
    {
        this.setLayout( new GridBagLayout() );

        this.add( new JLabel( "Name :" ),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        this.add( nameLabel,
            new GridBagConstraints( 1, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( new JLabel( "Path :" ),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        this.add( pathField,
            new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        this.add( browseButton,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( new JLabel( "Extension Specific Arguments" ),
            new GridBagConstraints( 0, 2, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        this.add( argScrollPane,
            new GridBagConstraints( 0, 3, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    }
}
