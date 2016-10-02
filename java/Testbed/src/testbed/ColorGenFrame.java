package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.concurrent.*;
import org.jutils.ui.ColorIcon;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

import com.jgoodies.looks.Options;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorGenFrame extends JFrame
{
    /**  */
    private JButton goButton;
    /**  */
    private JProgressBar progressBar;
    /**  */
    private DefaultListModel<GenericColor> colorModel;
    /**  */
    private List<GenericColor> colors;
    /**  */
    private JComboBox<Comparator<GenericColor>> comparatorComboBox;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColorGenFrame()
    {
        super( "Color Generation Frame" );

        ActionListener comparatorSelectedListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                Object item = comparatorComboBox.getSelectedItem();
                if( colors != null && item != null &&
                    item instanceof Comparator<?> )
                {
                    @SuppressWarnings( "unchecked")
                    Comparator<GenericColor> comp = ( Comparator<GenericColor> )item;
                    Collections.sort( colors, comp );
                    setData( colors );
                }
            }
        };

        // ---------------------------------------------------------------------
        // Setup the toolbar
        // ---------------------------------------------------------------------
        JToolBar toolbar = new JToolBar();
        JButton saveButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        goButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.CHECK_16 ) );

        saveButton.setFocusable( false );
        saveButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                saveColors();
            }
        } );

        goButton.setFocusable( false );
        goButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                generateColors();
            }
        } );

        toolbar.add( saveButton );
        toolbar.addSeparator();
        toolbar.add( goButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        // ---------------------------------------------------------------------
        // Setup color scroll pane.
        // ---------------------------------------------------------------------
        colorModel = new DefaultListModel<GenericColor>();
        JList<GenericColor> colorList = new JList<GenericColor>( colorModel );
        JScrollPane colorScrollPane = new JScrollPane( colorList );

        colorList.setFixedCellHeight( 30 );
        colorList.setCellRenderer(
            new LabelListCellRenderer( new ColorCellDecorator() ) );

        // ---------------------------------------------------------------------
        // Setup main panel.
        // ---------------------------------------------------------------------
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        JLabel compLabel = new JLabel( "Sort by:" );
        comparatorComboBox = new JComboBox<Comparator<GenericColor>>();
        progressBar = new JProgressBar();

        comparatorComboBox.setEnabled( false );
        comparatorComboBox.addActionListener( comparatorSelectedListener );
        comparatorComboBox.addItem( new GcRadiusComparator() );
        comparatorComboBox.addItem( new GcReverseRadiusComparator() );
        comparatorComboBox.addItem( new GcXyzComparator() );
        comparatorComboBox.addItem( new GcLogComparator() );

        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );
        progressBar.setStringPainted( true );
        progressBar.setString( "" );

        mainPanel.add( compLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        mainPanel.add( comparatorComboBox,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        mainPanel.add( progressBar,
            new GridBagConstraints( 0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        mainPanel.add( colorScrollPane,
            new GridBagConstraints( 0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup content panel.
        // ---------------------------------------------------------------------

        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.add( toolbar, BorderLayout.NORTH );
        contentPanel.add( mainPanel, BorderLayout.CENTER );

        setContentPane( contentPanel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void saveColors()
    {
        JFileChooser saveChooser = new JFileChooser();

        int choice = saveChooser.showSaveDialog( this );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File file = saveChooser.getSelectedFile();
            PrintStream outStream;

            try
            {
                outStream = new PrintStream( file );

                for( GenericColor c : colors )
                {
                    outStream.println( c.toString() );
                }

                outStream.close();
            }
            catch( FileNotFoundException ex )
            {
                JOptionPane.showMessageDialog( this,
                    "Error writing file: " + ex, "ERROR",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void generateColors()
    {
        ColorGenerator generator = new ColorGenerator( this );
        Stoppable stoppable = new Stoppable( generator );
        Thread thread = new Thread( stoppable );

        progressBar.setIndeterminate( true );
        thread.start();
    }

    /***************************************************************************
     * @param colors
     **************************************************************************/
    public void setData( List<GenericColor> colors )
    {
        this.colors = colors;

        progressBar.setIndeterminate( false );
        colorModel.clear();

        for( GenericColor c : colors )
        {
            colorModel.addElement( c );
        }
        comparatorComboBox.setEnabled( colors != null );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static final void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                ColorGenFrame frame = new ColorGenFrame();

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 500, 400 );
                frame.validate();
                // frame.pack();
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class ColorGenerator implements IStoppableTask
{
    /**  */
    private List<GenericColor> colors;
    /**  */
    private ColorGenFrame frame;

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public ColorGenerator( ColorGenFrame frame )
    {
        this.frame = frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        colors = getColors();

        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                frame.setData( colors );
            }
        } );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private List<GenericColor> getColors()
    {
        int max = 256;
        int dist = 64;
        List<GenericColor> gcList = new ArrayList<GenericColor>();

        for( int x = 0; x < max; x += dist )
        {
            for( int y = 0; y < max; y += dist )
            {
                for( int z = 0; z < max; z += dist )
                {
                    gcList.add( new GenericColor( x, y, z ) );
                }
            }
        }

        List<GenericColor> colors = new ArrayList<GenericColor>();

        for( GenericColor gc : gcList )
        {
            if( gc.x + gc.y + gc.z <= 384 )
            {
                colors.add( gc );
            }
        }

        return colors;
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class GcLogComparator implements Comparator<GenericColor>
{
    @Override
    public int compare( GenericColor gcThis, GenericColor gcThat )
    {
        double fact = 64.0;

        double xlThis = Math.pow( 10, gcThis.x / fact );
        double ylThis = Math.pow( 11, gcThis.y / fact );
        double zlThis = Math.pow( 12, gcThis.z / fact );
        double sumThis = xlThis + ylThis + zlThis;

        double xlThat = Math.pow( 10, gcThat.x / fact );
        double ylThat = Math.pow( 11, gcThat.y / fact );
        double zlThat = Math.pow( 12, gcThat.z / fact );
        double sumThat = xlThat + ylThat + zlThat;

        return Double.compare( sumThis, sumThat );
    }

    public String toString()
    {
        return "RGB Log";
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class GcRadiusComparator implements Comparator<GenericColor>
{
    @Override
    public int compare( GenericColor gcThis, GenericColor gcThat )
    {
        return Double.compare( gcThis.getRadius(), gcThat.getRadius() );
    }

    public String toString()
    {
        return "3D Distance";
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class GcReverseRadiusComparator implements Comparator<GenericColor>
{
    @Override
    public int compare( GenericColor gcThis, GenericColor gcThat )
    {
        GenericColor thisR = new GenericColor( 255 - gcThis.x, 255 - gcThis.y,
            255 - gcThis.z );
        GenericColor thatR = new GenericColor( 255 - gcThat.x, 255 - gcThat.y,
            255 - gcThat.z );
        return Double.compare( thisR.getRadius(), thatR.getRadius() );
    }

    public String toString()
    {
        return "Reverse 3D Distance";
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class GcXyzComparator implements Comparator<GenericColor>
{
    @Override
    public int compare( GenericColor gcThis, GenericColor gcThat )
    {
        int val = gcThis.x - gcThat.x;
        if( val == 0 )
        {
            val = gcThis.y - gcThat.y;
            if( val == 0 )
            {
                val = gcThis.z - gcThat.z;
            }
        }
        return val;
    }

    public String toString()
    {
        return "R->G->B";
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class ColorCellDecorator implements IListCellLabelDecorator
{
    /**  */
    private final Font boldFont;
    /**  */
    private ColorIcon icon;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColorCellDecorator()
    {
        Font f = UIManager.getFont( "Label.font" );

        this.icon = new ColorIcon( Color.white, 16 );
        this.boldFont = f.deriveFont( f.getStyle() | Font.BOLD );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void decorate( JLabel label, JList<?> list, Object value, int index,
        boolean isSelected, boolean cellHasFocus )
    {
        if( value != null )
        {
            label.setIcon( icon );
            GenericColor c = ( GenericColor )value;
            label.setText( "The quick brown fox jumped over the lazy dog. (" +
                c.toString() + ")" );
            label.setFont( boldFont );
            label.setForeground( c.getColor() );
            icon.setColor( c.getColor() );
        }
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class GenericColor
{
    /**  */
    protected int x;
    /**  */
    protected int y;
    /**  */
    protected int z;
    /**  */
    private double radius;

    /***************************************************************************
     * @param x
     * @param y
     * @param z
     **************************************************************************/
    public GenericColor( int x, int y, int z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        radius = -1.0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Color getColor()
    {
        return new Color( x, y, z );
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    public double getDistance( GenericColor c )
    {
        return calcRadius( c.x - x, c.y - y, c.z - z );
    }

    /***************************************************************************
     * @param x
     * @param y
     * @param z
     * @return
     **************************************************************************/
    public static double calcRadius( int x, int y, int z )
    {
        double radius = x * x + y * y + z * z;

        radius = Math.sqrt( radius );

        return radius;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getRadius()
    {
        if( radius < 0.0 )
        {
            radius = calcRadius( x, y, z );
        }

        return radius;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String toString()
    {
        return Utils.argsToString( x, y, z );
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class SmartColor extends GenericColor
{
    /**  */
    public static final int NUM_COLORS = 6;

    /***************************************************************************
     * @param x
     * @param y
     * @param z
     **************************************************************************/
    public SmartColor( int x, int y, int z )
    {
        super( x, y, z );
    }

    /***************************************************************************
     * @param idx
     * @return
     **************************************************************************/
    public static GenericColor getColor( SmartColor c, int idx )
    {
        int x = c.x;
        int y = c.y;
        int z = c.z;

        switch( idx )
        {
            case 0:
                return new GenericColor( x, y, z );
            case 1:
                return new GenericColor( x, z, y );
            case 2:
                return new GenericColor( y, x, z );
            case 3:
                return new GenericColor( y, z, x );
            case 4:
                return new GenericColor( z, x, y );
            case 5:
                return new GenericColor( z, y, x );
        }

        throw new ArrayIndexOutOfBoundsException(
            idx + " < 0 or >= " + NUM_COLORS );
    }

    /****************************************************************************
     * @param colorList
     * @param c
     ***************************************************************************/
    public static void addColors( List<GenericColor> colorList, SmartColor c )
    {
        boolean m1 = Math.abs( c.x - c.y ) <= 20;
        boolean m2 = Math.abs( c.x - c.z ) <= 20;
        boolean m3 = Math.abs( c.y - c.z ) <= 20;

        if( m1 && m2 && m3 )
        {
            colorList.add( getColor( c, 0 ) );
        }
        else if( m1 && m2 && !m3 )
        {
            colorList.add( getColor( c, 0 ) );
            colorList.add( getColor( c, 1 ) );
            colorList.add( getColor( c, 4 ) );
        }
        else if( m1 && !m2 && m3 )
        {
            colorList.add( getColor( c, 3 ) );
            colorList.add( getColor( c, 4 ) );
            colorList.add( getColor( c, 5 ) );
        }
        else if( !m1 && m2 && m3 )
        {
            colorList.add( getColor( c, 1 ) );
            colorList.add( getColor( c, 2 ) );
            colorList.add( getColor( c, 3 ) );
        }
        else
        {
            colorList.add( getColor( c, 0 ) );
            colorList.add( getColor( c, 1 ) );
            colorList.add( getColor( c, 2 ) );
            colorList.add( getColor( c, 3 ) );
            colorList.add( getColor( c, 4 ) );
            colorList.add( getColor( c, 5 ) );
        }
    }
}
