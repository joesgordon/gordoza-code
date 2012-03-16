package org.jutils.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class CardPanel extends JPanel
{
    private Map<Component, String> stringMap;
    private CardLayout layout;

    public CardPanel()
    {
        layout = new CardLayout();
        stringMap = new HashMap<Component, String>();

        setLayout( layout );
    }

    public void addCard( Component comp )
    {
        String str = Integer.toString( getComponentCount() );

        stringMap.put( comp, str );

        super.add( comp, str );
    }

    public void showCard( Component comp )
    {
        String str = stringMap.get( comp );

        if( str == null )
        {
            throw new IllegalArgumentException( "Card not found!" );
        }

        layout.show( this, str );
    }

    public void next()
    {
        layout.next( this );
    }

    public void last()
    {
        layout.last( this );
    }

    @Override
    public Component add( Component comp )
    {
        throw new RuntimeException();
    }

    @Override
    public Component add( Component comp, int i )
    {
        throw new RuntimeException();
    }

    @Override
    public Component add( String name, Component comp )
    {
        throw new RuntimeException();
    }

    @Override
    public void add( Component comp, Object constraints )
    {
        throw new RuntimeException();
    }

    @Override
    public void add( Component comp, Object constraints, int index )
    {
        throw new RuntimeException();
    }
}
