package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.ui.FrameRunner;

public class JHexApp implements ILibraryApp
{
    @Override
    public Icon getIcon()
    {
        return IconConstants.getIcon( IconConstants.BINARY_32 );
    }

    @Override
    public String getName()
    {
        return "JHex";
    }

    @Override
    public JFrame runApp()
    {
        FrameRunner r = new JHexMain();
        r.run();
        return r.getFrame();
    }
}
