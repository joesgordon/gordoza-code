package org.mc.ui;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import org.jutils.SwingUtils;
import org.mc.ui.BindingFrameView.IBindableView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BindableClosingListener extends WindowAdapter
{
    /**  */
    private final IBindableView view;
    /**  */
    private final Component parent;

    /***************************************************************************
     * @param view
     * @param parent
     **************************************************************************/
    public BindableClosingListener( IBindableView view, Component parent )
    {
        this.view = view;
        this.parent = parent;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void windowClosing( WindowEvent e )
    {
        try
        {
            view.unbind();
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( parent, ex.getMessage(),
                "Socket Close Error" );
        }
    }
}
