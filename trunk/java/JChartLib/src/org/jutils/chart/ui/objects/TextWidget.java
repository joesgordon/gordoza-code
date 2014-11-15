package org.jutils.chart.ui.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;

import org.jutils.chart.model.TextLabel;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextWidget implements IChartWidget
{
    /**  */
    private final TextLabel label;
    /**  */
    private final Layer2d layer;
    /**  */
    private final TextDirection direction;

    /**  */
    private FontMetrics metrics;

    /***************************************************************************
     * @param label
     **************************************************************************/
    public TextWidget( TextLabel label )
    {
        this( label, TextDirection.DOWN );
    }

    /***************************************************************************
     * @param label
     * @param direction
     **************************************************************************/
    public TextWidget( TextLabel label, TextDirection direction )
    {
        this.label = label;
        this.layer = new Layer2d();
        this.direction = direction;

        layer.getGraphics().setFont( label.font );
        metrics = layer.getGraphics().getFontMetrics();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        int textWidth = metrics.stringWidth( label.text );
        int textHeight = metrics.getHeight();

        if( layer.repaint )
        {
            layer.clear();

            if( textWidth > 0 && textHeight > 0 )
            {
                Graphics2D g2d = layer.setSize( textWidth, textHeight );

                g2d.setColor( label.color );
                g2d.setFont( label.font );
                g2d.drawString( label.text, 0, label.font.getSize() );
                // g2d.drawRect( 0, 0, textWidth - 1, textHeight - 1 );

                layer.repaint = false;
            }
        }

        int x = location.x;
        int y = location.y;
        int width = size.width;

        switch( label.alignment )
        {
            case CENTER:
                x = x + ( width - textWidth ) / 2;
                break;

            case RIGHT:
                x = x + width - textWidth;
                break;

            default:
                break;
        }

        // LogUtils.printDebug( "text: x: " + x + ", y: " + y + ", w: " + width
        // +
        // ", h: " + height + ", t: " + label.text );

        // LogUtils.printDebug( "text2: x: " + x + ", textWidth: " + textWidth +
        // ", textHeight: " + textHeight );
        if( direction != TextDirection.DOWN )
        {
            int xp = x + textWidth / 2;
            int yp = y + textHeight / 2;
            AffineTransform t = graphics.getTransform();
            AffineTransform r = AffineTransform.getRotateInstance(
                direction.angle, xp, yp );

            graphics.setTransform( r );
            layer.paint( graphics, x, y );
            // graphics.drawLine( xp, yp, xp, yp + 50 );

            graphics.setTransform( t );
            // layer.paint( graphics, x, y );
        }
        else
        {
            layer.paint( graphics, x, y );
        }

        // graphics.drawRect( location.x, location.y, size.width - 1,
        // size.height - 1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        Dimension dim = new Dimension();

        if( !label.visible )
        {
            return dim;
        }

        dim.width = metrics.stringWidth( label.text );
        dim.height = metrics.getHeight();

        if( direction == TextDirection.RIGHT || direction == TextDirection.LEFT )
        {
            int i = dim.width;
            dim.width = dim.height;
            dim.height = i;
        }

        return dim;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        layer.repaint = true;

        metrics = layer.getGraphics().getFontMetrics();
    }
}
