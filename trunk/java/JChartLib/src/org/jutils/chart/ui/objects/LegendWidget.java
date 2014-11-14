package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

        PlacementGrid grid = new PlacementGrid( availSize, isVertical, 6 );

        for( Series s : chart.series )
        {
            nameLabel.text = s.name;

            Dimension labelSize = nameWidget.calculateSize( canvasSize );
            labelSize.height = Math.max( labelSize.height, s.marker.weight ) + 4;

            grid.addItem( labelSize );
        }

        size = grid.getSize();
        size.width += extra;
        size.height += extra;

        return grid.getSize();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        graphics.setStroke( new BasicStroke( 2 ) );
        graphics.setColor( Color.black );
        graphics.drawRect( location.x + 1, location.y + 1, size.width - 2,
            size.height - 2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PlacementGrid
    {
        private final List<List<Dimension>> items;
        private final boolean isVertical;
        private final int directionLimit;
        private final int itemSpacing;

        public PlacementGrid( Dimension availableSize, boolean isVertical,
            int itemSpacing )
        {
            this.items = new ArrayList<>();
            this.isVertical = isVertical;
            this.itemSpacing = itemSpacing;
            this.directionLimit = isVertical ? availableSize.height
                : availableSize.width;

            items.add( new ArrayList<Dimension>() );
        }

        public Dimension getSize()
        {
            Dimension size = new Dimension();

            for( List<Dimension> list : items )
            {
                Dimension d = getListSize( list );

                if( isVertical )
                {
                    size.width += d.width;
                    size.height = Math.max( size.height, d.height );
                }
                else
                {
                    size.width = Math.max( size.width, d.width );
                    size.height += d.height;
                }
            }

            int extra = ( items.size() - 1 ) * itemSpacing;

            if( isVertical )
            {
                size.width += extra;
            }
            else
            {
                size.height += extra;
            }

            return size;
        }

        private Dimension getListSize( List<Dimension> list )
        {
            Dimension size = new Dimension();

            for( Dimension d : list )
            {
                if( isVertical )
                {
                    size.width = Math.max( d.width, size.width );
                    size.height += d.height;
                }
                else
                {
                    size.width += d.width;
                    size.height = Math.max( d.height, size.height );
                }
            }

            int extra = ( list.size() - 1 ) * itemSpacing;

            if( isVertical )
            {
                size.height += extra;
            }
            else
            {
                size.width += extra;
            }

            return size;
        }

        public void addItem( Dimension nextItem )
        {
            List<Dimension> list = getCurrentList();

            int len = getCurrentLen( list );
            int addLen = getItemLen( nextItem );

            if( len + addLen > directionLimit && !list.isEmpty() )
            {
                list = new ArrayList<>();
                items.add( list );
            }

            list.add( nextItem );

            // LogUtils.printDebug( "    item size: " + nextItem );
        }

        private List<Dimension> getCurrentList()
        {
            return items.isEmpty() ? null : items.get( items.size() - 1 );
        }

        private int getCurrentLen( List<Dimension> list )
        {
            int len = 0;

            if( list != null )
            {
                for( Dimension dim : list )
                {
                    len += getItemLen( dim );
                }

                len += ( ( list.size() - 1 ) * itemSpacing );
            }

            return len;
        }

        private int getItemLen( Dimension item )
        {
            return isVertical ? item.height : item.width;
        }
    }
}
