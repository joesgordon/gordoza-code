package chatterbox.ui;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.BooleanFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.net.MulticastInputsView;

import chatterbox.data.ChatterboxOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxOptionsView implements IDataView<ChatterboxOptions>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final StringFormField nameField;
    /**  */
    private final BooleanFormField popupField;
    /**  */
    private final MulticastInputsView inputsView;

    /**  */
    private ChatterboxOptions config;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatterboxOptionsView()
    {
        this.form = new StandardFormView();
        this.inputsView = new MulticastInputsView( false );
        this.nameField = new StringFormField( "Display Name" );
        this.popupField = new BooleanFormField( "Show Popups" );

        form.addField( nameField );
        form.addField( popupField );
        form.addComponent( inputsView.getView() );

        inputsView.getView().setBorder(
            new TitledBorder( "Connection Options" ) );

        setData( new ChatterboxOptions() );

        nameField.setUpdater( ( s ) -> config.displayName = s );
        popupField.setUpdater( ( b ) -> config.showPopups = b );
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
    public ChatterboxOptions getData()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ChatterboxOptions config )
    {
        this.config = config;

        inputsView.setData( config.chatCfg );
        nameField.setValue( config.displayName );
        popupField.setValue( config.showPopups );
    }
}
