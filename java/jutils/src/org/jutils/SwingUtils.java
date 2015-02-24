package org.jutils;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import org.jutils.io.XStreamUtils;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IDataView;

import com.thoughtworks.xstream.XStreamException;

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
        button.setMnemonic( -1 );
        toolbar.add( button );
    }

    /***************************************************************************
     * Sets the tool tip of the provided action.
     * @param action the action for which the tool tip will be set.
     * @param tooltip the tool tip to be set.
     **************************************************************************/
    public static void setActionToolTip( Action action, String tooltip )
    {
        action.putValue( Action.SHORT_DESCRIPTION, tooltip );
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
    public static TrayIcon createTrayIcon( Image img, String tooltip,
        JFrame frame, PopupMenu popup ) throws UnsupportedOperationException
    {
        TrayIcon icon = null;

        if( SystemTray.isSupported() )
        {
            ActionListener trayListener = new MiniMaximizeListener( frame );

            SystemTray tray = SystemTray.getSystemTray();

            icon = new TrayIcon( img, tooltip );
            icon.addActionListener( trayListener );
            icon.setImageAutoSize( true );
            try
            {
                tray.add( icon );
                frame.addWindowListener( new HideOnMinimizeListener( frame ) );
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
     * @param f
     **************************************************************************/
    private static void handleExtendedState( JFrame f )
    {
        if( f.isVisible() )
        {
            // -------------------------------------------------------------
            // This stinks because it cannot retain the extended state.
            // I've tried all sorts of ways and this is the only thing
            // that doesn't pop the window up behind other things and still
            // give it focus. So, don't remove it.
            // -------------------------------------------------------------
            f.setExtendedState( JFrame.NORMAL );
            f.requestFocus();
            f.toFront();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * @param icon
     * @param popup
     **************************************************************************/
    public static void addTrayMenu( TrayIcon icon, JPopupMenu popup )
    {
        icon.addMouseListener( new TrayMouseListener( popup ) );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public static Font getFixedFont( int size )
    {
        return new Font( Font.MONOSPACED, Font.PLAIN, size );
    }

    /***************************************************************************
     * @param color
     * @return
     **************************************************************************/
    public static Color inverseColor( Color color )
    {
        int a = color.getAlpha();
        int r = a - color.getRed();
        int g = a - color.getGreen();
        int b = a - color.getBlue();

        return new Color( r, g, b, a );
    }

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public static void toFrontRestoreState( JFrame frame )
    {
        if( frame.isVisible() )
        {
            int extState = frame.getExtendedState();

            if( ( extState & JFrame.ICONIFIED ) == JFrame.ICONIFIED )
            {
                if( ( extState & JFrame.MAXIMIZED_BOTH ) == JFrame.MAXIMIZED_BOTH )
                {
                    frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
                }
                else if( ( extState & JFrame.MAXIMIZED_HORIZ ) == JFrame.MAXIMIZED_HORIZ )
                {
                    frame.setExtendedState( JFrame.MAXIMIZED_HORIZ );
                }
                else if( ( extState & JFrame.MAXIMIZED_VERT ) == JFrame.MAXIMIZED_VERT )
                {
                }
                else
                {
                    frame.setExtendedState( JFrame.NORMAL );
                }
            }

            frame.toFront();
        }
        else
        {
            frame.setVisible( true );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static <T> Action createCopyAction( IDataView<T> view )
    {
        ActionListener listener = new CopyListener<T>( view );
        Icon icon = IconConstants.loader.getIcon( IconConstants.EDIT_COPY_16 );
        Action action = new ActionAdapter( listener, "Copy", icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static <T> Action createPasteAction(
        ItemActionListener<T> itemListener )
    {
        ActionListener listener = new PasteListener<T>( itemListener );
        Icon icon = IconConstants.loader.getIcon( IconConstants.EDIT_PASTE_16 );
        Action action = new ActionAdapter( listener, "Paste", icon );

        return action;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class CopyListener<T> implements ActionListener
    {
        private final IDataView<T> view;

        public CopyListener( IDataView<T> view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            T data = view.getData();
            try
            {
                String str = XStreamUtils.writeObjectXStream( data );
                Utils.setClipboardText( str );
            }
            catch( XStreamException ex )
            {
                ex.printStackTrace();
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }

        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class PasteListener<T> implements ActionListener
    {
        private final ItemActionListener<T> listener;

        public PasteListener( ItemActionListener<T> listener )
        {
            this.listener = listener;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            try
            {
                String str = Utils.getClipboardText();
                T data = XStreamUtils.readObjectXStream( str );

                listener.actionPerformed( new ItemActionEvent<T>( this, data ) );
            }
            catch( XStreamException ex )
            {
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class ShowFrameListener implements ActionListener
    {
        private JFrame f;

        public ShowFrameListener( JFrame frame )
        {
            f = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( f.isVisible() )
            {
                f.toFront();
            }
            else
            {
                f.setVisible( true );
                handleExtendedState( f );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class MiniMaximizeListener implements ActionListener
    {
        private JFrame f;

        public MiniMaximizeListener( JFrame frame )
        {
            f = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            f.setVisible( !f.isVisible() );
            SwingUtils.handleExtendedState( f );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected static class HideOnMinimizeListener extends WindowAdapter
    {
        private JFrame frame;

        public HideOnMinimizeListener( JFrame f )
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TrayMouseListener extends MouseAdapter
    {
        private final JPopupMenu popup;
        private final JDialog dialog;

        public TrayMouseListener( JPopupMenu popup )
        {
            this.popup = popup;
            this.dialog = new JDialog();

            popup.validate();

            dialog.setUndecorated( true );
            dialog.setSize( 10, 10 );
            dialog.validate();

            dialog.addWindowFocusListener( new WindowFocusListener()
            {
                @Override
                public void windowLostFocus( WindowEvent we )
                {
                    dialog.setVisible( false );
                }

                @Override
                public void windowGainedFocus( WindowEvent we )
                {
                }
            } );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 && !e.isConsumed() )
            {
                Point p = calcLocation( e.getPoint(), e.getLocationOnScreen() );

                popup.setLocation( p.x, p.y );
                dialog.setLocation( p.x, p.y );
                dialog.setMinimumSize( new Dimension( 0, 0 ) );
                dialog.setSize( 0, 0 );
                popup.setInvoker( dialog );
                dialog.setVisible( true );
                popup.setVisible( true );
            }
        }

        private Point calcLocation( Point p, Point loc )
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice [] gs = ge.getScreenDevices();

            for( int i = 0; i < gs.length; i++ )
            {
                DisplayMode dm = gs[i].getDisplayMode();
                GraphicsConfiguration gc = gs[i].getDefaultConfiguration();
                Rectangle r = new Rectangle( gc.getBounds() );

                if( r.contains( loc ) )
                {
                    if( ( dm.getHeight() + popup.getHeight() + 8 ) > p.y )
                    {
                        p.y -= ( popup.getHeight() + 8 );
                    }
                }
            }

            return p;
        }
    }
}
