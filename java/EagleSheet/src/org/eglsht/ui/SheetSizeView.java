package org.eglsht.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.eglsht.data.SheetSize;
import org.jutils.core.ui.SpinnerWheelListener;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SheetSizeView implements IDataView<SheetSize>
{
    private final JPanel view;
    private final JSpinner rowField;
    private final JSpinner colField;

    private SheetSize size;

    /***************************************************************************
     * 
     **************************************************************************/
    public SheetSizeView()
    {
        rowField = new JSpinner(
            new IntegerSpinnerModel( new RowUpdater( this ) ) );

        colField = new JSpinner(
            new IntegerSpinnerModel( new ColumnUpdater( this ) ) );

        view = createView();

        rowField.setEditor( new JSpinner.NumberEditor( rowField ) );
        rowField.addMouseWheelListener( new SpinnerWheelListener( rowField ) );
        colField.addMouseWheelListener( new SpinnerWheelListener( colField ) );

        setData( new SheetSize() );
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 10, 5, 10 ), 0, 0 );
        panel.add( new JLabel( "Columns:" ), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 10, 10, 10 ), 0, 0 );
        panel.add( colField, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 10, 10, 5, 10 ), 0, 0 );
        panel.add( new JLabel( "Rows:" ), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 10, 0, 10 ), 0, 0 );
        panel.add( rowField, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public SheetSize getData()
    {
        return size;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( SheetSize size )
    {
        this.size = size;

        rowField.setValue( size.rows );
        colField.setValue( size.cols );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class IntegerSpinnerModel extends SpinnerNumberModel
    {
        private static final long serialVersionUID = 1L;
        private final IUpdater<Integer> updater;

        public IntegerSpinnerModel( IUpdater<Integer> updater )
        {
            super( 0, 0, Integer.MAX_VALUE, 1 );

            this.updater = updater;
        }

        @Override
        public void setValue( Object value )
        {
            super.setValue( value );

            updater.update( ( Integer )value );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static interface IUpdater<T>
    {
        public void update( T item );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class RowUpdater implements IUpdater<Integer>
    {
        private final SheetSizeView view;

        public RowUpdater( SheetSizeView view )
        {
            this.view = view;
        }

        @Override
        public void update( Integer item )
        {
            view.size.rows = item;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class ColumnUpdater implements IUpdater<Integer>
    {
        private final SheetSizeView view;

        public ColumnUpdater( SheetSizeView view )
        {
            this.view = view;
        }

        @Override
        public void update( Integer item )
        {
            view.size.cols = item;
        }
    }
}
