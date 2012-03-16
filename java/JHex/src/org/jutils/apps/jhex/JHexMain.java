package org.jutils.apps.jhex;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexMain extends FrameRunner
{
    private final File file;

    /***************************************************************************
     * 
     **************************************************************************/
    public JHexMain()
    {
        this( null );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public JHexMain( File file )
    {
        this.file = file;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JFrame createFrame()
    {
        JHexFrame frame = new JHexFrame();

        frame.setSize( new Dimension( 800, 600 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        if( file != null )
        {
            frame.openFile( file );
        }

        return frame;
    }

    public void finalizeGui( JFrame frame )
    {
        JHexFrame hexFrame = ( JHexFrame )frame;

        if( file == null )
        {
            hexFrame.showOpenDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected boolean validate()
    {
        return true;
    }

    /***************************************************************************
     * @param args Unused arguments.
     **************************************************************************/
    public static void main( String[] args )
    {
        File file = null;

        if( args.length > 0 )
        {
            file = new File( args[0] );
            if( !file.isFile() )
            {
                System.err.println( "File does not exist: " +
                    file.getAbsolutePath() );
                System.exit( -1 );
            }
        }

        SwingUtilities.invokeLater( new JHexMain( file ) );
    }

}
