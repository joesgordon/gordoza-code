package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;

import org.jutils.data.UIProperty;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.fields.IDescriptor;
import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class SplitButtonView<T> implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JButton button;
    /**  */
    private final JButton arrowButton;
    /**  */
    private final ListPopup<T> popup;
    /**  */
    private final UpdaterList<T> selectedListeners;

    /***************************************************************************
     * @param text
     * @param icon
     * @param items
     * @param descriptor
     **************************************************************************/
    public SplitButtonView( String text, Icon icon, List<T> items,
        IDescriptor<T> descriptor )
    {
        this.button = new JButton( text, icon );
        this.arrowButton = new JButton( new ArrowIcon() );
        this.popup = new ListPopup<>( items );
        this.view = createView();
        this.selectedListeners = new UpdaterList<>();

        arrowButton.addActionListener( ( e ) -> togglePopup() );
        // arrowButton.addMouseListener( new ArrowButtonMouseListener( this ) );

        popup.addItemSelectedListener(
            ( e ) -> fireItemSelected( e.getItem() ) );

        setItems( items );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void togglePopup()
    {
        popup.show( arrowButton );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    private void setItems( List<T> items )
    {
        popup.setItems( items );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    private void fireItemSelected( T item )
    {
        popup.hide();

        // LogUtils.printDebug( "%s selected", item );
        selectedListeners.fireListeners( item );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        Dimension dim;

        // comboField.setPreferredSize( new Dimension( 15, dim.height ) );
        // comboField.setMaximumSize( comboField.getPreferredSize() );

        dim = button.getPreferredSize();

        arrowButton.setPreferredSize( new Dimension( 15, dim.height ) );
        arrowButton.setMinimumSize( arrowButton.getPreferredSize() );
        arrowButton.setMaximumSize( arrowButton.getPreferredSize() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( button, constraints );

        // constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
        // GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( comboField, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( arrowButton, constraints );

        panel.setMaximumSize( panel.getPreferredSize() );

        return panel;
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
     * @param toolbar
     **************************************************************************/
    public void install( JToolBar toolbar )
    {
        toolbar.add( button );
        // toolbar.add( comboField );
        toolbar.add( arrowButton );

        button.setFocusable( false );
        arrowButton.setFocusable( false );
    }

    public void addItemSelected( IUpdater<T> selectedListener )
    {
        selectedListeners.addUpdater( selectedListener );
    }

    public void addButtonListener( ActionListener listener )
    {
        button.addActionListener( listener );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ArrowIcon implements Icon
    {
        private final Color shadow;
        private final Color highlight;
        private final Color darkShadow;

        private static final int SIZE = 5;

        public ArrowIcon()
        {
            this.shadow = UIManager.getColor( "controlShadow" );
            this.darkShadow = Color.black;
            // this.darkShadow = UIManager.getColor( "controlDkShadow" );
            this.highlight = UIManager.getColor( "controlLtHighlight" );
        }

        @Override
        public void paintIcon( Component c, Graphics g, int x, int y )
        {
            int x1 = 1 + c.getWidth() / 2 - SIZE / 2;
            int y1 = c.getHeight() / 2 - SIZE / 2;

            paintTriangle( g, x1, y1, SIZE, true );
        }

        public void paintTriangle( Graphics g, int x, int y, int size,
            boolean isEnabled )
        {
            Color oldColor = g.getColor();
            int mid, i, j;

            j = 0;
            size = Math.max( size, 2 );
            mid = ( size / 2 ) - 1;

            g.translate( x, y );
            if( isEnabled )
            {
                g.setColor( darkShadow );
            }
            else
            {
                g.setColor( shadow );
            }

            if( !isEnabled )
            {
                g.translate( 1, 1 );
                g.setColor( highlight );
                for( i = size - 1; i >= 0; i-- )
                {
                    g.drawLine( mid - i, j, mid + i, j );
                    j++;
                }
                g.translate( -1, -1 );
                g.setColor( shadow );
            }

            j = 0;
            for( i = size - 1; i >= 0; i-- )
            {
                g.drawLine( mid - i, j, mid + i, j );
                j++;
            }

            g.translate( -x, -y );
            g.setColor( oldColor );
        }

        @Override
        public int getIconWidth()
        {
            return SIZE;
        }

        @Override
        public int getIconHeight()
        {
            return SIZE;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ListPopup<T>
    {
        private final JPopupMenu popup;
        private final CollectionListModel<T> model;
        private final JList<T> list;
        private final ItemActionList<T> selectedListeners;

        public ListPopup( List<T> items )
        {
            this.popup = new JPopupMenu();
            this.model = new CollectionListModel<>();
            this.list = new JList<>( model );
            this.selectedListeners = new ItemActionList<>();

            model.setData( items );

            list.setBackground(
                UIProperty.TEXTFIELD_INACTIVEBACKGROUND.getColor() );
            list.setVisibleRowCount( 10 );
            list.addListSelectionListener( ( e ) -> handleItemSelected( e ) );

            JScrollPane pane = new JScrollPane( list );

            pane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
            pane.getVerticalScrollBar().setUnitIncrement( 12 );
            pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

            popup.setBorder( new LineBorder( Color.black ) );
            popup.add( pane );

            Dimension dim = pane.getPreferredSize();

            dim.width += 10;

            popup.setPreferredSize( dim );
        }

        private void handleItemSelected( ListSelectionEvent e )
        {
            if( !e.getValueIsAdjusting() )
            {
                T item = list.getSelectedValue();
                selectedListeners.fireListeners( this, item );
            }
        }

        public void hide()
        {
            popup.setVisible( false );
        }

        public void addItemSelectedListener( ItemActionListener<T> l )
        {
            selectedListeners.addListener( l );
        }

        public void setItems( List<T> items )
        {
            model.setData( items );
        }

        public void show( JComponent comp )
        {
            if( model.getSize() > 0 )
            {
                list.setSelectedValue( model.get( 0 ), true );
            }
            list.clearSelection();
            popup.show( comp, 0, comp.getHeight() );
            list.requestFocus();
        }
    }
}
