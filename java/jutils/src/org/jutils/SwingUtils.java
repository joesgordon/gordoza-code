package org.jutils;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.jutils.ui.StatusBarPanel;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public final class SwingUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
    private SwingUtils()
    {
    }

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public static void installEscapeCloseOperation( JFrame frame )
    {
        installEscapeCloseOperation( frame, frame.getRootPane() );
    }

    /***************************************************************************
     * @param dialog
     **************************************************************************/
    public static void installEscapeCloseOperation( JDialog dialog )
    {
        installEscapeCloseOperation( dialog, dialog.getRootPane() );
    }

    /***************************************************************************
     * @param win
     * @param rootPane
     **************************************************************************/
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

    /***************************************************************************
     * @param parent
     * @param message
     * @param title
     * @param list
     * @param defaultChoice
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * @param toolbar
     * @param container
     * @return
     **************************************************************************/
    public static JPanel createStandardConentPane( JToolBar toolbar,
        Container container )
    {
        StatusBarPanel statusbar = new StatusBarPanel();

        return createStandardConentPane( toolbar, container, statusbar );
    }

    /***************************************************************************
     * @param toolbar
     * @param container
     * @param statusbar
     * @return
     **************************************************************************/
    private static JPanel createStandardConentPane( JToolBar toolbar,
        Container container, StatusBarPanel statusbar )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        if( toolbar != null )
        {
            constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( toolbar, constraints );
        }

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( container, constraints );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( statusbar.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param toolbar
     **************************************************************************/
    public static void setToolbarDefaults( JToolBar toolbar )
    {
        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );
    }

    /***************************************************************************
     * @param toolbar
     * @param action
     * @return
     **************************************************************************/
    public static JButton addActionToToolbar( JToolBar toolbar, Action action )
    {
        JButton button = new JButton();

        addActionToToolbar( toolbar, action, button );

        return button;
    }

    /***************************************************************************
     * @param toolbar
     * @param action
     * @param button
     **************************************************************************/
    public static void addActionToToolbar( JToolBar toolbar, Action action,
        JButton button )
    {
        button.setAction( action );
        button.setFocusable( false );
        button.setText( null );
        button.setToolTipText( action.getValue( Action.NAME ).toString() );
        toolbar.add( button );
    }

    /***************************************************************************
     * Creates and displays a tray icon with the provided image, tool tip, and
     * popup menu which displays/hides the provided frame when double-clicked.
     * @param img The image (icon) to be displayed.
     * @param tooltip The tool tip to be displayed.
     * @param frame The frame to be displayed/hidden.
     * @param popup The popup menu to be displayed on right-click.
     * @return The tray icon created or {@code null} if the system tray is not
     * supported.
     * @throws UnsupportedOperationException if {@link SystemTray#add(TrayIcon)}
     * throws an {@link AWTException}.
     **************************************************************************/
    public static final TrayIcon createTrayIcon( Image img, String tooltip,
        JFrame frame, PopupMenu popup ) throws UnsupportedOperationException
    {
        TrayIcon icon = null;

        if( SystemTray.isSupported() )
        {
            ActionListener trayListener = new TrayIconListener( frame );

            SystemTray tray = SystemTray.getSystemTray();

            icon = new TrayIcon( img, tooltip );
            icon.addActionListener( trayListener );
            icon.setImageAutoSize( true );
            try
            {
                tray.add( icon );
                frame.addWindowListener( new MinimizeListener( frame ) );
            }
            catch( AWTException ex )
            {
                throw new UnsupportedOperationException(
                    "Cannot load icon in tray", ex );
            }

            icon.setPopupMenu( popup );
        }

        return icon;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class TrayIconListener implements ActionListener
    {
        private JFrame f;

        public TrayIconListener( JFrame frame )
        {
            f = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            f.setVisible( !f.isVisible() );
            if( f.isVisible() )
            {
                // -------------------------------------------------------------
                // This stinks because it cannot retain the extended state.
                // I've tried all sorts of ways and this is the only thing
                // that doesn't pop the window up behind other things a still
                // give it focus. So, don't remove it.
                // -------------------------------------------------------------
                f.setExtendedState( JFrame.NORMAL );
                f.requestFocus();
                f.toFront();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class MinimizeListener extends WindowAdapter
    {
        private JFrame frame;

        public MinimizeListener( JFrame f )
        {
            frame = f;
        }

        public void windowIconified( WindowEvent e )
        {
            frame.setVisible( false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    public static boolean showOkCancelDialog( Component parent, Object msg,
        String title, String okText, final Runnable initialFocusSelector )
    {
        JDialog dialog;

        JOptionPane pane = new JOptionPane( msg, JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, null,
            new String[] { okText, "Cancel" }, okText )
        {
            @Override
            public void selectInitialValue()
            {
                initialFocusSelector.run();
            }
        };

        dialog = pane.createDialog( parent, title );

        dialog.setSize( 500, dialog.getHeight() );
        dialog.setVisible( true );

        return okText == pane.getValue();
    }
}
