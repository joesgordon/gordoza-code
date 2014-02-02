package org.jutils;

import java.awt.Window;
import java.awt.event.*;

import javax.swing.*;

//TODO comments

public final class SwingUtils
{
    private SwingUtils()
    {
    }

    public static void installEscapeCloseOperation( JFrame frame )
    {
        installEscapeCloseOperation( frame, frame.getRootPane() );
    }

    public static void installEscapeCloseOperation( JDialog dialog )
    {
        installEscapeCloseOperation( dialog, dialog.getRootPane() );
    }

    private static void installEscapeCloseOperation( Window win,
        JRootPane rootPane )
    {
        CloseAction dispatchClosing = new CloseAction( win );
        String mapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        KeyStroke escapeStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );

        rootPane.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put(
            escapeStroke, mapKey );
        rootPane.getActionMap().put( mapKey, dispatchClosing );
    }

    private static class CloseAction extends AbstractAction
    {
        private final Window win;

        public CloseAction( Window win )
        {
            this.win = win;
        }

        @Override
        public void actionPerformed( ActionEvent event )
        {
            win.dispatchEvent( new WindowEvent( win, WindowEvent.WINDOW_CLOSING ) );
        }
    }
}
