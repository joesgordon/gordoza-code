package testbed.fx.chart;

import java.util.Collections;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*******************************************************************************
 *
 ******************************************************************************/
public class ZoomableLineChartMain extends Application
{
    /**  */
    private static final int NUM_DATA_POINTS = 1000;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void start( Stage primaryStage )
    {
        final ZoomableChart chart = createChart();

        final HBox controls = new HBox( 10 );
        controls.setPadding( new Insets( 10 ) );
        controls.setAlignment( Pos.CENTER );

        final Button resetButton = new Button( "Reset" );

        resetButton.setOnAction( new EventHandler<ActionEvent>()
        {
            @Override
            public void handle( ActionEvent event )
            {
                chart.resetZoom();
            }
        } );

        controls.getChildren().addAll( resetButton );

        final BorderPane root = new BorderPane();
        root.setCenter( chart.getNode() );
        root.setBottom( controls );

        final Scene scene = new Scene( root, 600, 400 );
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    /***************************************************************************
     * @return the newly created chart.
     **************************************************************************/
    private static ZoomableChart createChart()
    {
        final LineChart<Number, Number> lchart = new LineChart<>(
            new NumberAxis(), new NumberAxis() );

        final ZoomableChart chart = new ZoomableChart( lchart );
        lchart.setData( generateChartData() );
        return chart;
    }

    /***************************************************************************
     * @return some data to fill the chart.
     **************************************************************************/
    private static ObservableList<Series<Number, Number>> generateChartData()
    {
        final Series<Number, Number> series = new Series<>();
        series.setName( "Data" );
        final Random rng = new Random();
        for( int i = 0; i < NUM_DATA_POINTS; i++ )
        {
            Data<Number, Number> dataPoint = new Data<Number, Number>( i,
                rng.nextInt( 100 ) );
            series.getData().add( dataPoint );
        }
        return FXCollections.observableArrayList(
            Collections.singleton( series ) );
    }

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String[] args )
    {
        launch( args );
    }
}
