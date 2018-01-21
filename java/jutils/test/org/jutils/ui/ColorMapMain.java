package org.jutils.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jutils.io.ResourceLoader;
import org.jutils.ui.ColorMapView;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorMapMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new ColorMapApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ColorMapApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            ColorMapView view = new ColorMapView();
            StandardFrameView frameView = new StandardFrameView();
            JFrame frame = frameView.getView();
            ResourceLoader rl = new ResourceLoader( ColorMapMain.class, "." );

            try( InputStream is = rl.getInputStream( "pollen.bmp" ) )
            {
                BufferedImage img = ImageIO.read( is );
                view.setImage( img );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }

            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setSize( 900, 640 );

            frameView.setContent( view.getView() );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
