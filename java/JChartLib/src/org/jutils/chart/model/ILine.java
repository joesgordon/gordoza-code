package org.jutils.chart.model;

import java.awt.Color;
import java.awt.Point;

import org.jutils.chart.ui.IChadget;

public interface ILine extends IChadget
{
    public void setPoints( Point p1, Point p2 );

    public void setColor( Color color );

    public void setSize( int size );

    public double getSize();
}
