package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.fields.HexLongFormField;
import org.jutils.ui.fields.LongFormField;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PositionIndicatorTestMain
{
    /***************************************************************************
     * @param args ignored
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
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView view = new StandardFrameView();
            PositionIndicatorTestView testView = new PositionIndicatorTestView();
            ActionListener listener = ( e ) -> testView.addBookmark();

            view.setTitle( "Position Test Frame" );
            view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            view.setSize( 700, 500 );
            view.setContent( testView.getView() );
            view.setToolbar( createToolbar( createAddAction( listener ) ) );

            return view.getView();
        }

        private JToolBar createToolbar( Action addAction )
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar, addAction );

            return toolbar;
        }

        private Action createAddAction( ActionListener listener )
        {
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            // TODO Auto-generated method stub
            return new ActionAdapter( listener, "Add Bookmark", icon );
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
    private static final class PositionIndicatorTestView
        implements IView<JComponent>
    {
        /**  */
        private final JPanel view;
        /**  */
        private final LongFormField lengthField;
        /**  */
        private final LongFormField unitLengthField;
        /**  */
        private final HexLongFormField offsetField;
        /**  */
        private final PositionIndicator indicator;

        /**  */
        private static final long LEN = 865716124;
        /**  */
        private static final long SIZE = 4 * 1024 * 1024;

        /**
         * 
         */
        public PositionIndicatorTestView()
        {
            this.lengthField = new LongFormField( "Length" );
            this.unitLengthField = new LongFormField( "Unit Length" );
            this.offsetField = new HexLongFormField( "Offset" );
            this.indicator = new PositionIndicator(
                ( d ) -> Long.toString( d ) );
            this.view = createView();

            indicator.setLength( LEN );
            indicator.setUnitLength( SIZE );
            indicator.setPosition( 0L );
            // indicator.getView().setBorder( new EtchedBorder() );
            indicator.getView().setBorder( new LineBorder( Color.gray ) );

            lengthField.setValue( indicator.getLength() );
            unitLengthField.setValue( indicator.getUnitLength() );
            offsetField.setValue( indicator.getPosition() );

            indicator.addPositionListener(
                ( e ) -> updatePosition( e.getItem() ) );

            lengthField.setUpdater( ( n ) -> indicator.setLength( n ) );
            unitLengthField.setUpdater( ( n ) -> indicator.setUnitLength( n ) );
            offsetField.setUpdater( ( n ) -> indicator.setPosition( n ) );
        }

        public void addBookmark()
        {
            indicator.addBookmark( indicator.getPosition() );
        }

        /**
         * @param position
         */
        private void updatePosition( long position )
        {
            offsetField.setValue( position );
            indicator.setPosition( position );

            LogUtils.printDebug( "new position: %d", position );
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            BorderLayout layout = new BorderLayout();
            JPanel panel = new JPanel( layout );

            layout.setHgap( 4 );

            panel.add( createForm(), BorderLayout.CENTER );
            panel.add( indicator.getView(), BorderLayout.SOUTH );

            return panel;
        }

        /**
         * @return
         */
        private Component createForm()
        {
            StandardFormView form = new StandardFormView();

            form.addField( lengthField );
            form.addField( unitLengthField );
            form.addField( offsetField );

            return form.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }
    }
}
