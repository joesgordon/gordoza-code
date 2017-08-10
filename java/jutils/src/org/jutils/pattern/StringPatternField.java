package org.jutils.pattern;

import java.awt.*;
import java.awt.event.MouseListener;

import javax.swing.*;

import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPatternField implements IDataFormField<StringPattern>
{
    private final JPanel view;
    /**  */
    private final StringFormField patternField;
    /**  */
    private final ComboFormField<StringPatternType> typeField;
    /**  */
    private final JCheckBoxMenuItem caseMenuItem;
    /**  */
    private final JPopupMenu contextMenu;

    /**  */
    private StringPattern pattern;
    /**  */
    private IUpdater<StringPattern> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPatternField( String name )
    {
        this.patternField = new StringFormField( name, 0, null );
        this.typeField = new ComboFormField<>( "Type",
            StringPatternType.values(), new NamedItemDescriptor<>() );
        this.view = createView();
        this.caseMenuItem = new JCheckBoxMenuItem();
        this.contextMenu = createContextMenu();

        setValue( new StringPattern() );

        patternField.setUpdater( ( t ) -> {
            if( pattern != null )
            {
                pattern.patternText = t;
            }
            invokeUpdater();
        } );
        typeField.setUpdater( ( d ) -> {
            if( pattern != null )
            {
                pattern.type = d;
            }
            invokeUpdater();
        } );

        MouseListener rcl = new RightClickListener(
            ( e ) -> showContextMenu( e.getComponent(), e.getPoint() ) );
        patternField.addMouseListener( rcl );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeUpdater()
    {
        if( updater != null )
        {
            updater.update( pattern );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 4 ), 0, 0 );
        panel.add( patternField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( typeField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createContextMenu()
    {
        JPopupMenu menu = new JPopupMenu();

        caseMenuItem.setText( "Case Sensitive" );

        caseMenuItem.addActionListener(
            ( e ) -> pattern.isCaseSensitive = caseMenuItem.isSelected() );

        menu.add( caseMenuItem );

        return menu;
    }

    /***************************************************************************
     * @param invoker
     * @param p
     **************************************************************************/
    private void showContextMenu( Component invoker, Point p )
    {
        contextMenu.show( invoker, p.x, p.y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public StringPattern getValue()
    {
        return pattern;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( StringPattern value )
    {
        this.pattern = value;

        if( value != null )
        {
            patternField.setValue( pattern.patternText );
            typeField.setValue( pattern.type );
            caseMenuItem.setSelected( pattern.isCaseSensitive );
        }
        else
        {
            patternField.setValue( null );
            typeField.setValue( StringPatternType.CONTAINS );
            caseMenuItem.setSelected( false );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<StringPattern> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<StringPattern> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        patternField.setEditable( editable );
        typeField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return patternField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        patternField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        patternField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return patternField.getValidity();
    }
}
