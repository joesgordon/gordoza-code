package org.jutils.drawing;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import org.jutils.drawing.HistogramView.HistogramConfig;
import org.jutils.drawing.SingleChannelImage.ImageStats;
import org.jutils.ui.ColorMapMain;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class SingleChannelImageMain
{
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new SingleChannelImageApp(), false );
    }

    public static SingleChannelImage getTestImage()
    {
        BufferedImage img = ColorMapMain.getTestImage();
        int w = img.getWidth();
        int h = img.getHeight();

        int [] pixels = new int[w * h];

        int [] pixel = new int[1];
        for( int x = 0; x < img.getRaster().getWidth(); x++ )
        {
            for( int y = 0; y < img.getRaster().getHeight(); y++ )
            {
                int idx = y * w + x;

                img.getRaster().getPixel( x, y, pixel );
                pixels[idx] = pixel[0];
            }
        }

        SingleChannelImage sci = new SingleChannelImage( pixels, w, h,
            img.getColorModel().getPixelSize() );

        return sci;
    }

    private static final class SingleChannelImageApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            frameView.setTitle( "Single Channel Image App" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setContent( createContent() );

            return frameView.getView();
        }

        private Container createContent()
        {
            SingleChannelImageView imgView = new SingleChannelImageView();
            SingleChannelImage img = getTestImage();
            HistogramConfig histConfig = new HistogramConfig();
            HistogramView histView = new HistogramView();

            ImageStats imgStats = img.getStats( histConfig );

            imgView.setData( img );
            imgView.setStats( imgStats.stats );

            histView.setData( histConfig );
            histView.setHistogram( imgStats.histogram );
            histView.setLowUpdater( ( d ) -> imgView.setLowThreshold( d ) );
            histView.setHighUpdater( ( d ) -> imgView.setHighThreshold( d ) );
            histView.setBinCountUpdater( ( d ) -> {
                ImageStats stats = img.getStats( histConfig );
                histView.setHistogram( stats.histogram );
            } );
            histView.setColorModelUpdater(
                ( d ) -> imgView.setColorModel( d ) );
            histView.setContrastUpdater( ( d ) -> imgView.setContrast( d ) );

            JPanel view = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                new Insets( 10, 20, 0, 20 ), 0, 0 );
            view.add( imgView.getView(), constraints );

            constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 10, 20, 20, 20 ), 0, 0 );
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
