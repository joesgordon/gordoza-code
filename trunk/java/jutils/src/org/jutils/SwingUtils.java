package org.jutils;

import java.awt.event.*;

import javax.swing.*;

//TODO comments

public final class SwingUtils
{
    private SwingUtils()
    {
    }

    public static void installEscapeCloseOperation( final JDialog dialog )
    {
        Action dispatchClosing = new AbstractAction()
        {
            public void actionPerformed( ActionEvent event )
            {
                dialog.dispatchEvent( new WindowEvent( dialog,
                    WindowEvent.WINDOW_CLOSING ) );
            }
        };

        String mapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        KeyStroke escapeStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
        JRootPane root = dialog.getRootPane();

        root.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put(
            escapeStroke, mapKey );
        root.getActionMap().put( mapKey, dispatchClosing );
    }
}
