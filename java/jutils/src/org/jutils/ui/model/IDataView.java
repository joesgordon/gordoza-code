package org.jutils.ui.model;

public interface IDataView<T> extends IComponentView
{
    public T getData();

    public void setData( T data );
}
