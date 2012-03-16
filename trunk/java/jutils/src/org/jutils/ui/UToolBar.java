package org.jutils.ui;

import javax.swing.JToolBar;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

public class UToolBar extends JToolBar
{
    public UToolBar()
    {
        super();

        init();
    }

    public UToolBar( int orientation )
    {
        super( orientation );

        init();
    }

    public UToolBar( String name )
    {
        super( name );

        init();
    }

    public UToolBar( String name, int orientation )
    {
        super( name, orientation );

        init();
    }

    private void init()
    {
        setFloatable( false );
        setRollover( true );
        setBorderPainted( false );

        putClientProperty( Options.HEADER_STYLE_KEY, HeaderStyle.BOTH );
    }
}
