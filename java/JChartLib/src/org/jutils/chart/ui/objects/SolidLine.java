package org.jutils.chart.ui.objects;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jutils.chart.ui.IJava2dObject;

public class SolidLine implements IJava2dObject
{
    public Color color;
    public int size;

    public SolidLine()
    {
        this.color = new Color( 0x0099CC );
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        // TODO Auto-generated method stub
    }
}
