package org.jutils.ui;

import java.awt.*;

import javax.swing.JComponent;

public class PositionIndicator extends JComponent
{
    private Color unitColor;
    private long length;
    private long unitLength;
    private long offset;

    public PositionIndicator()
    {
        unitColor = new Color( 0x3A6EA7 );
        unitLength = 10;
        length = 100;
        offset = 50;
    }

    public void setUnitColor( Color c )
    {
        unitColor = c;
        repaint();
    }

    public void setLength( long l )
    {
        length = l;
        repaint();
    }

    public void setUnitLength( long l )
    {
        unitLength = l;
        repaint();
    }

    public void setOffset( long l )
    {
        offset = l;
        repaint();
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        Graphics2D g2 = ( Graphics2D )g;

        super.paintComponent( g2 );

        g2.setColor( unitColor );

        Object aaHint = g2.getRenderingHint( RenderingHints.KEY_ANTIALIASING );

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );

        int x = ( int )( getWidth() * offset / ( double )length ) + 1;
        int y = 1;
        int w = ( int )( getWidth() * unitLength / ( double )length ) - 2;
        int h = getHeight() - 2;

        w = Math.max( w, 20 );

        g2.fillRoundRect( x, y, w, h, 8, 8 );

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );
    }
}
