package org.jutils.chart.ui.objects;

import org.jutils.chart.ui.IJava2dObject;

public interface IMarker extends IJava2dObject
{
    public void setX( int x );

    public void setY( int y );

    public void setBorderVisible( boolean visible );
}
