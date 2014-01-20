package org.jutils.ui;

import javax.swing.JToolBar;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

//TODO comments

public class JGoodiesToolBar extends JToolBar
{
    public JGoodiesToolBar()
    {
        super();

        init();
    }

    public JGoodiesToolBar( int orientation )
    {
        super( orientation );

        init();
    }

    public JGoodiesToolBar( String name )
    {
        super( name );

        init();
    }

    public JGoodiesToolBar( String name, int orientation )
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
