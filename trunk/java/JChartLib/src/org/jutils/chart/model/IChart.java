package org.jutils.chart.model;

import java.awt.Insets;
import java.util.Iterator;

import org.jutils.chart.data.QuadSide;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IChart
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isLegendVisible();

    /***************************************************************************
     * @return
     **************************************************************************/
    public QuadSide getLegendPlacement();

    /***************************************************************************
     * @param series
     **************************************************************************/
    public void addSeries( ISeries series );

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSeriesCount();

    /***************************************************************************
     * Returns the series at the provided index.
     * @param index the index of the series to return.
     * @return the series at the provided index.
     * @throws IndexOutOfBoundsException if the index is out of range
     * {@code (index < 0 || index >= size())}
     **************************************************************************/
    public ISeriesData getSeriesAt( int index )
        throws IndexOutOfBoundsException;

    /***************************************************************************
     * Returns an iterator over the series in this chart. These will be drawn
     * from first to last.
     **************************************************************************/
    public Iterator<ISeriesData> getSeriesIterator();

    /***************************************************************************
     * Returns the title label of this chart.
     **************************************************************************/
    public ILabel getTitle();

    /***************************************************************************
     * Returns the distances between the edge of the drawing area and the first
     * object to be drawn (e.g. axis label, axis, legend, etc. ).
     **************************************************************************/
    public Insets getMargin();
}
