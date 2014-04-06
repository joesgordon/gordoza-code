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
