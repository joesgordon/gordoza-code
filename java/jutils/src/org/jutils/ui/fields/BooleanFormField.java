package org.jutils.ui.fields;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BooleanFormField
    implements IDataFormField<Boolean>, IValidationField
{
    /**  */
    private final String name;
    /**  */
    private final JCheckBox flagField;
    /**  */

    /**  */
    private CheckBoxUpdater cbUpdater;

    /***************************************************************************
     * 
     **************************************************************************/
    public BooleanFormField( String name )
    {
        this.name = name;
        this.flagField = new JCheckBox();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getField()
    {
        return flagField;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Boolean getValue()
    {
        return flagField.isSelected();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Boolean value )
    {
        flagField.setSelected( value == null ? false : value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Boolean> updater )
    {
        if( cbUpdater != null )
        {
            flagField.removeActionListener( cbUpdater );
            cbUpdater = null;
        }

        cbUpdater = new CheckBoxUpdater( updater );
        flagField.addActionListener( cbUpdater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Boolean> getUpdater()
    {
        return cbUpdater == null ? null : cbUpdater.updater;
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
        flagField.setEnabled( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return new Validity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return flagField;
    }
}
