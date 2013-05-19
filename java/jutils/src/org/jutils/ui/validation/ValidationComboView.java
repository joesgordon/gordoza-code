package org.jutils.ui.validation;

import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * Wrapper class that puts a {@link ValidationComboField} inside a
 * {@link ValidationView}.
 * @param <T> the types of items to be added to the combo box.
 ******************************************************************************/
public class ValidationComboView<T> implements IView<JPanel>
{
    /**  */
    private final ValidationComboField<T> field;
    /**  */
    private final ValidationView view;

    /***************************************************************************
     * 
     **************************************************************************/
    // public ValidationComboView()
    // {
    // this( null );
    // }

    /***************************************************************************
     * @param units
     **************************************************************************/
    public ValidationComboView( T[] items )
    {
        this( Arrays.asList( items ) );
    }

    /***************************************************************************
     * @param units
     * @param columns
     **************************************************************************/
    public ValidationComboView( List<T> items )
    {
        this.field = new ValidationComboField<T>( items );
        this.view = new ValidationView( field );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setSelectedItem( T item )
    {
        field.setSelectedItem( item );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public T getSelectedItem()
    {
        return field.getSelectedItem();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ValidationComboField<T> getField()
    {
        return field;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view.getView();
    }
}
