package org.jutils.chart.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChadgetPanel extends JComponent
{
    private IChadget object;
    private final Object lock;

    public ChadgetPanel()
    {
        this( null );
    }

    public ChadgetPanel( IChadget object )
    {
        this.lock = new Object();
        this.object = object;
    }

    public void setObject( IChadget obj )
    {
        this.object = obj;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        IChadget obj = null;

        synchronized( lock )
        {
            obj = object;
        }

        if( obj == null )
        {
            return;
        }

        obj.paint( ( Graphics2D )g, getWidth(), getHeight() );
    }
}
