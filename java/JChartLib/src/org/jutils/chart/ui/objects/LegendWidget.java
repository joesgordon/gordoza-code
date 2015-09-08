package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.QuadSide;
import org.jutils.chart.model.HorizontalAlignment;
import org.jutils.chart.model.TextLabel;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendWidget implements IChartWidget
{
    /**  */
    private final ChartWidget chart;
    /**  */
    private final TextLabel nameLabel;
    /**  */
    private final TextWidget nameWidget;

    /**  */
    private final Layer2d layer;

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public LegendWidget( ChartWidget chart )
    {
        this.chart = chart;
        this.nameLabel = new TextLabel();
        this.nameWidget = new TextWidget( nameLabel );
        this.layer = new Layer2d();

        nameLabel.alignment = HorizontalAlignment.RIGHT;
        nameLabel.font = nameLabel.font.deriveFont( 14.0f );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        layer.repaint = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        Dimension size = new Dimension();

        if( chart.chart.legend.visible )
        {
            canvasSize.width = Math.max( canvasSize.width, 100 );
            canvasSize.height = Math.max( canvasSize.height, 100 );

            switch( chart.chart.legend.side )
            {
                case TOP:
                case BOTTOM:
                    return caluclateSize( canvasSize, false );
                case LEFT:
                case RIGHT:
                    return caluclateSize( canvasSize, true );
            }

            throw new IllegalStateException(
                "Unsupported legend side: " + chart.chart.legend.side );
        }

        return size;
    }

    /***************************************************************************
     * @param canvasSize
     * @param isVertical
     * @return
     **************************************************************************/
    private Dimension caluclateSize( Dimension canvasSize, boolean isVertical )
    {
        Dimension size = new Dimension();
        int extra = 16 + 2 * chart.chart.legend.border.thickness;
        Dimension availSize = new Dimension( canvasSize );

        availSize.width -= extra;
        availSize.height -= extra;

        PlacementGrid grid = buildGrid( availSize, isVertical );

        size = new Dimension( grid.size );
        size.width += extra;
        size.height += extra;

        return size;
    }

    /***************************************************************************
     * @param availSize
     * @param isVertical
     * @return
     **************************************************************************/
    private PlacementGrid buildGrid( Dimension availSize, boolean isVertical )
    {
        List<SeriesKey> keys = new ArrayList<>();

        for( SeriesWidget s : chart.plot.serieses )
        {
            if( s.series.visible )
            {
                nameLabel.text = s.series.name;

                Dimension keySize = nameWidget.calculateSize( availSize );
                keySize.height = Math.max( keySize.height,
                    s.series.marker.weight ) + 4;
                keySize.width += 8 + s.series.marker.weight * 2;

                SeriesKey key = new SeriesKey( s, keySize );

                keys.add( key );
            }
        }

        return new PlacementGrid( keys, availSize, isVertical, 6 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        layer.setSize( size );

        if( layer.repaint )
        {
            draw( layer.getGraphics(), size );
        }

        layer.paint( graphics, location.x, location.y );
    }

    /***************************************************************************
     * @param graphics
     * @param size
     **************************************************************************/
    private void draw( Graphics2D graphics, Dimension size )
    {
        Point location = new Point();

        // ---------------------------------------------------------------------
        // Fill
        // ---------------------------------------------------------------------
        graphics.setColor( chart.chart.legend.fill );
        graphics.fillRect( location.x, location.y, size.width, size.height );

        // ---------------------------------------------------------------------
        // Draw border
        // ---------------------------------------------------------------------
        if( chart.chart.legend.border.visible &&
            chart.chart.legend.border.thickness > 0 )
        {
            int thickness = chart.chart.legend.border.thickness;
            int half = thickness / 2;
            graphics.setStroke( new BasicStroke( thickness ) );
            graphics.setColor( chart.chart.legend.border.color );
            graphics.drawRect( location.x + half, location.y + half,
                size.width - thickness, size.height - thickness );

            location.x += thickness;
            location.y += thickness;

            size.width -= 2 * thickness;
            size.height -= 2 * thickness;
        }

        // ---------------------------------------------------------------------
        // Draw keys
        // ---------------------------------------------------------------------
        int extra = 8;
        location.x += extra;
        location.y += extra;

        size.width -= 2 * extra;
        size.height -= 2 * extra;

        boolean isVertical = chart.chart.legend.side == QuadSide.LEFT ||
            chart.chart.legend.side == QuadSide.RIGHT;
        PlacementGrid grid = buildGrid( size, isVertical );

        graphics.setStroke( new BasicStroke( 2 ) );
        graphics.setColor( Color.black );

        for( KeyList list : grid.items )
        {
            // LogUtils.printDebug( "drawing series keys at " + colPoint );

            for( SeriesKey key : list.keys )
            {
                // LogUtils.printDebug( "\tdrawing series " + key.s.series.name
                // );
                int x = location.x + key.loc.x;
                int y = location.y + key.loc.y;
                Point p = new Point( x, y );

                // graphics.drawRect( x + 1, y + 1, key.size.width - 2,
                // key.size.height - 2 );

                nameLabel.text = key.s.series.name;
                Dimension ts = nameWidget.calculateSize( key.size );
                nameWidget.repaint();
                nameWidget.draw( graphics,
                    new Point( x, y + ( key.size.height - ts.height ) / 2 ),
                    key.size );

                p.x = p.x + 6;
                p.y = p.y + key.size.height / 2;
                int ms = key.s.marker.getSize();
                key.s.marker.setSize( 10 );
                key.s.marker.setLocation( p );
                key.s.marker.draw( graphics, p, size );
                key.s.marker.setSize( ms );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PlacementGrid
    {
        public final List<KeyList> items;
        public final Dimension size;

        private final boolean isVertical;

        public PlacementGrid( List<SeriesKey> keys, Dimension availableSize,
            boolean isVertical, int itemSpacing )
        {
            this.items = new ArrayList<>();
            this.isVertical = isVertical;
            this.size = new Dimension();

            int directionLimit = isVertical ? availableSize.height
                : availableSize.width;

            KeyList list = new KeyList();
            int x = 0;
            int y = 0;

            items.add( list );

            for( SeriesKey key : keys )
            {
                int len = getItemLen( list.size );
                int nextLen = len + getItemLen( key.size );

                if( nextLen > directionLimit && !list.keys.isEmpty() )
                {
                    x = isVertical ? x + list.size.width + itemSpacing : 0;
                    y = isVertical ? 0 : y + list.size.height + itemSpacing;

                    list = new KeyList();
                    items.add( list );
                }

                key.loc.x = x;
                key.loc.y = y;

                list.keys.add( key );

                int space = list.keys.size() < 1 ? 0 : itemSpacing;

                x += isVertical ? 0 : key.size.width + itemSpacing;
                y += isVertical ? key.size.height + itemSpacing : 0;

                list.size.width = isVertical
                    ? Math.max( list.size.width, key.size.width )
                    : list.size.width + key.size.width + space;

                list.size.height = isVertical
                    ? list.size.height + key.size.height + space
                    : Math.max( list.size.height, key.size.height );
            }

            for( KeyList kl : items )
            {
                size.width += kl.size.width;
                size.height += kl.size.height;
            }
        }

        private int getItemLen( Dimension item )
        {
            return isVertical ? item.height : item.width;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class KeyList
    {
        public final List<SeriesKey> keys;
        public final Dimension size;

        public KeyList()
        {
            this.keys = new ArrayList<>();
            this.size = new Dimension();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SeriesKey
    {
        public final SeriesWidget s;
        public final Point loc;
        public final Dimension size;

        public SeriesKey( SeriesWidget s, Dimension size )
        {
            this.s = s;
            this.loc = new Point();
            this.size = size;
        }
    }
}
