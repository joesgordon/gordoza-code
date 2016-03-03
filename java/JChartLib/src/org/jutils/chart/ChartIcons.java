package org.jutils.chart;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartIcons
{
    /**  */
    public static final String CHART_016 = "chart016.png";
    /**  */
    public static final String CHART_024 = "chart024.png";
    /**  */
    public static final String CHART_032 = "chart032.png";
    /**  */
    public static final String CHART_048 = "chart048.png";
    /**  */
    public static final String CHART_064 = "chart064.png";
    /**  */
    public static final String CHART_128 = "chart128.png";
    /**  */
    public static final String CHART_256 = "chart256.png";
    /**  */
    public static final String ZOOM_OUT_016 = "zoom-out016.png";
    /**  */
    public static final String ZOOM_IN_016 = "zoom-in016.png";

    /**  */
    public static final String [] CHART_NAMES = new String[] { CHART_016,
        CHART_024, CHART_032, CHART_064, CHART_128, CHART_256 };

    /**  */
    public static final IconLoader loader = new IconLoader( ChartIcons.class,
        "icons" );

    /***************************************************************************
     * 
     **************************************************************************/
    private ChartIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getChartImages()
    {
        return loader.getImages( CHART_NAMES );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static Icon getIcon( String str )
    {
        return loader.getIcon( str );
    }
}
