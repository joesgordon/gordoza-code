package org.jutils.chart.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Java2dPanel extends JComponent
{
    private IJava2dObject object;
    private final Object lock;

    public Java2dPanel()
    {
        this( null );
    }

    public Java2dPanel( IJava2dObject object )
    {
        this.lock = new Object();
        this.object = object;
    }

    public void setObject( IJava2dObject obj )
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

        IJava2dObject obj = null;

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
