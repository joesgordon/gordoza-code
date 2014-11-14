package org.jutils.chart.model;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chart
{
    /**  */
    public final ChartOptions options;
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;
    /**  */
    public final TextLabel topBottomLabel;
    /**  */
    public final Axis domainAxis;
    /**  */
    public final Axis rangeAxis;
    /**  */
    public final Legend legend;

    /**  */
    public final List<Series> series;

    /***************************************************************************
     * 
     **************************************************************************/
    public Chart()
    {
        this.options = new ChartOptions();
        this.title = new TextLabel();
        this.subtitle = new TextLabel();
        this.topBottomLabel = new TextLabel();
        this.domainAxis = new Axis();
        this.rangeAxis = new Axis();
        this.legend = new Legend();

        this.series = new ArrayList<>();

        topBottomLabel.font = topBottomLabel.font.deriveFont( 10.0f );
        topBottomLabel.visible = false;
        topBottomLabel.alignment = HorizontalAlignment.CENTER;

        subtitle.font = subtitle.font.deriveFont( 12.0f );
        subtitle.visible = false;

        rangeAxis.minorSectionCount = 2;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Span calculatePrimaryDomainSpan()
    {
        Double min = null;
        Double max = null;

        for( Series s : series )
        {
            if( s.isPrimaryDomain )
            {
                Span span = s.calcDomainSpan();

                if( span == null )
                {
                    continue;
                }

                if( min == null )
                {
                    min = span.min;
                    max = span.max;
                }
                else
                {
                    min = Math.min( min, span.min );
                    max = Math.max( max, span.max );
                }
            }
        }

        if( min == null )
        {
            min = -5.0;
            max = 5.0;
        }
        else if( min.equals( max ) )
        {
            min -= 0.5;
            max += 0.5;
        }

        double r = max - min;

        return new Span( min - 0.03 * r, max + 0.03 * r );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Span calculatePrimaryRangeSpan()
    {
        Double min = null;
        Double max = null;

        for( Series s : series )
        {
            if( s.isPrimaryRange )
            {
                Span span = s.calcRangeSpan();

                if( span == null )
                {
                    continue;
                }

                if( min == null )
                {
                    min = span.min;
                    max = span.max;
                }
                else
                {
                    min = Math.min( min, span.min );
                    max = Math.max( max, span.max );
                }
            }
        }

        if( min == null )
        {
            min = -5.0;
            max = 5.0;
        }
        else if( min.equals( max ) )
        {
            min -= 0.5;
            max += 0.5;
        }

        double r = max - min;

        return new Span( min - 0.03 * r, max + 0.03 * r );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Span calculateSecondaryDomainSpan()
    {
        Double min = null;
        Double max = null;

        for( Series s : series )
        {
            if( !s.isPrimaryDomain )
            {
                Span span = s.calcDomainSpan();

                if( span == null )
                {
                    continue;
                }

                if( min == null )
                {
                    min = span.min;
                    max = span.max;
                }
                else
                {
                    min = Math.min( min, span.min );
                    max = Math.max( max, span.max );
                }
            }
        }

        if( min == null )
        {
            return null;
        }
        else if( min.equals( max ) )
        {
            min -= 0.5;
            max += 0.5;
        }

        double r = max - min;

        return new Span( min - 0.03 * r, max + 0.03 * r );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Span calculateSecondaryRangeSpan()
    {
        Double min = null;
        Double max = null;

        for( Series s : series )
        {
            if( !s.isPrimaryRange )
            {
                Span span = s.calcRangeSpan();

                if( span == null )
                {
                    continue;
                }

                if( min == null )
                {
                    min = span.min;
                    max = span.max;
                }
                else
                {
                    min = Math.min( min, span.min );
                    max = Math.max( max, span.max );
                }
            }
        }

        if( min == null )
        {
            return null;
        }
        else if( min.equals( max ) )
        {
            min -= 0.5;
            max += 0.5;
        }

        double r = max - min;

        return new Span( min - 0.03 * r, max + 0.03 * r );
    }
}
