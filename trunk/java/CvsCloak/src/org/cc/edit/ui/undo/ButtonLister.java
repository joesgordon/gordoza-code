package org.cc.edit.ui.undo;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * Defines a button that will display a list of type T when the user hovers the
 * mouse cursor over the button.
 * @param <T> The type of the value to be displayed in the list.
 ******************************************************************************/
public class ButtonLister<T> extends JButton
{
    /**  */
    private ItemPanel<T> itemPanel;

    /***************************************************************************
     * @param panel
     **************************************************************************/
    public ButtonLister( ItemPanel<T> panel )
    {
        super();
        init( panel );
    }

    /***************************************************************************
     * @param icon
     * @param panel
     **************************************************************************/
    public ButtonLister( Icon icon, ItemPanel<T> panel )
    {
        super( icon );
        init( panel );
    }

    /***************************************************************************
     * @param text
     * @param panel
     **************************************************************************/
    public ButtonLister( String text, ItemPanel<T> panel )
    {
        super( text );
        init( panel );
    }

    /***************************************************************************
     * @param a
     * @param panel
     **************************************************************************/
    public ButtonLister( Action a, ItemPanel<T> panel )
    {
        super( a );
        init( panel );
    }

    /***************************************************************************
     * @param text
     * @param icon
     * @param panel
     **************************************************************************/
    public ButtonLister( String text, Icon icon, ItemPanel<T> panel )
    {
        super( text, icon );
        init( panel );
    }

    /***************************************************************************
     * @param panel
     **************************************************************************/
    private void init( ItemPanel<T> panel )
    {
        itemPanel = panel;

        addMouseListener( new DoMouseListener( itemPanel ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DoMouseListener extends MouseAdapter
    {
        private JButton b;
        private PopupHider hider;

        public DoMouseListener( ItemPanel<T> pane )
        {
            b = ButtonLister.this;
            hider = new PopupHider( ButtonLister.this, pane, b );

            pane.addItemSelectedListener( new ItemSelectedListener() );
        }

        public void mouseEntered( MouseEvent e )
        {
            Thread t = new Thread()
            {
                public void run()
                {
                    try
                    {
                        Thread.sleep( 100 );
                    }
                    catch( InterruptedException ex )
                    {
                    }

                    final Point pt = b.getLocationOnScreen();

                    pt.y += b.getHeight();

                    SwingUtilities.invokeLater( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            hider.showPopup( pt );
                        }
                    } );
                }
            };
            t.start();
        }

        public void mouseExited( MouseEvent e )
        {
            hider.hidePopup();
        }

        private class ItemSelectedListener implements ItemActionListener<T>
        {
            @Override
            public void actionPerformed( ItemActionEvent<T> event )
            {
                hider.hideNow();
            }
        }
    }
}
