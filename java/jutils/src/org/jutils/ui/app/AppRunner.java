package org.jutils.ui.app;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBluer;

//TODO comments

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
            String lafName = app.getLookAndFeelName();

            if( lafName == null )
            {
                UIManager.put( "TabbedPaneUI",
                    BasicTabbedPaneUI.class.getCanonicalName() );

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
                UIManager.put(
                    "CheckBoxMenuItem.acceleratorSelectionBackground", c );
                UIManager.put( "CheckBoxMenuItem.selectionBackground", c );
                UIManager.put( "EditorPane.selectionBackground", c );
                UIManager.put( "FormattedTextField.selectionBackground", c );
                UIManager.put( "List.selectionBackground", c );
                UIManager.put( "Menu.acceleratorForeground", c );
                UIManager.put( "MenuItem.acceleratorSelectionBackground", c );
                UIManager.put( "MenuItem.selectionBackground", c );
                UIManager.put( "PasswordField.selectionBackground", c );
                UIManager.put( "RadioButton.check", c );
                UIManager.put(
                    "RadioButtonMenuItem.acceleratorSelectionBackground", c );
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
            }

            UIManager.setLookAndFeel( lafName );

            KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
                "focusOwner", new ScrollPaneFocusListener() );

            app.createAndShowUi();
        }
        catch( Throwable ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }

    private static class ScrollPaneFocusListener implements
        PropertyChangeListener
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
}
