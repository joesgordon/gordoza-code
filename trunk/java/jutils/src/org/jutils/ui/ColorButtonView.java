package org.jutils.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
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

    /**  */
    private final ItemActionList<Color> updateListeners;
    /**  */
    private boolean showDescription;

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
        this( c, 32 );
    }

    /***************************************************************************
     * @param c
     * @param size
     **************************************************************************/
    public ColorButtonView( Color c, int size )
    {
        this( c, size, true );
    }

    public ColorButtonView( Color c, int size, boolean showDescription )
    {
        this.icon = new ColorIcon( c, size );
        this.button = new JButton( icon );
        this.updateListeners = new ItemActionList<>();
        this.showDescription = showDescription;

        // Dimension dim = button.getPreferredSize();
        // dim.width = dim.height;
        // button.setPreferredSize( dim );
        // button.setMinimumSize( dim );
        // button.setMaximumSize( dim );

        button.addActionListener( new ColorButtonListener( this ) );

        setData( c );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addUpdateListener( ItemActionListener<Color> l )
    {
        updateListeners.addListener( l );
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
        String text = String.format( "%02X%02X%02X:%02X", color.getRed(),
            color.getGreen(), color.getBlue(), color.getAlpha() );
        icon.setColor( color );
        if( showDescription )
        {
            button.setText( text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ColorButtonListener implements ActionListener
    {
        private final ColorButtonView view;

        public ColorButtonListener( ColorButtonView button )
        {
            this.view = button;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Color c = JColorChooser.showDialog( view.getView(),
                "Choose new color", view.getData() );

            if( c != null )
            {
                view.setData( c );
                view.updateListeners.fireListeners( view, c );
            }
        }
    }
}
