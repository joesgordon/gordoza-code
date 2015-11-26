package org.jutils.apps.jhex;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import org.jutils.ui.app.IFrameApp;

/***************************************************************************
 * 
 **************************************************************************/
public class JHexApp implements IFrameApp
{
    /**  */
    private final File file;
    /**  */
    private final boolean closeFileWithFrame;

    /**  */
    private JHexFrame view;

    /***************************************************************************
     *
     **************************************************************************/
    public JHexApp()
    {
        this( null );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public JHexApp( File file )
    {
        this( file, true );
    }

    /***************************************************************************
     * @param file
     * @param closeFileWithFrame
     **************************************************************************/
    public JHexApp( File file, boolean closeFileWithFrame )
    {
        this.file = file;
        this.closeFileWithFrame = closeFileWithFrame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JHexFrame getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        view = new JHexFrame( closeFileWithFrame );
        JFrame frame = view.getView();

        frame.setSize( new Dimension( 800, 600 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
        if( file != null )
        {
            view.openFile( file );
        }
    }
}
