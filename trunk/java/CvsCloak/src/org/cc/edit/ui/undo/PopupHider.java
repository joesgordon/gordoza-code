package org.cc.edit.ui.undo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PopupHider
{
    /**  */
    private Popup popup;
    /**  */
    private List<Component> components;
    /**  */
    private PopupFactory factory;
    /**  */
    private Component content;
    /**  */
    private Component parent;

    /***************************************************************************
     * @param parent
     * @param content
     * @param comps
     **************************************************************************/
    public PopupHider( Component parent, Component content, Component... comps )
    {
        this.parent = parent;
        this.components = new ArrayList<Component>( Arrays.asList( comps ) );
        components.add( content );
        this.popup = null;
        this.content = content;
        this.factory = PopupFactory.getSharedInstance();
    }

    /***************************************************************************
     * @param pt
     **************************************************************************/
    public void showPopup( Point pt )
    {
        if( popup == null )
        {
            popup = factory.getPopup( parent, content, pt.x, pt.y );
            popup.show();
        }
    }

    /***************************************************************************
     * Hides the popup only if the mouse is not over c.
     **************************************************************************/
    public void hidePopup()
    {
        if( popup != null )
        {
            Timer t = new Timer( 75, new HideListener() );
            t.start();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void hideNow()
    {
        if( popup != null )
        {
            popup.hide();
            popup = null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private synchronized void hide()
    {
        if( popup != null )
        {
            boolean isContained = false;

            for( Component comp : components )
            {
                if( !comp.isShowing() )
                {
                    continue;
                }

                Point mpt = MouseInfo.getPointerInfo().getLocation();
                Point min = comp.getLocationOnScreen();
                Point max = new Point( min );

                max.x += comp.getWidth();
                max.y += comp.getHeight();

                // mLoc.x -= pLoc.x;
                // mLoc.y -= pLoc.y;

                // LogUtils.printDebug( "min: [" + min.x + ", " + min.y + "]"
                // );
                // LogUtils.printDebug( "max: [" + max.x + ", " + max.y + "]"
                // );
                // LogUtils.printDebug( "mpt: [" + max.x + ", " + mpt.y + "]"
                // );

                if( !( mpt.x < min.x || mpt.x > max.x || mpt.y < min.y || mpt.y > max.y ) )
                {
                    isContained = true;
                    break;
                }
            }

            if( !isContained )
            {
                hideNow();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class HideListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            hide();
        }
    }
}
