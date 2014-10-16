package org.jutils.chart.model;

import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Series
{
    /**  */
    public final TextLabel title;
    /**  */
    public String resource;
    /**  */
    public final ISeriesData<?> data;
    /**  */
    public boolean visible;
    /**  */
    public final MarkerStyle marker = new MarkerStyle();
    /**  */
    public final MarkerStyle highlight = new MarkerStyle();
    /**  */
    public final LineStyle line = new LineStyle();
    /**  */
    public boolean isPrimaryDomain;
    /**  */
    public boolean isPrimaryRange;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public Series( ISeriesData<?> data )
    {
        this.title = new TextLabel();
        this.data = data;
        this.isPrimaryDomain = true;
        this.isPrimaryRange = true;

        if( data.getCount() == 0 )
        {
            throw new IllegalArgumentException( "The series contains no data" );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Span calcDomainSpan()
    {
        Double min = null;
        Double max = null;

        for( IDataPoint p : data )
        {
            if( !p.isHidden() )
            {
                if( min == null )
                {
                    min = p.getX();
                    max = p.getX();
                }
                else
                {
                    min = Math.min( min, p.getX() );
                    max = Math.max( max, p.getX() );
                }
            }
        }

        if( min == null )
        {
            return null;
        }

        return new Span( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Span calcRangeSpan()
    {
        Double min = null;
        Double max = null;

        for( IDataPoint p : data )
        {
            if( !p.isHidden() )
            {
                if( min == null )
                {
                    min = p.getY();
                    max = p.getY();
                }
                else
                {
                    min = Math.min( min, p.getY() );
                    max = Math.max( max, p.getY() );
                }
            }
        }

        if( min == null )
        {
            return null;
        }

        return new Span( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getResourceFile()
    {
        return resource == null ? null : new File( resource );
    }
}
