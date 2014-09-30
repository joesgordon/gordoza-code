package org.jutils.chart.model;

import java.awt.Color;
import java.awt.Point;

import org.jutils.chart.ui.IChartWidget;

public interface IMarker extends IChartWidget
{
    public void setBorderVisible( boolean visible );

    public void setLocation( Point p );

    public void setColor( Color color );

    public void setBorderColor( Color color );

    public void setRadius( int r );
}
