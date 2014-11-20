package org.jutils.chart.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jutils.*;
import org.jutils.chart.*;
import org.jutils.chart.app.JChartAppConstants;
import org.jutils.chart.app.UserData;
import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.io.DataFileReader;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.objects.ChartWidget;
import org.jutils.chart.ui.objects.SeriesWidget;
import org.jutils.io.IOUtils;
import org.jutils.io.OptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.DropActionType;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final WidgetPanel mainPanel;
    /**  */
    private final ChartWidget chartWidget;
    /**  */
    private final IPalette palette;
    /**  */
    public final PropertiesView propertiesView;

    /**  */
    public final Action openAction;
    /**  */
    public final Action saveAction;
    /**  */
    public final Action propertiesAction;

    /**  */
    private final ItemActionList<File> fileLoadedListeners;

    /**  */
    private final OptionsSerializer<UserData> userio;

    /**  */
    public final Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartView()
    {
        this( true, true );
    }

    /***************************************************************************
     * @param allowOpen
     * @param gradientToolbar
     **************************************************************************/
    public ChartView( boolean allowOpen, boolean gradientToolbar )
    {
        this.userio = JChartAppConstants.getUserIO();
        this.chart = new Chart();
        this.mainPanel = new WidgetPanel();
        this.chartWidget = new ChartWidget( chart );
        this.palette = new PresetPalette();
        this.propertiesView = new PropertiesView( chart );

        this.openAction = createOpenAction();
        this.saveAction = createSaveAction();
        this.propertiesAction = createPropertiesAction();

        this.view = createView( allowOpen, gradientToolbar );

        this.fileLoadedListeners = new ItemActionList<>();

        mainPanel.setObject( chartWidget );

        ChartMouseListenter ml = new ChartMouseListenter( this );

        mainPanel.addComponentListener( new ChartComponentListener( this ) );
        mainPanel.addMouseListener( ml );
        mainPanel.addMouseMotionListener( ml );
        mainPanel.addMouseWheelListener( ml );

        if( allowOpen )
        {
            mainPanel.setDropTarget( new FileDropTarget( new ChartDropTarget(
                this ) ) );
        }

        mainPanel.setFocusable( true );

        String actionMapKey = "delete_point";
        KeyStroke deleteKey = KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 );
        ActionMap amap = mainPanel.getActionMap();
        InputMap imap = mainPanel.getInputMap( JTable.WHEN_FOCUSED );

        imap.put( deleteKey, actionMapKey );
        amap.put( actionMapKey, new ActionAdapter( new DeletePointListener(
            this ), actionMapKey, null ) );

        mainPanel.setMinimumSize( new Dimension( 150, 150 ) );
    }

    /***************************************************************************
     * @param allowOpen
     * @param gradientToolbar
     * @return
     **************************************************************************/
    private JPanel createView( boolean allowOpen, boolean gradientToolbar )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createToolbar( allowOpen, gradientToolbar ), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( mainPanel, constraints );

        return panel;
    }

    /***************************************************************************
     * @param allowOpen
     * @param gradientToolbar
     * @return
     **************************************************************************/
    private Component createToolbar( boolean allowOpen, boolean gradientToolbar )
    {
        JToolBar toolbar;

        if( gradientToolbar )
        {
            toolbar = new JGoodiesToolBar();
        }
        else
        {
            toolbar = new JToolBar();
            SwingUtils.setToolbarDefaults( toolbar );
        }

        if( allowOpen )
        {
            SwingUtils.addActionToToolbar( toolbar, openAction );
        }

        SwingUtils.addActionToToolbar( toolbar, saveAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, propertiesAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createZoomInAction() );

        SwingUtils.addActionToToolbar( toolbar, createZoomOutAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Action action;
        FileChooserListener listener;
        Icon icon;
        String name;

        name = "Open";
        icon = IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 );
        listener = new FileChooserListener( getView(), "Choose Data File",
            new OpenListener( this ), false );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Save";
        icon = IconConstants.loader.getIcon( IconConstants.SAVE_16 );
        listener = new SaveListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createPropertiesAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Properties";
        icon = IconConstants.loader.getIcon( IconConstants.CONFIG_16 );
        listener = new PropertiesDialogListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createZoomInAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Zoom In";
        icon = ChartIcons.loader.getIcon( ChartIcons.ZOOM_IN_016 );
        listener = new ZoomInListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createZoomOutAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Zoom Out";
        icon = ChartIcons.loader.getIcon( ChartIcons.ZOOM_OUT_016 );
        listener = new ZoomOutListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFileLoadedListener( ItemActionListener<File> l )
    {
        fileLoadedListeners.addListener( l );
    }

    /***************************************************************************
     * @param s
     **************************************************************************/
    public void addSeries( Series s )
    {
        addSeries( s, false );
    }

    /***************************************************************************
     * @param s
     * @param addData
     **************************************************************************/
    public void addSeries( Series s, boolean addData )
    {
        if( !addData )
        {
            clear();
            propertiesView.removeAllSeries();
        }

        if( s.data.getCount() == 0 )
        {
            return;
        }

        chart.series.add( s );
        chartWidget.plot.serieses.add( new SeriesWidget( chart, s,
            chartWidget.context ) );
        restoreAndRepaintChart();

        propertiesView.addSeries( s, chart.series.size() );
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void importData( List<File> files )
    {
        boolean addData = false;

        clear();

        for( File file : files )
        {
            importData( file, addData );
            addData = true;
        }
    }

    /***************************************************************************
     * @param file
     * @param addData
     **************************************************************************/
    public void importData( File file, boolean addData )
    {
        try
        {
            DataFileReader reader = new DataFileReader();
            ISeriesData<?> data = reader.read( file );
            Series s = new Series( data );

            Color c = palette.next();

            s.name = IOUtils.removeFilenameExtension( file );
            s.resource = file.getAbsolutePath();
            s.marker.color = c;
            s.highlight.color = c;
            s.line.color = c;

            addSeries( s, addData );

            // LogUtils.printDebug( String.format( "x => (%f,%f)",
            // chart.plot.context.xMin, chart.plot.context.xMax ) );
            //
            // LogUtils.printDebug( String.format( "y => (%f,%f)",
            // chart.plot.context.yMin, chart.plot.context.yMax ) );
            // LogUtils.printDebug( "" );

            fileLoadedListeners.fireListeners( this, file );
        }
        catch( FileNotFoundException ex )
        {
            JOptionPane.showMessageDialog( mainPanel,
                "The file was not found: " + file.getAbsolutePath(),
                "File Not Found", JOptionPane.ERROR_MESSAGE );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( mainPanel,
                "I/O Exception: " + ex.getMessage(), "I/O Exception",
                JOptionPane.ERROR_MESSAGE );
        }

        if( chartWidget.plot.serieses.size() < 2 )
        {
            setTitle( file.getName() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        chart.title.text = "Title";
        chart.series.clear();
        chartWidget.plot.serieses.clear();

        restoreAndRepaintChart();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void zoomRestore()
    {
        chartWidget.context.restoreAutoBounds();
        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void zoomIn()
    {
        Bounds b = chartWidget.context.getBounds();

        b.primaryDomainSpan = b.primaryDomainSpan.zoomIn();
        b.primaryRangeSpan = b.primaryRangeSpan.zoomIn();

        if( b.secondaryDomainSpan != null )
        {
            b.secondaryDomainSpan = b.secondaryDomainSpan.zoomIn();
        }

        if( b.secondaryRangeSpan != null )
        {
            b.secondaryRangeSpan = b.secondaryRangeSpan.zoomIn();
        }

        chartWidget.context.setBounds( b );

        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void zoomOut()
    {
        Bounds b = chartWidget.context.getBounds();

        b.primaryDomainSpan = b.primaryDomainSpan.zoomOut();
        b.primaryRangeSpan = b.primaryRangeSpan.zoomOut();

        if( b.secondaryDomainSpan != null )
        {
            b.secondaryDomainSpan = b.secondaryDomainSpan.zoomOut();
        }

        if( b.secondaryRangeSpan != null )
        {
            b.secondaryRangeSpan = b.secondaryRangeSpan.zoomOut();
        }

        chartWidget.context.setBounds( b );

        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void restoreAndRepaintChart()
    {
        chartWidget.calculateBounds();
        repaintChart();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void repaintChart()
    {
        chartWidget.title.repaint();
        chartWidget.subtitle.repaint();
        chartWidget.topBottom.repaint();
        chartWidget.plot.seriesLayer.repaint = true;
        chartWidget.plot.highlightLayer.repaint = true;
        chartWidget.axes.axesLayer.repaint = true;
        mainPanel.repaint();
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
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        chart.title.text = title;
        chartWidget.title.repaint();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void saveAsImage( File file )
    {
        saveAsImage( file, new Dimension( 640, 480 ) );
    }

    /***************************************************************************
     * @param file
     * @param size
     **************************************************************************/
    public void saveAsImage( File file, Dimension size )
    {
        view.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

        int w = size.width;
        int h = size.height;

        BufferedImage image = Utils.createTransparentImage( w, h );
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC );
        g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON );
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY );
        g2d.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE );

        repaintChart();
        chartWidget.setTrackingVisible( false );
        chartWidget.draw( g2d, new Point(), size );
        chartWidget.setTrackingVisible( true );

        try
        {
            if( !ImageIO.write( image, "png", file ) )
            {
                throw new IllegalStateException(
                    "No writer found for PNG files!" );
            }
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( mainPanel,
                "I/O Error: " + ex.getMessage(), "I/O Error",
                JOptionPane.ERROR_MESSAGE );
        }

        view.setCursor( Cursor.getDefaultCursor() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartDropTarget implements
        ItemActionListener<IFileDropEvent>
    {
        private final ChartView view;

        public ChartDropTarget( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent fde = event.getItem();
            List<File> files = fde.getFiles();

            boolean addData = fde.getActionType() == DropActionType.COPY;

            for( int i = 0; i < files.size(); i++ )
            {
                view.importData( files.get( i ), addData );
                addData = true;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartMouseListenter extends MouseAdapter
    {
        private final ChartView view;

        public ChartMouseListenter( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void mouseDragged( MouseEvent e )
        {
            view.chartWidget.plot.selection.visible = true;
            view.chartWidget.plot.selection.end = e.getPoint();

            view.chartWidget.plot.highlightLayer.repaint = true;
            view.mainPanel.repaint();
        }

        public void mouseWheelMoved( MouseWheelEvent e )
        {
            if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
            {
                int zoomAmount = e.getWheelRotation();

                if( zoomAmount < 0 )
                {
                    view.zoomIn();
                }
                else
                {
                    view.zoomOut();
                }
            }
        }

        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() == 2 )
            {
                view.zoomRestore();
            }
            else if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                for( SeriesWidget sw : view.chartWidget.plot.serieses )
                {
                    sw.clearSelected();
                    view.chartWidget.plot.seriesLayer.repaint = true;
                    view.mainPanel.repaint();
                }
            }
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            view.mainPanel.requestFocus();

            view.chartWidget.plot.selection.start = e.getPoint();
        }

        @Override
        public void mouseReleased( MouseEvent evt )
        {
            if( !view.chartWidget.plot.selection.visible )
            {
                return;
            }

            view.chartWidget.plot.selection.visible = false;

            ChartContext context = view.chartWidget.context;

            Point s = view.chartWidget.plot.selection.start;
            Point e = evt.getPoint();

            if( s == null )
            {
                return;
            }

            s.x = Math.max( s.x, context.x );
            s.x = Math.min( s.x, context.x + context.width );
            s.y = Math.max( s.y, context.y );
            s.y = Math.min( s.y, context.y + context.height );

            e.x = Math.max( e.x, context.x );
            e.x = Math.min( e.x, context.x + context.width );
            e.y = Math.max( e.y, context.y );
            e.y = Math.min( e.y, context.y + context.height );

            s.x -= context.x;
            e.x -= context.x;

            s.y -= context.y;
            e.y -= context.y;

            int xmin = Math.min( s.x, e.x );
            int xmax = Math.max( s.x, e.x );

            int ymin = Math.min( s.y, e.y );
            int ymax = Math.max( s.y, e.y );

            double dmin;
            double dmax;

            Span pds;
            Span prs;
            Span sds = null;
            Span srs = null;

            dmin = context.domain.primary.fromScreen( xmin );
            dmax = context.domain.primary.fromScreen( xmax );
            pds = new Span( dmin, dmax );

            dmin = context.range.primary.fromScreen( ymax );
            dmax = context.range.primary.fromScreen( ymin );
            prs = new Span( dmin, dmax );
            // LogUtils.printDebug( "primary domain from " + ymin + " to " +
            // ymax );
            // LogUtils.printDebug( "primary domain from " + dmin + " to " +
            // dmax );

            if( context.domain.secondary != null )
            {
                dmin = context.domain.secondary.fromScreen( xmin );
                dmax = context.domain.secondary.fromScreen( xmax );
                sds = new Span( dmin, dmax );
            }

            if( context.range.secondary != null )
            {
                dmin = context.range.secondary.fromScreen( ymax );
                dmax = context.range.secondary.fromScreen( ymin );
                srs = new Span( dmin, dmax );
            }

            if( pds.range == 0.0 || prs.range == 0.0 ||
                ( sds != null && sds.range == 0.0 ) ||
                ( srs != null && srs.range == 0.0 ) )
            {
                view.chartWidget.plot.highlightLayer.repaint = true;
                view.mainPanel.repaint();
                return;
            }

            if( SwingUtilities.isLeftMouseButton( evt ) )
            {
                Bounds b = context.getBounds();

                b.primaryDomainSpan = pds;
                b.primaryRangeSpan = prs;
                b.secondaryDomainSpan = sds;
                b.secondaryRangeSpan = srs;

                view.chartWidget.setBounds( b );
            }
            else
            {
                Span ds;
                Span rs;

                for( SeriesWidget sw : view.chartWidget.plot.serieses )
                {
                    if( sw.series.isPrimaryDomain )
                    {
                        ds = pds;
                    }
                    else
                    {
                        ds = sds;
                    }

                    if( sw.series.isPrimaryRange )
                    {
                        rs = prs;
                    }
                    else
                    {
                        rs = srs;
                    }

                    sw.setSelected( ds, rs );
                }
            }

            view.chartWidget.plot.seriesLayer.repaint = true;
            view.chartWidget.plot.highlightLayer.repaint = true;
            view.mainPanel.repaint();
        }

        @Override
        public void mouseMoved( MouseEvent e )
        {
            Point p = new Point( e.getX() - view.chartWidget.context.x,
                e.getY() - 20 );
            XYPoint xy = new XYPoint();
            int idx;

            ChartContext context = view.chartWidget.context;

            // if( p.x < 0 || p.y < 0 || p.x > context.width ||
            // p.y > context.height )
            // {
            // for( SeriesWidget s : view.chartWidget.plot.serieses )
            // {
            // s.highlight.setLocation( new Point( -5, -5 ) );
            // }
            // }

            // LogUtils.printDebug( "hover: " + mx );

            int seriesIdx = 0;
            for( SeriesWidget s : view.chartWidget.plot.serieses )
            {
                Point sp = new Point( p );
                IDimensionCoords domainCoords;
                IDimensionCoords rangeCoords;

                if( !s.series.visible )
                {
                    continue;
                }

                if( s.series.isPrimaryDomain )
                {
                    domainCoords = context.domain.primary;
                }
                else
                {
                    domainCoords = context.domain.secondary;
                }

                if( domainCoords != null )
                {
                    xy.x = domainCoords.fromScreen( sp.x );

                    idx = ChartUtils.findNearest( s.series.data, xy.x );

                    if( idx > -1 )
                    {
                        if( s.series.isPrimaryRange )
                        {
                            rangeCoords = context.range.primary;
                        }
                        else
                        {
                            rangeCoords = context.range.secondary;
                        }

                        if( domainCoords == null || rangeCoords == null )
                        {
                            continue;
                        }

                        xy = new XYPoint( s.series.data.get( idx ) );
                        sp.x = domainCoords.fromCoord( xy.x );
                        sp.y = rangeCoords.fromCoord( xy.y );

                        // LogUtils.printDebug( "hover [" + s.series.name +
                        // "]: " +
                        // p.x + xy.x );

                        s.highlight.setLocation( new Point( sp ) );

                        view.propertiesView.setSelected( seriesIdx, idx );
                    }
                }
                seriesIdx++;
            }

            view.chartWidget.plot.highlightLayer.repaint = true;
            view.mainPanel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements IFileSelectionListener
    {
        private final ChartView view;

        public OpenListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            return view.userio.getOptions().recentFiles.first();
        }

        @Override
        public void filesChosen( File [] files )
        {
            List<File> fileList = Arrays.asList( files );

            view.importData( fileList );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener implements ActionListener,
        ItemActionListener<Boolean>
    {
        private final ChartView view;

        private SaveView saveView;

        public SaveListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveView = new SaveView();
            SaveOptions options = new SaveOptions();
            OkDialogView okView = new OkDialogView( view.getView(),
                saveView.getView(), OkDialogButtons.OK_CANCEL );
            JDialog dialog = okView.getView();

            options.file = getDefaultFile();
            options.size.width = view.mainPanel.getWidth();
            options.size.height = view.mainPanel.getHeight();

            saveView.setData( options );

            okView.addOkListener( this );

            dialog.setTitle( "Save Chart" );
            dialog.pack();
            dialog.setLocationRelativeTo( view.getView() );
            dialog.setVisible( true );
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            if( !event.getItem() )
            {
                return;
            }

            SaveOptions options = saveView.getData();

            view.userio.getOptions().lastImageFile = options.file;
            view.userio.write();

            view.saveAsImage( options.file, options.size );
        }

        public File getDefaultFile()
        {
            File f = view.userio.getOptions().lastImageFile;

            if( f == null )
            {
                f = IOUtils.replaceExtension(
                    view.userio.getOptions().recentFiles.first(), "png" );
            }

            return f;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartComponentListener extends ComponentAdapter
    {
        private final ChartView view;

        public ChartComponentListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void componentResized( ComponentEvent e )
        {
            view.chartWidget.axes.axesLayer.repaint = true;
            view.chartWidget.plot.seriesLayer.repaint = true;
            view.chartWidget.plot.highlightLayer.clear();
            view.chartWidget.plot.highlightLayer.repaint = false;
            view.mainPanel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DeletePointListener implements ActionListener
    {
        private final ChartView view;

        public DeletePointListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // System.out.println( "Deleting points..." );

            for( SeriesWidget series : view.chartWidget.plot.serieses )
            {
                for( IDataPoint xy : series.series.data )
                {
                    if( xy.isSelected() )
                    {
                        xy.setHidden( true );
                        xy.setSelected( false );
                    }
                }
            }

            ChartContext context = view.chartWidget.context;

            if( context.isAutoBounds() )
            {
                context.setAutoBounds( view.chart );
            }
            else
            {
                context.calculateAutoBounds( view.chart );
            }

            view.chartWidget.plot.seriesLayer.repaint = true;
            view.chartWidget.axes.axesLayer.repaint = true;
            view.mainPanel.repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class SeriesChangedEvent
    {
        public final Series s;
        public final int index;
        public final boolean added;

        public SeriesChangedEvent( Series s, int index, boolean added )
        {
            this.s = s;
            this.index = index;
            this.added = added;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PropertiesDialogListener implements ActionListener,
        ItemActionListener<Boolean>
    {
        private final ChartView view;

        private OkDialogView okView;

        public PropertiesDialogListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( okView == null )
            {
                createDialog();
            }

            JDialog dialog = okView.getView();

            if( !dialog.isVisible() )
            {
                dialog.setVisible( true );
            }
        }

        private void createDialog()
        {
            this.okView = new OkDialogView( view.getView(),
                view.propertiesView.getView(), ModalityType.MODELESS,
                OkDialogButtons.OK_APPLY );

            JDialog d = okView.getView();

            okView.addOkListener( this );

            d.setTitle( "Series Properties" );
            d.setSize( 650, 400 );
            d.validate();
            d.setLocationRelativeTo( view.getView() );
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            if( event.getItem() )
            {
                view.restoreAndRepaintChart();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomInListener implements ActionListener
    {
        private final ChartView view;

        public ZoomInListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.zoomIn();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ZoomOutListener implements ActionListener
    {
        private final ChartView view;

        public ZoomOutListener( ChartView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.zoomOut();
        }
    }
}
