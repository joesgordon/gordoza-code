package org.jutils.ui.hex;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.ItemComboBoxModel;
import org.jutils.ui.validation.ValidationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexBytesField implements IDataFormField<byte []>
{
    /**  */
    private final ItemComboBoxModel<byte []> model;
    /**  */
    private final JComboBox<byte []> comboField;
    /**  */
    private final HexBytesFormField bytesField;
    /**  */
    private final ValidationView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexBytesField()
    {
        this( new ArrayList<>() );
    }

    /***************************************************************************
     * @param quickList
     **************************************************************************/
    public HexBytesField( List<byte []> quickList )
    {
        this.model = new ItemComboBoxModel<>( quickList );
        this.comboField = new JComboBox<>( model );
        this.bytesField = new HexBytesFormField( "" );
        this.view = new ValidationView( getValidationField(), null,
            comboField );

        comboField.setRenderer( new SyncRenderer() );

        comboField.setEditable( true );
        comboField.setEditor( new SyncComboEditor( bytesField ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFieldName()
    {
        return "SYNC";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getField()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        int index = comboField.getSelectedIndex();
        return index < 0 ? null : comboField.getItemAt( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( byte [] value )
    {
        bytesField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        bytesField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return bytesField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return bytesField.getValidationField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        bytesField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SyncRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent( JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            super.getListCellRendererComponent( list, value, index, isSelected,
                cellHasFocus );

            byte [] bytes = ( byte [] )value;

            super.setText( HexUtils.toHexString( bytes ) );

            return this;
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SyncComboEditor implements ComboBoxEditor
    {
        private final HexBytesFormField field;

        public SyncComboEditor( HexBytesFormField bytesField )
        {
            this.field = bytesField;
        }

        @Override
        public Component getEditorComponent()
        {
            return field.getTextField();
        }

        @Override
        public void setItem( Object item )
        {
            byte [] value = ( byte [] )item;

            field.setValue( value );
        }

        @Override
        public byte [] getItem()
        {
            return field.getValue();
        }

        @Override
        public void selectAll()
        {
            field.getTextField().selectAll();
        }

        @Override
        public void addActionListener( ActionListener l )
        {
            field.getTextField().addActionListener( l );
        }

        @Override
        public void removeActionListener( ActionListener l )
        {
            field.getTextField().removeActionListener( l );
        }
    }
}
