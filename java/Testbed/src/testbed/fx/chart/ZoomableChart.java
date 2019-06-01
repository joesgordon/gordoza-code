package testbed.fx.chart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 */
public class ZoomableChart
{
    /**  */
    private final StackPane chartContainer;
    /**  */
    private final XYChart<Number, Number> chart;
    /**  */
    private final ValueAxis<Number> xAxis;
    /**  */
    private final ValueAxis<Number> yAxis;
    /**  */
    private final Rectangle zoomRect;

    /**  */
    private double xMin;
    /**  */
    private double xMax;
    /**  */
    private double yMin;
    /**  */
    private double yMax;

    /**
     * @param chart the chart to be zoomed.
     */
    public ZoomableChart( XYChart<Number, Number> chart )
    {
        this.chart = chart;
        this.xAxis = ( ValueAxis<Number> )chart.getXAxis();
        this.yAxis = ( ValueAxis<Number> )chart.getYAxis();

        this.chartContainer = new StackPane( chart );

        this.xMin = 0;
        this.xMax = 100;
        this.yMin = 0;
        this.yMax = 100;

        initAxis( xAxis, xMin, xMax );
        initAxis( yAxis, yMin, yMax );

        this.zoomRect = new Rectangle();

        chart.setAnimated( false );

        zoomRect.setManaged( false );
        zoomRect.setFill( Color.LIGHTSEAGREEN.deriveColor( 0, 1, 1, 0.5 ) );

        chartContainer.getChildren().add( zoomRect );

        setUpZooming( zoomRect );
    }

    /**
     * 
     */
    public void resetZoom()
    {
        xAxis.setLowerBound( xMin );
        xAxis.setUpperBound( xMax );

        yAxis.setLowerBound( yMin );
        yAxis.setUpperBound( yMax );

        zoomRect.setWidth( 0 );
        zoomRect.setHeight( 0 );

        xAxis.setAutoRanging( true );
        yAxis.setAutoRanging( true );

        chart.layout();

        xMin = xAxis.getLowerBound();
        xMax = xAxis.getUpperBound();

        yMin = yAxis.getLowerBound();
        yMax = yAxis.getUpperBound();

        xAxis.setAutoRanging( false );
        yAxis.setAutoRanging( false );
    }

    /**
     * @param axis
     * @param min
     * @param max
     * @return the newly created axis.
     */
    private static void initAxis( ValueAxis<Number> axis, double min,
        double max )
    {
        axis.setAutoRanging( false );
        axis.setLowerBound( min );
        axis.setUpperBound( max );
    }

    /**
     * @param zoomRect the rectangle to store the zoom limits
     */
    private void setUpZooming( final Rectangle zoomRect )
    {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();

        chart.setOnMouseClicked( new EventHandler<MouseEvent>()
        {
            @Override
            public void handle( MouseEvent event )
            {
                if( event.getClickCount() == 2 &&
                    event.getButton() == MouseButton.PRIMARY )
                {
                    resetZoom();
                }
            }
        } );

        chart.setOnMousePressed( new EventHandler<MouseEvent>()
        {
            @Override
            public void handle( MouseEvent event )
            {
                if( event.isPrimaryButtonDown() )
                {
                    mouseAnchor.set(
                        new Point2D( event.getX(), event.getY() ) );
                    zoomRect.setWidth( 0 );
                    zoomRect.setHeight( 0 );
                }
            }
        } );

        chart.setOnMouseDragged( new EventHandler<MouseEvent>()
        {
            @Override
            public void handle( MouseEvent event )
            {
                if( event.isPrimaryButtonDown() )
                {
                    double x = event.getX();
                    double y = event.getY();

                    zoomRect.setX( Math.min( x, mouseAnchor.get().getX() ) );
                    zoomRect.setY( Math.min( y, mouseAnchor.get().getY() ) );
                    zoomRect.setWidth(
                        Math.abs( x - mouseAnchor.get().getX() ) );
                    zoomRect.setHeight(
                        Math.abs( y - mouseAnchor.get().getY() ) );
                }
            }
        } );

        chart.setOnMouseReleased( new EventHandler<MouseEvent>()
        {
            @Override
            public void handle( MouseEvent event )
            {
                if( zoomRect.getWidth() > 0 && zoomRect.getHeight() > 0 )
                {
                    doZoom( zoomRect );
                }
            }
        } );

        chart.setOnScroll( new EventHandler<ScrollEvent>()
        {
            @Override
            public void handle( ScrollEvent event )
            {
                double direction = event.getDeltaY();

                scrollAxis( xAxis, direction, xMin, xMax );
                scrollAxis( yAxis, direction, yMin, yMax );
            }
        } );
    }

    /**
     * @param axis the axis to be scrolled.
     * @param direction the direction to be scrolled
     * @param vMin
     * @param vMax
     */
    private static void scrollAxis( ValueAxis<Number> axis, double direction,
        double vMin, double vMax )
    {
        double min = axis.getLowerBound();
        double max = axis.getUpperBound();
        double range = max - min;

        if( direction > 0 )
        {
            double maxTest = Math.nextUp( Math.nextUp( Math.nextUp(
                Math.nextUp( Math.nextUp( Math.nextUp( min ) ) ) ) ) );

            if( max > maxTest && range > 0.0 )
            {
                double r = range / 3.0;

                min += r;
                max -= r;
            }
        }
        else
        {
            double rangeTest = range * 3.0;

            if( !Double.isNaN( rangeTest ) && !Double.isInfinite( rangeTest ) )
            {
                min -= range;
                max += range;
            }
        }

        min = Math.max( vMin, min );
        max = Math.min( vMax, max );

        axis.setLowerBound( min );
        axis.setUpperBound( max );
    }

    /**
     * @param zoomRect the rectangle to store the zoom limits
     */
    private void doZoom( Rectangle zoomRect )
    {
        Point2D zoomScene = chart.localToScene(
            new Point2D( zoomRect.getX(), zoomRect.getY() ) );

        Point2D zoomTopLeft = new Point2D( zoomScene.getX(), zoomScene.getY() );
        Point2D zoomBottomRight = new Point2D(
            zoomScene.getX() + zoomRect.getWidth(),
            zoomScene.getY() + zoomRect.getHeight() );

        Point2D yAxisInScene = yAxis.localToScene( 0, 0 );
        Point2D xAxisInScene = xAxis.localToScene( 0, 0 );

        double xOffset = zoomTopLeft.getX() - yAxisInScene.getX() -
            yAxis.getWidth();
        double yOffset = xAxisInScene.getY() - zoomBottomRight.getY();

        // double xAxisScale = xAxis.getScale();
        // double yAxisScale = yAxis.getScale();
        //
        // double xLower = xAxis.getLowerBound() + xOffset / xAxisScale;
        // double xUpper = xAxis.getLowerBound() +
        // zoomRect.getWidth() / xAxisScale;
        //
        // double yLower = yAxis.getLowerBound() + yOffset / yAxisScale;
        // double yUpper = yAxis.getLowerBound() -
        // zoomRect.getHeight() / yAxisScale;

        double xLower = xAxis.getValueForDisplay( xOffset ).doubleValue();
        double xUpper = xAxis.getValueForDisplay(
            xOffset + zoomRect.getWidth() ).doubleValue();

        double yDist = yOffset;

        yOffset = yAxis.getHeight() - yOffset;

        System.out.println(
            String.format( "y dist to bottom: %f, off: %f, h: %f", yDist,
                yOffset, yAxis.getHeight() ) );

        double yLower = yAxis.getValueForDisplay( yOffset ).doubleValue();
        double yUpper = yAxis.getValueForDisplay(
            yOffset - zoomRect.getHeight() ).doubleValue();

        xAxis.setLowerBound( xLower );
        xAxis.setUpperBound( xUpper );

        yAxis.setLowerBound( yLower );
        yAxis.setUpperBound( yUpper );

        zoomRect.setWidth( 0 );
        zoomRect.setHeight( 0 );
    }

    /**
     * @return the base node of the chart.
     */
    public Node getNode()
    {
        return chartContainer;
    }
}
