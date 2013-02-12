package org.jutils.ui;

import java.awt.Insets;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.jgoodies.looks.*;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class JGoodiesMenuBar extends JMenuBar
{
    public JGoodiesMenuBar()
    {
        super();

        putClientProperty( Options.HEADER_STYLE_KEY, HeaderStyle.BOTH );
        putClientProperty( PlasticLookAndFeel.BORDER_STYLE_KEY,
            BorderStyle.SEPARATOR );
    }

    public JMenu add( JMenu m )
    {
        m.setMargin( new Insets( 0, 2, 0, 2 ) );
        return super.add( m );
    }
}
