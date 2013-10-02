package org.jutils.chart.ui.objects;

import java.awt.Color;
import java.awt.Point;

import org.jutils.chart.ui.IJava2dObject;

public interface IMarker extends IJava2dObject
{
    public void setBorderVisible( boolean visible );

    public void setLocation( Point p );

    public void setColor( Color color );

    public void setBorderColor( Color color );

    public void setRadius( int r );
}
