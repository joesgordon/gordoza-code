package org.jutils;

import java.awt.*;
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

    public static <T> String showEditableMessage( Component parent,
        String message, String title, T [] list, T defaultChoice )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel msgLabel = new JLabel( message );
        JComboBox<T> nameField = new JComboBox<>( list );
        GridBagConstraints constraints;

        int ans;
        String name;

        // ---------------------------------------------------------------------
        // Build message UI.
        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 8, 8,
                0, 8 ), 0, 0 );
        panel.add( msgLabel, constraints );

        nameField.setEditable( true );
        nameField.setSelectedItem( defaultChoice );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( nameField, constraints );

        // ---------------------------------------------------------------------
        // Prompt user.
        // ---------------------------------------------------------------------
        ans = JOptionPane.showOptionDialog( parent, panel, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
            null, null );

        name = nameField.getSelectedItem().toString();

        if( ans != JOptionPane.OK_OPTION )
        {
            name = null;
        }

        return name;
    }
}
