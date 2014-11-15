package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.QuadSide;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendWidget implements IChartWidget
{
    /**  */
    private final Chart chart;
    /**  */
    private final TextLabel nameLabel;
    /**  */
    private final TextWidget nameWidget;

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public LegendWidget( Chart chart )
    {
        this.chart = chart;
        this.nameLabel = new TextLabel();
        this.nameWidget = new TextWidget( nameLabel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        Dimension size = new Dimension();

        if( chart.legend.visible )
        {
            canvasSize.width = Math.max( canvasSize.width, 100 );
            canvasSize.height = Math.max( canvasSize.height, 100 );

            switch( chart.legend.side )
            {
                case TOP:
                case BOTTOM:
                    return caluclateSize( canvasSize, false );
                case LEFT:
                case RIGHT:
                    return caluclateSize( canvasSize, true );
            }

            throw new IllegalStateException( "Unsupported legend side: " +
                chart.legend.side );
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
        int extra = 16 + 2 * chart.legend.border.thickness;
        Dimension availSize = new Dimension( canvasSize );

        availSize.width -= extra;
        availSize.height -= extra;

        PlacementGrid grid = buildGrid( availSize, isVertical );

        size = new Dimension( grid.size );
        size.width += extra;
        size.height += extra;

        return size;
    }

    /**
     * @param availSize
     * @param isVertical
     * @return
     */
    private PlacementGrid buildGrid( Dimension availSize, boolean isVertical )
    {
        List<SeriesKey> keys = new ArrayList<>();

        for( Series s : chart.series )
        {
            nameLabel.text = s.name;

            Dimension keySize = nameWidget.calculateSize( availSize );
            keySize.height = Math.max( keySize.height, s.marker.weight ) + 4;

            SeriesKey key = new SeriesKey( s, keySize );

            keys.add( key );
        }

        return new PlacementGrid( keys, availSize, isVertical, 6 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        // ---------------------------------------------------------------------
        // Fill
        // ---------------------------------------------------------------------
        graphics.setColor( chart.legend.fill );
        graphics.fillRect( location.x, location.y, size.width, size.height );

        // ---------------------------------------------------------------------
        // Draw border
        // ---------------------------------------------------------------------
        if( chart.legend.border.visible && chart.legend.border.thickness > 0 )
        {
            int thickness = chart.legend.border.thickness;
            graphics.setStroke( new BasicStroke( thickness ) );
            graphics.setColor( Color.black );
            graphics.drawRect( location.x, location.y, size.width - thickness,
                size.height - thickness );

            location.x += thickness;
            location.y += thickness;

            size.width -= 2 * thickness;
            size.height -= 2 * thickness;
        }

        // ---------------------------------------------------------------------
        // Draw keys
        // ---------------------------------------------------------------------
        boolean isVertical = chart.legend.side == QuadSide.LEFT ||
            chart.legend.side == QuadSide.RIGHT;
        PlacementGrid grid = buildGrid( size, isVertical );

        graphics.setStroke( new BasicStroke( 2 ) );
        graphics.setColor( Color.black );

        location.x += 8;
        location.y += 8;

        for( KeyList list : grid.items )
        {
            // LogUtils.printDebug( "drawing series keys at " + colPoint );

            for( SeriesKey key : list.keys )
            {
                // LogUtils.printDebug( "\tdrawing series key at " + itemPoint
                // );
                int x = location.x + key.loc.x;
                int y = location.y + key.loc.y;

                graphics.drawRect( x, y, key.size.width - 2,
                    key.size.height - 2 );
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
                int addLen = getItemLen( key.size );

                if( len + addLen > directionLimit && !list.keys.isEmpty() )
                {
                    x = isVertical ? x + list.size.width + itemSpacing : 0;
                    y = isVertical ? 0 : y + list.size.height + itemSpacing;

                    list = new KeyList();
                    items.add( list );
                }

                key.loc.x = x;
                key.loc.y = y;

                list.keys.add( key );

                x += isVertical ? 0 : key.size.width + itemSpacing;
                y += isVertical ? key.size.height + itemSpacing : 0;

                list.size.width = isVertical ? Math.max( list.size.width,
                    key.size.width ) : list.size.width + key.size.width;

                list.size.height = isVertical ? list.size.height +
                    key.size.height : Math.max( list.size.height,
                    key.size.height );
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
        public final Point loc;
        public final Dimension size;

        public KeyList()
        {
            this.keys = new ArrayList<>();
            this.loc = new Point();
            this.size = new Dimension();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SeriesKey
    {
        public final Series s;
        public final Point loc;
        public final Dimension size;

        public SeriesKey( Series s, Dimension size )
        {
            this.s = s;
            this.loc = new Point();
            this.size = size;
        }
    }
}
