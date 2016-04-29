package org.jutils.chart.ui;

import java.awt.*;

import javax.swing.JComponent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class WidgetPanel extends JComponent
{
    /**  */
    private IChartWidget object;
    /**  */
    private final Object lock;
    /**  */
    private final Layer2d layer;

    /***************************************************************************
     * 
     **************************************************************************/
    public WidgetPanel()
    {
        this( null );
    }

    /***************************************************************************
     * @param object
     **************************************************************************/
    public WidgetPanel( IChartWidget object )
    {
        this.lock = new Object();
        this.object = object;
        this.layer = new Layer2d();
    }

    /***************************************************************************
     * @param obj
     **************************************************************************/
    public void setObject( IChartWidget obj )
    {
        this.object = obj;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        Graphics2D graphics = ( Graphics2D )g;
        super.paintComponent( g );

        IChartWidget obj = null;

        synchronized( lock )
        {
            obj = object;
        }

        if( obj == null )
        {
            return;
        }

        Insets borderSize = super.getInsets();
        int width = super.getWidth() - borderSize.left - borderSize.right - 1;
        int height = super.getHeight() - borderSize.top - borderSize.bottom - 1;
        int x = borderSize.left;
        int y = borderSize.top;
        Dimension min = getMinimumSize();

        width = Math.max( width, min.width );
        height = Math.max( height, min.height );

        Dimension size = new Dimension( width, height );

        // graphics.setColor( Color.red );
        // graphics.drawRect( x + 1, y + 1, size.width - 2, size.height - 2 );
        // graphics.setColor( Color.cyan );
        // graphics.drawLine( x + 1, y + 5, x + size.width - 1, y + 5 );

        layer.setSize( new Dimension( size.width + 1, size.height + 1 ) );
        obj.calculateSize( size );
        obj.draw( layer.getGraphics(), new Point(), size );

        // Graphics2D g2 = layer.getGraphics();
        // g2.setColor( Color.black );
        // g2.drawRect( 0, 0, size.width, size.height );
        // g2.setColor( Color.green );
        // g2.drawLine( 1, 6, size.width - 1, 6 );

        layer.paint( graphics, x, y );

        // if( "".isEmpty() )
        // {
        // return;
        // }
    }
}
