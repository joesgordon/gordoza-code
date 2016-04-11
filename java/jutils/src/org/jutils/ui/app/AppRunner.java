package org.jutils.ui.app;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBluer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppRunner implements Runnable
{
    /**  */
    private final IApplication app;

    /***************************************************************************
     * @param app
     **************************************************************************/
    public AppRunner( IApplication app )
    {
        this.app = app;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run()
    {
        try
        {
            setDefaultLaf( app.getLookAndFeelName() );

            app.createAndShowUi();
        }
        catch( IllegalStateException ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }

    public static void setDefaultLaf() throws IllegalStateException
    {
        setDefaultLaf( null );
    }

    public static void setDefaultLaf( String lafName )
        throws IllegalStateException
    {
        if( lafName == null )
        {
            Color c;

            c = new Color( 0x3A6EA7 );

            UIManager.put( "activeCaptionBorder", c );
            UIManager.put( "desktop", c );
            UIManager.put( "Button.focus", c );
            UIManager.put( "CheckBox.focus", c );
            UIManager.put( "Desktop.background", c );
            UIManager.put( "ProgressBar.foreground", c );
            UIManager.put( "RadioButton.focus", c );
            UIManager.put( "ScrollBar.thumb", c );
            UIManager.put( "Slider.focus", c );
            UIManager.put( "TabbedPane.focus", c );
            UIManager.put( "Slider.foreground", c );
            UIManager.put( "Table.dropLineColor", c );
            UIManager.put( "ToggleButton.focus", c );
            UIManager.put( "Tree.selectionBorderColor", c );

            c = new Color( 0x0A315A );

            UIManager.put( "CheckBox.check", c );
            UIManager.put( "CheckBoxMenuItem.acceleratorSelectionBackground",
                c );
            UIManager.put( "CheckBoxMenuItem.selectionBackground", c );
            UIManager.put( "EditorPane.selectionBackground", c );
            UIManager.put( "FormattedTextField.selectionBackground", c );
            UIManager.put( "List.selectionBackground", c );
            UIManager.put( "Menu.acceleratorForeground", c );
            UIManager.put( "MenuItem.acceleratorSelectionBackground", c );
            UIManager.put( "MenuItem.selectionBackground", c );
            UIManager.put( "PasswordField.selectionBackground", c );
            UIManager.put( "RadioButton.check", c );
            UIManager.put( "RadioButtonMenuItem.acceleratorSelectionBackground",
                c );
            UIManager.put( "RadioButtonMenuItem.selectionBackground", c );
            UIManager.put( "ScrollBar.thumbShadow", c );
            UIManager.put( "SimpleInternalFrame.activeTitleBackground", c );
            UIManager.put( "Table.dropLineShortColor", c );
            UIManager.put( "Table.selectionBackground", c );
            UIManager.put( "TextArea.selectionBackground", c );
            UIManager.put( "TextField.selectionBackground", c );
            UIManager.put( "TextPane.selectionBackground", c );
            UIManager.put( "TitledBorder.titleColor", c );
            UIManager.put( "ToolBar.dockingForeground", c );
            UIManager.put( "Tree.selectionBackground", c );
            UIManager.put( "textHighlight", c );

            PlasticLookAndFeel.setPlasticTheme( new DesertBluer() );
            Options.setSelectOnFocusGainEnabled( true );

            lafName = Options.PLASTICXP_NAME;

            UIManager.put( "TabbedPaneUI",
                BasicTabbedPaneUI.class.getCanonicalName() );
        }

        try
        {
            UIManager.setLookAndFeel( lafName );
        }
        catch( ClassNotFoundException ex )
        {
            throw new IllegalStateException(
                "Cannot find the class for the provided Look-and-Feel: " +
                    lafName,
                ex );
        }
        catch( InstantiationException ex )
        {
            throw new IllegalStateException(
                "Cannot create the provided Look-and-Feel: " + lafName, ex );
        }
        catch( IllegalAccessException ex )
        {
            throw new IllegalStateException(
                "Cannot access the provided Look-and-Feel: " + lafName, ex );
        }
        catch( UnsupportedLookAndFeelException ex )
        {
            throw new IllegalStateException(
                "Cannot support the provided Look-and-Feel: " + lafName, ex );
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
            "focusOwner", new ScrollPaneFocusListener() );
    }

    /***************************************************************************
     * @param message
     * @param title
     * @param options
     * @param defaultValue
     * @return
     **************************************************************************/
    public static <T> T invokeOptions( String message, String title,
        T [] options, T defaultValue )
    {
        OptionsApp<T> app = new OptionsApp<>( message, title, options,
            defaultValue );

        invokeAndWait( app );

        return app.getAnswer();
    }

    /***************************************************************************
     * @param message
     * @param title
     * @param options
     * @param defaultValue
     * @return
     **************************************************************************/
    public static <T> T invokeInput( String message, String title, T [] options,
        T defaultValue )
    {
        InputApp<T> app = new InputApp<>( message, title, options,
            defaultValue );

        invokeAndWait( app );

        return app.getAnswer();
    }

    /***************************************************************************
     * @param message
     * @param title
     **************************************************************************/
    public static void invokeError( String message, String title )
    {
        invokeMessage( message, title, JOptionPane.ERROR_MESSAGE );
    }

    /***************************************************************************
     * @param message
     * @param title
     * @param type
     **************************************************************************/
    public static void invokeMessage( String message, String title, int type )
    {
        MessageApp app = new MessageApp( message, title, type );

        invokeAndWait( app );
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    public static void invokeLater( IApplication app )
    {
        SwingUtilities.invokeLater( new AppRunner( app ) );
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    public static void invokeAndWait( IApplication app )
    {
        try
        {
            SwingUtilities.invokeAndWait( new AppRunner( app ) );
        }
        catch( InvocationTargetException ex )
        {
        }
        catch( InterruptedException ex )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ScrollPaneFocusListener
        implements PropertyChangeListener
    {
        @Override
        public void propertyChange( PropertyChangeEvent evt )
        {
            if( !( evt.getNewValue() instanceof JComponent ) )
            {
                return;
            }

            Object focusedObj = evt.getNewValue();

            if( focusedObj instanceof Component )
            {
                Component focused = ( Component )focusedObj;
                Container parent = focused.getParent();

                if( parent instanceof JComponent )
                {
                    JComponent focusedParent = ( JComponent )parent;

                    focusedParent.scrollRectToVisible( focused.getBounds() );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageApp implements IApplication
    {
        private final String message;
        private final String title;
        private final int type;

        public MessageApp( String message, String title, int type )
        {
            this.message = message;
            this.title = title;
            this.type = type;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            JOptionPane.showMessageDialog( null, message, title, type );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class InputApp<T> implements IApplication
    {
        private final String message;
        private final String title;
        private final T [] selections;
        private final T defaultValue;

        private T answer;

        public InputApp( String message, String title, T [] selections,
            T defaultValue )
        {
            this.message = message;
            this.title = title;
            this.selections = selections;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        @SuppressWarnings( "unchecked")
        public void createAndShowUi()
        {
            answer = ( T )JOptionPane.showInputDialog( null, message, title,
                JOptionPane.QUESTION_MESSAGE, null, selections, defaultValue );
        }

        public T getAnswer()
        {
            return answer;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionsApp<T> implements IApplication
    {
        private final String message;
        private final String title;
        private final T [] selections;
        private final T defaultValue;

        private T answer;

        public OptionsApp( String message, String title, T [] selections,
            T defaultValue )
        {
            this.message = message;
            this.title = title;
            this.selections = selections;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            int idx = JOptionPane.showOptionDialog( null, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                selections, defaultValue );

            if( idx == JOptionPane.CLOSED_OPTION )
            {
                answer = null;
            }
            else
            {
                answer = selections[idx];
            }
        }

        public T getAnswer()
        {
            return answer;
        }
    }
}
