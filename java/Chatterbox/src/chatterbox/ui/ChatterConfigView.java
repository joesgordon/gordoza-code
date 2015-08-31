package chatterbox.ui;

import java.awt.Component;

import javax.swing.JCheckBox;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;

import chatterbox.data.ChatterConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterConfigView implements IDataView<ChatterConfig>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final StringFormField nameField;
    /**  */
    private final StringFormField addressField;
    /**  */
    private final IntegerFormField portField;
    /**  */
    private final JCheckBox popupField;

    /**  */
    private ChatterConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterConfigView()
    {
        this.form = new StandardFormView();
        this.nameField = new StringFormField( "Name" );
        this.addressField = new StringFormField( "Address" );
        this.portField = new IntegerFormField( "Port", 0, 65535 );
        this.popupField = new JCheckBox();

        form.addField( nameField );
        form.addField( addressField );
        form.addField( portField );

        setData( new ChatterConfig() );

        nameField.setUpdater(
            new ReflectiveUpdater<>( this, "config.chatCfg.displayName" ) );
        addressField.setUpdater(
            new ReflectiveUpdater<>( this, "config.chatCfg.address" ) );
        portField.setUpdater(
            new ReflectiveUpdater<>( this, "config.chatCfg.port" ) );
        popupField.addActionListener( new CheckBoxUpdater(
            new ReflectiveUpdater<>( this, "config.showPopups" ) ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatterConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ChatterConfig config )
    {
        this.config = config;

        nameField.setValue( config.chatCfg.displayName );
        addressField.setValue( config.chatCfg.address );
        portField.setValue( config.chatCfg.port );
    }
}
