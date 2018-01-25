package org.jutils.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jutils.drawing.HistogramView;
import org.jutils.io.ResourceLoader;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * Defines a test application for {@link ColorMapView}
 ******************************************************************************/
public class ColorMapMain
{
    /***************************************************************************
     * The application entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new ColorMapApp() );
    }

    /***************************************************************************
     * Defines the app to display the view.
     **************************************************************************/
    private static class ColorMapApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            JFrame frame = frameView.getView();

            frameView.setTitle( "Color Map Test" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 550, 800 );
            frameView.setContent( createView() );

            return frame;
        }

        private Container createView()
        {
            ColorMapView mapView = new ColorMapView();
            ResourceLoader rl = new ResourceLoader( ColorMapMain.class, "." );
            int [] hist = new int[256];

            try( InputStream is = rl.getInputStream( "pollen.bmp" ) )
            {
                BufferedImage img = ImageIO.read( is );
                mapView.setImage( img );

                int [] pixel = new int[1];
                for( int x = 0; x < img.getRaster().getWidth(); x++ )
                {
                    for( int y = 0; y < img.getRaster().getHeight(); y++ )
                    {
                        img.getRaster().getPixel( x, y, pixel );

                        hist[pixel[0]]++;
                    }
                }
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }

            HistogramView histView = new HistogramView( 8 );

            histView.setHistogram( hist );

            JPanel view = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            view.add( mapView.getView(), constraints );

            constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            view.add( histView.getView(), constraints );

            constraints = new GridBagConstraints( 0, 2, 2, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            view.add( Box.createHorizontalStrut( 0 ), constraints );

            return view;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
