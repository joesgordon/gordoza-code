package org.jutils.ui;

import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.data.ColorMapFactory;
import org.jutils.data.ColorMapType;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.NamedItemDescriptor;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorMapView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final ComboFormField<ColorMapType> colorMapField;

    /**  */
    private final ComponentView exampleView;
    /**  */
    private final ComponentView legendView;
    /**  */
    private final ColorMapFactory mapFactory;

    private BufferedImage exampleImage;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColorMapView()
    {
        this.colorMapField = new ComboFormField<>( "Color Map",
            ColorMapType.values(), new NamedItemDescriptor<ColorMapType>() );
        this.exampleView = new ComponentView();
        this.legendView = new ComponentView();
        this.mapFactory = new ColorMapFactory();
        this.view = createView();

        colorMapField.setValue( ColorMapType.STANDARD );
        colorMapField.setUpdater( ( m ) -> setModel( m ) );

        setModel( ColorMapType.STANDARD );
    }

    /***************************************************************************
     * @param icm
     * @return
     **************************************************************************/
    private JLabel createExample( IndexColorModel icm )
    {
        BufferedImage img;

        if( exampleImage != null )
        {
            int w = exampleImage.getWidth();
            int h = exampleImage.getHeight();
            img = new BufferedImage( w, h, BufferedImage.TYPE_BYTE_INDEXED,
                icm );

            exampleImage.copyData( img.getRaster() );

            // DataBuffer imgBuf = img.getRaster().getDataBuffer();
            // DataBuffer exBuf = exampleImage.getRaster().getDataBuffer();
            //
            // for( int i = 0; i < exBuf.getSize(); i++ )
            // {
            // imgBuf.setElem( i, exBuf.getElem( i ) );
            // }

            // img.getGraphics().drawImage( exampleImage, 0, 0, null );
        }
        else
        {
            img = new BufferedImage( 640, 512, BufferedImage.TYPE_BYTE_INDEXED,
                icm );

            fillExampleImage( img );
        }

        return new JLabel( new ImageIcon( img ) );
    }

    /***************************************************************************
     * @param icm
     * @return
     **************************************************************************/
    private static JLabel createLegend( IndexColorModel icm )
    {
        BufferedImage img = new BufferedImage( 50, 256,
            BufferedImage.TYPE_BYTE_INDEXED, icm );

        fillLegendImage( img );

        return new JLabel( new ImageIcon( img ) );
    }

    /***************************************************************************
     * @param img
     **************************************************************************/
    private static void fillExampleImage( BufferedImage img )
    {
        DataBufferByte imgBuffer = ( DataBufferByte )img.getRaster().getDataBuffer();
        byte [] imgBytes = imgBuffer.getData();

        int w = img.getWidth();

        byte [] rowBytes = new byte[w];

        int cnt = 32;
        int delta = w / cnt;
        int idx = 0;
        for( int i = 0; i < cnt; i++ )
        {
            byte p = ( byte )( i + 0x28 );
            int to = idx + delta - 1;

            Arrays.fill( rowBytes, idx, to, p );
            // rowBytes[to] = 0;
            rowBytes[to] = p;

            idx += delta;
        }

        for( int r = 0; r < img.getHeight(); r++ )
        {
            Utils.byteArrayCopy( rowBytes, 0, imgBytes, r * w, w );
        }
    }

    /***************************************************************************
     * @param img
     **************************************************************************/
    private static void fillLegendImage( BufferedImage img )
    {
        DataBufferByte imgBuffer = ( DataBufferByte )img.getRaster().getDataBuffer();
        byte [] imgBytes = imgBuffer.getData();
        int w = img.getWidth();

        for( int d = 255; d > -1; d-- )
        {
            byte p = ( byte )d;
            int idx = ( 255 - d ) * w;
            Arrays.fill( imgBytes, idx, idx + w, p );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {

        JPanel mapPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets( 0, 6, 6, 40 ), 0, 0 );
        mapPanel.add( exampleView.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets( 0, 0, 6, 6 ), 0, 0 );
        mapPanel.add( legendView.getView(), constraints );

        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createOptions(), BorderLayout.NORTH );
        panel.add( mapPanel, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createOptions()
    {
        StandardFormView form = new StandardFormView();

        form.addField( colorMapField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param map
     **************************************************************************/
    public void setModel( ColorMapType map )
    {
        IndexColorModel icm = mapFactory.get( map, 0, 255 );

        exampleView.setComponent( createExample( icm ) );
        legendView.setComponent( createLegend( icm ) );
    }

    /***************************************************************************
     * @param img
     **************************************************************************/
    public void setImage( BufferedImage img )
    {
        this.exampleImage = img;
        setModel( colorMapField.getValue() );
    }
}
