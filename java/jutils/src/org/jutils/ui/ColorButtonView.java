package org.jutils.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorButtonView implements IDataView<Color>
{
    /**  */
    private final JButton button;
    /**  */
    private final ColorIcon icon;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColorButtonView()
    {
        this( Color.red );
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public ColorButtonView( Color c )
    {
        icon = new ColorIcon( c, 32 );
        button = new JButton( icon );

        Dimension dim = button.getPreferredSize();
        dim.width = dim.height;
        button.setPreferredSize( dim );
        button.setMinimumSize( dim );
        button.setMaximumSize( dim );

        button.addActionListener( new ColorButtonListener( button ) );

        setData( c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JButton getView()
    {
        return button;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Color getData()
    {
        return icon.getColor();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Color color )
    {
        icon.setColor( color );
        button.setText( color.toString() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ColorButtonListener implements ActionListener
    {
        private final JButton button;

        public ColorButtonListener( JButton button )
        {
            this.button = button;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Color c = JColorChooser.showDialog( button, "Choose new color",
                button.getBackground() );

            button.setBackground( c );
        }
    }
}
