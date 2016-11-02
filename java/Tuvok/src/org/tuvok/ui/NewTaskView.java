package org.tuvok.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class NewTaskView implements IView<JPanel>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     *
     **************************************************************************/
    public NewTaskView()
    {
        this.view = createMainPanel();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JTextField jTextField1 = new JTextField();
        JLabel nameLabel = new JLabel( "Name :" );
        JPanel panel1 = new JPanel( new GridBagLayout() );

        jTextField1.setColumns( 15 );

        panel1.add( nameLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 5, 5, 5, 5 ), 0, 0 ) );
        panel1.add( jTextField1,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 5, 5, 5, 5 ), 0, 0 ) );

        panel1.add( Box.createHorizontalStrut( 0 ),
            new GridBagConstraints( 0, 1, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return panel1;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }
}
