package org.jutils.drawing;

import java.awt.Color;

import org.jutils.drawing.HistogramView.HistogramConfig;

public interface IColorModel
{
    public Color getColor( int pixel );

    public int getColorValue( int pixel );

    public void setContrast( int contrast );

    public void setLowThreshold( int pixel, Color thresholdColor );

    public void setHighThreshold( int pixel, Color thresholdColor );

    public void setHistogramConfig( HistogramConfig histConfig );

    public HistogramConfig getHistogramConfig();
}
