package org.cc.edit.ui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class InfoPanel<T> extends JPanel
{
    private T data;

    public InfoPanel()
    {
        super();
    }

    public InfoPanel( LayoutManager l )
    {
        super( l );
    }

    public void setData( T data )
    {
        this.data = data;
        displayData( data );
    }

    public T getData()
    {
        return data;
    }

    protected abstract void displayData( T data );
}
