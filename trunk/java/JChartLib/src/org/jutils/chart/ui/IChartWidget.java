package org.jutils.chart.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;

/*******************************************************************************
 * Defines a object that may be drawn on a chart.
 ******************************************************************************/
public interface IChartWidget
{
    /***************************************************************************
     * Calculates the size needed to draw this object with no clipping or loss
     * of data.
     * @return the calculated width/height.
     **************************************************************************/
    public Dimension calculateSize();

    /***************************************************************************
     * Draws the object to the provided graphics at the x/y and width/height
     * provided.
     * @param graphics the context on which the object is drawn.
     * @param x the x position of the top left corner of this object.
     * @param y the y position of the top left corner of this object.
     * @param width the width of the drawing area.
     * @param height the height of the drawing area.
     **************************************************************************/
    public void draw( Graphics2D graphics, int x, int y, int width, int height );
}
