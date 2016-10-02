package org.jutils.ui.model;

import java.awt.Component;

import javax.swing.*;

/*******************************************************************************
 * A {@link ListCellRenderer} that uses the provided decorator to render cells
 * as a {@link JLabel}.
 ******************************************************************************/
public class LabelListCellRenderer extends DefaultListCellRenderer
{
    /** The decorator to be used to render cells. */
    private final IListCellLabelDecorator decorator;

    /***************************************************************************
     * @param decorator the decorator used to render cells.
     **************************************************************************/
    public LabelListCellRenderer( IListCellLabelDecorator decorator )
    {
        this.decorator = decorator;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getListCellRendererComponent( JList<?> list, Object value,
        int index, boolean isSelected, boolean cellHasFocus )
    {
        super.getListCellRendererComponent( list, value, index, isSelected,
            cellHasFocus );

        decorator.decorate( this, list, value, index, isSelected,
            cellHasFocus );

        return this;
    }

    /***************************************************************************
     * A decorator used to render a cell of a {@link JList} as a label.
     **************************************************************************/
    public static interface IListCellLabelDecorator
    {
        /***********************************************************************
         * Decorates the provided label with the value of the list.
         * @param label the label to be decorated.
         * @param list the list in which the label is rendered.
         * @param value the value in the list that used to decorate the label.
         * @param index the index to be rendered.
         * @param isSelected {@code true} if the cell is selected.
         * @param cellHasFocus {@code true} if the cell has focus.
         **********************************************************************/
        public void decorate( JLabel label, JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus );
    }
}
