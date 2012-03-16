package org.jutils.ui;

import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBluer;

/*******************************************************************************
 * Helper class that sets up the UI in a standard way. When a class,
 * <code>SomeMain</code>, extends this that classes main is normally very small:
 * <code> public static void main( String [] args ) {
 * SwingUtilities.invokeLater( new SomeMain() ); }
 ******************************************************************************/
public abstract class MainRunner implements Runnable
{
    /***************************************************************************
     * Function called by the event queue.
     **************************************************************************/
    @Override
    public final void run()
    {
        try
        {
            String lafName = getLookAndFeelName();

            if( lafName == null )
            {
                lafName = Options.PLASTICXP_NAME;
            }

            UIManager.setLookAndFeel( lafName );
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }

        createAndShowGui();
    }

    /***************************************************************************
     * Returns the look and feel full qualified class path.
     **************************************************************************/
    protected String getLookAndFeelName()
    {
        // PlasticLookAndFeel.setTabStyle(
        // PlasticLookAndFeel.TAB_STYLE_METAL_VALUE );
        // PlasticLookAndFeel.setTabStyle( "Metal" );

        UIManager.put( "TabbedPaneUI",
            BasicTabbedPaneUI.class.getCanonicalName() );
        PlasticLookAndFeel.setPlasticTheme( new DesertBluer() );
        Options.setSelectOnFocusGainEnabled( true );

        return Options.PLASTICXP_NAME;
    }

    /***************************************************************************
     * Function should create and show the GUI.
     **************************************************************************/
    protected abstract void createAndShowGui();
}
