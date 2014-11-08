package org.jutils.apps.jhex;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import org.jutils.io.OptionsSerializer;
import org.jutils.ui.app.IFrameApp;

/***************************************************************************
 * 
 **************************************************************************/
public class JHexApp implements IFrameApp
{
    /**  */
    private final File file;
    /**  */
    private final OptionsSerializer<JHexOptions> userio;
    /**  */
    private final boolean closeFileWithFrame;

    /**  */
    private JHexFrame view;

    /***************************************************************************
     * @param userio
     **************************************************************************/
    public JHexApp( OptionsSerializer<JHexOptions> userio )
    {
        this( userio, null );
    }

    /***************************************************************************
     * @param userio
     * @param file
     **************************************************************************/
    public JHexApp( OptionsSerializer<JHexOptions> userio, File file )
    {
        this( userio, file, true );
    }

    /***************************************************************************
     * @param userio
     * @param file
     * @param closeFileWithFrame
     **************************************************************************/
    public JHexApp( OptionsSerializer<JHexOptions> userio, File file,
        boolean closeFileWithFrame )
    {
        this.userio = userio;
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
        view = new JHexFrame( userio, closeFileWithFrame );
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
