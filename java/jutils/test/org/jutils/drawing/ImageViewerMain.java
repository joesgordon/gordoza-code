package org.jutils.drawing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileChooserListener.ILastFile;
import org.jutils.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageViewerMain
{
    /**  */
    private static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "ImageViewer", "options.xml" );

    /***************************************************************************
     * Program main entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new ImageViewerApp(), false );
    }

    /***************************************************************************
     * @return the test image embedded with this code.
     **************************************************************************/
    public static SingleChannelImage getTestImage()
    {
        BufferedImage img = ColorMapMain.getTestImage();

        return SingleChannelImage.fromBufferedImage( img );
    }

    /***************************************************************************
     * @return user options for this application.
     **************************************************************************/
    private static OptionsSerializer<ImageViewUserData> getUserOptions()
    {
        return OptionsSerializer.getOptions( ImageViewUserData.class,
            USER_OPTIONS_FILE );
    }

    /***************************************************************************
     * Defines the application to test {@link ImageViewer}.
     **************************************************************************/
    private static final class ImageViewerApp implements IFrameApp
    {
        /**  */
        private StandardFrameView frameView;
        /**  */
        private ImageViewer viewer;
        /**  */
        // private LabelImage viewer;
        /**  */
        private RecentFilesViews recentFiles;

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            this.frameView = new StandardFrameView();
            this.viewer = new ImageViewer();
            // this.viewer = new LabelImage();
            this.recentFiles = new RecentFilesViews( 20 );
            // JScrollPane pane = new JScrollPane( viewer.getView() );

            // pane.getVerticalScrollBar().setUnitIncrement( 12 );

            SingleChannelImage img = getTestImage();

            viewer.setImage( img );

            frameView.setTitle( "ImageViewer App" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setContent( viewer.getView() );
            frameView.setToolbar( createToolbar() );

            return frameView.getView();
        }

        /**
         * @return the toolbar for this frame
         */
        private JToolBar createToolbar()
        {
            JGoodiesToolBar toolbar = new JGoodiesToolBar();

            recentFiles.install( toolbar, createOpenAction() );
            recentFiles.setListeners( ( f, c ) -> openFile( f ) );

            recentFiles.setData(
                getUserOptions().getOptions().recentFiles.toList() );

            return toolbar;
        }

        /**
         * @return the action that opens images and converts to grayscale.
         */
        private ActionListener createOpenAction()
        {
            IFileSelected fileSelected = ( f ) -> openFile( f );
            ILastFile lastFile = () -> getUserOptions().getOptions().recentFiles.first();
            FileChooserListener listener = new FileChooserListener(
                frameView.getView(), "Open Image", false, fileSelected,
                lastFile );

            return listener;
        }

        /**
         * @param f the file to open
         */
        private void openFile( File f )
        {
            BufferedImage image = null;

            OptionsSerializer<ImageViewUserData> userio = getUserOptions();
            userio.getOptions().recentFiles.push( f );
            userio.write();

            try
            {
                image = ImageIO.read( f );
            }
            catch( IOException ex )
            {
                SwingUtils.showErrorMessage( frameView.getView(),
                    "Unable to read image: " + ex.getMessage(), "I/O Error" );
                return;
            }

            if( image.getType() != BufferedImage.TYPE_BYTE_GRAY )
            {
                BufferedImage gsImage = new BufferedImage( image.getWidth(),
                    image.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
                Graphics g = gsImage.getGraphics();
                g.drawImage( image, 0, 0, null );
                g.dispose();
                image = gsImage;
            }

            viewer.setImage( SingleChannelImage.fromBufferedImage( image ) );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static final class ImageViewUserData
    {
        /**  */
        public final MaxQueue<File> recentFiles;

        /**
         * 
         */
        public ImageViewUserData()
        {
            this.recentFiles = new MaxQueue<>( 20 );
        }

        /**
         * @param data the data to be copied.
         */
        public ImageViewUserData( ImageViewUserData data )
        {
            this();

            if( data.recentFiles != null )
            {
                this.recentFiles.addAll( data.recentFiles );
            }
        }
    }

    private static final class LabelImage
    {
        private final JLabel label;

        public LabelImage()
        {
            this.label = new JLabel();
        }

        public Component getView()
        {
            return label;
        }

        public void setImage( SingleChannelImage img )
        {
            BufferedScImage bsi = new BufferedScImage( img );
            label.setIcon( new ImageIcon( bsi.getBufferedImage() ) );
        }
    }
}
