package org.jutils.ui.model;

/*******************************************************************************
 * Defines a view that allows for display and optionally editing of data.
 * @param <T> the type of the data to be displayed/edited.
 ******************************************************************************/
public interface IDataView<T> extends IComponentView
{
    /***************************************************************************
     * Returns the data as seen in the view. This function may optionally build
     * this data when called, or return the original data instance (meaning the
     * data was updated without creating a new instance).
     * @return the data as seen in the view.
     **************************************************************************/
    public T getData();

    /***************************************************************************
     * Sets the components in this view to the fields within the provided data.
     * @param data the data to be displayed.
     **************************************************************************/
    public void setData( T data );
}
