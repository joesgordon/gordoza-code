package org.jutils.ui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.data.BuildInfo;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class BuildInfoView implements IView<JComponent>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public BuildInfoView()
    {
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();
        StringFormField versionField = new StringFormField( "Version" );
        StringFormField dateField = new StringFormField( "Build Date" );

        versionField.setEditable( false );
        dateField.setEditable( false );

        form.addField( versionField );
        form.addField( dateField );

        BuildInfo info = BuildInfo.load();

        versionField.setValue( info.version );
        dateField.setValue( info.buildDate );

        return form.getView();
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
     * 
     **************************************************************************/
    public static void show()
    {
        show( null );
    }

    /***************************************************************************
     * @param parent
     **************************************************************************/
    private static void show( Component parent )
    {
        BuildInfoView view = new BuildInfoView();
        OkDialogView dialog = new OkDialogView( parent, view.createView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        dialog.show( "JUtils Build Info", null );
    }
}
