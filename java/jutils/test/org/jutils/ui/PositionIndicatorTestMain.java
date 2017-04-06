package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;

import org.jutils.io.LogUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.fields.HexLongFormField;
import org.jutils.ui.fields.LongFormField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PositionIndicatorTestMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new PositionIndicatorApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PositionIndicatorApp implements IFrameApp
    {
        @Override
        public void finalizeGui()
        {
        }

        @Override
        public JFrame createFrame()
        {
            StandardFrameView view = new StandardFrameView();
            PositionIndicatorTestView testView = new PositionIndicatorTestView();

            view.setTitle( "Position Test Frame" );
            view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            view.setSize( 700, 500 );
            view.setContent( testView.getView() );

            return view.getView();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PositionIndicatorTestView
        implements IView<JComponent>
    {
        private final JPanel view;
        private final LongFormField lengthField;
        private final LongFormField unitLengthField;
        private final HexLongFormField offsetField;
        private final PositionIndicator indicator;

        private static final long LEN = 865716124;
        private static final long SIZE = 4 * 1024 * 1024;

        public PositionIndicatorTestView()
        {
            this.lengthField = new LongFormField( "Length" );
            this.unitLengthField = new LongFormField( "Unit Length" );
            this.offsetField = new HexLongFormField( "Offset" );
            this.indicator = new PositionIndicator();
            this.view = createView();

            indicator.setLength( LEN );
            indicator.setUnitLength( SIZE );
            indicator.setOffset( 0L );

            lengthField.setValue( indicator.getLength() );
            unitLengthField.setValue( indicator.getUnitLength() );
            offsetField.setValue( indicator.getOffset() );

            indicator.addPositionListener(
                ( e ) -> updatePosition( e.getItem() ) );

            lengthField.setUpdater( ( n ) -> indicator.setLength( n ) );
            unitLengthField.setUpdater( ( n ) -> indicator.setUnitLength( n ) );
            offsetField.setUpdater( ( n ) -> indicator.setOffset( n ) );
        }

        private void updatePosition( long position )
        {
            offsetField.setValue( position );
            indicator.setOffset( position );

            LogUtils.printDebug( "new position: %d", position );
        }

        private JPanel createView()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createForm(), BorderLayout.CENTER );
            panel.add( indicator.getView(), BorderLayout.SOUTH );

            return panel;
        }

        private Component createForm()
        {
            StandardFormView form = new StandardFormView();

            form.addField( lengthField );
            form.addField( unitLengthField );
            form.addField( offsetField );

            return form.getView();
        }

        @Override
        public JComponent getView()
        {
            return view;
        }
    }
}
