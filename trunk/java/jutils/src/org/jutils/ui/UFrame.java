package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;

public class UFrame extends JFrame
{
    /***************************************************************************
     * @throws HeadlessException
     **************************************************************************/
    public UFrame() throws HeadlessException
    {
        super();
        init();
    }

    /***************************************************************************
     * @param gc
     **************************************************************************/
    public UFrame( GraphicsConfiguration gc )
    {
        super( gc );
        init();
    }

    /***************************************************************************
     * @param title
     * @throws HeadlessException
     **************************************************************************/
    public UFrame( String title ) throws HeadlessException
    {
        super( title );
        init();
    }

    /***************************************************************************
     * @param title
     * @param gc
     **************************************************************************/
    public UFrame( String title, GraphicsConfiguration gc )
    {
        super( title, gc );
        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init()
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void doDefaultCloseOperation()
    {
        WindowEvent wev = new WindowEvent( this, WindowEvent.WINDOW_CLOSING );
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( wev );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public class ExitListener implements ActionListener
    {
        public ExitListener()
        {
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            doDefaultCloseOperation();
        }
    }
}
