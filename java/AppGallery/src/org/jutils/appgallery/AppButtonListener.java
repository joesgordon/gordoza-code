package org.jutils.appgallery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/*******************************************************************************
 * ActionListener that runs the provided application upon invocation.
 ******************************************************************************/
public class AppButtonListener implements ActionListener
{
    private final ILibraryApp app;

    /***************************************************************************
     * Creates a new listener using the provided application.
     **************************************************************************/
    public AppButtonListener( ILibraryApp app )
    {
        this.app = app;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        JFrame frame = app.runApp();

        if( frame != null )
        {
            frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        }
    }
}
