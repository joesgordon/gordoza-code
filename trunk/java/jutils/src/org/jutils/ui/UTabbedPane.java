package org.jutils.ui;

import javax.swing.JTabbedPane;

public class UTabbedPane extends JTabbedPane
{
    public UTabbedPane()
    {
        this( TOP, JTabbedPane.SCROLL_TAB_LAYOUT );
    }

    public UTabbedPane( int tabPlacement )
    {
        this( tabPlacement, SCROLL_TAB_LAYOUT );
    }

    public UTabbedPane( int tabPlacement, int tabLayoutPolicy )
    {
        super( tabPlacement, tabLayoutPolicy );
    }

}
