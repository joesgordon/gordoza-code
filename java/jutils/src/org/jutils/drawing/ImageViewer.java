package org.jutils.drawing;

import java.awt.*;

import javax.swing.*;

import org.jutils.drawing.SingleChannelImage.ImageStats;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Defines an {@link IView} that displays a {@link SingleChannelImageView} and a
 * {@link HistogramView}.
 ******************************************************************************/
public class ImageViewer implements IView<JComponent>
{
    /** The panel that contins all the items in this view. */
    private final JPanel view;
    /** The display of the current image. */
    private final SingleChannelImageView imgView;
    /** The histogram for the image currently being displayed. */
    private final HistogramView histView;

    /** The image currently being displayed. */
    private SingleChannelImage image;

    /***************************************************************************
     * Create a new image viewer.
     **************************************************************************/
    public ImageViewer()
    {
        this.imgView = new SingleChannelImageView();
        this.histView = new HistogramView();
        this.view = createView();
    }

    /***************************************************************************
     * Creates the panel that contains all the items in this view.
     * @return the panel that contains all the items in this view.
     **************************************************************************/
    private JPanel createView()
    {
        histView.setLowUpdater( ( d ) -> imgView.setLowThreshold( d ) );
        histView.setHighUpdater( ( d ) -> imgView.setHighThreshold( d ) );
        histView.setBinCountUpdater( ( d ) -> {
            ImageStats stats = image.getStats( histView.getData() );
            histView.setHistogram( stats.histogram );
        } );
        histView.setColorModelUpdater( ( d ) -> imgView.setColorModel( d ) );
        histView.setContrastUpdater( ( d ) -> imgView.setContrast( d ) );

        JPanel view = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets( 10, 20, 0, 20 ), 0, 0 );
        view.add( imgView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 20, 20, 20 ), 0, 0 );
        view.add( histView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 2, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( Box.createHorizontalStrut( 0 ), constraints );

        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * Sets the image to be shown.
     * @param image the image to be shown.
     **************************************************************************/
    public void setImage( SingleChannelImage image )
    {
        this.image = image;

        ImageStats imgStats = image.getStats( histView.getData() );

        imgView.setData( image );
        imgView.setStats( imgStats.stats );

        histView.setHistogram( imgStats.histogram );

        view.validate();
    }
}
