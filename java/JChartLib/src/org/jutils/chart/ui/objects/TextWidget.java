package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.TextLabel;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

public class TextWidget implements IChartWidget
{
    private TextLabel label;
    private final Layer2d layer;
    private FontMetrics metrics;

    public TextWidget( TextLabel label )
    {
        this.label = label;
        this.layer = new Layer2d();

        layer.getGraphics().setFont( label.font );
        metrics = layer.getGraphics().getFontMetrics();
    }

    @Override
    public void draw( Graphics2D graphics, int x, int y, int width, int height )
    {
        int textWidth = metrics.stringWidth( label.text );
        int textHeight = metrics.getHeight();

        if( layer.repaint )
        {
            Graphics2D g2d = layer.setSize( textWidth, textHeight );

            layer.clear();

            g2d.setColor( label.color );
            g2d.setFont( label.font );
            g2d.drawString( label.text, 0, label.font.getSize() );
            // g2d.drawRect( 0, 0, textWidth - 1, textHeight - 1 );

            layer.repaint = false;
        }

        // LogUtils.printDebug( "text: x: " + x + ", y: " + y + ", w: " + width
        // +
        // ", h: " + height + ", t: " + label.text );

        x = ( int )( x + ( width - textWidth ) / 2.0 );
        // LogUtils.printDebug( "text2: x: " + x + ", textWidth: " + textWidth +
        // ", textHeight: " + textHeight );

        layer.paint( graphics, x, y );
    }

    @Override
    public Dimension calculateSize()
    {
        Dimension dim = new Dimension();

        if( !label.visible )
        {
            return dim;
        }

        dim.width = metrics.stringWidth( label.text );
        dim.height = metrics.getHeight();

        return dim;
    }

    public void repaint()
    {
        layer.repaint = true;
    }
}
