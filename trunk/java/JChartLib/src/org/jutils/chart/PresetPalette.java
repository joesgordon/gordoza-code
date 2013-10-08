package org.jutils.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PresetPalette implements IPalette
{
    private final CyclingPalette cycle;

    public PresetPalette()
    {
        List<Color> colors = new ArrayList<>();

        colors.add( Color.black );
        colors.add( new Color( 0x0066CC ) );
        colors.add( new Color( 0xFF9933 ) );
        colors.add( new Color( 0xCC6622 ) );
        colors.add( new Color( 0x339933 ) );

        this.cycle = new CyclingPalette( colors );
    }

    @Override
    public Color next()
    {
        return cycle.next();
    }
}
