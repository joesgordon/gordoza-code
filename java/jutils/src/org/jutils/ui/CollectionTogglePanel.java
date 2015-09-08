package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jutils.io.LogUtils;

import com.jgoodies.looks.Options;

/*******************************************************************************
 * This is a panel that will display a list of toggle or radio buttons to the
 * user that come from a selection of type T
 * @param <T> The type of data to be displayed.
 ******************************************************************************/
public class CollectionTogglePanel<T> extends JPanel
{
    /** Map of types to their corresponding buttons. */
    private HashMap<T, UserValueStorage<T>> buttonMap;

    /** List of values that are displayed. */
    private List<T> values;

    /** The group preventing more than one choice at a time. */
    private ButtonGroup group;

    /** Indicates whether to use toggle or radio buttons. */
    private boolean useToggles = true;

    /***************************************************************************
     * Creates an empty panel.
     **************************************************************************/
    public CollectionTogglePanel()
    {
        this( new ArrayList<T>() );
    }

    /***************************************************************************
     * Creates a panel which displays the list of values as choices to the user.
     * @param vals The choices from which the user may choose only one.
     **************************************************************************/
    public CollectionTogglePanel( List<T> vals )
    {
        group = new ButtonGroup();
        buttonMap = new HashMap<T, UserValueStorage<T>>();
        setValues( vals );
    }

    /***************************************************************************
     * Add the action listener to all the generated buttons.
     * @param l The listener to be added.
     **************************************************************************/
    public void addActionListener( ActionListener l )
    {
        Collection<UserValueStorage<T>> buttons = buttonMap.values();

        for( UserValueStorage<T> button : buttons )
        {
            button.addActionListener( l );
        }
    }

    /***************************************************************************
     * Removes the action listener from all the generated buttons.
     * @param l The listener to be removed.
     **************************************************************************/
    public void removeActionListener( ActionListener l )
    {
        Collection<UserValueStorage<T>> buttons = buttonMap.values();

        for( UserValueStorage<T> button : buttons )
        {
            button.removeActionListener( l );
        }
    }

    /***************************************************************************
     * Sets the toggle property. If <code>false</code> then radio buttons will
     * be generated instead of toggle buttons. If the value passed is different
     * than the current value, the buttons will be regenerated.
     * @param toggle
     **************************************************************************/
    public void setToggles( boolean toggle )
    {
        boolean changed = !( toggle ^ useToggles );

        useToggles = toggle;
        if( changed )
        {
            createGui();
        }
    }

    /***************************************************************************
     * Sets the choices to be displayed.
     * @param vals The choices to be displayed.
     **************************************************************************/
    public void setValues( List<T> vals )
    {
        values = new ArrayList<T>( vals );
        createGui();
    }

    /***************************************************************************
     * Sets the item that is to appear chosen.
     * @param value The choice.
     **************************************************************************/
    public void setValue( T value )
    {
        buttonMap.get( value ).setSelected( true );
    }

    /***************************************************************************
     * Returns the chosen item.
     * @return The chosen item.
     **************************************************************************/
    public T getValue()
    {
        Collection<UserValueStorage<T>> buttons = buttonMap.values();

        for( UserValueStorage<T> button : buttons )
        {
            if( button.isSelected() )
            {
                return button.getValue();
            }
        }

        return null;
    }

    /***************************************************************************
     * Removes the old buttons, if appropriate, and generates the new buttons.
     **************************************************************************/
    private void createGui()
    {
        this.removeAll();
        this.setLayout( new GridBagLayout() );
        group = new ButtonGroup();
        buttonMap.clear();

        for( int i = 0; i < values.size(); i++ )
        {
            T val = values.get( i );
            AbstractButton button = createButton( val );

            if( i == 0 )
            {
                button.setSelected( true );
            }

            add( button,
                new GridBagConstraints( 0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets( 2, 0, 2, 0 ), 0, 0 ) );
        }
    }

    /***************************************************************************
     * Creates a button with the given value.
     * @param val The choice to appear as a button.
     * @return The choice button.
     **************************************************************************/
    private AbstractButton createButton( T val )
    {
        AbstractButton button = null;
        UserValueStorage<T> uvs = null;

        if( useToggles )
        {
            uvs = new ValueToggleButton<T>();
        }
        else
        {
            uvs = new ValueRadioButton<T>();
        }

        uvs.setValue( val );
        button = ( AbstractButton )uvs;
        button.setText( val.toString() );
        group.add( button );
        buttonMap.put( val, uvs );

        return button;
    }

    /***************************************************************************
     * Creates a frame to display this panel with default values. Intent is for
     * debugging purposes only.
     * @param args Ignored arguments to this application.
     **************************************************************************/
    public static void main( String [] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                JFrame frame = new JFrame();
                JPanel panel = new JPanel();
                final JLabel label = new JLabel();
                final CollectionTogglePanel<TestEnum> cp0;
                final CollectionTogglePanel<TestEnum> cp1;

                cp0 = new CollectionTogglePanel<TestEnum>();
                cp1 = new CollectionTogglePanel<TestEnum>();

                cp0.setToggles( false );
                cp0.setValues( Arrays.asList( TestEnum.values() ) );
                cp0.setBorder(
                    BorderFactory.createTitledBorder( "Collection Panel 0" ) );
                cp0.addActionListener( new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        TestEnum te = cp0.getValue();
                        String str = te != null ? te.toString() : "NULL";
                        String cls = te != null ? te.getClass().getName() : "";
                        label.setText( str );
                        LogUtils.printDebug( "Clicked [" + cls + "]: " + str );
                        cp1.setValue( te );
                    }
                } );

                cp1.setValues( Arrays.asList( TestEnum.values() ) );
                cp1.setBorder(
                    BorderFactory.createTitledBorder( "Collection Panel 1" ) );

                label.setText( cp0.getValue() != null
                    ? cp0.getValue().toString() : "NULL" );

                panel.setLayout( new GridBagLayout() );
                panel.add( cp0,
                    new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets( 4, 4, 4, 4 ), 0, 0 ) );
                panel.add( cp1,
                    new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets( 4, 4, 4, 4 ), 0, 0 ) );
                panel.add( label,
                    new GridBagConstraints( 0, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets( 4, 4, 4, 4 ), 0, 0 ) );

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setContentPane( panel );
                frame.pack();
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }

    /***************************************************************************
     * An enumeration for use with the test application.
     **************************************************************************/
    private enum TestEnum
    {
        FIRST,
        SECOND,
        THIRD,
        FORTH,
        FIFTH_SIXTH_SEVENTH;

        public String toString()
        {
            switch( this )
            {
                case FIRST:
                    return "The first one!";
                case SECOND:
                    return "The second one!";
                case THIRD:
                    return "The third one!";
                case FORTH:
                    return "The forth one!";
                case FIFTH_SIXTH_SEVENTH:
                    return "The fifth through the seventh ones, inclusive!";
                default:
                    return "I got nothin'!";
            }
        }
    }
}

/***************************************************************************
 * @param <T>
 **************************************************************************/
interface UserValueStorage<T>
{
    public void setValue( T value );

    public T getValue();

    public boolean isSelected();

    public void setSelected( boolean selected );

    public void addActionListener( ActionListener l );

    public void removeActionListener( ActionListener l );
}

/***************************************************************************
 * @param <T>
 **************************************************************************/
class ValueRadioButton<T> extends JRadioButton implements UserValueStorage<T>
{
    private T userValue;

    public ValueRadioButton()
    {
        super();
        userValue = null;
    }

    public void setValue( T value )
    {
        userValue = value;
    }

    public T getValue()
    {
        return userValue;
    }
}

/***************************************************************************
 * @param <T>
 **************************************************************************/
class ValueToggleButton<T> extends JToggleButton implements UserValueStorage<T>
{
    private T userValue;

    public ValueToggleButton()
    {
        super();
        userValue = null;
    }

    public void setValue( T value )
    {
        userValue = value;
    }

    public T getValue()
    {
        return userValue;
    }
}
