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
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorGenFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final JButton goButton;
    /**  */
    private final JProgressBar progressBar;
    /**  */
    private final DefaultListModel<GenericColor> colorModel;
    /**  */
    private final JComboBox<Comparator<GenericColor>> comparatorComboBox;

    /**  */
    private List<GenericColor> colors;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColorGenFrame()
    {
        this.frameView = new StandardFrameView();

        frameView.setTitle( "Color Generation Frame" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 400 );

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

        frameView.setContent( contentPanel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void saveColors()
    {
        JFileChooser saveChooser = new JFileChooser();

        int choice = saveChooser.showSaveDialog( getView() );

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
                JOptionPane.showMessageDialog( getView(),
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
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static final void main( String[] args )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                ColorGenFrame frameView = new ColorGenFrame();
                JFrame frame = frameView.getView();

                // frame.pack();
                frame.validate();

                return frame;
            }
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ColorGenerator implements IStoppableTask
    {
        /**  */
        private List<GenericColor> colors;
        /**  */
        private ColorGenFrame frame;

        /***********************************************************************
         * @param frame
         **********************************************************************/
        public ColorGenerator( ColorGenFrame frame )
        {
            this.frame = frame;
        }

        /***********************************************************************
         * 
         **********************************************************************/
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

        /***********************************************************************
         * @return
         **********************************************************************/
        private static List<GenericColor> getColors()
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class GcLogComparator implements Comparator<GenericColor>
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

        @Override
        public String toString()
        {
            return "RGB Log";
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class GcRadiusComparator implements Comparator<GenericColor>
    {
        @Override
        public int compare( GenericColor gcThis, GenericColor gcThat )
        {
            return Double.compare( gcThis.getRadius(), gcThat.getRadius() );
        }

        @Override
        public String toString()
        {
            return "3D Distance";
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class GcReverseRadiusComparator
        implements Comparator<GenericColor>
    {
        @Override
        public int compare( GenericColor gcThis, GenericColor gcThat )
        {
            GenericColor thisR = new GenericColor( 255 - gcThis.x,
                255 - gcThis.y, 255 - gcThis.z );
            GenericColor thatR = new GenericColor( 255 - gcThat.x,
                255 - gcThat.y, 255 - gcThat.z );
            return Double.compare( thisR.getRadius(), thatR.getRadius() );
        }

        @Override
        public String toString()
        {
            return "Reverse 3D Distance";
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class GcXyzComparator implements Comparator<GenericColor>
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

        @Override
        public String toString()
        {
            return "R->G->B";
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ColorCellDecorator implements IListCellLabelDecorator
    {
        /**  */
        private final Font boldFont;
        /**  */
        private ColorIcon icon;

        /***********************************************************************
         *
         **********************************************************************/
        public ColorCellDecorator()
        {
            Font f = UIManager.getFont( "Label.font" );

            this.icon = new ColorIcon( Color.white, 16 );
            this.boldFont = f.deriveFont( f.getStyle() | Font.BOLD );
        }

        /***********************************************************************
         * 
         **********************************************************************/
        @Override
        public void decorate( JLabel label, JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            if( value != null )
            {
                label.setIcon( icon );
                GenericColor c = ( GenericColor )value;
                label.setText(
                    "The quick brown fox jumped over the lazy dog. (" +
                        c.toString() + ")" );
                label.setFont( boldFont );
                label.setForeground( c.getColor() );
                icon.setColor( c.getColor() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class GenericColor
    {
        /**  */
        protected int x;
        /**  */
        protected int y;
        /**  */
        protected int z;
        /**  */
        private double radius;

        /***********************************************************************
         * @param x
         * @param y
         * @param z
         **********************************************************************/
        public GenericColor( int x, int y, int z )
        {
            this.x = x;
            this.y = y;
            this.z = z;
            radius = -1.0;
        }

        /***********************************************************************
         * @return
         **********************************************************************/
        public Color getColor()
        {
            return new Color( x, y, z );
        }

        /***********************************************************************
         * @param x
         * @param y
         * @param z
         * @return
         **********************************************************************/
        public static double calcRadius( int x, int y, int z )
        {
            double radius = x * x + y * y + z * z;

            radius = Math.sqrt( radius );

            return radius;
        }

        /***********************************************************************
         * @return
         **********************************************************************/
        public double getRadius()
        {
            if( radius < 0.0 )
            {
                radius = calcRadius( x, y, z );
            }

            return radius;
        }

        /***********************************************************************
         *
         **********************************************************************/
        @Override
        public String toString()
        {
            return Utils.argsToString( x, y, z );
        }
    }
}
