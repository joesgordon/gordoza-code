package org.jutils.chart.ui.objects;

import java.awt.Color;
import java.awt.Point;

import org.jutils.chart.ui.IJava2dObject;

public interface ILine extends IJava2dObject
{
    public void setPoints( Point p1, Point p2 );

    public void setColor( Color color );

    public void setSize( int size );

    public double getSize();
}
