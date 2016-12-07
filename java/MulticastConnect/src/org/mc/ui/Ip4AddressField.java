package org.mc.ui;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;
import org.mc.io.Ip4Address;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4AddressField implements IDataFormField<Ip4Address>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Ip4Address getValue()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Ip4Address value )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Ip4Address> updater )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Ip4Address> getUpdater()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
