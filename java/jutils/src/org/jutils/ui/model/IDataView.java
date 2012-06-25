package org.jutils.ui.model;

public interface IDataView<T> extends IJcompView
{
    public T getData();

    public void setData( T data );
}
