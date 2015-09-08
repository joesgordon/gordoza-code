package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UsableFormField<T>
    implements IDataFormField<Usable<T>>, IValidationField
{
    /**  */
    private final JPanel panel;
    /**  */
    private final JCheckBox usedField;
    /**  */
    private final IDataFormField<T> field;

    /**  */
    private Usable<T> usable;
    /**  */
    private IUpdater<Usable<T>> updater;

    /***************************************************************************
     * @param field
     **************************************************************************/
    public UsableFormField( IDataFormField<T> field )
    {
        this.field = field;
        this.usedField = new JCheckBox();
        this.panel = createView();

        field.setUpdater( new DataUpdater<T>( this,
            new ReflectiveUpdater<T>( this, "usable.data" ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        usedField.addActionListener( new UsedListener( this ) );

        panel.add( usedField, BorderLayout.WEST );
        panel.add( field.getField(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFieldName()
    {
        return field.getFieldName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getField()
    {
        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Usable<T> getValue()
    {
        return usable;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Usable<T> value )
    {
        this.usable = value;

        usedField.setSelected( value.isUsed );

        if( value.data != null )
        {
            field.setValue( value.data );
        }

        field.setEditable( value.isUsed );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Usable<T>> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Usable<T>> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return this;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void callUpdater()
    {
        if( updater != null )
        {
            updater.update( usable );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return getField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.getValidationField().addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.getValidationField().removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isValid()
    {
        return usedField.isSelected() ? field.getValidationField().isValid()
            : true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getInvalidationReason()
    {
        return field.getValidationField().getInvalidationReason();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValidBackground( Color bg )
    {
        field.getValidationField().setValidBackground( bg );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setInvalidBackground( Color bg )
    {
        field.getValidationField().setInvalidBackground( bg );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UsedListener implements ActionListener
    {
        private final UsableFormField<?> field;

        public UsedListener( UsableFormField<?> field )
        {
            this.field = field;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            field.usable.isUsed = field.usedField.isSelected();
            field.field.setEditable( field.usable.isUsed );

            if( field.updater != null )
            {
                field.callUpdater();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DataUpdater<T> implements IUpdater<T>
    {
        private final UsableFormField<T> field;
        private final IUpdater<T> updater;

        public DataUpdater( UsableFormField<T> field, IUpdater<T> updater )
        {
            this.field = field;
            this.updater = updater;
        }

        @Override
        public void update( T data )
        {
            updater.update( data );

            field.callUpdater();
        }
    }
}
