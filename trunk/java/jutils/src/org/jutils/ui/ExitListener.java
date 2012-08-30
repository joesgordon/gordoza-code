package org.jutils.ui;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;

public class ExitListener implements ActionListener
{
    private final Window win;

    public ExitListener( Window win )
    {
        this.win = win;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        doDefaultCloseOperation( win );
    }

    public static void doDefaultCloseOperation( Window win )
    {
        WindowEvent wev = new WindowEvent( win, WindowEvent.WINDOW_CLOSING );
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( wev );
    }
}
