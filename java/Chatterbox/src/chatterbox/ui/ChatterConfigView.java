package chatterbox.ui;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;
import org.mc.ui.MulticastInputsView;

import chatterbox.data.ChatterConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterConfigView implements IDataView<ChatterConfig>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final MulticastInputsView inputsView;
    /**  */
    private final StringFormField nameField;
    /**  */
    private final BooleanFormField popupField;

    /**  */
    private ChatterConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterConfigView()
    {
        this.form = new StandardFormView();
        this.inputsView = new MulticastInputsView();
        this.nameField = new StringFormField( "Name" );
        this.popupField = new BooleanFormField( "Show Popups" );

        form.addField( nameField );
        form.addField( popupField );
        form.addField( null, inputsView.getView() );

        inputsView.getView().setBorder(
            new TitledBorder( "Connection Options" ) );

        setData( new ChatterConfig() );

        nameField.setUpdater(
            new ReflectiveUpdater<>( this, "config.displayName" ) );
        popupField.setUpdater(
            new ReflectiveUpdater<>( this, "config.showPopups" ) );
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

        inputsView.setData( config.chatCfg );
        nameField.setValue( config.displayName );
        popupField.setValue( config.showPopups );
    }
}
